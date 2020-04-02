package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams
import java.io.DataOutputStream
import java.io.IOException
import java.nio.file.Paths

/**
 * Cd command.
 *
 * Changes the current working directory.
 * With no arguments goes to home directory, else to the given one.
 * Ignores input stream.
 */
class Cd(private val environment: Environment, arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        if (arguments.size > 1) {
            streams.errorStream.println("Error in cd. Too many arguments")
            return 1
        }
        if (arguments.isEmpty()) {
            environment.currentDirectory = System.getProperty("user.home")
        }
        else if (arguments.size == 1) {
            val newPath = Paths.get(environment.currentDirectory, arguments[0]).toAbsolutePath().normalize().toFile()
            if (!newPath.exists()) {
                streams.errorStream.println("Error in cd. No such file or directory")
                return 1
            }
            if (!newPath.isDirectory) {
                streams.errorStream.println("Error in cd. Not a directory")
                return 1
            }
            environment.currentDirectory = newPath.absolutePath
        }
        try {
            DataOutputStream(streams.outputStream).writeBytes(System.lineSeparator())
        } catch (e : IOException) {
            streams.errorStream.println("Error in cd${System.lineSeparator()}${e.message}")
            return 1
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Cd) {
            return false
        }

        return arguments == other.arguments
    }
}
