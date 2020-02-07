package ru.spbau.smirnov.cli.commands.grep

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import java.lang.StringBuilder

/**
 * Arguments for grep command.
 *
 * Parsed with JCommander
 */
internal class GrepArguments {

    /** Case insensitive matching */
    @Parameter(names = ["-i"], description = "Case insensitive matching")
    var caseInsensitive = false

    /** Only full word matches */
    @Parameter(names = ["-w"], description = "Only full word matches")
    var fullWord = false

    /** Number of lines that will be output after matched line */
    @Parameter(names = ["-A"], description = "Number of lines that will be output after matched line. Should be non-negative")
    var outputLines: Int = 0

    /** All the unparsed by JCommander parameters will be placed here */
    @Parameter
    private var unparsed = mutableListOf<String>()

    /** Prints help. If presented, other arguments aren't checked */
    @Parameter(names = ["--help"], help = true)
    var help = false

    private val parser = JCommander.newBuilder()
        .addObject(this)
        .build()

    /** Message with usage options */
    val usage: String

    /** Pattern to search by */
    lateinit var pattern: String

    /** Files to search in */
    lateinit var files: List<String>

    init {
        val builder = StringBuilder()
        parser.programName = "grep"
        parser.usageFormatter = GrepUsageFormatter(parser)
        parser.usageFormatter.usage(builder)
        usage =  builder.toString()
    }

    private fun afterJCommanderParsing() {
        if (help) {
            return
        }

        for (unparsedArgument in unparsed) {
            if (unparsedArgument.startsWith("-")) {
                throw GrepParserException("Wrong key $unparsedArgument.")
            }
        }

        if (unparsed.isEmpty()) {
            throw GrepParserException("Pattern was not given.")
        }

        if (outputLines < 0) {
            throw GrepParserException("Number of lines should be non negative.")
        }

        pattern = unparsed[0]
        files = unparsed.subList(1, unparsed.size)
    }

    /**
     * Parses arguments listed in `arguments` to this class and organizes unparsed arguments
     *
     * @throws GrepParserException if pattern is not listed in arguments
     * @throws GrepParserException if unexpected key is listed as argument
     * @throws GrepParserException if number of lines that should be printed is negative
     */
    fun parseFrom(arguments: List<String>) {
        parser.parse(*arguments.toTypedArray())
        afterJCommanderParsing()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other !is GrepArguments) {
            return false
        }

        return caseInsensitive == other.caseInsensitive &&
                fullWord == other.fullWord &&
                outputLines == other.outputLines &&
                help == other.help &&
                pattern == other.pattern &&
                files == other.files
    }
}
