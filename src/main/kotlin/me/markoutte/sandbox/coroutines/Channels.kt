package me.markoutte.sandbox.coroutines

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() = runBlocking {
    val channel = Channel<Int>()

    launch {
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