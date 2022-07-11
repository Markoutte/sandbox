package me.markoutte.sandbox.coroutines

import com.formdev.flatlaf.FlatDarculaLaf
import kotlinx.coroutines.*
import java.awt.Dimension
import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val refresh = 16
const val animationDuration = 250
val easing: (Double) -> Double = { sin((it * PI) / 2) }

fun main() {
    FlatDarculaLaf.setup()
    val mainThread = Thread.currentThread()
    
    val frame = JFrame("Hello, world!")
    frame.rootPane.putClientProperty( "apple.awt.fullWindowContent", true)
    frame.rootPane.putClientProperty( "apple.awt.transparentTitleBar", true)
    frame.rootPane.putClientProperty( "apple.awt.windowTitleVisible", false)
    frame.title = null
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.preferredSize = Dimension(640, 480)
    
    frame.contentPane = JPanel(null).apply {
        add(JButton("Clicked me").apply {
            var job: Job? = null
            bounds = Rectangle(640 / 2 - 50, 480 / 2 - 20, 100, 40)
            addActionListener {
                job?.cancel()
                job = CoroutineScope(Dispatchers.Default).launch {
                    measureTimeMillis {
                        check(!SwingUtilities.isEventDispatchThread())
                        check(mainThread != Thread.currentThread())
                        val x = (x changeTo Random.nextInt(0, 100)).iterator()
                        val y = (y changeTo Random.nextInt(10, 40)).iterator()
                        val w = (width changeTo Random.nextInt(10, frame.width)).iterator()
                        val h = (height changeTo Random.nextInt(10, frame.height)).iterator()
                        for (i in 0 until animationDuration step refresh) {
                            delay(refresh.toLong())
                            withContext(EDT) {
                                check(SwingUtilities.isEventDispatchThread())
                                bounds = Rectangle(x.next(), y.next(), w.next(), h.next())
                            }
                        }
                        job = null
                    }.let(System.out::println)
                }
            }
        })
    }
    
    frame.pack()
    frame.isVisible = true
}

private infix fun Int.changeTo(value: Int): Sequence<Int> = sequence {
    val step = (value - this@changeTo) / animationDuration.toDouble()
    fun easing(i: Int) = easing(i / animationDuration.toDouble()) * animationDuration
    for (i in 1 .. animationDuration step refresh) {
        yield((this@changeTo + step * easing(i)).roundToInt())
    }
}

object EDT : CoroutineDispatcher() {
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !SwingUtilities.isEventDispatchThread()
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        SwingUtilities.invokeLater(block)
    }
}