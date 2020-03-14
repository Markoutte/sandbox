package me.markoutte.sandbox.algorithms.graphs

import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

/**
 *  A Fast Algorithm for Finding Dominators in a Flowgraph
 *  THOMAS LENGAUER and ROBERT ENDRE TARJAN
 *  Stanford University
 */
val root = 'R'
val graph = mapOf(
        'R' to listOf('C', 'B', 'A'),
        'C' to listOf('F', 'G'),
        'F' to listOf('I'),
        'G' to listOf('I', 'J'),
        'I' to listOf('K'),
        'J' to listOf('I'),
        'B' to listOf('E', 'A', 'D'),
        'K' to listOf('R', 'I'),
        'E' to listOf('H'),
        'H' to listOf('K', 'E'),
        'D' to listOf('L'),
        'L' to listOf('H'),
        'A' to listOf('D')
)
val result = """
        R -> (1, *), *
        C -> (2, R), R
        F -> (3, C), C
        I -> (4, R), R
        K -> (5, R), R
        G -> (6, C), C
        J -> (7, G), G
        B -> (8, R), R
        E -> (9, R), R
        H -> (10, R), R
        A -> (11, R), R
        D -> (12, B), R
        L -> (13, D), D
""".trimIndent()
val V = ArrayList(graph.keys).sorted()

private fun Char.index(): Int = maxOf(V.binarySearch(this) + 1, 0)

private fun Int.char(): Char = if (this == 0) '*' else V[this - 1]

fun succ(v: Int): List<Int> = if (v == 0) listOf(root.index()) else graph[v.char()]?.map { it.index() } ?: emptyList()

// init
var n = 0
val pred = mutableMapOf<Int, MutableList<Int>>()
val semi = IntArray(V.size + 1)
val vertex = IntArray(V.size + 1)
val parent = IntArray(V.size + 1)
val ancestor = IntArray(V.size + 1)
val label = IntArray(V.size + 1) { it }
val bucket = mutableMapOf<Int, MutableList<Int>>()
val dom = IntArray(V.size + 1) { it }

fun main() {
    for (v in 0..V.size) {
        pred[v] = mutableListOf()
        bucket[v] = CopyOnWriteArrayList()
    }

    // step 1
    dfs(0)

    for (i in n - 1 downTo 1) {
        val w = vertex[i]
        // step 2
        pred[w]!!.forEach { v ->
            val u = eval(v)
            if (semi[u] < semi[w]) {
                semi[w] = semi[u]
            }
        }
        bucket[vertex[semi[w]]]!!.add(w)
        link(parent[w], w)
        // step 3
        bucket[parent[w]]!!.forEach { v ->
            bucket[parent[w]]!!.remove(v)
            val u = eval(v)
            dom[v] = if (semi[u] < semi[v]) u else parent[w]
        }
    }

    // step 4
    for (i in 1 until n) {
        val w = vertex[i]
        if (dom[w] != vertex[semi[w]]) {
            dom[w] = dom[dom[w]]
        }
    }
    dom[0] = 0

    println("NODE -> (DFS, SDOM), IDOM")
    println("=========================")
    val sb = StringBuilder()

    vertex.forEachIndexed { dfs, node ->
        if (dfs != 0) {
            sb.append("${node.char()} -> (${dfs}, ${vertex[semi[node]].char()}), ${dom[node].char()}")
            if (dfs != V.size) {
                sb.append('\n')
            }
        }
    }
    if (sb.toString() != result) {
        System.err.println("EXPECTED RESULT:")
        System.err.println(result)
        System.err.println("\nBUT HAS:")
        System.err.println(sb)
        System.err.flush()
    } else {
        println(sb)
    }
}

// DFS

fun dfs(v: Int) {
    semi[v] = n
    vertex[n] = v
    n += 1
    succ(v).forEach { w ->
        if (semi[w] == 0) {
            parent[w] = v
            dfs(w)
        }
        pred[w]?.add(v) ?: error("$w")
    }
}

fun eval(v: Int) : Int {
    return if (ancestor[v] == 0) {
        v
    } else {
        compress(v)
        label[v]
    }
}

fun compress(v: Int) {
    assert(ancestor[v] != 0)
    if (ancestor[ancestor[v]] != 0) {
        compress(ancestor[v])
        if (semi[label[ancestor[v]]] < semi[label[v]]) {
            label[v] = label[ancestor[v]]
        }
        ancestor[v] = ancestor[ancestor[v]]
    }
}

fun link(v: Int, w: Int) {
    ancestor[w] = v
}