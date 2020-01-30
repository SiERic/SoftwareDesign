package ru.spbau.smirnov.cli.executor.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.executor.commands.Wc

class WcTest {
    @Test
    fun `Should count one file`() {
        val wc = Wc(listOf(TestUtils.resourcesDir + "AnotherFile.txt"))
        TestUtils.runExecutorTest(
            wc,
            "some input",
            "3 4 27 ${TestUtils.resourcesDir}AnotherFile.txt\n",
            ""
        )
    }

    @Test
    fun `Should read input if no arguments passed`() {
        val wc = Wc(listOf())
        TestUtils.runExecutorTest(
            wc,
            "some input",
            "0 2 10\n",
            ""
        )
    }

    @Test
    fun `Should calculate total on two files`() {
        val wc = Wc(listOf(TestUtils.resourcesDir + "AnotherFile.txt", TestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        TestUtils.runExecutorTest(
            wc, "some input",
            """3 4 27 ${TestUtils.resourcesDir}AnotherFile.txt
            |5 5 24 ${TestUtils.resourcesDir}JustAFileWithSomeContent.txt
            |8 9 51 total
            |""".trimMargin(), ""
        )
    }

    @Test
    fun `Should print error if cannot find file`() {
        val wc = Wc(listOf("a"))
        TestUtils.runExecutorTest(
            wc,
            "some input",
            "",
            "some error"
        )
    }
}
