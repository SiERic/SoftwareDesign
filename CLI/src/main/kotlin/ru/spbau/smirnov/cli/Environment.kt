package ru.spbau.smirnov.cli

import java.nio.file.Paths

/**
 * Stores environmental variables and path to current directory
 */
class Environment {
    /**
     * Environmental variables.
     *
     * It is not private because on executing unknown command we have to put all environmental
     * variables to a new process
     */
    val variableToValue = mutableMapOf<String, String>().apply { putAll(System.getenv()) }

    /** Returns current directory as a string */
    val currentDirectory: String = Paths.get("").toAbsolutePath().toString()

    /**
     * Assigns `value` to `variable`.
     *
     * Rewrites previous value, if any
     */
    fun assignVariable(variable: String, value: String) {
        variableToValue[variable] = value
    }

    /**
     * Returns value assigned to `variable`.
     *
     * If there is no such variable, returns empty string
     */
    fun getVariableValue(variable: String): String = variableToValue[variable] ?: ""
}
