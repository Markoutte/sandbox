package me.markoutte.sandbox.poc

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

class PipelineContext {
    private val storage = ConcurrentHashMap<Any, Any>()
    internal val debug = DebugInfo()

    operator fun set(key: Any, value: Any) {
        storage[key] = value
    }

    operator fun <T> get(key: Any): T? {
        @Suppress("UNCHECKED_CAST")
        return storage[key] as T?
    }

    internal fun <T> trace(phase: Phase<*, T>, result: T) {
        debug.debugTrace += phase to result
    }

    internal suspend fun <T> waited(value: ComputableValue<*, *>, block: suspend () -> T) : T {
        try {
            debug.debugWaitingList += value
            return block()
        } finally {
            debug.debugWaitingList -= value
        }
    }

    internal inner class DebugInfo {
        val debugWaitingList = CopyOnWriteArraySet<ComputableValue<*, *>>()
        val debugTrace = linkedMapOf<Phase<*, *>, Any?>()

        /**
         * Use this to generate info about all completed phases and its values.
         */
        @Suppress("unused")
        internal fun printDebug(): String = buildString {
            debugTrace.forEach { (phase, value) ->
                appendLine("[${phase.debugName}] -> ${value?.javaClass ?: "unknown type"}:$value")
            }
        }
    }
}