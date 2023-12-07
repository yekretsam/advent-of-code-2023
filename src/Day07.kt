import kotlin.time.measureTime

class Hand(val cards: List<Int>, val bid: Int, var strength: Int = 0) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        return if(strength > other.strength) {
            1
        } else if(strength < other.strength) {
            -1
        } else {
            for(i in cards.indices) {
                if(cards[i] > other.cards[i]) {
                    return 1
                } else if(cards[i] < other.cards[i]) {
                    return -1
                }
            }
            0
        }
    }
}

fun main() {
    fun getWinnings(input: List<String>, jAsJokers: Boolean) : Int {
        var result = 0

        input.map { line ->
            val s = line.split(" ")
            Hand(
                s[0].map { card ->
                    when(card) {
                        'A' -> 14
                        'K' -> 13
                        'Q' -> 12
                        'J' -> if(jAsJokers) 1 else 11
                        'T' -> 10
                        else -> card.digitToInt()
                    }
                },
                s[1].toInt()
            ).apply {
                val sameLabels = cards.filter { it != 1 }.groupingBy { it }.eachCount().values.filter { it > 1 }.toMutableList()
                val nrOfJokers = cards.count { it == 1 }
                if(sameLabels.isNotEmpty()) {
                    sameLabels[0] += nrOfJokers
                } else if(nrOfJokers == 5) {
                    sameLabels.add(5)
                } else {
                    sameLabels.add(1 + nrOfJokers)
                }

                strength = if(sameLabels.contains(5)) {
                    7
                } else if(sameLabels.contains(4)){
                    6
                } else if(sameLabels.contains(3) && sameLabels.contains(2)){
                    5
                } else if(sameLabels.contains(3)){
                    4
                } else if(sameLabels.count { it == 2 } == 2){
                    3
                } else if(sameLabels.contains(2)){
                    2
                } else {
                    1
                }
            }
        }.sorted().forEachIndexed { index, hand ->
            result += (index+1) * hand.bid
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return getWinnings(input, false)
    }

    fun part2(input: List<String>): Int {
        return getWinnings(input, true)
    }

    measureTime {
        // test if implementation meets criteria from the description, like:
        val testInput = readInput("Day07_test")
        check(part1(testInput) == 6440)
        val testInput2 = readInput("Day07_test")
        check(part2(testInput2) == 5905)

        // solution
        val input = readInput("Day07")
        part1(input).println()
        part2(input).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}