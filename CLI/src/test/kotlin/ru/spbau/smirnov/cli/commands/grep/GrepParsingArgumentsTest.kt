package ru.spbau.smirnov.cli.commands.grep

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GrepParsingArgumentsTest {

    private lateinit var arguments: GrepArguments

    @BeforeEach
    fun initArguments() {
        arguments = GrepArguments()
    }

    @Test
    fun `Should parse only pattern`() {
        arguments.parseFrom(listOf("some pattern"))

        val expected = GrepArguments().apply {
            caseInsensitive = false
            fullWord = false
            outputLines = 0
            pattern = "some pattern"
            files = listOf()
            help = false
        }

        assertEquals(expected, arguments)
    }

    @Test
    fun `Should parse keys and pattern`() {
        arguments.parseFrom(listOf("some pattern", "-i", "-w"))

        val expected = GrepArguments().apply {
            caseInsensitive = true
            fullWord = true
            outputLines = 0
            pattern = "some pattern"
            files = listOf()
            help = false
        }

        assertEquals(expected, arguments)
    }

    @Test
    fun `Should parse -A key`() {
        arguments.parseFrom(listOf("some pattern", "-A", "500"))

        val expected = GrepArguments().apply {
            caseInsensitive = false
            fullWord = false
            outputLines = 500
            pattern = "some pattern"
            files = listOf()
            help = false
        }

        assertEquals(expected, arguments)
    }

    @Test
    fun `Should parse one file`() {
        arguments.parseFrom(listOf("some pattern", "-A", "500", "filename1"))

        val expected = GrepArguments().apply {
            caseInsensitive = false
            fullWord = false
            outputLines = 500
            pattern = "some pattern"
            files = listOf("filename1")
            help = false
        }

        assertEquals(expected, arguments)
    }

    @Test
    fun `Should parse many files`() {
        arguments.parseFrom(listOf("some pattern", "-A", "0", "filename1", "filename2", "filename3"))

        val expected = GrepArguments().apply {
            caseInsensitive = false
            fullWord = false
            outputLines = 0
            pattern = "some pattern"
            files = listOf("filename1", "filename2", "filename3")
            help = false
        }

        assertEquals(expected, arguments)
    }

    @Test
    fun `Should parse arguments in any order`() {
        arguments.parseFrom(listOf("-A", "500", "some pattern", "-i", "filename1", "-w"))

        val expected = GrepArguments().apply {
            caseInsensitive = true
            fullWord = true
            outputLines = 500
            pattern = "some pattern"
            files = listOf("filename1")
            help = false
        }

        assertEquals(expected, arguments)
    }

    @Test
    fun `Should parse help`() {
        arguments.parseFrom(listOf("--help"))
        assertEquals(true, arguments.help)
    }

    @Test
    fun `Should parse help even with other arguments`() {
        arguments.parseFrom(listOf("-i", "--help"))
        assertEquals(true, arguments.help)
    }

    @Test
    fun `Should throw if pattern was not given`() {
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf("-i")) }
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf()) }
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf("-w")) }
    }

    @Test
    fun `Should throw if -A argument is negative`() {
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf("-A", "-1", "pattern")) }
    }

    @Test
    fun `Should throw on unparsed keys`() {
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf("-dd", "pattern")) }
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf("-42", "pattern")) }
        assertThrows(GrepParserException::class.java) { arguments.parseFrom(listOf("-w", "-iunexpected")) }
    }
}
