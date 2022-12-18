#!/usr/bin/kotlinc -script

import java.io.File
import java.util.LinkedList
import java.util.Queue

typealias Heightmap = List<String>

typealias Position = Pair<Int, Int>
val Position.x get() = first
val Position.y get() = second
operator fun Position.plus(other: Position): Position = this.x + other.x to this.y + other.y


fun Heightmap.firstPositionOf(needle: Char): Position =
    indexOfFirst { row -> needle in row }
        .let { y -> this[y].indexOfFirst { it == needle } to y }

fun Heightmap.positionsOf(needle: Char): List<Position> =
    mapIndexed { r, row ->
        row.mapIndexedNotNull { c, tile ->
            if (tile == needle) c to r else null
        }
    }.flatten()

operator fun Heightmap.get(position: Position) = this[position.y][position.x]


data class Environment(
    val start: Position,
    val destination: Position,
    val heightmap: Heightmap,
) {
    /**
     * Counts steps of a shortest path found using BFS ([Breadth-first search](https://en.wikipedia.org/wiki/Breadth-first_search))
     *
     * @return count of steps if found else `null`
     */
    fun shortestPath(): Int? {
        val queue: Queue<Pair<Position, Int>> = LinkedList<Pair<Position, Int>>().apply { add(start to 0) }
        val alreadyHandled = mutableSetOf<Position>(start)

        while (queue.isNotEmpty()) {
            val (position, steps) = queue.poll()

            if (position == destination)
                return steps

            val go = { step: Position ->
                val from = heightmap[position]
                val goto = heightmap[position + step]

                (goto - from) to (position + step)
            }

            listOfNotNull(
                if (position.y > 0) go(0 to -1) else null, // up
                if (position.x < heightmap.first().lastIndex) go(1 to 0) else null, // right
                if (position.y < heightmap.lastIndex) go(0 to 1) else null, // down
                if (position.x > 0) go(-1 to 0) else null, // left
            )
                .filter { (heightDelta, newPosition) -> heightDelta <= 1 && newPosition !in alreadyHandled }
                .forEach { (_, nextPosition) ->
                    queue.add(nextPosition to steps + 1)
                    alreadyHandled.add(nextPosition)
                }
        }

        return null
    }
}

fun List<String>.toEnvironment(): Environment {
    val start = firstPositionOf('S')
    val destination = firstPositionOf('E')

    val heightmap = toMutableList().apply {
         this[start.y] = this[start.y].replace('S', 'a')
         this[destination.y] = this[destination.y].replace('E', 'z')
    }

    return Environment(start, destination, heightmap)
}

//region examples of part 1
val example = listOf(
    "Sabqponm",
    "abcryxxl",
    "accszExk",
    "acctuvwj",
    "abdefghi",
)

example
    .toEnvironment()
    .shortestPath()
    .also { require(it == 31) }
//endregion

//region examples of part 2
example
    .toEnvironment()
    .run {
        heightmap
            .positionsOf('a')
            .mapNotNull { copy(start = it).shortestPath() }
            .min()
    }
    .also { require(it == 29) }
//endregion


val input = File("./input.txt").readLines()

Pair(
    first = input
        .toEnvironment()
        .shortestPath(), // 339

    second = input
        .toEnvironment()
        .run {
            heightmap
                .positionsOf('a')
                .mapNotNull { copy(start = it).shortestPath() }
                .min()
        }, // 332
)
