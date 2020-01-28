package ru.spbau.smirnov.cli.executor.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams

/**
 * Assignment operation. Stores new environmental variable.
 *
 * `arguments[0]` must be variable name, `arguments[1]` must be value
 * Other parameters will be ignored.
 * Streams input is also ignored.
 */
class Assignment(
    private val environment: Environment,
    arguments: List<String>
) : Executable(arguments) {
    override fun execute(streams: Streams): Int {
        environment.assignVariable(arguments[0], arguments[1])
        return 0
    }
}
