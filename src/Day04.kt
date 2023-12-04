import kotlin.math.pow

data class Card(val winningNrs: List<Int>, val myNrs: List<Int>, var cnt: Int = 1)

fun main() {
    fun strNumbersToListNumbers(strNumbers : String) : List<Int> {
        return strNumbers.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    }

    fun getCard(input: String) : Card {
        val matchResult = "Card(?:\\s)+(\\d+):((?:\\d|\\s)+) [|] ((?:\\d|\\s)*)".toRegex().find(input)
        if(matchResult != null) {
            return Card(
                strNumbersToListNumbers(matchResult.groupValues[2]),
                strNumbersToListNumbers(matchResult.groupValues[3])
            )
        } else {
            throw IllegalStateException()
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { cardInput ->
            var cnt = 0
            getCard(cardInput).let { card ->
                card.winningNrs.forEach { winningNumber ->
                    if(card.myNrs.contains(winningNumber)) cnt++
                }
            }
            if(cnt > 0) {
                2.0.pow(cnt - 1).toInt()
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cardList = input.map { getCard(it) }

        cardList.forEachIndexed { index, card ->
            var cnt = 0

            card.winningNrs.forEach { winningNumber ->
                if (card.myNrs.contains(winningNumber)) cnt++
            }

            while(cnt > 0) {
                cardList[index+cnt].cnt += card.cnt
                cnt--
            }
        }

        return cardList.sumOf { it.cnt }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day04_test")
    check(part2(testInput2) == 30)

    // solution
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}