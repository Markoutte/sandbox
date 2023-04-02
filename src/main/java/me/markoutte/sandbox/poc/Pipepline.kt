package me.markoutte.sandbox.poc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Pipeline is an object that can bind and run phases.
 *
 * Pipeline is a template that can be run for different values. It is thread-safe to run several instances
 * of a pipeline with different values
 */
class Pipeline<INPUT, OUTPUT> internal constructor(
    private val bindings: List<suspend CoroutineScope.(ComputableStorage) -> Unit>,
    private val input: Phase<INPUT, *>,
    private val output: Phase<*, OUTPUT>,
) {

    suspend fun submit(
        value: INPUT,
        context: CoroutineContext = Dispatchers.Unconfined,
        onComplete: suspend PipelineContext.() -> Unit = {},
    ): OUTPUT = coroutineScope {
        val ctx = PipelineContext()
        val c = ComputableStorage(ctx)
        c(input).process(value, ctx)
        bindings.forEach { binding ->
            launch(context) { binding(c) }
        }
        c(output).awaitResult(ctx).also {
            ctx.onComplete()
        }
    }
}