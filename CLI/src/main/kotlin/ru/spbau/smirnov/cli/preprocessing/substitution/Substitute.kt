package ru.spbau.smirnov.cli.preprocessing.substitution

import ru.spbau.smirnov.cli.Environment

/**
 * Class for substitution variable names with their values.
 *
 * Variable names will be substituted, if they are not inside weak quotes
 */
class Substitute(private val environment: Environment) {
    /**
     * Performs substitution.
     *
     * @throws SubstitutionParserException if some error occurred while parsing
     * (i.e. not closed quotes)
     */
    fun doSubstitution(input: String): String {
        var stateMachine: StateMachineVertex = UsualState
        val tokens = mutableListOf<Token>()

        for (symbol in input) {
            stateMachine = stateMachine.parseSymbol(symbol, tokens)
        }
        stateMachine.finishParsing(tokens)

        return tokens.joinToString(separator = "", transform = { it.toValue(environment) })
    }
}
