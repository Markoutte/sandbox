package me.markoutte.sandbox.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

suspend fun main() {
    val channel = Channel<Int>()

    GlobalScope.launch {
        repeat(1000) {
            delay(20)
            channel.send(it)
        }
        channel.close()
    }

    while (true) try {
        val receive = channel.receive()
        println("${Date()}: $receive")
    } catch (e: ClosedReceiveChannelException) {
        println("Channel is closed")
        break
    }
}