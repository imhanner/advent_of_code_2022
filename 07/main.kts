#!/usr/bin/kotlinc -script

import java.io.File
import kotlin.math.abs


fun parseFileSystem(commands: List<String>, currentDir: FileSystemItem.Directory? = null): FileSystemItem.Directory {

    if (commands.isEmpty()) {
        return currentDir!!.root
    }


    val command = commands.first()
    val commandsLeft = commands.run { takeLast(size - 1) }

    when {
        command.startsWith("$ ls") -> {
            val lsOutput = commandsLeft
                .takeWhile { !it.startsWith("$") }
                .onEach { lsItem ->
                    val tokens = lsItem.split(" ")
                    val fsItem = if (lsItem.startsWith("dir")) {
                        val (_, dirName) = tokens
                        FileSystemItem.Directory(dirName, currentDir)
                    }
                    else {
                        val (filesize, filename) = tokens
                        FileSystemItem.File(filename, filesize.toInt(), currentDir)
                    }

                    currentDir!!.children.add(fsItem)
                }

            return parseFileSystem(commandsLeft.run { takeLast(size - lsOutput.size) }, currentDir)
        }

        command == "$ cd .." ->
            return parseFileSystem(commandsLeft, currentDir?.parent)

        command.startsWith("$ cd") -> {
            val (_, _, changeToDirName) = command.split(" ")

            return parseFileSystem(
                commands = commandsLeft,
                currentDir = if (currentDir == null) {
                    FileSystemItem.Directory(changeToDirName)
                }
                else {
                    currentDir.children.first { it is FileSystemItem.Directory && it.name == changeToDirName } as FileSystemItem.Directory
                },
            )
        }

        else -> TODO("command '$command'")
    }
}


sealed class FileSystemItem {
    abstract val name: String
    abstract val size: Int
    abstract val parent: Directory?

    val root: Directory get() = parent?.root ?: this as Directory

    fun toPrettyString() = buildList<String> {
        fun magic(item: FileSystemItem, level: Int) {
            listOf(
                " ".repeat(2 * level) + "-",
                item.name,
                when (item) {
                    is Directory -> "(dir)"
                    is File -> "(file, size=${item.size})"
                    else -> "???"
                },
            ).joinToString(" ").let(::add)

            if (item is Directory) {
                item.children.forEach { magic(it, level + 1) }
            }
        }

        magic(this@FileSystemItem, 0)

    }.joinToString("\n")

    fun findDir(dirName: String): Directory? = when (this) {
        is File -> null
        is Directory -> {
            if (dirName == name)
                this
            else
                children.firstNotNullOfOrNull { it.findDir(dirName) }
        }
    }

    fun directories(): Set<Directory> = when (this) {
        is File -> emptySet()
        is Directory -> setOf(this) + children.map { it.directories() }.flatten()
    }


    class Directory(
        override val name: String,
        override val parent: Directory? = null,
        val children: MutableSet<FileSystemItem> = mutableSetOf(),
    ) : FileSystemItem() {
        override val size get() = children.sumOf { it.size }
    }

    class File(
        override val name: String,
        override val size: Int,
        override val parent: Directory? = null,
    ) : FileSystemItem()
}

//region examples of part 1
listOf(
    "$ cd /",
    "$ ls",
    "dir a",
    "14848514 b.txt",
    "8504156 c.dat",
    "dir d",
    "$ cd a",
    "$ ls",
    "dir e",
    "29116 f",
    "2557 g",
    "62596 h.lst",
    "$ cd e",
    "$ ls",
    "584 i",
    "$ cd ..",
    "$ cd ..",
    "$ cd d",
    "$ ls",
    "4060174 j",
    "8033020 d.log",
    "5626152 d.ext",
    "7214296 k",
).let { commands ->
    val fileSystem = parseFileSystem(commands)

    require(fileSystem.toPrettyString() == """
        - / (dir)
          - a (dir)
            - e (dir)
              - i (file, size=584)
            - f (file, size=29116)
            - g (file, size=2557)
            - h.lst (file, size=62596)
          - b.txt (file, size=14848514)
          - c.dat (file, size=8504156)
          - d (dir)
            - j (file, size=4060174)
            - d.log (file, size=8033020)
            - d.ext (file, size=5626152)
            - k (file, size=7214296)
    """.trimIndent())

    listOf(
        "e" to 584,
        "a" to 94853,
        "d" to 24933642,
        "/" to 48381165,
    ).forEach { (dirName, expectedSize) -> require(fileSystem.findDir(dirName)!!.size == expectedSize) }

    require(fileSystem.directories().filter { it.size <= 100_000 }.sumOf { it.size } == 95437)
}
//endregion


val allCommands = File("./input.txt").readLines()

Pair(
    first = parseFileSystem(allCommands)
        .directories()
        .filter { it.size <= 100_000 }
        .sumOf { it.size }, // 1886043

    second = parseFileSystem(allCommands)
        .let { root ->
            val diskSpace = 70_000_000
            val requiredUpdateSpace = 30_000_000
            val usedSpace = root.size

            val missingFreeSpaceAtLeast = requiredUpdateSpace - (diskSpace - usedSpace)

            root.directories()
                .filter { it.size >= missingFreeSpaceAtLeast }
                .minOf { it.size }
                .also { require(requiredUpdateSpace <= diskSpace - usedSpace + it) }
        }, // 3842121
)
