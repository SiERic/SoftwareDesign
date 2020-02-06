package ru.spbau.smirnov.cli.commands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.executor.Streams

class AssignmentTest {
    private val environment = Environment()

    @Test
    fun `Should set value`() {
        Assignment(environment, listOf("a", "b")).execute(Streams(System.`in`, System.out, System.err))
        assertEquals("b", environment.getVariableValue("a"))
    }

    @Test
    fun `Should reset previous value`() {
        Assignment(environment, listOf("a", "b")).execute(Streams(System.`in`, System.out, System.err))
        Assignment(environment, listOf("a", "c")).execute(Streams(System.`in`, System.out, System.err))
        assertEquals("c", environment.getVariableValue("a"))
    }
}