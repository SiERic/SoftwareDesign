package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment

class UnknownCommandTest {
    @Test
    fun `Should run sort command`() {
        val command = UnknownCommand(Environment(), "sort",
            listOf(CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt")
        )
        CommandTestUtils.runExecutorTest(command, "some ignored\ninput", "42\ncontent\nhere\nis\nsome\n",
            "")
    }

    @Test
    fun `Should sort input if no arguments passed`() {
        val command = UnknownCommand(Environment(), "sort", listOf())
        CommandTestUtils.runExecutorTest(command, "some\ninput\n", "input\nsome\n", "")
    }

    @Test
    fun `Should print error on unknown command`() {
        val command = UnknownCommand(Environment(), "someUnknownCommand", listOf())
        CommandTestUtils.runExecutorTest(command, "some\ninput\n", "", "some errors")
    }
}