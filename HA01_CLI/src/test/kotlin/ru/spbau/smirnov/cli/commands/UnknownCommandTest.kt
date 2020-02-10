package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.fillFiles

class UnknownCommandTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun initFiles() {
            fillFiles()
        }
    }

    @Test
    fun `Should run sort command`() {
        val command = UnknownCommand(Environment(), "sort",
            listOf(CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt")
        )
        CommandTestUtils.runExecutorTest(
            command,
            "some ignored" + System.lineSeparator() +
                "input" + System.lineSeparator(),
            "42" + System.lineSeparator() +
                "content" + System.lineSeparator() +
                "here" + System.lineSeparator() +
                "is" + System.lineSeparator() +
                "some" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should sort input if no arguments passed`() {
        val command = UnknownCommand(Environment(), "sort", listOf())
        CommandTestUtils.runExecutorTest(
            command,
            "some" + System.lineSeparator() +
                "input" + System.lineSeparator(),
            "input" + System.lineSeparator() +
                    "some" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print error on unknown command`() {
        val command = UnknownCommand(Environment(), "someUnknownCommand", listOf())
        CommandTestUtils.runExecutorTest(
            command,
            "some" + System.lineSeparator() +
                    "input" + System.lineSeparator(),
            "",
            "some errors"
        )
    }
}