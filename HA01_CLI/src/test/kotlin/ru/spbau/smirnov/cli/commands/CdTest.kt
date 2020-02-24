package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import java.nio.file.Paths
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class CdTest {
    @Test
    fun `Should go to inside folder`() {
        val expectedNewCurrentDirectory = Paths.get("", "src").toAbsolutePath().toString()
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf("src")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        assertEquals(expectedNewCurrentDirectory, environment.currentDirectory)
    }

    @Test
    fun `Should go deeper`() { // https://knowyourmeme.com/memes/we-need-to-go-deeper
        val expectedNewCurrentDirectory = Paths.get("", "src", "main", "kotlin").toAbsolutePath().toString()
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf("src" + File.separator + "main" + File.separator + "kotlin")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        assertEquals(expectedNewCurrentDirectory, environment.currentDirectory)
    }

    @Test
    fun `Should print error if cannot find directory`() {
        CommandTestUtils.runExecutorTest(
            Cd(Environment(), listOf("keksik")),
            "some useless input",
            "",
            "Some error"
        )
    }

    @Test
    fun `Should print error if argument is not a directory`() {
        CommandTestUtils.runExecutorTest(
            Cd(Environment(), listOf("src" + File.separator + "test" + File.separator + "resources" + File.separator + "example.txt")),
            "some useless input",
            "",
            "Some error"
        )
    }

    @Test
    fun `Should print error if there is more than 1 argument`() {
        CommandTestUtils.runExecutorTest(
            Cd(Environment(), listOf("keksik", "keksik")),
            "some useless input",
            "",
            "Some error"
        )
    }

    @Test
    fun `Should go home`() {
        val expectedNewCurrentDirectory = System.getProperty("user.home")
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf()),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        assertEquals(expectedNewCurrentDirectory, environment.currentDirectory)
    }

    @Test
    fun `Should go in the parent folder`() {
        val expectedNewCurrentDirectory = Paths.get("").toAbsolutePath().toString()
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf("src")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf("..")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        assertEquals(expectedNewCurrentDirectory, environment.currentDirectory)
    }

    @Test
    fun `Should go in the cousin folder`() {
        val expectedNewCurrentDirectory = Paths.get("", "src", "test").toAbsolutePath().toString()
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf("src" + File.separator + "main")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf(".." + File.separator + "test")),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        assertEquals(expectedNewCurrentDirectory, environment.currentDirectory)
    }

    @Test
    fun `Should correctly work with pwd`() {
        val pathToTestFolder = Paths.get("", "src", "test", "resources").toString()
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf(pathToTestFolder)),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        val absolutePathToTestFolder = Paths.get("", "src", "test", "resources").toAbsolutePath().toString()

        CommandTestUtils.runExecutorTest(
            Pwd(environment, listOf()),
            "some useless input",
            absolutePathToTestFolder + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should correctly work with cat`() {
        val pathToTestFolder = Paths.get("", "src", "test", "resources").toString()
        val environment = Environment()
        CommandTestUtils.runExecutorTest(
            Cd(environment, listOf(pathToTestFolder)),
            "some useless input",
            System.lineSeparator(),
            ""
        )
        CommandTestUtils.runExecutorTest(
            Cat(environment, listOf("example.txt")),
            "some useless input",
            "Some example text" + System.lineSeparator(),
            ""
        )
    }
}