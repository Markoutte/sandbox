package me.markoutte.sandbox.poc

/**
 * Computable storage stores [ComputableValue] per phase.
 *
 * Should be never shared between different pipelines.
 */
internal class ComputableStorage(
    val context: PipelineContext,
) {
    private val storage: MutableMap<Phase<*, *>, ComputableValue<*, *>> = mutableMapOf()

    /**
     * Returns [ComputableValue] for [phase].
     *
     * For each call it is true that:
     * ```
     * val phase1 = ...
     * invoke(phase1) === invoke(phase1)
     * ```
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <INPUT, OUTPUT> invoke(phase: Phase<INPUT, OUTPUT>): ComputableValue<INPUT, OUTPUT> {
        return storage[phase] as? ComputableValue<INPUT, OUTPUT> ?: ComputableValue(phase).apply { storage[phase] = this }
    }
}