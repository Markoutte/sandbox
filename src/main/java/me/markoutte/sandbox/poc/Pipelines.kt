package me.markoutte.sandbox.poc

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

suspend fun main() {    
    runBlocking {
        // start --> upper -------------
        //       -                      -> finish
        //        -> lower1 --> lower2 -

        data class H(val s: String)

        val start = Phase<Int, String> { debugPrint("start", 1000) {
            it.toString(2)
        } }
        val upper = Phase<String, H> { debugPrint("upper", 2000) {
            H(it)
        } }
        val lower1 = Phase<String, Long> { debugPrint("lower1", 700) {
            it.toLong() + 30
        } }
        val lower2 = Phase<Long, H> { debugPrint("lower2", 2000) {
            H(it.toString(2))
        } }
        val finish = Phase<List<H>, String> { debugPrint("finish", 1000) {
            it.mapIndexed { index, h -> "$index. ${h.s}" }.joinToString(separator = "\n\r")
        } }


        debugPrint("pipeline", 0) {
            val result = Pipeline {
                start transfer listOf(upper, lower1)
                lower1 transfer lower2
                listOf(upper, lower2) transfer finish
            }.run(start, 100, finish)
            println(result)
        }
    }

    // experimental
    runBlocking {
        println()
        data class G(val s: String, val i: Int)

        val start = Phase<Unit, Unit> {}
        val intProducer = Phase<Unit, Int> { 0 }
        val stringProducer = Phase<Unit, String> { "Hello" }
        val combine = Phase<Pair<String, Int>, G> { G(it.first, it.second) }

        val result = Pipeline {
            bind { pipeline(start, listOf(intProducer, stringProducer)) }
            bind { pipeline(stringProducer, intProducer, combine) }
        }.run(start, Unit, combine)
        println(result)
    }
    
    runBlocking { 
        val getDirectory = Phase<String, Path?> {
            Paths.get(it).takeIf { p -> Files.exists(p) && Files.isDirectory(p) }
        }
        val getChildren = Phase<Path?, List<File>> {
            it?.toFile()?.listFiles()?.toList() ?: emptyList()
        }
        val filterOnlyFiles = Phase<List<File>, List<File>> {
            it.filter { f ->
                f.isFile
            }
        }
        val readData = Phase<List<File>, List<ByteArray>> {
            val jobs = mutableListOf<Deferred<ByteArray?>>()
            it.forEach {
                jobs += async {
                    try {
                        it.readBytes()
                    } catch (_ : Throwable) {
                        null
                    }
                }
            }
            jobs.awaitAll().filterNotNull()
        }
        val collect = Phase<List<ByteArray>, Long> {
            var long = 0L
            it.forEach { array -> 
                long += array.size
            }
            long
        }

        val path = Paths.get(System.getProperty("user.home"))
        val size = Pipeline {
            getDirectory transfer getChildren
            getChildren transfer filterOnlyFiles
            filterOnlyFiles transfer readData
            readData transfer collect
        }.run(getDirectory, path.toString(), collect)
        println("$path has files with size $size")
    }
}

private suspend fun <T> debugPrint(name: String, wait: Long, block: suspend CoroutineScope.() -> T): T = coroutineScope {
    println(">> $name")
    val start = System.currentTimeMillis()
    delay(wait)
    val result = block()
    val end = System.currentTimeMillis()
    println("<< $name (${end - start} ms, ${end - time} ms total)")
    result
}

var time = System.currentTimeMillis()

class Pipeline(init: Pipeline.() -> Unit) {

    private val pipelines = mutableListOf<suspend () -> Unit>()

    init {
        init()
    }
    
    infix fun <TRANSFER> Phase<*, TRANSFER>.transfer(receiver: Phase<TRANSFER, *>) {
        pipelines += { pipeline(this, receiver) }
    }

    infix fun <TRANSFER> Phase<*, TRANSFER>.transfer(receiver: List<Phase<TRANSFER, *>>) {
        pipelines += { pipeline(this, receiver) }
    }

    infix fun <TRANSFER> List<Phase<*, TRANSFER>>.transfer(receiver: Phase<List<TRANSFER>, *>) {
        pipelines += { pipeline(this, receiver) }
    }

    fun bind(block: suspend () -> Unit) {
        pipelines += block
    }

    suspend fun <INIT, R> run(start: Phase<INIT, *>, value: INIT, finite: Phase<*, R>): R = coroutineScope {
        pipelines.forEach { pipeline ->
            launch { pipeline() }
        }
        start.process(value)
        finite.awaitResult()
    }

}

class Phase<INPUT, OUTPUT>(private val action: suspend (INPUT) -> OUTPUT) {

    private var result = Channel<OUTPUT>()

    suspend fun awaitResult(): OUTPUT {
        return result.receive()
    }

    suspend fun process(value: INPUT) {
        result.send(action(value))
    }
}

suspend fun <TRANSFER> pipeline(sender: Phase<*, TRANSFER>, receiver: Phase<TRANSFER, *>) {
    receiver.process(sender.awaitResult())
}

suspend fun <TRANSFER> pipeline(sender: Phase<*, TRANSFER>, receivers: List<Phase<TRANSFER, *>>) = coroutineScope {
    val transfer = sender.awaitResult()
    receivers.forEach { receiver ->
        launch { receiver.process(transfer) }
    }
}
suspend fun <TRANSFER> pipeline(senders: List<Phase<*, TRANSFER>>, receiver: Phase<List<TRANSFER>, *>) = coroutineScope {
    val jobs = mutableListOf<Deferred<TRANSFER>>()
    senders.forEach { sender ->
        jobs += this.async { sender.awaitResult() }
    }
    launch {
        receiver.process(jobs.awaitAll())
    }
}

suspend fun <T1, T2> pipeline(sender1: Phase<*, T1>, sender2: Phase<*, T2>, receiver: Phase<Pair<T1, T2>, *>) = coroutineScope {
    val job1 = async { sender1.awaitResult() }
    val job2 = async { sender2.awaitResult() }
    launch { 
        receiver.process(Pair(job1.await(), job2.await()))
    }
}