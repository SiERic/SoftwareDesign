package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams
import java.io.*
import java.nio.file.Paths

/**
 * Cat command.
 *
 * Concatenates files listed in `arguments` to `streams.outputStream`
 * If `arguments` is empty, prints only `streams.inputStream`
 */
class Cat(private val environment: Environment, arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        val output = DataOutputStream(streams.outputStream)
        if (arguments.isEmpty()) {
            try {
                output.writeBytes(String(DataInputStream(streams.inputStream).readBytes()))
            } catch (e: IOException) {
                streams.errorStream.println("Error in cat while reading from inputStream!${System.lineSeparator()}${e.message}")
                return 1
            }
        } else {
            for (filename in arguments) {
                val filePath = Paths.get(environment.currentDirectory, filename).toAbsolutePath().toString()
                try {
                    FileInputStream(filePath).use {
                        output.writeBytes(String(DataInputStream(it).readBytes()))
                    }
                } catch (e: IOException) {
                    streams.errorStream.println("Error in cat while reading file $filename!${System.lineSeparator()}${e.message}")
                    return 1
                }
            }
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Cat) {
            return false
        }

        return arguments == other.arguments
    }
}
