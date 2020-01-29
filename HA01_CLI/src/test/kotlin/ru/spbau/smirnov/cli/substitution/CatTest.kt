package ru.spbau.smirnov.cli.substitution

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.executor.commands.Cat

class CatTest {
    @Test
    fun `Should print file content`() {
        val cat = Cat(listOf(TestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        TestUtils.runExecutorTest(cat, "", "some\ncontent\nis\nhere\n42\n", "")
    }

    @Test
    fun `Should print input if no arguments`() {
        val cat = Cat(listOf())
        TestUtils.runExecutorTest(cat, "some input", "some input", "")
    }

    @Test
    fun `Should print file content even if input stream is not empty`() {
        val cat = Cat(listOf(TestUtils.resourcesDir + "JustAFileWithSomeContent.txt"))
        TestUtils.runExecutorTest(cat, "someInput", "some\ncontent\nis\nhere\n42\n", "")
    }

    @Test
    fun `Should concatenate two files passed as arguments`() {
        val cat = Cat(listOf(TestUtils.resourcesDir + "JustAFileWithSomeContent.txt", TestUtils.resourcesDir + "AnotherFile.txt"))
        TestUtils.runExecutorTest(cat, "someInput",
            """some
              |content
              |is
              |here
              |42
              |Some
              |Other awesome
              |Content
              |""".trimMargin(), "")
    }

    @Test
    fun `Should print error if cannot find file`() {
        val cat = Cat(listOf("a"))
        TestUtils.runExecutorTest(cat, "", "", "Some error")
    }
}