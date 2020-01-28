package ru.spbau.smirnov.cli.executor.commands

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
                .writeBytes(environment.currentDirectory.toAbsolutePath().toString() + "\n")
        } catch(e: IOException) {
            streams.errorStream.println("Error in pwd.\n${e.message}")
            return 1
        }
        return 0
    }
}
