package ru.spbau.smirnov.cli.commands

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
                output.writeBytes("$newlines $words $bytes${System.lineSeparator()}")
            } catch (e: IOException) {
                streams.errorStream.println("Error in wc while reading from inputStream${System.lineSeparator()}${e.message}")
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
                        output.writeBytes("$newlines $words $bytes $filename${System.lineSeparator()}")

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
                    output.writeBytes("$totalNewlines $totalWords $totalBytes total${System.lineSeparator()}")
                } catch(e: IOException) {
                    streams.errorStream.println("Error in wc.${System.lineSeparator()}${e.message}")
                }
            }
        }
        return 0
    }

    private fun countNewlines(string: String): Int {
        val lineSeparatorLength = System.lineSeparator().length
        var lastIndex = -lineSeparatorLength
        var ctr = 0
        while (true) {
            lastIndex = string.indexOf(System.lineSeparator(), lastIndex + lineSeparatorLength)
            if (lastIndex == -1) {
                break
            }
            ctr++
        }
        return ctr
    }

    private fun calculateFile(inputStream: InputStream): Triple<Int, Int, Int> {
        val bytes = DataInputStream(inputStream).readBytes()
        val string = String(bytes)
        val newlines = countNewlines(string)
        val words = string.split(' ', '\t', '\n').count { it.isNotEmpty() }
        return Triple(newlines, words, bytes.size)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Wc) {
            return false
        }

        return arguments == other.arguments
    }
}
