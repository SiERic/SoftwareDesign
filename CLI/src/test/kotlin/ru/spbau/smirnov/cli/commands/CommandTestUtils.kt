package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import ru.spbau.smirnov.cli.executor.Streams
import java.io.*


object CommandTestUtils {
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

        // we cannot compare them because of languages, systems, ...
        // so just check that error exists or not
        assertTrue(String(processErrorStream.toByteArray()).isNotEmpty() == expectedErrors.isNotEmpty())
    }
}
