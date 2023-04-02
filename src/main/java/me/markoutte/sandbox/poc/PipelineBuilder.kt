package me.markoutte.sandbox.poc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PipelineBuilder<INPUT, OUTPUT> private constructor(init: PipelineBuilder<INPUT, OUTPUT>.() -> Unit) {
    private val bindings = mutableListOf<suspend CoroutineScope.(ComputableStorage) -> Unit>()

    private lateinit var start: Phase<INPUT, *>
    private lateinit var finish: Phase<*, OUTPUT>

    init {
        this.init()
    }

    infix fun Phase<INPUT, *>.endWith(phase: Phase<*, OUTPUT>) {
        if (this == phase) {
            val from = identityPhase<INPUT>()
            start = from
            finish = phase
            from sendTo this
        } else {
            start = this
            finish = phase
        }
    }

    infix fun <TRANSFER> Phase<*, TRANSFER>.sendTo(receiver: Phase<TRANSFER, *>) {
        bindings += { c ->
            c(receiver).process(c(this@sendTo).awaitResult(c.context), c.context)
        }
    }

    infix fun <TRANSFER> Phase<*, TRANSFER>.sendTo(receivers: List<Phase<TRANSFER, *>>) {
        bindings += { c ->
            val result = c(this@sendTo).awaitResult(c.context)
            receivers.forEach { receiver ->
                launch {
                    c(receiver).process(result, c.context)
                }
            }
        }
    }

    infix fun <TRANSFER> List<Phase<*, TRANSFER>>.sendTo(receiver: Phase<List<TRANSFER>, *>) {
        this collectTo receiver
    }

    infix fun <TRANSFER> List<Phase<*, TRANSFER>>.collectTo(receiver: Phase<List<TRANSFER>, *>) {
        bindings += { c ->
            val jobs = mutableListOf<Deferred<TRANSFER>>()
            this@collectTo.map { c(it) }.forEach { sender ->
                jobs += this.async { sender.awaitResult(c.context) }
            }
            launch {
                c(receiver).process(jobs.awaitAll(), c.context)
            }
        }
    }

    infix fun <IN, OUT> Phase<*, List<IN>>.forEach(processor: Phase<IN, OUT>): Phase<List<IN>, List<OUT>> {
        val forEachPhase = object : Phase<List<IN>, List<OUT>>("forEach[${debugName}]") {
            override suspend fun action(input: List<IN>, ctx: PipelineContext): List<OUT> = coroutineScope {
                input.map { value -> async { processor.action(value, ctx) } }.awaitAll()
            }
        }
        this sendTo forEachPhase
        return forEachPhase
    }

    infix fun <IN, OUT> Phase<*, List<IN>>.forEach(block: (IN) -> OUT): Phase<List<IN>, List<OUT>> {
        val forEachPhase = object : Phase<List<IN>, List<OUT>>("forEach[${debugName}]") {
            override suspend fun action(input: List<IN>, ctx: PipelineContext): List<OUT> {
                return input.map(block)
            }
        }
        this sendTo forEachPhase
        return forEachPhase
    }

    infix fun <TRANSFER, CONVERTED> Phase<*, TRANSFER>.convertWith(block: (TRANSFER) -> CONVERTED): Phase<TRANSFER, CONVERTED> {
        val propagated = object : Phase<TRANSFER, CONVERTED>("propagated[${debugName}]") {
            override suspend fun action(input: TRANSFER, ctx: PipelineContext): CONVERTED {
                return block(input)
            }
        }
        this sendTo propagated
        return propagated
    }

    companion object {
        fun <INPUT, OUTPUT> createPipeline(from: Phase<INPUT, *>, to: Phase<*, OUTPUT>, init: PipelineBuilder<INPUT, OUTPUT>.() -> Unit) : Pipeline<INPUT, OUTPUT> {
            return createPipeline {
                from endWith to
                init()
            }
        }

        fun <INPUT, OUTPUT> createPipeline(init: PipelineBuilder<INPUT, OUTPUT>.() -> Unit) : Pipeline<INPUT, OUTPUT> {
            val builder = PipelineBuilder(init)
            return Pipeline(builder.bindings.toList(), builder.start, builder.finish)
        }
    }
}