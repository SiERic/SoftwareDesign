package ru.spbau.smirnov.cli.preprocessing.substitution

interface StateMachineVertex {
    fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex
    fun finishParsing(list: MutableList<Token>)
}

class UsualState : StateMachineVertex {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '$'  -> VariableNameParsing(StringBuilder())
            '\'' -> InsideWeakQuotes().also { list.add(CharToken('\'')) }
            '"'  -> InsideDoubleQuotes().also { list.add(CharToken('"')) }
            else -> UsualState().also { list.add(CharToken(symbol)) }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
    }
}

abstract class AnyNameParsing(protected val writtenSymbols: StringBuilder) :
    StateMachineVertex {
    protected val badSymbols = listOf(' ', '\t', '\n', '\r', '\'', '"', '|', '=')

    protected fun alreadyRead(list: MutableList<Token>) {
        if (writtenSymbols.isEmpty()) {
            list.add(CharToken('$'))
        } else {
            list.add(VariableToken(writtenSymbols.toString()))
        }
    }
}

class VariableNameParsing(writtenSymbols: StringBuilder) : AnyNameParsing(writtenSymbols) {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '$' -> VariableNameParsing(StringBuilder()).also { alreadyRead(list) }
            !in badSymbols -> this.also { writtenSymbols.append(symbol) }
            else ->
                UsualState().also {
                    alreadyRead(list)
                    list.add(CharToken(symbol))
                }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        alreadyRead(list)
    }
}

class VariableNameParsingInsideDoubleQuotes(writtenSymbols: StringBuilder) : AnyNameParsing(writtenSymbols) {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '"' -> UsualState().also {
                alreadyRead(list)
                list.add(CharToken('"'))
            }
            !in badSymbols -> this.also { writtenSymbols.append(symbol) }
            '$' -> VariableNameParsingInsideDoubleQuotes(StringBuilder()).also {
                alreadyRead(list)
            }
            else -> InsideDoubleQuotes().also {
                alreadyRead(list)
                list.add(CharToken(symbol))
            }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        throw SubstitutionParserException("Parsing error. \"\"\" expected, end of expression found.")
    }
}

class InsideWeakQuotes : StateMachineVertex {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '\'' -> UsualState().also { list.add(CharToken('\'')) }
            else -> InsideWeakQuotes().also { list.add(CharToken(symbol)) }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        throw SubstitutionParserException("Parsing error. \"'\" expected, end of expression found.")
    }
}

class InsideDoubleQuotes :
    StateMachineVertex {
    override fun parseSymbol(symbol: Char, list: MutableList<Token>): StateMachineVertex {
        return when (symbol) {
            '$' -> VariableNameParsingInsideDoubleQuotes(StringBuilder())
            '"' -> UsualState().also { list.add(CharToken('"')) }
            else -> InsideDoubleQuotes().also { list.add(CharToken(symbol)) }
        }
    }

    override fun finishParsing(list: MutableList<Token>) {
        throw SubstitutionParserException("Parsing error. \"\"\" expected, end of expression found.")
    }
}
