package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class LsTest() {

    private val pathToLsFolder = Paths.get("", "src", "test", "resources", "ls").toString()
    private val ANSI_RESET = "\u001B[0m"
    private val ANSI_BLUE = "\u001B[34m"

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
        Files.createDirectory(Paths.get(pathToLsFolder, "heh", "wow"))
        CommandTestUtils.runExecutorTest(
            Ls(Environment(), listOf(pathToLsFolder + File.separator + "heh" + File.separator + "wow")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        Files.deleteIfExists(Paths.get(pathToLsFolder, "heh", "wow"))
    }

    @Test
    fun `Should list directory files with no args`() {
        val environment = Environment()
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