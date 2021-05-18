package me.markoutte.sandbox.profiling

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.markoutte.sandbox.swing.SwingExample
import me.markoutte.sandbox.swing.monsters.EventCounter
import org.openjdk.jmh.infra.Blackhole
import java.awt.*
import java.awt.geom.Rectangle2D
import javax.swing.*
import kotlin.properties.Delegates

class MyPanel : JComponent() {

    var hue : Int by Delegates.observable(0) { _, _, _ ->
        repaint()
    }

    private val colors: Array<MyColor> = Array(256 * 256) { i ->
        MyColor {
            Blackhole.consumeCPU(600)
            Color.getHSBColor(
                hue / 256f,
                (i % 256) / 256f,
                (i / 256) / 256f
            )
        }
    }

    class MyColor(val producer: () -> Color) : Color(0) {
        override fun getRGB(): Int {
            return producer().rgb
        }
    }

//    private var cached: Array<Color>? = null
//
//    var isCached: Boolean = false
//        set(value) {
//            field = value
//            cached = if (value) Array(colors.size) { i ->
//                Color(colors[i].rgb)
//            } else null
//        }
//        get() = cached != null

    override fun paintComponent(g: Graphics) {
        val threshold = 5
        val width = width * threshold / 256f
        val height = height * threshold / 256f
        for (i in 0..255 step threshold) {
            for (j in 0..255 step threshold) {
                g.color = colors[j * 256 + i]//(cached ?: colors)[j * 256 + i]
                (g as Graphics2D).fill(
                    Rectangle2D.Float(
                        width * i / threshold,
                        height * (255 - j) / threshold,
                        width,
                        height
                    )
                )
            }
        }

        counter.update()
    }

    private val counter = EventCounter(100000)

    val count: Int
        get() = counter.size()
}

fun main() {
    val panel = MyPanel()
    val label = JLabel()
    SwingExample.inFrame("Theme Colors", JPanel().apply {
        layout = BorderLayout()
        panel.layout = null
        label.isOpaque = true
        label.background = Color.WHITE
        panel.add(label)
        val slider = JSlider(0, 255, 0)
        slider.addChangeListener {
            panel.hue = slider.value
        }
//        val checkbox = JCheckBox("Use cache")
//        checkbox.addActionListener {
//            panel.isCached = checkbox.isSelected
//        }
        add(panel, BorderLayout.CENTER)
        add(JPanel().apply {
            layout = BoxLayout(this, BoxLayout.LINE_AXIS)
//            add(checkbox)
            add(slider)
        }, BorderLayout.SOUTH)
    }).isVisible = true
    GlobalScope.launch {
        @Suppress("BlockingMethodInNonBlockingContext")
        while (true) {
            delay(1)
            SwingUtilities.invokeAndWait {
                panel.paintImmediately(panel.visibleRect)
            }
            label.text = "Count: ${panel.count}"
            label.bounds = Rectangle(5, 5, label.preferredSize.width, label.preferredSize.height)
        }
    }
}