package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams
import java.io.IOException

/**
 * Executes not built-in command
 *
 * Runs new process with environment from shell. Doesn't change environment after execution.
 * I.e. if `cd` command was called inside, in a shell it will not be seen
 */
class UnknownCommand(
    private val environment: Environment,
    private val commandName: String,
    arguments: List<String>
) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        try {
            val process = buildProcess()
            try {
                process.outputStream.write(streams.inputStream.readAllBytes())
                process.outputStream.close()
            } catch (ignored: IOException) {
                // if process output stream is closed, it started work with arguments
                // and no input stream will be used
            }
            process.waitFor()
            streams.outputStream.write(process.inputStream.readAllBytes())
            streams.errorStream.write(process.errorStream.readAllBytes())
            return process.exitValue()
        } catch (e: IOException) {
            streams.errorStream.println(e.message)
            return 1
        }
    }

    private fun buildProcess(): Process {
        val processBuilder = ProcessBuilder(
            mutableListOf(commandName).apply {
                addAll(arguments)
            }
        )
        environment.passEnvironmentalVariables(processBuilder)
        return processBuilder.start()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is UnknownCommand) {
            return false
        }

        return environment === other.environment && commandName == other.commandName && arguments == other.arguments
    }
}
