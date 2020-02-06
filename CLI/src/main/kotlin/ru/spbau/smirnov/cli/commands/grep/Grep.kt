package ru.spbau.smirnov.cli.commands.grep
import ru.spbau.smirnov.cli.commands.Executable
import ru.spbau.smirnov.cli.executor.Streams
import java.io.*
import java.util.regex.Pattern

class Grep(arguments: List<String>) : Executable(arguments) {

    private lateinit var grepArguments: GrepArguments
    private lateinit var pattern: Pattern

    override fun execute(streams: Streams): Int {
        try {
            parseArguments()
        } catch (e: GrepParserException) {
            streams.errorStream.println(e.message + "\n" + grepArguments.usage)
            return 1
        }

        if (grepArguments.help) {
            DataOutputStream(streams.outputStream).writeBytes(grepArguments.usage)
            return 0
        }

        generatePattern()

        return if (grepArguments.files.isEmpty()) {
            executeGrepWithoutFiles(streams)
        } else {
            executeGrepWithFiles(streams)
        }
    }

    private fun executeGrepWithFiles(streams: Streams): Int {
        if (grepArguments.files.size == 1) {
            return executeGrepOnOneFile(grepArguments.files[0], streams, "", "")
        }

        for (filename in grepArguments.files) {
            val result = executeGrepOnOneFile(filename, streams, "$filename:", "$filename-")
            if (result != 0) {
                return result
            }
        }

        return 0
    }

    private fun executeGrepOnOneFile(filename: String,
                               streams: Streams,
                               prefixMatch: String,
                               prefixNotMatch: String): Int {
        try {
            executeOnInputStream(
                FileInputStream(filename),
                DataOutputStream(streams.outputStream),
                prefixMatch,
                prefixNotMatch
            )
        } catch(e: IOException) {
            streams.errorStream.println("Error in grep while reading from file $filename\n${e.message}")
            return 1
        }
        return 0
    }

    private fun executeGrepWithoutFiles(streams: Streams): Int {
        try {
            executeOnInputStream(streams.inputStream, DataOutputStream(streams.outputStream))
        } catch (e: IOException) {
            streams.errorStream.println("Error in grep while reading from input stream\n${e.message}")
            return 1
        }
        return 0
    }

    private fun executeOnInputStream(inputStream: InputStream,
                                     outputStream: DataOutputStream,
                                     prefixMatch: String = "",
                                     prefixNotMatch: String = "") {
        val reader = InputStreamReader(inputStream)
        var remainsToPrint = 0

        for (line in reader.readLines()) {
            if (pattern.matcher(line).find()) {
                outputStream.writeBytes("$prefixMatch$line\n")
                remainsToPrint = grepArguments.outputLines
            } else if (remainsToPrint > 0) {
                outputStream.writeBytes("$prefixNotMatch$line\n")
                remainsToPrint--
            }
        }
    }

    private fun generatePattern() {
        var flags = 0
        if (grepArguments.caseInsensitive) {
            flags = flags or Pattern.CASE_INSENSITIVE
        }
        var stringPattern = grepArguments.pattern
        if (grepArguments.fullWord) {
            stringPattern = "\\b$stringPattern\\b"
        }
        pattern = Pattern.compile(stringPattern, flags)
    }

    private fun parseArguments() {
        grepArguments = GrepArguments()
        grepArguments.parseFrom(arguments)
    }
}
