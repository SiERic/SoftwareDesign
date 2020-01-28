package ru.spbau.smirnov.cli.executor.commands

import ru.spbau.smirnov.cli.executor.Streams

class UnknownCommand(private val commandName: String, arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {

        return 0
    }
}