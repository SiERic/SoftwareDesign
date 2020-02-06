package ru.spbau.smirnov.cli.preprocessing.substitution

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment

internal class SubstitutionTest {
    @Test
    fun `Should parse easy line`() {
        assertEquals("aaa", Substitute(
            Environment()
        ).doSubstitution("aaa"))
    }

    @Test
    fun `Should substitute variable`() {
        assertEquals("aaa bbb", Substitute(
            Environment().apply {
                assignVariable("FOO", "aaa")
            }).doSubstitution("\$FOO bbb"))
    }

    @Test
    fun `Should substitute variable inside double quotes`() {
        assertEquals("\"aaa bbb\"", Substitute(
            Environment().apply {
                assignVariable("FOO", "aaa")
            }).doSubstitution("\"\$FOO bbb\""))
    }

    @Test
    fun `Should not substitute variable inside unary quotes`() {
        assertEquals("\'\$FOO bbb\'", Substitute(
            Environment().apply {
                assignVariable("FOO", "aaa")
            }).doSubstitution("\'\$FOO bbb\'"))
    }

    @Test
    fun `Should parse variable on the end of input`() {
        assertEquals("baaa", Substitute(
            Environment().apply {
                assignVariable("FOO", "aaa")
            }).doSubstitution("b\$FOO"))
    }

    @Test
    fun `Should parse variable on the end of double quotes`() {
        assertEquals("\"baaa\"", Substitute(
            Environment().apply {
                assignVariable("FOO", "aaa")
            }).doSubstitution("\"b\$FOO\""))
    }

    @Test
    fun `Should pay no attention to unary quotes inside double`() {
        assertEquals("\"'\"", Substitute(
            Environment()
        ).doSubstitution("\"'\""))
    }

    @Test
    fun `Should pay no attention to double quotes inside unary`() {
        assertEquals("'\"'", Substitute(
            Environment()
        ).doSubstitution("'\"'"))
    }

    @Test
    fun `Should replace with empty string if no variable found`() {
        assertEquals(" bar", Substitute(
            Environment()
        ).doSubstitution("\$FOO bar"))
    }

    @Test
    fun `Should throw on bad input`() {
        assertThrows(SubstitutionParserException::class.java) {
            Substitute(
                Environment()
            ).doSubstitution("\"bar")
        }

        assertThrows(SubstitutionParserException::class.java) {
            Substitute(
                Environment()
            ).doSubstitution("'bar")
        }

        assertThrows(SubstitutionParserException::class.java) {
            Substitute(
                Environment()
            ).doSubstitution("\"\$FOO")
        }
    }

    @Test
    fun `Should parse $x$y`() {
        assertEquals("exit", Substitute(
            Environment().apply {
                assignVariable("x", "ex")
                assignVariable("y", "it")
            }).doSubstitution("\$x\$y"))
    }

    @Test
    fun `Should substitute inside double quotes inside single`() {
        assertEquals("\"'ex'\"", Substitute(
            Environment().apply {
                assignVariable("x", "ex")
            }).doSubstitution("\"'\$x'\""))
    }

    @Test
    fun `Should nod substitute inside single quotes inside double`() {
        assertEquals("'\"\$x\"'", Substitute(
            Environment().apply {
                assignVariable("x", "ex")
            }).doSubstitution("'\"\$x\"'"))
    }

    @Test
    fun `Should parse quotes after variable`() {
        assertEquals("ex'it'", Substitute(
            Environment().apply {
                assignVariable("x", "ex")
            }
        ).doSubstitution("\$x'it'"))
    }
}
