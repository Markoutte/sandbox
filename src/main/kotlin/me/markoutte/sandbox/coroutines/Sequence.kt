package me.markoutte.sandbox.coroutines

fun main() {
    val fib = sequence {
        var a = 0L
        var b = 1L
        while (a <= b) {
            yield(a)
            val c = a + b
            a = b
            b = c
        }
    }
    
    fib.forEach(System.out::println)
}