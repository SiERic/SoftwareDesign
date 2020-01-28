package ru.spbau.smirnov.cli.preprocessing.parsing

import ru.spbau.smirnov.CLIBaseVisitor
import ru.spbau.smirnov.CLIParser
import ru.spbau.smirnov.cli.executor.ExecutableFactory
import ru.spbau.smirnov.cli.executor.commands.Executable

class OperationVisitor(
    private val commands: MutableList<Executable>,
    private val executableFactory: ExecutableFactory
): CLIBaseVisitor<String?>() {

    override fun visitAssignment(ctx: CLIParser.AssignmentContext): String? {
        val variableName = ctx.variable.text
        val value = ctx.value.accept(this)!!

        commands.add(executableFactory.createExecutable("=",
            listOf(variableName, value)))

        return null
    }

    override fun visitOperation(ctx: CLIParser.OperationContext): String? {
        for (command in ctx.commands) {
            command.accept(this)
        }

        return null
    }

    override fun visitUsualCommand(ctx: CLIParser.UsualCommandContext): String? {
        val arguments = mutableListOf<String>()
        for (argument in ctx.args) {
            val argumentValue = argument.accept(this)!!
            arguments.add(argumentValue)
        }

        val commandName = ctx.commandName.accept(this)!!
        commands.add(executableFactory.createExecutable(commandName, arguments))

        return null
    }

    override fun visitDoubleQuote(ctx: CLIParser.DoubleQuoteContext): String {
        return ctx.text.substring(1 until ctx.text.lastIndex)
    }

    override fun visitSingleQuote(ctx: CLIParser.SingleQuoteContext): String {
        return ctx.text.substring(1 until ctx.text.lastIndex)
    }

    override fun visitUnquote(ctx: CLIParser.UnquoteContext): String {
        return ctx.text
    }
}
