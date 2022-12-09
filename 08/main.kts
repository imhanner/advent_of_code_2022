#!/usr/bin/kotlinc -script

import java.io.File


class Forest(treeRows: List<String>) {

    val trees = treeRows.map { row -> row.map { it.toString().toInt() } }

    val positions by lazy {
        (0 until trees.size).flatMap { a ->
            (0 until trees.first().size).map { b -> a to b }
        }
    }

    operator fun get(row: Int, col: Int): Int = trees[row][col]

    private fun treesOfAllDirections(row: Int, col: Int): List<List<Int>> = listOf(
        (0 until row).map { r -> this[r, col] }.reversed(), // treesAbove
        (row + 1 until trees.size).map { r -> this[r, col] }, // treesBelow
        trees[row].subList(0, col).reversed(), // treesLhs
        trees[row].run {subList(col + 1, size) }, // treesRhs
    )



    fun isTreeVisibleAt(position: Pair<Int, Int>): Boolean = position.let { (row, col) ->
        if (row == 0 || row == trees.lastIndex || col == 0 || col == trees[row].lastIndex)
            return@let true // edges are visible

        treesOfAllDirections(row, col).fold(false) { isVisible, otherTrees ->
            isVisible || otherTrees.none { it >= this[row, col] }
        }
    }

    fun scenicScore(position: Pair<Int, Int>): Int = position.let { (row, col) ->
        if (row == 0 || row == trees.lastIndex || col == 0 || col == trees[row].lastIndex)
            return@let 0 // edges have viewing distance (= at least one factor) 0

        treesOfAllDirections(row, col).fold(1) { score, otherTrees ->
            val scoreOfDirection = otherTrees
                .indexOfFirst { it >= this@Forest[row, col] }
                .takeUnless { it < 0 }
                ?.let { it + 1 }
                ?: otherTrees.size

            score * scoreOfDirection
        }
    }
}


//region examples of part 1
val exampleForest = listOf(
    "30373",
    "25512",
    "65332",
    "33549",
    "35390",
).let(::Forest)

with(exampleForest) {
    positions
        .count { position-> isTreeVisibleAt(position) }
        .also { visibleCount -> require(visibleCount == 21) }
}
//endregion

//region examples of part 2
with(exampleForest) {
    require(scenicScore(1 to 2) == 4)
    require(scenicScore(3 to 2) == 8)
}
//endregion


val forest = File("./input.txt").readLines().let(::Forest)

Pair(
    first = with(forest) {
        positions.count { isTreeVisibleAt(it) }
    }, // 1736

    second = with(forest) {
        positions.maxOf { scenicScore(it) }
    }, // 268800
)
