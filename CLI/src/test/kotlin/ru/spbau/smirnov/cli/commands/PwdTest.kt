package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import java.nio.file.Paths

class PwdTest {
    @Test
    fun `Should write path`() {
        val expectedPath = Paths.get("").toAbsolutePath().toString()
        CommandTestUtils.runExecutorTest(
            Pwd(Environment(), listOf()),
            "some useless input",
            expectedPath + System.lineSeparator(),
            ""
        )
    }
}
