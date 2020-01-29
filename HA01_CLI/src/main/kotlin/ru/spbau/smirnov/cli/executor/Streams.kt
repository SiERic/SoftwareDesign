package ru.spbau.smirnov.cli.executor

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream

/** Class for storing streams for passing them as one parameter to command */
class Streams(
    /** Stream that is used as input for a command */
    val inputStream: InputStream,
    /** Stream that is used as output for a command */
    val outputStream: OutputStream,
    /** Stream that is used as error for a command */
    val errorStream: PrintStream
)
