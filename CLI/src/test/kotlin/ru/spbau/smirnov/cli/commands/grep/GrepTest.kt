package ru.spbau.smirnov.cli.commands.grep

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.commands.CommandTestUtils

internal class GrepTest {
    @Test
    fun `Should work on simple test (file input)`() {
        val grep = Grep(
            listOf(
                "e", // pattern
                CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            "someInput",
            """some
                |content
                |here
                |""".trimMargin(),
            ""
        )
    }

    @Test
    fun `Should work on simple test (stream input)`() {
        val grep = Grep(
            listOf(
                "42" // pattern
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            """42 is a number
                |it is not prime
                |but 42 is cool
                |""".trimMargin(),
            """42 is a number
                |but 42 is cool
                |""".trimMargin(),
            ""
        )
    }

    @Test
    fun `Should work with case insensitive option`() {
        val grep = Grep(
            listOf(
                "CoNtEnT", // pattern
                "-i",
                CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            "input",
            "content\n",
            ""
        )
    }

    @Test
    fun `Should work without -i option`() {
        val grep = Grep(
            listOf(
                "CoNtEnT", // pattern
                CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            "input",
            "",
            ""
        )
    }

    @Test
    fun `Should work without -A option`() {
        val grep = Grep(
            listOf(
                "o", // pattern
                "-A",
                "2",
                CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            "input",
            """some
                |content
                |is
                |here
                |""".trimMargin(),
            ""
        )
    }

    @Test
    fun `Regexp in pattern should work`() {
        val grep = Grep(
            listOf(
                "c.*e.t", // pattern
                CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            "input",
            "content\n",
            ""
        )
    }

    @Test
    fun `Word match should work`() {
        val grep = Grep(
            listOf(
                "c.*e.t", // pattern
                "-w"
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            """prefixce42t
                |some str c ey 4242 42 est 42
                |\\ce4t{
                |certsuffix
                |""".trimMargin(),
            """some str c ey 4242 42 est 42
                |\\ce4t{
                |""".trimMargin(),
            ""
        )
    }

    @Test
    fun `Should work with many files`() {
        val firstFileName = CommandTestUtils.resourcesDir + "JustAFileWithSomeContent.txt"
        val secondFileName =  CommandTestUtils.resourcesDir + "AnotherFile.txt"

        val grep = Grep(
            listOf(
                "content", // pattern
                "-i",
                "-A",
                "1",
                firstFileName,
                secondFileName
            )
        )

        CommandTestUtils.runExecutorTest(
            grep,
            "input",
            """$firstFileName:content
                |$firstFileName-is
                |$secondFileName:Content
                |""".trimMargin(),
            ""
        )
    }
}
