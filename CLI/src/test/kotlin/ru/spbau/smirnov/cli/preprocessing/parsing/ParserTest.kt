package ru.spbau.smirnov.cli.preprocessing.parsing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.Shell
import ru.spbau.smirnov.cli.commands.ExecutableFactory
import ru.spbau.smirnov.cli.commands.*

class ParserTest {

    private val environment = Environment()
    private val shell = Shell()
    private val executableFactory = ExecutableFactory(environment, shell)
    private val parser = Parser(executableFactory)

    @Test
    fun `Should parse empty line`() {
        assertEquals(listOf<Executable>(), parser.parse(""))
    }

    @Test
    fun `Should parse single command without parameters`() {
        val cat = Cat(listOf())
        assertEquals(listOf<Executable>(cat), parser.parse("cat"))
    }

    @Test
    fun `Should parse single command with parameters`() {
        val wc = Wc(listOf("parameter1", "parameter2"))
        assertEquals(listOf<Executable>(wc), parser.parse("wc parameter1 parameter2"))
    }

    @Test
    fun `Should parse pipe`() {
        val echo = Echo(listOf("parameter1"))
        val pwd = Pwd(environment, listOf("parameter2"))
        assertEquals(listOf(echo, pwd), parser.parse("echo parameter1 | pwd parameter2"))
    }

    @Test
    fun `Should not parse command starting with pipe`() {
        assertThrows(ParserException::class.java) { parser.parse("| cat a.txt") }
    }

    @Test
    fun `Should not parse empty command inside`() {
        assertThrows(ParserException::class.java) { parser.parse("echo 42 | | cat a.txt") }
    }

    @Test
    fun `Should parse assignment`() {
        val assignment = Assignment(environment, listOf("parameter1", "parameter2"))
        assertEquals(listOf<Executable>(assignment), parser.parse("parameter1 = parameter2"))
    }

    @Test
    fun `Should not parse assignment with pipe`() {
        assertThrows(ParserException::class.java) { parser.parse("a=b | echo 42") }
        assertThrows(ParserException::class.java) { parser.parse("echo 42 | a=b") }
    }

    @Test
    fun `Should parse unknown command`() {
        val unknownCommand = UnknownCommand(environment, "sort", listOf("a.txt", "b.txt"))
        assertEquals(listOf<Executable>(unknownCommand), parser.parse("sort a.txt b.txt"))
    }

    @Test
    fun `Should consider quoted text as one token without quotes`() {
        val wc = Wc(listOf("parameter1 and 42", "parameter2 or 42"))
        assertEquals(listOf<Executable>(wc), parser.parse("wc 'parameter1 and 42' \"parameter2 or 42\""))
    }

    @Test
    fun `Should leave quotes inside quotes`() {
        val wc = Wc(listOf("parameter1 and \"42\"", "parameter2 or '42'"))
        assertEquals(listOf<Executable>(wc), parser.parse("wc 'parameter1 and \"42\"' \"parameter2 or '42'\""))
    }

    @Test
    fun `Should parse something with stupid placed spaces`() {
        val wc = Wc(listOf("parameter1", "parameter2"))
        val exit = Exit(shell, listOf())
        assertEquals(listOf(wc, exit), parser.parse("     wc   parameter1     parameter2|    exit    "))
    }

    @Test
    fun `Should not parse command stuck with argument as a known command`() {
        val unknownCommand = UnknownCommand(environment, "echo42", listOf("spbau"))
        assertEquals(listOf<Executable>(unknownCommand), parser.parse("echo42 spbau"))
    }
}
