package ru.spbau.smirnov.cli.preprocessing.parsing

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.spbau.smirnov.CLILexer
import ru.spbau.smirnov.CLIParser
import ru.spbau.smirnov.cli.executor.ExecutableFactory
import ru.spbau.smirnov.cli.executor.commands.Executable
import java.lang.RuntimeException

class Parser(private val executableFactory: ExecutableFactory) {
    fun parse(input: String): List<Executable> {
        try {
            val lexer = CLILexer(CharStreams.fromString(input))
            val parser = CLIParser(CommonTokenStream(lexer))

            val commands = mutableListOf<Executable>()
            parser.operation().accept(OperationVisitor(commands, executableFactory))

            return commands.toList()
        } catch (e: RuntimeException) {
            throw ParserException(e)
        }
    }
}
