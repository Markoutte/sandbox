package me.markoutte.sandbox.profiling

import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.system.measureNanoTime

fun main() = measureAndPrint {
    val path = Paths.get("dir")
    for (i in 0..99999) {
//        if (!Files.exists(path)) {
            Files.createDirectories(path)
//        }
    }
}

fun measureAndPrint(block: () -> Unit) {
    val nanos = measureNanoTime(block)
    val millis = TimeUnit.NANOSECONDS.toMillis(nanos)
    println("Total time $millis ms")
}