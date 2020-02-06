package ru.spbau.smirnov.cli.preprocessing.parsing

import java.lang.RuntimeException

/** Exception thrown by parser */
class ParserException(other: RuntimeException) : Exception(other)