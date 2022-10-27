package me.markoutte.sandbox.algorithms.tree

import org.yaml.snakeyaml.util.ArrayStack
import java.util.regex.Pattern

class Tree<T> {
    
    var root: Node? = null
    
    inner class Node(
        var key: T,
        var left: Node? = null,
        var right: Node? = null,
    )

    fun traverse(traverse: Traverse) = sequence {
        when (traverse) {
            Traverse.DEEP_PRE, Traverse.DEEP_IN, Traverse.DEEP_POST -> traverseDepth(root, traverse)
            Traverse.BREADTH -> traverseBreadth(root)
            Traverse.DEEP_PRE_ITER -> traverseDepthIterPre(root)
            Traverse.DEEP_IN_ITER -> traverseDepthIterIn(root)
            Traverse.DEEP_POST_ITER -> traverseDepthIterPost(root)
        }
    }

    private suspend fun SequenceScope<T>.traverseDepthIterPre(root: Node?) {
        if (root == null) return
        val stack = ArrayStack<Node>(20)
        stack.push(root)
        while (stack.isEmpty.not()) {
            val next = stack.pop()
            yield(next.key)
            if (next.right != null) stack.push(next.right!!)
            if (next.left != null) stack.push(next.left!!)
        }
    }

    private suspend fun SequenceScope<T>.traverseDepthIterIn(root: Node?) {
        var node = root
        val stack = ArrayStack<Node>(20)
        while (stack.isEmpty.not() || node != null) {
            if (node != null) {
                stack.push(node)
                node = node.left
            } else {
                node = stack.pop()
                yield(node.key)
                node = node.right
            }
        }
    }

    private suspend fun SequenceScope<T>.traverseDepthIterPost(root: Node?) {
        var node = root
        var lastNodeVisited: Node? = null
        val stack = ArrayDeque<Node>(20)
        while (stack.isNotEmpty() || node != null) {
            if (node != null) {
                stack.addLast(node)
                node = node.left
            } else {
                val peekNode = stack.last()
                if (peekNode.right != null && lastNodeVisited != peekNode.right) {
                    node = peekNode.right
                } else {
                    yield(peekNode.key)
                    lastNodeVisited = stack.removeLast()
                }
            }
        }
    }

    private suspend fun SequenceScope<T>.traverseDepth(node: Node?, traverse: Traverse) {
        if (node == null) return
        if (traverse == Traverse.DEEP_PRE) yield(node.key)
        traverseDepth(node.left, traverse)
        if (traverse == Traverse.DEEP_IN) yield(node.key)
        traverseDepth(node.right, traverse)
        if (traverse == Traverse.DEEP_POST) yield(node.key)
    }

    private suspend fun SequenceScope<T>.traverseBreadth(node: Node?) {
        if (node == null) return
        val queue = ArrayDeque(listOf(node))
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            yield(next.key)
            if (next.left != null) queue.add(next.left!!)
            if (next.right != null) queue.add(next.right!!)
        }
    }
    
    enum class Traverse {
        DEEP_PRE, DEEP_IN, DEEP_POST, 
        BREADTH,
        DEEP_PRE_ITER, DEEP_IN_ITER, DEEP_POST_ITER,
    }
}

fun parse(str: String): Tree<Int> {
    val tree = Tree<Int>()
    val stack = ArrayStack<Tree<Int>.Node>(100)
    val matches = Pattern.compile("\\(\\s*(\\d+)|[)]").matcher(str)
    while (matches.find()) {
        val next = matches.group(0)
        if (next.startsWith('(')) {
            val node = tree.Node(matches.group(1).toInt())
            if (stack.isEmpty) {
                tree.root = node
            } else {
                val parent = stack.pop()
                if (parent.left == null) {
                    parent.left = node
                }
                else if (parent.right == null) {
                    parent.right = node
                } else {
                    error("Too many nodes")
                }
                stack.push(parent)
            }
            stack.push(node)
        }
        else if (next == ")") {
            stack.pop()
        } else {
            error("Bad input string $str")
        }
    }
    return tree
}

fun main() {
    val tree = parse("(8 (3 (1) (6 (4) (7))) (10 (14 (13))))")
    println(listOf(8, 3, 1, 6, 4, 7, 10, 14, 13) == tree.traverse(Tree.Traverse.DEEP_PRE).toList())
    println(listOf(1, 3, 4, 6, 7, 8, 13, 14, 10) == tree.traverse(Tree.Traverse.DEEP_IN).toList())
    println(listOf(1, 4, 7, 6, 3, 13, 14, 10, 8) == tree.traverse(Tree.Traverse.DEEP_POST).toList())
    println(listOf(8, 3, 10, 1, 6, 14, 4, 7, 13) == tree.traverse(Tree.Traverse.BREADTH).toList())
    println(listOf(8, 3, 1, 6, 4, 7, 10, 14, 13) == tree.traverse(Tree.Traverse.DEEP_PRE_ITER).toList())
    println(listOf(1, 3, 4, 6, 7, 8, 13, 14, 10) == tree.traverse(Tree.Traverse.DEEP_IN_ITER).toList())
    println(listOf(8, 3, 1, 6, 4, 7, 10, 14, 13) == tree.traverse(Tree.Traverse.DEEP_PRE_ITER).toList())
}