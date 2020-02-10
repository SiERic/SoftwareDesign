package ru.spbau.smirnov.cli

import java.nio.file.Paths

/**
 * Stores environmental variables and path to current directory
 */
class Environment {
    /**
     * Environmental variables.
     */
    private val variableToValue = mutableMapOf<String, String>().apply { putAll(System.getenv()) }

    /** Returns current directory as a string */
    val currentDirectory: String = Paths.get("").toAbsolutePath().toString()

    /**
     * Sets environmental variables of a new process to current environmental variables
     */
    fun passEnvironmentalVariables(processBuilder: ProcessBuilder) {
        val processEnvironment = processBuilder.environment()
        processEnvironment.clear()
        processEnvironment.putAll(variableToValue)
    }

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
