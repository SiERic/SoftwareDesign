package ru.spbau.smirnov.cli.executor

import ru.spbau.smirnov.cli.commands.Executable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/** Class for executing operations */
object Executor {
    /**
     * Executes operation (set of commands, connected with pipe).
     *
     * Executes them consistently. If some command's return code is not zero, stops invocation
     * and returns this code. Otherwise returns 0.
     * First command takes `streams.inputStream` as input stream
     * Last command takes `streams.outputStream` as output stream
     * Every command takes `streams.errorStream` as error stream
     */
    fun execute(commands: List<Executable>, streams: Streams): Int {
        var currentInputStream = streams.inputStream

        val currentCommandIterator = commands.iterator()
        while (currentCommandIterator.hasNext()) {
            val command = currentCommandIterator.next()
            val currentOutputStream = if (!currentCommandIterator.hasNext()) {
                streams.outputStream
            } else {
                ByteArrayOutputStream()
            }

            val resultCode = command.execute(Streams(currentInputStream, currentOutputStream, streams.errorStream))

            if (resultCode != 0) {
                return resultCode
            }

            if (currentCommandIterator.hasNext()) {
                val byteArray = (currentOutputStream as ByteArrayOutputStream).toByteArray()
                currentInputStream = ByteArrayInputStream(byteArray)

                currentOutputStream.close()
            }
        }
        return 0
    }
}
