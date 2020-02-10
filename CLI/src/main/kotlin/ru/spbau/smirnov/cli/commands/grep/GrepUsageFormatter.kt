package ru.spbau.smirnov.cli.commands.grep

import com.beust.jcommander.DefaultUsageFormatter
import com.beust.jcommander.JCommander


/**
 * Class that overrides only `appendMainLine` method from `DefaultUsageFormatter`.
 *
 * Prints options of grep command and a short description
 */
internal class GrepUsageFormatter(
    private val commander: JCommander
) : DefaultUsageFormatter(commander) {

    override fun appendMainLine(
        out: java.lang.StringBuilder?,
        hasOptions: Boolean,
        hasCommands: Boolean,
        indentCount: Int,
        indent: String?
    ) {
        val programName = if (commander.programDisplayName != null) commander.programDisplayName else "<main class>"
        val mainLine = StringBuilder()
        mainLine.append(indent)
            .append("Usage: ")
            .append(programName)
            .append(" [OPTION]... PATTERN [FILE]...${System.lineSeparator()}")
            .append("Searches for PATTERN in each FILE")

        wrapDescription(out, indentCount, mainLine.toString())
        out!!.append(System.lineSeparator())
    }
}
