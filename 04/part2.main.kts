#!/usr/bin/kotlinc -script

import java.io.File


fun String.toIntRange(): IntRange =
    this.split("-")
        .let { (start, end) -> start.toInt() .. end.toInt() }

infix fun IntRange.overlap(other: IntRange): Boolean =
    this.intersect(other)
        .isNotEmpty()

File("./input.txt")
    .readLines()
    .map { line ->
        val range = line
            .split(",")
            .let { it[0].toIntRange() to it[1].toIntRange() }

        if (range.first overlap range.second)
            return@map 1

        return@map 0
    }
    .sum() // 933
