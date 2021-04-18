package me.markoutte.sandbox.swing.monsters

import androidx.compose.animation.core.*
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

fun main() = Window(
    title = "Monsters Compose",
    size = IntSize(640, 480)
) {
    val width = remember { mutableStateOf(200) }
    val height = remember { mutableStateOf(200) }
    val catch = { SpriteImage32.catchOne(sprite[Random.nextInt(sprite.size)]) }
    val cages = remember { mutableListOf<SpriteImage32<ImageBitmap>>().also {
        it.add(catch())
    }}
    val repaints = remember { EventCounter(1000) }
    val count = remember { mutableStateOf(cages.size) }
    val fps = remember { mutableStateOf(0) }
    val px = 100 // pixels per second
    val ms = 1000 / px
    val counter = rememberInfiniteTransition().animateValue(
        initialValue = 0,
        targetValue = 10_000 * px,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(tween(durationMillis = 10_000 * px * ms, easing = LinearEasing))
    )

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .onSizeChanged {
            width.value = it.width
            height.value = it.height
        }
        .clickable(role = Role.Image) {
            (0 until 1000).map { catch() }.forEach(cages::add)
            count.value = cages.size
        }
    ) {
        val s = counter.value
        cages.forEach {
            val w = size.width + it.width * density
            val h = size.height + it.height * density
            val x = ((it.x * w + s) % w - it.width * density).roundToInt()
            val y = ((it.y * h + s) % h - it.height * density).roundToInt()
            val c = it.source.width / SpriteImage32.SIZE
            val offset = s / 10 % c * SpriteImage32.SIZE
            drawImage(
                it.source,
                srcOffset = IntOffset(offset, 0),
                srcSize = IntSize(it.width, it.height),
                dstOffset = IntOffset(x, y),
                dstSize = IntSize((it.width * density).toInt(), (it.height * density).toInt()),
            )
        }
        fps.value = repaints.update()
    }

    Text("${count.value} monsters, fps = ${fps.value}",
        modifier = Modifier
            .offset(5.dp, 5.dp)
            .background(Color.White)
            .padding(5.dp)
    )

}

private val sprite = Monsters.values().asSequence().map {
    SpriteImage32.createOne(it.base64) { data ->
        org.jetbrains.skija.Image.makeFromEncoded(data).asImageBitmap()
    }
}.toList()