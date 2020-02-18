package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import java.nio.file.Paths
import org.junit.jupiter.api.Assertions.assertEquals

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
            Cd(environment, listOf("src/main/kotlin")),
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
            Cd(Environment(), listOf("test/resources/example.txt")),
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
}