package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.fillFiles

class CatTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun initFiles() {
            fillFiles()
        }
    }

    @Test
    fun `Should print file content`() {
        val cat = Cat(listOf(CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        CommandTestUtils.runExecutorTest(
            cat,
            "",
            "some" + System.lineSeparator() +
                "content" + System.lineSeparator() +
                "is" + System.lineSeparator() +
                "here" + System.lineSeparator() +
                "42" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print input if no arguments`() {
        val cat = Cat(listOf())
        CommandTestUtils.runExecutorTest(
            cat,
            "some input",
            "some input",
            ""
        )
    }

    @Test
    fun `Should print file content even if input stream is not empty`() {
        val cat = Cat(listOf(CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        CommandTestUtils.runExecutorTest(
            cat,
            "someInput",
            "some" + System.lineSeparator() +
                    "content" + System.lineSeparator() +
                    "is" + System.lineSeparator() +
                    "here" + System.lineSeparator() +
                    "42" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should concatenate two files passed as arguments`() {
        val cat = Cat(listOf(CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt", CommandTestUtils.resourcesDir + "AnotherFile.txt"))
        CommandTestUtils.runExecutorTest(
            cat,
            "someInput",
            "some" + System.lineSeparator() +
                    "content" + System.lineSeparator() +
                    "is" + System.lineSeparator() +
                    "here" + System.lineSeparator() +
                    "42" + System.lineSeparator() +
                    "Some" + System.lineSeparator() +
                    "Other awesome" + System.lineSeparator() +
                    "Content" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print error if cannot find file`() {
        val cat = Cat(listOf("a"))
        CommandTestUtils.runExecutorTest(cat, "", "", "Some error")
    }
}
