package me.markoutte.sandbox.profiling

import me.markoutte.sandbox.swing.SwingExample
import me.markoutte.sandbox.swing.monsters.EventCounter
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.util.*
import javax.swing.JComponent

fun main() {
    SwingExample.inFrame("Color of the Year", MyPanel()).isVisible = true
}

class MyPanel : JComponent() {
    override fun paintComponent(g: Graphics) {
        g as Graphics2D
        val width = width / 256f
        val height = height / 256f
        for (i in 0..255) {
            for (j in 0..255) {
                g.color = MyColor
                    .fromProperties("color")
                    .withSaturation(i / 256f)
                    .withBrightness(j / 256f)
                val rect = Rectangle2D.Float(width * i, height * (255 - j), width, height)
                g.fill(rect)
            }
        }
    }
}

class MyColor(val producer: () -> Color) : Color(0) {

    override fun getRGB(): Int {
        return producer().rgb
    }

    fun withSaturation(s: Float) = MyColor {
        withHSB { h, _, b ->
            getHSBColor(h, s, b)
        }
    }

    fun withBrightness(b: Float) = MyColor {
        withHSB { h, s, _ ->
            getHSBColor(h, s, b)
        }
    }

    private fun <R> withHSB(block: (hue: Float, saturation: Float, brightness: Float) -> R): R {
        val hsb = FloatArray(3)
        RGBtoHSB(red, green, blue, hsb)
        return block(hsb[0], hsb[1], hsb[2])
    }

    companion object {
        fun fromProperties(key: String) = MyColor {
            parseColor(property(key))
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun parseColor(hex: String) = MyColor {
            Color(Integer.parseInt(hex, 16))
        }
    }
}


private val properties = Properties().apply {
    put("color", "F5DF4D")
}

@Suppress("SameParameterValue")
private fun property(key: String) = properties.getProperty(key)

@Suppress("unused")
private val counter = EventCounter(100000)
