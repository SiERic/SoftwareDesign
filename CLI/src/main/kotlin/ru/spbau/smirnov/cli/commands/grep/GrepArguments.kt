package ru.spbau.smirnov.cli.commands.grep

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import java.lang.StringBuilder

class GrepArguments {

    /** Case insensitive matching */
    @Parameter(names = ["-i"], description = "Case insensitive matching")
    var caseInsensitive= false

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

    fun parseFrom(arguments: List<String>) {
        parser.parse(*arguments.toTypedArray())
        afterJCommanderParsing()
    }
}
