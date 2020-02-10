package ru.spbau.smirnov.cli

import ru.spbau.smirnov.cli.commands.ExecutableFactory
import ru.spbau.smirnov.cli.executor.Executor
import ru.spbau.smirnov.cli.executor.Streams
import ru.spbau.smirnov.cli.commands.Executable
import ru.spbau.smirnov.cli.preprocessing.parsing.Parser
import ru.spbau.smirnov.cli.preprocessing.parsing.ParserException
import ru.spbau.smirnov.cli.preprocessing.substitution.Substitute
import ru.spbau.smirnov.cli.preprocessing.substitution.SubstitutionParserException
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintStream

/** Class for running shell */
class Shell {
    private var finish = false
    private val environment = Environment()
    private val executableFactory = ExecutableFactory(environment, this)
    private val substitute = Substitute(environment)
    private val parser = Parser(executableFactory)

    /** Sets `finish` flag that means that shall will be closed after execution of current operation */
    fun finishShell() {
        finish = true
    }

    /**
     * Runs shell.
     *
     * All commands will be read from `inputStream`, results will be written to `outputStream`
     * and all the errors will be listed in `errorStream`
     */
    fun run(inputStream: InputStream, outputStream: PrintStream, errorStream: PrintStream) {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val streams = Streams(InputStream.nullInputStream(), outputStream, errorStream)

        while (!finish) {
            outputStream.print("> ")
            outputStream.flush()
            val input = bufferedReader.readLine()
            val parsedInput: List<Executable>
            val substitutedInput: String

            try {
                substitutedInput = substitute.doSubstitution(input)
            } catch (e: SubstitutionParserException) {
                errorStream.println("Preparsing error!${System.lineSeparator()}${e.message}")
                continue
            }

            try {
                parsedInput = parser.parse(substitutedInput)
            } catch (e: ParserException) {
                errorStream.println("Parsing error!${System.lineSeparator()}${e.message}")
                continue
            }

            Executor.execute(parsedInput, streams)
        }
    }
}

/** Runs shell as console application */
fun main(args: Array<String>) {
    Shell().run(System.`in`, System.out, System.err)
}
