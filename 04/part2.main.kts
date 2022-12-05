#!/usr/bin/kotlinc -script

@file:Import("common.kt")


infix fun IntRange.overlap(other: IntRange): Boolean =
    this.intersect(other)
        .isNotEmpty()

count { range ->
    range.first overlap range.second
} // 933
