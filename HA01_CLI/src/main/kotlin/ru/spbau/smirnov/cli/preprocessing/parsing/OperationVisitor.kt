package ru.spbau.smirnov.cli.preprocessing.parsing

import ru.spbau.smirnov.CLIBaseVisitor
import ru.spbau.smirnov.CLIParser
import ru.spbau.smirnov.cli.commands.ExecutableFactory
import ru.spbau.smirnov.cli.commands.Executable

/**
 * Visitor for grammar from `CLI.g4`.
 *
 * Visits command in their execution order and adds them to `commands` list
 */
class OperationVisitor(
    private val commands: MutableList<Executable>,
    private val executableFactory: ExecutableFactory
): CLIBaseVisitor<String?>() {

    /** Visits assignment statement and creates `Assignment` object */
    override fun visitAssignment(ctx: CLIParser.AssignmentContext): String? {
        val variableName = ctx.variable.text
        val value = ctx.value.accept(this)!!

        commands.add(executableFactory.createExecutable("=",
            listOf(variableName, value)))

        return null
    }

    /** Visits set of command in their execution order */
    override fun visitSetOfCommands(ctx: CLIParser.SetOfCommandsContext): String? {
        for (command in ctx.commands) {
            command.accept(this)
        }

        return null
    }

    /** Creates command */
    override fun visitCommand(ctx: CLIParser.CommandContext): String? {
        val arguments = mutableListOf<String>()
        for (argument in ctx.args) {
            val argumentValue = argument.accept(this)!!
            arguments.add(argumentValue)
        }

        val commandName = ctx.commandName.accept(this)!!
        commands.add(executableFactory.createExecutable(commandName, arguments))

        return null
    }

    /** Deletes quotes from double quoted token */
    override fun visitDoubleQuote(ctx: CLIParser.DoubleQuoteContext): String {
        return ctx.text.substring(1 until ctx.text.lastIndex)
    }

    /** Deletes quotes from single quoted token */
    override fun visitSingleQuote(ctx: CLIParser.SingleQuoteContext): String {
        return ctx.text.substring(1 until ctx.text.lastIndex)
    }

    /** Returns token's value */
    override fun visitUnquote(ctx: CLIParser.UnquoteContext): String {
        return ctx.text
    }
}
