#!/usr/bin/kotlinc -script

import java.io.File


typealias CycleResults = List<Int>

fun List<String>.toCycleResults(): CycleResults = buildList {
    var V = 1

    add(V)

    this@toCycleResults.map {
            when {
                it == "noop" -> listOf(0)
                it.startsWith("addx") -> listOf(0, it.split(" ").last().toInt())
                else -> TODO("instruction $it")
            }
        }
        .flatten()
        .forEach {
            V += it
            add(V)
        }
}

fun CycleResults.duringCycle(i: Int) = this[i - 1]

fun CycleResults.toPixels() = (1 .. 240).map { pixelDrawCycle ->
    val registerPosition = duringCycle(pixelDrawCycle)

    if ((pixelDrawCycle - 1) % 40 in registerPosition - 1 .. registerPosition + 1) "#" else "."
}

fun List<String>.toCrtImage() = listOf(
    slice(0 until 40),
    slice(40 until 80),
    slice(80 until 120),
    slice(120 until 160),
    slice(160 until 200),
    slice(200 until 240),
)
    .map { it.joinToString("") }
    .joinToString("\n")

//region examples of part 1
val example = File("./example.txt").readLines()

example.toCycleResults().let { cycleResults ->
    (20 .. 220 step 40)
        .zip(listOf(420, 1140, 1800, 2940, 2880, 3960))
        .onEach { (i, expectedSignalStrength) ->
            val actualSignalStrength = i * cycleResults.duringCycle(i)
            require(actualSignalStrength == expectedSignalStrength) { "expected: $expectedSignalStrength VS actual: $actualSignalStrength" }
        }
        .fold(0) { sum, (i, _) -> sum + i * cycleResults.duringCycle(i) }
        .also { require(it == 13140) { "actual sum: $it" } }
}
//endregion

//region examples of part 2
example.toCycleResults().toPixels().toCrtImage().also { image ->
    val expectedImage = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    require(image == expectedImage) { listOf("expected:", expectedImage, "actual:", image).joinToString("\n") }
}
//endregion


val cycleResults: CycleResults = File("./input.txt").readLines().toCycleResults()

Pair(
    first = (20 .. 220 step 40).fold(0) { sum, i -> sum + i * cycleResults.duringCycle(i) }, // 13480

    second = cycleResults.toPixels().toCrtImage(), // EGJBGCFK
    // ####..##....##.###...##...##..####.#..#.
    // #....#..#....#.#..#.#..#.#..#.#....#.#..
    // ###..#.......#.###..#....#....###..##...
    // #....#.##....#.#..#.#.##.#....#....#.#..
    // #....#..#.#..#.#..#.#..#.#..#.#....#.#..
    // ####..###..##..###...###..##..#....#..#.
)
