package me.markoutte.sandbox.poc

import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.TimeUnit

class PipelineTest {

    /**
     * This pipeline is used for test:
     *
     * ```
     * generator \---> upper ----------------------\
     *            \                                 \
     *             \---> lower-left ---> lower-right \---> collector
     * ```
     */

    private val generator = phase<Unit, String>("generator") {
        "Hello, world!"
    }

    private val upper = phase<String, CharArray>("collect punctuations") {
        val punctuations = ",!?"
        it.toCharArray().filter { c -> punctuations.contains(c) }.toCharArray()
    }

    private val lowerLeft = phase<String, List<String>>("comma splitter") {
        it.split(",", "!").filter { s -> s.isNotBlank() }
    }

    private val lowerRight = phase<List<String>, CharArray>("remove name") {
        it[0].toCharArray()
    }

    private val collector = phase<List<CharArray>, String>("build string") {
        buildString {
            it.forEach {
                append(it)
            }
        }
    }

    @Test
    fun `default pipeline is verified`() {
        val defaultPipeline = pipeline(generator, collector) {
            generator sendTo listOf(upper, lowerLeft)
            lowerLeft sendTo lowerRight
            // the order matters
            listOf(lowerRight, upper) sendTo collector
        }

        runBlocking {
            assertEquals("Hello,!", defaultPipeline.submit(Unit))
        }
    }

    @Test
    fun `default pipeline rises an exception when phase is processed second time`() {
        val defaultPipeline = pipeline(generator, collector) {
            generator sendTo listOf(upper, lowerLeft)
            lowerLeft sendTo lowerRight
            lowerRight convertWith { listOf(it) } sendTo collector
            upper convertWith { listOf(it) } sendTo collector
        }

        assertThrows(IllegalStateException::class.java) {
            runBlocking {
                assertEquals("Hello,!", defaultPipeline.submit(Unit))
            }
        }
    }

    @Test
    fun `partial pipeline is verified`() {
        val defaultPipeline = pipeline(lowerLeft, collector) {
            lowerLeft sendTo lowerRight
            lowerRight convertWith { listOf(it) } sendTo collector
        }

        runBlocking {
            assertEquals("Anybody?", defaultPipeline.submit("Anybody?"))
        }
    }

    @Test
    fun `timeout cancels pipeline`() {
        val phase = phase<Unit, Unit>("delay") {
            delay(1000)
        }

        val printFailed = phase<Unit, Unit>("failed") {
            assertTrue(false) { "This phase must not be invoked" }
        }

        assertThrows(TimeoutCancellationException::class.java) {
            runBlocking {
                withTimeout(100) {
                    pipeline(phase, printFailed) {
                        phase sendTo printFailed
                    }.submit(Unit)
                }
            }
        }
    }

    @Test
    fun `concurrent execution is ok`() {
        fun func(n: Int): Int {
            var count = 0
            for (i in 0..1_000_000) {
                if (i % n == 0) count++
            }
            return count
        }

        val pipeline = pipeline<Unit, Long> {
            val generator = phase<Unit, Int>("generator") { 3 }
            val consumer = phase<List<Int>, Long>("consumer") { it.sumOf { v -> v.toLong() } }

            val processors = (1..10).map { value ->
                phase<Int, Int>("processor #$value") { func(value) }
            }

            generator sendTo processors
            processors collectTo consumer

            generator endWith consumer
        }
        runBlocking {
            println(pipeline.submit(Unit, Dispatchers.Default))
        }
    }

    @Test
    @Timeout(1, unit = TimeUnit.SECONDS)
    fun `no loop`(): Unit = runBlocking {
        val pipeline = pipeline<Unit, Unit> {
            val phase1 = phase<Unit, Unit>("loop") {
                println("running")
            }
            @Suppress("UnnecessaryVariable")
            val phase2 = phase1
            phase1 endWith phase2

            phase1 sendTo phase2
        }
        assertThrows<IllegalStateException> {
            pipeline.submit(Unit)
        }
    }

    @Test
    @Timeout(1, unit = TimeUnit.SECONDS)
    fun `no loop with tail only`(): Unit = runBlocking {
        val pipeline = pipeline<Unit, Unit> {
            val phase = phase<Unit, Unit>("start") {
                print("started")
            }
            val phase1 = phase<Unit, Unit>("loop") {
                println("running")
            }
            @Suppress("UnnecessaryVariable")
            val phase2 = phase1
            phase endWith phase2

            phase sendTo phase1
            phase1 sendTo phase2
        }
        assertThrows<IllegalStateException> {
            pipeline.submit(Unit)
        }
    }

    @Test
    @Timeout(1, unit = TimeUnit.SECONDS)
    fun `no loop with multiple`(): Unit = runBlocking {
        val pipeline = pipeline<Unit, Unit> {
            val phase = phase<Unit, Unit>("start") {
                print("started")
            }
            val phase1 = phase<Unit, Unit>("loop") {
                println("running")
            }
            @Suppress("UnnecessaryVariable")
            val phase2 = phase1
            phase endWith phase2

            phase sendTo listOf(phase1, phase2)
            phase1 sendTo phase2
        }
        assertThrows<IllegalStateException> {
            pipeline.submit(Unit)
        }
    }

    @Test
    fun `pipeline waits some long phase but finish phase is already processed`(): Unit = runBlocking {
        val isDone = CompletableDeferred<Boolean>()

        pipeline<Unit, Unit> {
            val start = phase<Unit, Unit>("start") { }
            val longButBranch = phase<Unit, Boolean>("branch") {
                delay(200)
                isDone.complete(true)
            }
            val finish = phase<Unit, Unit>("finish") {}

            start endWith finish

            start sendTo listOf(finish, longButBranch)
        }.submit(Unit, Dispatchers.Default) {
            assertEquals(2, debug.debugTrace.size) { "Branch phase shouldn't be processed but trace is:\n\r ${debug.printDebug()}" }
            val result = isDone.await()
            assertTrue(result)
            assertEquals(3, debug.debugTrace.size) { "Branch phase should be processed but trace is:\n\r ${debug.printDebug()}" }
        }
    }

    @Test
    fun `pipeline waits some long phase but finish phase is already processed and branch has another phase to continue`(): Unit = runBlocking {
        val isDone = CompletableDeferred<Boolean>()

        pipeline<Unit, Unit> {
            val start = phase<Unit, Unit>("start") { }
            val longButBranch1 = phase<Unit, Unit>("branch1") {
                delay(200)
            }
            val longButBranch2 = phase<Unit, Boolean>("branch2") {
                isDone.complete(true)
            }
            val finish = phase<Unit, Unit>("finish") {}

            start endWith finish

            start sendTo listOf(finish, longButBranch1)
            longButBranch1 sendTo longButBranch2
        }.submit(Unit, Dispatchers.Default) {
            assertEquals(1, debug.debugWaitingList.size) { "Some phases is still waiting for returning result" }
            assertEquals("branch1", debug.debugWaitingList.first().toString()) { "branch1 should be in process and branch2 is waiting it" }
            assertEquals(2, debug.debugTrace.size) { "Branch phase shouldn't be processed but trace is:\n\r ${debug.printDebug()}" }
            val result = isDone.await()
            assertTrue(result)
            assertTrue(debug.debugWaitingList.isEmpty()) { "Some phases is still waiting for returning result" }
            assertEquals(4, debug.debugTrace.size) { "Branch phase should be processed but trace is:\n\r ${debug.printDebug()}" }
        }
    }

    @Test
    fun `pipeline is waiting in runBlocking block`() {
        runBlocking {
            var isFinished = false
            var isDone = false
            pipeline<Unit, Unit> {
                val start = phase<Unit, Unit>("start") { }
                val longButBranch = phase<Unit, Unit>("branch") {
                    delay(2000)
                    isDone = true
                }
                val finish = phase<Unit, Unit>("finish") {
                    isFinished = true
                }

                start endWith finish

                start sendTo listOf(finish, longButBranch)
            }.submit(Unit, newFixedThreadPoolContext(2, "test context")) {
                assertFalse(isDone)
                assertTrue(isFinished)
            }

            assertTrue(isDone)
            assertTrue(isFinished)
        }
    }

    @Test
    fun `pipeline in pipeline`(): Unit = runBlocking {
        val power = phase<Int, List<Int>>("power") {
            (1 .. it).map { i -> i * i }
        }

        val toString = phase<List<Int>, String>("as string") {
            it.toString()
        }

        val outerPhase = phase<Int, String>("outer") {
            pipeline<Int, String> {
                power endWith toString
                power sendTo toString
            }.submit(it)
        }

        val result = pipeline<Int, String> {
            outerPhase endWith outerPhase
        }.submit(3)

        assertEquals("[1, 4, 9]", result)
    }

    @Test
    fun `reuse phase`(): Unit = runBlocking {
        val p = phase<String, String>("test") {
            if (it.first().isUpperCase()) {
                it.toLowerCase()
            } else {
                it.toUpperCase()
            }
        }

        assertEquals("HELLO", pipeline(p, p) {}.submit("hello"))
        val copy = p.copy()
        assertEquals("hello", pipeline(p, copy) {
            p sendTo copy
        }.submit("hello"))
    }
}