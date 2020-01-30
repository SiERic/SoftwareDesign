package ru.spbau.smirnov.cli

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ShellTest {
    private val resourcesDir = "src" + File.separator + "test" + File.separator + "resources" + File.separator

    @Test
    fun `Should start and exit shell`() {
        runShell("exit\n", "> ")
    }

    @Test
    fun `Should execute simple echo`() {
        runShell("echo 4 2\nexit\n", "> 4 2\n> ")
    }

    @Test
    fun `Should store variables`() {
        runShell("FILE=${resourcesDir}example.txt\ncat \$FILE\nexit\n", "> > Some example text\n> ")
    }

    @Test
    fun `Pipe should work`() {
        runShell("FILE=${resourcesDir}example.txt\ncat \$FILE | wc\nexit\n", "> > 1 3 18\n> ")
    }

    @Test
    fun `Variable as a command should work`() {
        runShell("x=exit\n\$x", "> > ")
        runShell("x=ex\ny=it\n\$x\$y", "> > > ")
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
