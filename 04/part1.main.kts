#!/usr/bin/kotlinc -script

import java.io.File


fun String.toIntRange(): IntRange =
    this.split("-")
        .let { (start, end) -> start.toInt() .. end.toInt() }

operator fun IntRange.contains(other: IntRange): Boolean =
    (this.first <= other.first && other.last <= this.last) || (other.first <= this.first && this.last <= other.last)

File("./input.txt")
    .readLines()
    .map { line ->
        val range = line
            .split(",")
            .let { it[0].toIntRange() to it[1].toIntRange() }

        if (range.first in range.second)
            return@map 1

        return@map 0
    }
    .sum() // 584
