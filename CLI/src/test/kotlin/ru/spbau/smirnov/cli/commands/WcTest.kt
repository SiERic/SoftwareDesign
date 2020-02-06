package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test

class WcTest {
    @Test
    fun `Should count one file`() {
        val wc = Wc(listOf(CommandTestUtils.resourcesDir + "AnotherFile.txt"))
        CommandTestUtils.runExecutorTest(
            wc,
            "some input",
            "3 4 27 ${CommandTestUtils.resourcesDir}AnotherFile.txt\n",
            ""
        )
    }

    @Test
    fun `Should read input if no arguments passed`() {
        val wc = Wc(listOf())
        CommandTestUtils.runExecutorTest(
            wc,
            "some input",
            "0 2 10\n",
            ""
        )
    }

    @Test
    fun `Should calculate total on two files`() {
        val wc = Wc(listOf(CommandTestUtils.resourcesDir + "AnotherFile.txt", CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        CommandTestUtils.runExecutorTest(
            wc, "some input",
            """3 4 27 ${CommandTestUtils.resourcesDir}AnotherFile.txt
            |5 5 24 ${CommandTestUtils.resourcesDir}JustAFileWithSomeContent.txt
            |8 9 51 total
            |""".trimMargin(), ""
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
