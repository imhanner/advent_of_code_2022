#!/usr/bin/kotlinc -script

import java.io.File


class Monkey(
    val items: MutableList<Long>,
    private val itemTest: ItemTest,
    private val operation: Operation,
    private val relief: (old: Long) -> Long,
) {
    var inspectedItemsCount = 0L

    val inspectedCount by ::inspectedItemsCount
    val divisor by itemTest::divisor


    fun turn(throwItemToMonkey: (item: Long, monkeyIndex: Int) -> Unit) {
        val (passedItems, failedItems) = items
            .map { old -> operation.run { operator(lhs ?: old, rhs ?: old) }.let(relief) }
            .also { inspectedItemsCount += it.size }
            .partition { worryLevel -> worryLevel % itemTest.divisor == 0L }

        passedItems.forEach { item -> throwItemToMonkey(item, itemTest.passedMonkey) }
        failedItems.forEach { item -> throwItemToMonkey(item, itemTest.failedMonkey) }
        items.clear()
    }

    fun catch(item: Long) {
        items.add(item)
    }


    data class Operation(
        val lhs: Long?,
        val rhs: Long?,
        val operator: (Long, Long) -> Long,
    )

    data class ItemTest(
        val divisor: Long,
        val passedMonkey: Int,
        val failedMonkey: Int,
    )
}


fun List<String>.toMonkeys(relief: (Long) -> Long = { it }): List<Monkey> = chunked(7) { lines ->
    Monkey(
        items = lines[1].split("items: ").last().split(", ").map { it.toLong() }.toMutableList(),
        operation = lines[2].split("new = ").last().split(" ").let { operationTokens ->
            Monkey.Operation(
                lhs = operationTokens[0].toLongOrNull(),
                operator = when (operationTokens[1]) {
                    "+" -> Long::plus
                    "*" -> Long::times
                    else -> TODO("operator '${operationTokens[1]}'")
                },
                rhs = operationTokens[2].toLongOrNull(),
            )
        },
        itemTest = Monkey.ItemTest(
            divisor = lines[3].split("divisible by ").last().toLong(),
            passedMonkey = lines[4].split("throw to monkey ").last().toInt(),
            failedMonkey = lines[5].split("throw to monkey ").last().toInt(),
        ),
        relief = relief,
    )
}

fun List<Monkey>.playKeepAway(rounds: Int = 20, isResidualClassGroup: Boolean = false) = also { monkeys ->

    val residualClassGroupModulo = fold(1L) { result, monkey -> result * monkey.divisor }

    repeat(rounds) {
        for (monkey in monkeys) {
            monkey.turn { item, catcherIndex ->
                monkeys[catcherIndex].catch(if (isResidualClassGroup) item % residualClassGroupModulo else item)
            }
        }
    }
}

fun List<Monkey>.monkeyBusiness(): Long = map { it.inspectedCount }
    .sorted()
    .takeLast(2)
    .fold(1L) { result, count -> result * count }


//region examples of part 1
val example = File("./example.txt").readLines()

example.toMonkeys { old -> old / 3L }
    .playKeepAway()
    .monkeyBusiness()
    .also { require(it == 10605L) }
//endregion

//region examples of part 2
example.toMonkeys()
    .playKeepAway(10_000, true)
    .monkeyBusiness()
    .also { require(it == 2713310158L) }
//endregion


val input = File("./input.txt").readLines()

Pair(
    first = input
        .toMonkeys { old -> old / 3L }
        .playKeepAway()
        .monkeyBusiness(), // 113220

    second = input
        .toMonkeys()
        .playKeepAway(10_000, true)
        .monkeyBusiness(), // 30599555965
)
