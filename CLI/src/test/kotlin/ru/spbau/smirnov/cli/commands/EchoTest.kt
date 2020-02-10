package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test

class EchoTest {
    @Test
    fun `Should print newline if no arguments passed`() {
        val echo = Echo(listOf())
        CommandTestUtils.runExecutorTest(
            echo,
            "some input",
            System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print one argument`() {
        val echo = Echo(listOf("argument"))
        CommandTestUtils.runExecutorTest(
            echo,
            "some input",
            "argument" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print some arguments separated with spaces`() {
        val echo = Echo(listOf("argument1", "argument2"))
        CommandTestUtils.runExecutorTest(
            echo,
            "some input",
            "argument1 argument2" + System.lineSeparator(),
            ""
        )
    }
}
