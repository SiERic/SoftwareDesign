package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.nio.file.Paths

class Ls(private val environment: Environment, arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        if (arguments.size > 1) {
            streams.errorStream.println("Error in ls. Too many arguments")
            return 1
        }
        if (arguments.isEmpty()) {
            return ls(Paths.get(environment.currentDirectory, "").toAbsolutePath().normalize().toFile(), streams)
        }
        if (arguments.size == 1) {
            return ls(Paths.get(environment.currentDirectory, arguments[0]).toAbsolutePath().normalize().toFile(), streams)
        }
        return 0
    }

    private fun ls(file: File, streams: Streams) : Int {
        if (!file.exists()) {
            streams.errorStream.println("Error in ls. No such file or directory")
            return 1
        }
        try {
            DataOutputStream(streams.outputStream).writeBytes(
                file.listFiles().map {toColoredPath(it)}.sorted().joinToString(
                    System.lineSeparator())
                        + System.lineSeparator()
            )
        } catch (e : IOException) {
            streams.errorStream.println("Error in ls${System.lineSeparator()}${e.message}")
            return 1
        }
        return 0
    }

    private fun toColoredPath(file : File) : String {
        val ANSI_RESET = "\u001B[0m"
        val ANSI_GREEN = "\u001B[32m"
        val ANSI_BLUE = "\u001B[34m"
        if (file.isDirectory) {
            return ANSI_BLUE + file.name + ANSI_RESET
        }
        if (file.canExecute()) {
            return ANSI_GREEN + file.name + ANSI_RESET
        }
        return file.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Ls) {
            return false
        }

        return arguments == other.arguments
    }
}
