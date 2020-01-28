package ru.spbau.smirnov.cli.executor.commands

import ru.spbau.smirnov.cli.Shell
import ru.spbau.smirnov.cli.executor.Streams

/**
 * Exit command.
 *
 * Exits shell after finishing current operation. Ignores arguments and input stream
 */
class Exit(private val shell: Shell, arguments: List<String>) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        shell.finishShell()
        return 0
    }
}
