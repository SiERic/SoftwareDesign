package ru.spbau.smirnov.cli.executor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Shell
import ru.spbau.smirnov.cli.commands.Cat
import ru.spbau.smirnov.cli.commands.Echo
import ru.spbau.smirnov.cli.commands.Executable
import ru.spbau.smirnov.cli.commands.Exit
import ru.spbau.smirnov.cli.fillFiles
import java.io.*

class ExecutorTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun initFiles() {
            fillFiles()
        }
    }

    @Test
    fun `Should execute one command`() {
        testExecutor(
            listOf(
                Echo(listOf("42", "17"))
            ),
            "42 17${System.lineSeparator()}",
            0
        )
    }

    @Test
    fun `Should not crash on empty list`() {
        testExecutor(
            listOf(
            ),
            "",
            0
        )
    }

    @Test
    fun `Should execute many commands`() {
        testExecutor(
            listOf(
                Echo(listOf("42", "17")),
                Cat(listOf()),
                Cat(listOf())
            ),
            "42 17${System.lineSeparator()}",
            0
        )
    }

    @Test
    fun `Should first execute all and after that exit`() {
        testExecutor(
            listOf(
                Exit(Shell(), listOf()),
                Echo(listOf("42", "17")),
                Cat(listOf()),
                Cat(listOf())
            ),
            "42 17${System.lineSeparator()}",
            0
        )
    }

    @Test
    fun `Should fail at first error`() {
        testExecutor(
            listOf(
                Cat(listOf("fileThatDoesNotExist.txt")),
                Echo(listOf("42", "17"))
            ),
            "",
            1
        )
    }

    private fun testExecutor(input: List<Executable>, expectedOutput: String, expectedReturnCode: Int) {
        val inputStream = InputStream.nullInputStream()
        val outputStream = ByteArrayOutputStream()
        val errorStream = PrintStream(ByteArrayOutputStream())
        val streams = Streams(inputStream, outputStream, errorStream)
        assertEquals(expectedReturnCode, Executor.execute(input, streams))
        assertEquals(expectedOutput, String(outputStream.toByteArray()))
    }
}