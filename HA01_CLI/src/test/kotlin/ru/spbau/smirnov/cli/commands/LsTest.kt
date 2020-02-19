package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import java.io.File
import java.nio.file.Paths

class LsTest() {

    val pathToLsFolder = Paths.get("", "src", "test", "resources", "ls").toString()
    val ANSI_RESET = "\u001B[0m"
    val ANSI_BLUE = "\u001B[34m"

    @Test
    fun `Should list directory files`() {
        CommandTestUtils.runExecutorTest(
            Ls(Environment(), listOf(pathToLsFolder)),
            "some useless input",
            ANSI_BLUE + "heh" + ANSI_RESET + System.lineSeparator() +
            "kek" + System.lineSeparator() +
            "lol" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should list file files`() {
        CommandTestUtils.runExecutorTest(
            Ls(Environment(), listOf(pathToLsFolder + File.separator + "kek")),
            "some useless input",
            "kek" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should list empty folder files`() {
        CommandTestUtils.runExecutorTest(
            Ls(Environment(), listOf(pathToLsFolder + File.separator + "heh")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should list directory files with no args`() {
        var environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf(pathToLsFolder)),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        CommandTestUtils.runExecutorTest(
            Ls(environment, listOf()),
            "some useless input",
            ANSI_BLUE + "heh" + ANSI_RESET + System.lineSeparator() +
                    "kek" + System.lineSeparator() +
                    "lol" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print error if cannot find file`() {
        CommandTestUtils.runExecutorTest(
            Ls(Environment(), listOf("keksik")),
            "some useless input",
            "",
            "Some error"
        )
    }

    @Test
    fun `Should print error if there is more than 1 argument`() {
        CommandTestUtils.runExecutorTest(
            Ls(Environment(), listOf("keksik", "keksik")),
            "some useless input",
            "",
            "Some error"
        )
    }
}