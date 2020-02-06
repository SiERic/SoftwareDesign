package ru.spbau.smirnov.cli.preprocessing.parsing

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.spbau.smirnov.CLILexer
import ru.spbau.smirnov.CLIParser
import ru.spbau.smirnov.cli.commands.ExecutableFactory
import ru.spbau.smirnov.cli.commands.Executable
import java.lang.RuntimeException

/** Class for parsing operations over strings preprocessed with `Substitute` */
class Parser(private val executableFactory: ExecutableFactory) {
    /**
     * Parses input using grammar from `CLI.g4`.
     *
     * Returns list of executables where commands placed by their execution order
     * I.e. for 'cat a.txt | wc' list will be `[Cat(a.txt), Wc]`
     *
     * @throws ParserException if parsing failed
     */
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
