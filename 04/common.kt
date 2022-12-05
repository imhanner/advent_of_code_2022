import java.io.File


fun String.toIntRange(): IntRange =
    this.split("-")
        .let { (start, end) -> start.toInt() .. end.toInt() }


fun count(condition: (range: Pair<IntRange, IntRange>) -> Boolean): Int = File("./input.txt")
    .readLines()
    .map { line ->
        val range = line
            .split(",")
            .let { it[0].toIntRange() to it[1].toIntRange() }

        if (condition(range))
            return@map 1

        return@map 0
    }
    .sum()
