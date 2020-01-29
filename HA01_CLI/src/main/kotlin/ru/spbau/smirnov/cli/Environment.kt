package ru.spbau.smirnov.cli

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Stores environmental variables and path to current directory
 */
class Environment {
    private val variableToValue = System.getenv()
    /** Stores path to current directory */
    var currentDirectory: Path
        get(): Path {
           return Paths.get("")
        }
        set(path: Path) {

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
