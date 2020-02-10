package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.executor.Streams
import java.io.DataOutputStream
import java.io.IOException

/**
 *  Echo command.
 *
 *  Ignores `streams.inputStream`. Prints all arguments listed in `arguments` splitted with space
 */
class Echo(arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        try {
            DataOutputStream(streams.outputStream)
                .writeBytes(arguments.joinToString(separator = " ") + System.lineSeparator())
        } catch (e: IOException) {
            streams.errorStream.println("Error in echo${System.lineSeparator()}${e.message}")
            return 1
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Echo) {
            return false
        }

        return arguments == other.arguments
    }
}
