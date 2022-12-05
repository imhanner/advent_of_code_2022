#!/usr/bin/kotlinc -script

@file:Import("common.kt")


operator fun IntRange.contains(other: IntRange): Boolean =
    (this.first <= other.first && other.last <= this.last) || (other.first <= this.first && this.last <= other.last)

count { range ->
    range.first in range.second
} // 584
