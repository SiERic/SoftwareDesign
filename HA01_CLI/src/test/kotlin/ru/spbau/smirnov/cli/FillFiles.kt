package ru.spbau.smirnov.cli

import java.io.File

val resourcesDir = "src" + File.separator + "test" + File.separator + "resources" + File.separator

fun fillFiles() {
    fillAnotherFile()
    fillExample()
    fillJustAFileWithSomeContent()
}

private fun fillAnotherFile() {
    val filename = "${resourcesDir}AnotherFile.txt"
    val printWriter = File(filename).printWriter()
    printWriter.println(
        "Some" + System.lineSeparator() +
            "Other awesome" + System.lineSeparator() +
            "Content"
    )
    printWriter.close()
}

private fun fillExample() {
    val filename = "${resourcesDir}example.txt"
    val printWriter = File(filename).printWriter()
    printWriter.println("Some example text")
    printWriter.close()
}

private fun fillJustAFileWithSomeContent() {
    val filename = "${resourcesDir}JustAFileWithSomeContent.txt"
    val printWriter = File(filename).printWriter()
    printWriter.println(
        "some" + System.lineSeparator() +
        "content" + System.lineSeparator() +
        "is" + System.lineSeparator() +
        "here" + System.lineSeparator() +
        "42"
    )
    printWriter.close()
}