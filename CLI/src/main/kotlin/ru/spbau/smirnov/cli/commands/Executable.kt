package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.executor.Streams

/** Interface for every command in cli */
abstract class Executable(
    /** Arguments for a command */
    protected val arguments: List<String>
) {
    /**
     * Executes command.
     *
     * Writes output to `streams.outputStream`, uses `streams.inputStream` as input,
     * writes errors to `streams.errorStream`
     * Doesn't throw exceptions
     * @return 0 if execution finished correctly, any other value otherwise
     */
    abstract fun execute(streams: Streams): Int
}
