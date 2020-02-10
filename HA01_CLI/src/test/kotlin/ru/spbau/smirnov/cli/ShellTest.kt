package ru.spbau.smirnov.cli

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ShellTest {
    private val resourcesDir = "src" + File.separator + "test" + File.separator + "resources" + File.separator

    companion object {
        @BeforeAll
        @JvmStatic
        fun initFiles() {
            fillFiles()
        }
    }

    @Test
    fun `Should start and exit shell`() {
        runShell(
            "exit" + System.lineSeparator(),
            "> "
        )
    }

    @Test
    fun `Should execute simple echo`() {
        runShell(
            "echo 4 2" + System.lineSeparator() +
                    "exit" + System.lineSeparator(),
            "> 4 2" + System.lineSeparator() +
                    "> "
        )
    }

    @Test
    fun `Should store variables`() {
        runShell(
            "FILE=${resourcesDir}example.txt" + System.lineSeparator() +
                    "cat \$FILE" + System.lineSeparator() +
                    "exit" + System.lineSeparator(),
            "> > Some example text" + System.lineSeparator() +
                    "> "
        )
    }

    @Test
    fun `Pipe should work`() {
        runShell(
            "FILE=${resourcesDir}example.txt" + System.lineSeparator() +
                    "cat \$FILE | wc" + System.lineSeparator() +
                    "exit" + System.lineSeparator(),
            "> > 1 3 ${17 + System.lineSeparator().length}" + System.lineSeparator() +
                    "> "
        )
    }

    @Test
    fun `Variable as a command should work`() {
        runShell(
            "x=exit" + System.lineSeparator() +
                "\$x", "> > "
        )
        runShell(
            "x=ex" + System.lineSeparator() +
                    "y=it" + System.lineSeparator() +
                    "\$x\$y",
            "> > > "
        )
    }

    private fun runShell(input: String, expectedOutput: String) {
        val inputStream = ByteArrayInputStream(input.toByteArray())
        val outputStream = ByteArrayOutputStream()
        // Just ignore errors. Hope that we found them on previous steps
        // and our tests doesn't cause errors
        val errorStream = System.err
        Shell().run(inputStream, PrintStream(outputStream), errorStream)

        assertEquals(expectedOutput, String(outputStream.toByteArray()))
    }
}
