package ru.spbau.smirnov.cli.substitution

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import ru.spbau.smirnov.cli.executor.Streams
import ru.spbau.smirnov.cli.executor.commands.Executable
import java.io.*


object TestUtils {
    val resourcesDir = "src" + File.separator + "test" + File.separator + "resources" + File.separator

    fun runExecutorTest(command: Executable,
                        input: String,
                        expectedOutput: String,
                        expectedErrors: String) {

        val processInputStream = ByteArrayInputStream(input.toByteArray())
        val processOutputStream = ByteArrayOutputStream()
        val processErrorStream = ByteArrayOutputStream()

        val streams = Streams(processInputStream, processOutputStream, PrintStream(processErrorStream))
        command.execute(streams)

        assertEquals(expectedOutput, String(processOutputStream.toByteArray()))
        if (expectedErrors.isNotEmpty()) {
            // we cannot compare them because of languages, systems,...
            assertTrue(String(processErrorStream.toByteArray()).isNotEmpty())
        }
    }
}