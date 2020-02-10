package ru.spbau.smirnov.cli.preprocessing.substitution

/**
 * Vertex of state machine that parses input.
 *
 * The only thing this machine does is detection of environmental variables usages
 */
sealed class StateMachineVertex {
    /**
     * Takes next symbol and returns new machine's state
     * Adds new tokens to `list` if needed
     */
    abstract fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex
    /**
     * Shut downs machine.
     * Adds not added tokens to `list`
     *
     * @throws SubstitutionParserException if machine is in a bad state for finishing
     */
    abstract fun finishParsing(list: MutableList<Token>)
}

/** State not inside quotes and not reading variable name */
object UsualState : StateMachineVertex() {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '$' -> VariableNameParsing(StringBuilder())
            '\'' -> {
                list.add(CharToken('\''))
                InsideWeakQuotes
            }
            '"' -> {
                list.add(CharToken('"'))
                InsideDoubleQuotes
            }
            else -> {
                list.add(CharToken(symbol))
                UsualState
            }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
    }
}

/** Parsing variable name (inside or not quotes) */
abstract class AnyNameParsing(
    /** Prefix of variable name that was already read */
    protected val writtenSymbols: StringBuilder
) : StateMachineVertex() {

    /** Symbols that variable name cannot contain */
    protected val badSymbols = listOf(' ', '\t', '\n', '\r', '\'', '"', '|', '=')

    /**
     * Method that is called when variable name is read fully
     * and we have to transform it into token.
     *
     * Lonely `$` symbol is considered as `CharToken`
     */
    protected fun alreadyRead(list: MutableList<Token>) {
        if (writtenSymbols.isEmpty()) {
            list.add(CharToken('$'))
        } else {
            list.add(VariableToken(writtenSymbols.toString()))
        }
    }
}

/** Variable name parsing not inside quotes */
class VariableNameParsing(writtenSymbols: StringBuilder) : AnyNameParsing(writtenSymbols) {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '$'  -> {
                alreadyRead(list)
                VariableNameParsing(StringBuilder())
            }
            '\'' -> {
                alreadyRead(list)
                list.add(CharToken(symbol))
                InsideWeakQuotes
            }
            '"'  -> {
                alreadyRead(list)
                list.add(CharToken(symbol))
                InsideDoubleQuotes
            }
            !in badSymbols -> {
                writtenSymbols.append(symbol)
                this
            }
            else -> {
                alreadyRead(list)
                list.add(CharToken(symbol))
                UsualState
            }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        alreadyRead(list)
    }
}

/** Variable name parsing inside double quotes */
class VariableNameParsingInsideDoubleQuotes(writtenSymbols: StringBuilder) : AnyNameParsing(writtenSymbols) {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '"' -> {
                alreadyRead(list)
                list.add(CharToken('"'))
                UsualState
            }
            !in badSymbols -> {
                writtenSymbols.append(symbol)
                this
            }
            '$' -> {
                alreadyRead(list)
                VariableNameParsingInsideDoubleQuotes(StringBuilder())
            }
            else -> {
                alreadyRead(list)
                list.add(CharToken(symbol))
                InsideDoubleQuotes
            }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        throw SubstitutionParserException("Parsing error. \"\"\" expected, end of expression found.")
    }
}

/** Parsing inside weak quotes */
object InsideWeakQuotes : StateMachineVertex() {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '\'' -> {
                list.add(CharToken('\''))
                UsualState
            }
            else -> {
                list.add(CharToken(symbol))
                InsideWeakQuotes
            }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        throw SubstitutionParserException("Parsing error. \"'\" expected, end of expression found.")
    }
}

/** Parsing inside double quotes, but not variable name */
object InsideDoubleQuotes :
    StateMachineVertex() {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '$' -> VariableNameParsingInsideDoubleQuotes(StringBuilder())
            '"' -> {
                list.add(CharToken('"'))
                UsualState
            }
            else -> {
                list.add(CharToken(symbol))
                InsideDoubleQuotes
            }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        throw SubstitutionParserException("Parsing error. \"\"\" expected, end of expression found.")
    }
}
