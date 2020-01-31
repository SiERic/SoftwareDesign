package ru.spbau.smirnov.cli.commands

import ru.spbau.smirnov.cli.Environment
import ru.spbau.smirnov.cli.Shell

/** Factory for creating executable by command name */
class ExecutableFactory(private val environment: Environment, private val shell: Shell) {
    /**
     * Creates executable by name.
     *
     * If there is no such built-in executable, creates `UnknownCommand` executable
     */
    fun createExecutable(commandName: String, arguments: List<String>): Executable {
        return when (commandName) {
            "="    -> Assignment(environment, arguments)
            "echo" -> Echo(arguments)
            "pwd"  -> Pwd(environment, arguments)
            "exit" -> Exit(shell, arguments)
            "cat"  -> Cat(arguments)
            "wc"   -> Wc(arguments)
            else   -> UnknownCommand(environment, commandName, arguments)
        }
    }
}