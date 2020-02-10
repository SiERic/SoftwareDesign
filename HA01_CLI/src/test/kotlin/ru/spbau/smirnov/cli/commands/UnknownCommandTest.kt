package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment

class UnknownCommandTest {
    @Test
    fun `Should run sort command`() {
        val command = UnknownCommand(Environment(), "sort",
            listOf(CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt")
        )
        CommandTestUtils.runExecutorTest(
            command,
            """some ignored
                |input
            """.trimMargin(),
            """42
                |content
                |here
                |is
                |some
                |
            """.trimMargin(),
            ""
        )
    }

    @Test
    fun `Should sort input if no arguments passed`() {
        val command = UnknownCommand(Environment(), "sort", listOf())
        CommandTestUtils.runExecutorTest(
            command,
            """some
                |input
                |
            """.trimMargin(),
            """input
                |some
                |
            """.trimMargin(),
            ""
        )
    }

    @Test
    fun `Should print error on unknown command`() {
        val command = UnknownCommand(Environment(), "someUnknownCommand", listOf())
        CommandTestUtils.runExecutorTest(
            command,
            """some
                |input
                |
            """.trimMargin(),
            "",
            "some errors"
        )
    }
}