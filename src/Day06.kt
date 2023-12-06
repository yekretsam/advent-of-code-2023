import kotlin.time.measureTime

fun main() {
    // we don't even need any optimization for finding the first winner?
    fun computeResult(times: List<Long>, distances: List<Long>) : Int {
        var result = 1
        for(raceIndex in times.indices) {
            var winPossibilities = 0
            var foundWinners = false
            for(timePressing in 1..<times[raceIndex]) {
                if(timePressing * (times[raceIndex] - timePressing) > distances[raceIndex]) {
                    foundWinners = true
                    winPossibilities++
                } else if(foundWinners) {
                    break // end of the winners
                }
            }
            result *= winPossibilities
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val times = input[0].split(" ").filter { it.isNotBlank() && it[0].isDigit() }.map { it.toLong() }
        val distances = input[1].split(" ").filter { it.isNotBlank() && it[0].isDigit() }.map { it.toLong() }

        return computeResult(times, distances)
    }

    fun part2(input: List<String>): Int {
        val time = input[0].filter { it.isDigit() }.toLong()
        val distance = input[1].filter { it.isDigit() }.toLong()

        return computeResult(listOf(time), listOf(distance))
    }

    measureTime {
        // test if implementation meets criteria from the description, like:
        val testInput = readInput("Day06_test")
        check(part1(testInput) == 288)
        val testInput2 = readInput("Day06_test")
        check(part2(testInput2) == 71503)

        // solution
        val input = readInput("Day06")
        part1(input).println()
        part2(input).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}