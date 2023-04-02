package me.markoutte.sandbox.poc

import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object GetDirectory : Phase<String, Path?>("GetDirectory") {
    override suspend fun action(input: String, ctx: PipelineContext): Path? {
        return Paths.get(input).takeIf { p -> Files.exists(p) && Files.isDirectory(p) }
    }
}

object GetChildren : Phase<Path?, List<File>>("GetChildren") {
    override suspend fun action(input: Path?, ctx: PipelineContext): List<File> {
        return input?.toFile()?.listFiles()?.toList() ?: emptyList()
    }
}

object FilterOnlyFiles : Phase<List<File>, List<File>>("FilterOnlyFiles") {
    override suspend fun action(input: List<File>, ctx: PipelineContext): List<File> {
        return input.filter { f -> f.isFile }
    }
}

object ReadData : Phase<File, ByteArray?>("ReadData") {
    override suspend fun action(input: File, ctx: PipelineContext): ByteArray? {
        return try { input.readBytes() } catch (_: Throwable) { null }
    }
}

object FilterNonNull : Phase<List<ByteArray?>, List<ByteArray>>("FilterNonNull") {
    override suspend fun action(input: List<ByteArray?>, ctx: PipelineContext): List<ByteArray> {
        return input.filterNotNull()
    }
}

object Collect : Phase<List<ByteArray>, Long>("Collect") {
    override suspend fun action(input: List<ByteArray>, ctx: PipelineContext): Long {
        return input.sumOf { arr -> arr.size.toLong() }
    }
}

fun main() = runBlocking {
    var path: Path? = null
    val pipeline = pipeline(GetDirectory, Collect) {
        val remember = phase<Path?, Unit>("Init local variable with path") { v ->
            path = v
        }

        val log = phase<List<String>, Unit>("log file names") {
            it.forEach(::println)
        }

        GetDirectory sendTo GetChildren
        GetDirectory sendTo remember
        GetChildren sendTo FilterOnlyFiles
        FilterOnlyFiles forEach ReadData sendTo FilterNonNull
        FilterNonNull sendTo Collect
        // log filenames
        FilterOnlyFiles forEach { it.absolutePath } sendTo log
    }
    try {
        val size = pipeline.submit(System.getProperty("user.home"))
        println("$path has files with size $size")
    } catch (t: IOException) {
        print("An error occurred ${t.message}")
    }
}