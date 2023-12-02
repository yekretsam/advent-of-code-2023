enum class Direction { First, Last }

fun main() {
    val spelledDigitsMap = hashMapOf(
        "one" to '1',
        "two" to '2',
        "three" to '3',
        "four" to '4',
        "five" to '5',
        "six" to '6',
        "seven" to '7',
        "eight" to '8',
        "nine" to '9')

    fun part1(input: List<String>): Int {
        return input.sumOf { str ->
            "${str.first { it.isDigit() }}${str.last { it.isDigit() }}".toInt()
        }
    }

    fun findMeMyDigit(
        inputString: String,
        direction: Direction) : Char {
        val digitIndex = when(direction) {
            Direction.First -> inputString.indexOfFirst { it.isDigit() }
            Direction.Last -> inputString.indexOfLast { it.isDigit() }
        }
        val wordPair = when(direction) {
            Direction.First -> inputString.findAnyOf(spelledDigitsMap.keys)
            Direction.Last -> inputString.findLastAnyOf(spelledDigitsMap.keys)
        }

        return if(wordPair != null && (digitIndex == -1 || when(direction) {
                Direction.First -> wordPair.first < digitIndex
                Direction.Last -> wordPair.first > digitIndex
            })) {
            spelledDigitsMap[wordPair.second]!!
        } else {
            inputString[digitIndex]
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { str ->
            val first = findMeMyDigit(str, Direction.First)
            val last = findMeMyDigit(str, Direction.Last)

            "$first$last".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test_part1")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test_part2")
    check(part2(testInput2) == 281)

    // solution
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
