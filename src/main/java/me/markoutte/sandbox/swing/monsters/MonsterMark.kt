package me.markoutte.sandbox.swing.monsters

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.jetbrains.skija.Image
import org.jetbrains.skija.Rect
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong
import kotlin.random.Random

fun main() = Window(title = "Monsters Compose", size = IntSize(640, 480)) {

    val state = remember { GameState() }
    val scope = currentRecomposeScope

//   https://github.com/JetBrains/compose-jb/issues/153#issuecomment-817831381
   LaunchedEffect(state) {
       while (true) withFrameNanos {
           state.update(it)
           scope.invalidate()
       }
   }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .onSizeChanged {
            state.size = IntSize(it.width, it.height)
        }
        .clickable(
            onClick = {
                repeat(1000) { cages.add(catchOne()) }
                state.count = cages.size
            },
            // don't draw an animation on click
            indication = null,
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() })
    ) {
        val s = (state.counter * density).roundToLong()
        cages.forEach { cage ->
            val p = cage.calcPoint(s, state.size.width, state.size.height, density)
            val frame = state.time % state.nanos / (state.nanos / cage.source.width * SpriteImage32.SIZE)
            val offset = frame * SpriteImage32.SIZE
            drawIntoCanvas {
                it.nativeCanvas.drawImageRect(
                    cage.source,
                    Rect.makeXYWH(
                        offset.toFloat(),
                        0f,
                        cage.width.toFloat(),
                        cage.height.toFloat()
                    ),
                    Rect.makeXYWH(
                        p.x.toFloat(),
                        p.y.toFloat(),
                        cage.width.toFloat() * density,
                        cage.height.toFloat() * density
                    ),
                    null,
                    true
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

private val repaints = EventCounter(10000)

// we don't use mutable states here, because scope will be invalidate manually
private class GameState {
    var size = IntSize(200, 200)
    var count = cages.size
    var fps = 0
    var counter = 0.0
        private set
    var time = System.nanoTime()
        private set
    val px = 50 // pixels per second
    val nanos = TimeUnit.SECONDS.toNanos(1)

    fun update(current: Long) {
        counter += (current - time) / nanos.toDouble() * px
        if (counter > px * 10000) {
            counter %= (px * 10000).toDouble()
        }
        time = current
    }
}

private val sprite = Monsters.values().map {
    SpriteImage32.createOne(it.base64, Image::makeFromEncoded)
}

private fun catchOne() = SpriteImage32.catchOne(sprite[Random.nextInt(sprite.size)])

private val cages = mutableListOf(catchOne())