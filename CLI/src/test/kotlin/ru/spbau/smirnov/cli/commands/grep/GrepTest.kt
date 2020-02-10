package ru.spbau.smirnov.cli.commands.grep

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.commands.CommandTestUtils
import ru.spbau.smirnov.cli.fillFiles

internal class GrepTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun initFiles() {
            fillFiles()
        }
    }


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
            "some" + System.lineSeparator() +
                "content" + System.lineSeparator() +
                "here" + System.lineSeparator(),
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
            "42 is a number" + System.lineSeparator() +
                "it is not prime" + System.lineSeparator() +
                "but 42 is cool" + System.lineSeparator(),
            "42 is a number" + System.lineSeparator() +
                "but 42 is cool" + System.lineSeparator(),
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
            "some" + System.lineSeparator() +
                "content" + System.lineSeparator() +
                "is" + System.lineSeparator() +
                "here" + System.lineSeparator(),
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
            "content" + System.lineSeparator(),
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
            "prefixce42t" + System.lineSeparator() +
                "some str c ey 4242 42 est 42" + System.lineSeparator() +
                "\\ce4t{" + System.lineSeparator() +
                "certsuffix" + System.lineSeparator(),
            "some str c ey 4242 42 est 42" + System.lineSeparator() +
                "\\ce4t{" + System.lineSeparator(),
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
            "$firstFileName:content" + System.lineSeparator() +
                "$firstFileName-is" + System.lineSeparator() +
                "$secondFileName:Content" + System.lineSeparator(),
            ""
        )
    }
}
