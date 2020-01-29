package ru.spbau.smirnov.cli.substitution

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.executor.commands.Echo

class EchoTest {
    @Test
    fun `Should print newline if no arguments passed`() {
        val echo = Echo(listOf())
        TestUtils.runExecutorTest(echo, "some input", "\n", "")
    }

    @Test
    fun `Should print one argument`() {
        val echo = Echo(listOf("argument"))
        TestUtils.runExecutorTest(echo, "some input", "argument\n", "")
    }

    @Test
    fun `Should print some arguments separated with spaces`() {
        val echo = Echo(listOf("argument1", "argument2"))
        TestUtils.runExecutorTest(echo, "some input", "argument1 argument2\n", "")
    }
}
