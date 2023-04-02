package me.markoutte.sandbox.poc

/**
 * Phase of the pipeline that processes input into output.
 */
abstract class Phase<INPUT, OUTPUT>(val debugName: String) {
    abstract suspend fun action(input: INPUT, ctx: PipelineContext) : OUTPUT

    override fun toString(): String = debugName

    fun copy(): Phase<INPUT, OUTPUT> = object : Phase<INPUT, OUTPUT>("copyOf[$debugName]") {
        override suspend fun action(input: INPUT, ctx: PipelineContext): OUTPUT = this@Phase.action(input, ctx)
    }
}