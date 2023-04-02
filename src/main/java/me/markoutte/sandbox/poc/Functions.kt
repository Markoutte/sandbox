package me.markoutte.sandbox.poc

fun <INPUT, OUTPUT> phase(debugName: String, block: suspend PipelineContext.(INPUT) -> OUTPUT) = object : Phase<INPUT, OUTPUT>(debugName) {
    override suspend fun action(input: INPUT, ctx: PipelineContext): OUTPUT = block(ctx, input)
}

fun <T> identityPhase() = phase<T, T>("identity") { it }

fun <INPUT, OUTPUT> pipeline(init: PipelineBuilder<INPUT, OUTPUT>.() -> Unit) = PipelineBuilder.createPipeline(init)

fun <INPUT, OUTPUT> pipeline(from: Phase<INPUT, *>, to: Phase<*, OUTPUT>, init: PipelineBuilder<INPUT, OUTPUT>.() -> Unit) = PipelineBuilder.createPipeline(from, to, init)