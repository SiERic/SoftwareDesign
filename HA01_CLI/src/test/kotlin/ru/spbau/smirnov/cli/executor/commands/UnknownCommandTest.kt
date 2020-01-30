package ru.spbau.smirnov.cli.executor.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.commands.TestUtils
import ru.spbau.smirnov.cli.executor.commands.UnknownCommand

class UnknownCommandTest {
    @Test
    fun `Should run sort command`() {
        val command = UnknownCommand(Environment(), "sort",
            listOf(TestUtils.resourcesDir + "JustAFileWithSomeContent.txt")
        )
        TestUtils.runExecutorTest(command, "some ignored\ninput", "42\ncontent\nhere\nis\nsome\n",
            "")
    }

    @Test
    fun `Should sort input if no arguments passed`() {
        val command = UnknownCommand(Environment(), "sort", listOf())
        TestUtils.runExecutorTest(command, "some\ninput\n", "input\nsome\n", "")
    }

    @Test
    fun `Should print error on unknown command`() {
        val command = UnknownCommand(Environment(), "someUnknownCommand", listOf())
        TestUtils.runExecutorTest(command, "some\ninput\n", "", "some errors")
    }

}