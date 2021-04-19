package me.markoutte.sandbox.swing.monsters

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.random.Random

fun main() = Window(
    title = "Monsters Compose",
    size = IntSize(640, 480)
) {

    val state = remember { GameState() }
    val scope = currentRecomposeScope
    rememberCoroutineScope().launch {
        delay(5)
        state.update(System.nanoTime())
        scope.invalidate()
    }

//   https://github.com/JetBrains/compose-jb/issues/153#issuecomment-817831381
//   LaunchedEffect(state) {
//       while (true) {
//           withFrameNanos {
//               state.update(it)
//           }
//       }
//   }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .onSizeChanged {
            state.size = IntSize(it.width, it.height)
        }
        .clickable(
            onClick = {
                (0 until 1000).map { catchOne() }.forEach(cages::add)
                state.count = cages.size
            },
            // don't draw an animation on click
            indication = null,
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() })
    ) {
        val s = state.counter.roundToInt()
        cages.forEach {
            val w = size.width + it.width * density
            val h = size.height + it.height * density
            val x = ((it.x * w + s) % w - it.width * density).roundToInt()
            val y = ((it.y * h + s) % h - it.height * density).roundToInt()
            val c = it.source.width / SpriteImage32.SIZE
            val offset = (s / 20 % c * SpriteImage32.SIZE)
            drawIntoCanvas { canvas ->
                canvas.drawImageRect(
                    it.source,
                    srcOffset = IntOffset(offset, 0),
                    srcSize = IntSize(it.width, it.height),
                    dstOffset = IntOffset(x, y),
                    dstSize = IntSize((it.width * density).toInt(), (it.height * density).toInt()),
                    Paint().apply {
                        filterQuality = FilterQuality.None
                    }
                )
            }
        }
        state.fps = repaints.update()
    }

    Text("${state.count} monsters, fps = ${state.fps}",
        modifier = Modifier
            .offset(5.dp, 5.dp)
            .background(Color.White)
            .padding(5.dp)
    )
}

private val repaints = EventCounter(1000)

private class GameState {
    var size by mutableStateOf(IntSize(200, 200))
    var count by mutableStateOf(cages.size)
    var fps by mutableStateOf(0)
    var counter by mutableStateOf(0.0)

    private var time = System.nanoTime()
    private val px = 100 // pixels per second
    private val nanos = TimeUnit.SECONDS.toNanos(1)

    fun update(current: Long) {
        counter += (current - time) / nanos.toDouble() * px
        if (counter > px * 10000) {
            counter %= (px * 10000).toDouble()
        }
        time = current
    }
}

private val sprite = Monsters.values().asSequence().map {
    SpriteImage32.createOne(it.base64) { data ->
        org.jetbrains.skija.Image.makeFromEncoded(data).asImageBitmap()
    }
}.toList()

private fun catchOne() = SpriteImage32.catchOne(sprite[Random.nextInt(sprite.size)])

private val cages = mutableListOf<SpriteImage32<ImageBitmap>>(catchOne())