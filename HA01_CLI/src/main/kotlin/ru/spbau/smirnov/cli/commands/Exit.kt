package ru.spbau.smirnov.cli.commands

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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is Exit) {
            return false
        }

        return shell === other.shell && arguments == other.arguments
    }
}
