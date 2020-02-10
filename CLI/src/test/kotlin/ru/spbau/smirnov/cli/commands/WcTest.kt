package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.fillFiles

class WcTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun initFiles() {
            fillFiles()
        }
    }

    @Test
    fun `Should count one file`() {
        val wc = Wc(listOf(CommandTestUtils.resourcesDir + "AnotherFile.txt"))
        CommandTestUtils.runExecutorTest(
            wc,
            "some input",
            "3 4 ${24 + 3 * System.lineSeparator().length} ${CommandTestUtils.resourcesDir}AnotherFile.txt" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should read input if no arguments passed`() {
        val wc = Wc(listOf())
        CommandTestUtils.runExecutorTest(
            wc,
            "some input",
            "0 2 10" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should calculate total on two files`() {
        val wc = Wc(listOf(CommandTestUtils.resourcesDir + "AnotherFile.txt", CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        CommandTestUtils.runExecutorTest(
            wc,
            "some input",
            "3 4 ${24 + 3 * System.lineSeparator().length} ${CommandTestUtils.resourcesDir}AnotherFile.txt" + System.lineSeparator() +
                "5 5 ${19 + 5 * System.lineSeparator().length} ${CommandTestUtils.resourcesDir}JustAFileWithSomeContent.txt" + System.lineSeparator() +
                "8 9 ${43 + 8 * System.lineSeparator().length} total" + System.lineSeparator(),
            ""
        )
    }

    @Test
    fun `Should print error if cannot find file`() {
        val wc = Wc(listOf("a"))
        CommandTestUtils.runExecutorTest(
            wc,
            "some input",
            "",
            "some error"
        )
    }
}
