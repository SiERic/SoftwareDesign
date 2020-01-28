package ru.spbau.smirnov.cli.executor.commands

import ru.spbau.smirnov.cli.executor.Streams
import java.io.*

/**
 * Wc command.
 *
 * If no arguments are given, counts number of newlines, words and bytes from input stream
 * Otherwise counts same parameters for files listed in arguments and total parameters for all files
 * if there were at least two arguments
 */
class Wc(arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        val output = DataOutputStream(streams.outputStream)
        if (arguments.isEmpty()) {
            try {
                val (newlines, words, bytes) = calculateFile(streams.inputStream)
                output.writeBytes("$newlines $words $bytes\n")
            } catch (e: IOException) {
                streams.errorStream.println("Error in wc while reading from inputStream\n${e.message}")
                return 1
            }
        } else {
            var totalNewlines = 0
            var totalWords = 0
            var totalBytes = 0

            for (filename in arguments) {
                try {
                    FileInputStream(filename).use {
                        val (newlines, words, bytes) = calculateFile(it)
                        output.writeBytes("$newlines $words $bytes $filename\n")

                        totalNewlines += newlines
                        totalWords += words
                        totalBytes += bytes
                    }
                } catch (e: IOException) {
                    streams.errorStream.println("Error in wc while reading from ${filename}!\n${e.message}")
                    return 1
                }
            }
            if (arguments.size > 1) {
                try {
                    output.writeBytes("$totalNewlines $totalWords $totalBytes total\n")
                } catch(e: IOException) {
                    streams.errorStream.println("Error in wc.\n${e.message}")
                }
            }
        }
        return 0
    }

    private fun calculateFile(inputStream: InputStream): Triple<Int, Int, Int> {
        val bytes = DataInputStream(inputStream).readBytes()
        val string = String(bytes)
        val newlines = string.count { it.toString() == System.lineSeparator() }
        val words = string.split(' ', '\t', '\n').count { it.isNotEmpty() }
        return Triple(newlines, words, bytes.size)
    }
}
