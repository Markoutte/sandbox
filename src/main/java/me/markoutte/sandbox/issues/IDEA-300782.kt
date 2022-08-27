package me.markoutte.sandbox.issues

fun foo1(value: Double) {
    val v = value
    println(value == value)
}

fun foo2(value: Double) {
    println(value == value)
}

fun main() {
    val a = Double.NaN
    println(a == a)
}