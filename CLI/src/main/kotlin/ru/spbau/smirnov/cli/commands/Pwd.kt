package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams
import java.io.DataOutputStream
import java.io.IOException


/**
 * Pwd command.
 *
 * Prints current directory. Ignores arguments and input stream
 */
class Pwd(private val environment: Environment, arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        try {
            DataOutputStream(streams.outputStream)
                .writeBytes(environment.currentDirectory + System.lineSeparator())
        } catch(e: IOException) {
            streams.errorStream.println("Error in pwd.${System.lineSeparator()}${e.message}")
            return 1
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Pwd) {
            return false
        }

        return environment === other.environment && arguments == other.arguments
    }
}
