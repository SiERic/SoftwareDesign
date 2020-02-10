package ru.spbau.smirnov.cli.preprocessing.substitution

import ru.spbau.smirnov.cli.Environment

/** Abstract class to represent tokens in substitution parser */
sealed class Token(
    /** Token value as an element of input */
    protected val value: String
) {
    /** Returns value of this token after substitution */
    abstract fun toValue(environment: Environment): String
}

/** Token that contains only one symbol. Will not be substituted */
class CharToken(value: Char) : Token(value.toString()) {

    override fun toValue(environment: Environment): String = value
}

/** Variable name that will be substituted with its value */
class VariableToken(value: String) : Token(value) {
    override fun toValue(environment: Environment): String =
        environment.getVariableValue(value)
}
