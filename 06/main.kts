#!/usr/bin/kotlinc -script

import java.io.File

val stream = File("./input.txt")
    .readLines()
    .first()


fun String.isStartOfPaket(position: Int, windowSize: Int) =
    (0 + position until windowSize + position)
        .let { window -> this.subSequence(window).toSet().size == windowSize }


fun firstStartOfPaket(stream: String, windowSize: Int = 4) = with(stream) {
    var i = 0
    while (!isStartOfPaket(i, windowSize)) i += 1
    i + windowSize
}

//region examples of part 1
listOf(
    "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
    "nppdvjthqldpwncqszvftbrmjlhg" to 6,
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10,
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11,
).forEach { (example, expectedResult) -> require(firstStartOfPaket(example) == expectedResult) }
//endregion


fun firstStartOfMessage(stream: String) = firstStartOfPaket(stream, 14)

//region examples of part 2
listOf(
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 19,
    "bvwbjplbgvbhsrlpgdmjqwftvncz" to 23,
    "nppdvjthqldpwncqszvftbrmjlhg" to 23,
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 29,
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 26,
).forEach { (example, expectedResult) -> require(firstStartOfMessage(example) == expectedResult) }
//endregion


Pair(
    first = firstStartOfMessage(stream), // 1598

    second = firstStartOfMessage(stream), // 2414
)
