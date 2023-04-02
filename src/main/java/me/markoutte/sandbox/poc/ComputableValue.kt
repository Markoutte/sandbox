package me.markoutte.sandbox.poc

import kotlinx.coroutines.CompletableDeferred
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Computable value runs the phase and waits until result is returns.
 *
 * Computable value can be calculated only once.
 */
internal class ComputableValue<INPUT, VALUE>(
    private val phase: Phase<INPUT, VALUE>
) {
    private val deferred = CompletableDeferred<VALUE>()
    private val started = AtomicBoolean()

    /**
     * Wait until result is computed or return immediately.
     */
    suspend fun awaitResult(ctx: PipelineContext): VALUE {
        return ctx.waited(this) { deferred.await() }
    }

    /**
     * Process the value for the first time or throw [IllegalStateException] if value calculation is already started.
     */
    suspend fun process(value: INPUT, ctx: PipelineContext) {
        if (started.getAndSet(true)) {
            throw IllegalStateException("${phase.debugName} is already processed")
        }
        try {
            val result = phase.action(value, ctx)
            ctx.trace(phase, result)
            deferred.complete(result)
        } catch (t: Throwable) {
            deferred.completeExceptionally(t)
        }
    }

    override fun toString(): String {
        return phase.toString()
    }
}