import kotlin.time.measureTime

fun main() {
    fun predictHistory(currReading: List<Int>, directionRight: Boolean): Int {
        if(currReading.all { it == 0 }) {
            return 0
        }

        val nextReading: ArrayList<Int> = arrayListOf()
        if(directionRight) {
            for (i in 0..<currReading.size - 1) {
                nextReading.add(currReading[i + 1] - currReading[i])
            }
        } else {
            for(i in 1..<currReading.size) {
                nextReading.add(currReading[i] - currReading[i-1])
            }
        }

        val nextHistory = predictHistory(nextReading, directionRight)
        val result = if(directionRight) {
            nextHistory + currReading.last()
        } else {
            currReading.first() - nextHistory
        }
        return result
    }

    fun solution(input: List<String>, directionRight: Boolean): Int {
        val readings = input.map { it.split(" ").map { it.toInt() } }

        return readings.fold(0) { acc, reading ->
            acc + predictHistory(reading, directionRight)
        }
    }

    measureTime {
        // test if implementation meets criteria from the description, like:
        val testInput = readInput("Day09_test")
        solution(testInput, true).let {
            println(it)
            check(it == 114)
        }
        val testInput2 = readInput("Day09_test")
        solution(testInput2, false).let {
            println(it)
            check(it == 2)
        }

        // solution
        val input = readInput("Day09")
        solution(input, true).println()
        solution(input, false).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}