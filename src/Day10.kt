import kotlin.time.measureTime

var cnt: Long = 0L
var input: List<String> = listOf()
var explored: ArrayList<IntArray> = arrayListOf()

fun main() {
    fun getStartCoords(): IntArray {
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, char ->
                if(char == 'S') {
                    return intArrayOf(i, j)
                }
            }
        }
        throw IllegalStateException("S not found :(")
    }

    fun isValidPath(start: IntArray, toCheck: IntArray, includeS: Boolean) : Boolean {
        val toCheckRelative = intArrayOf(
            toCheck[0] - start[0],
            toCheck[1] - start[1]
        )

        // no diagonals
        if(toCheckRelative.any { it == 0 }){
            val currChar = input[start[0]][start[1]]
            val toCheckChar = input[toCheck[0]][toCheck[1]]
            if(toCheckRelative[0] == -1) { // top
                if(currChar == '|' || currChar == 'L' || currChar == 'J' || currChar == 'S') {
                    return toCheckChar == '|' || toCheckChar == '7' || toCheckChar == 'F' || (includeS && toCheckChar == 'S')
                }
            } else if(toCheckRelative[0] == 1) { // bottom
                if(currChar == '|' || currChar == '7' || currChar == 'F' || currChar == 'S') {
                    return toCheckChar == '|' || toCheckChar == 'L' || toCheckChar == 'J' || (includeS && toCheckChar == 'S')
                }
            } else if(toCheckRelative[1] == -1) { // left
                if(currChar == '-' || currChar == 'J' || currChar == '7' || currChar == 'S') {
                    return toCheckChar == '-' || toCheckChar == 'F' || toCheckChar == 'L' || (includeS && toCheckChar == 'S')
                }
            } else if(toCheckRelative[1] == 1) { // right
                if(currChar == '-' || currChar == 'F' || currChar == 'L' || currChar == 'S') {
                    return toCheckChar == '-' || toCheckChar == 'J' || toCheckChar == '7' || (includeS && toCheckChar == 'S')
                }
            }
            return false
        } else {
            return false
        }
    }

    fun findStepsToS(currLocation: IntArray, currSteps: Int) : Int {
        //println("$currSteps: ${currLocation.toList()}: ${input[currLocation[0]][currLocation[1]]}")

        if(input[currLocation[0]][currLocation[1]] == 'S') {
            if(currSteps > 2) {
                return currSteps
            }
        }

        if(currSteps > 10000) {
            return -1
        }

        // check all directions
        for(i in currLocation[0]-1..currLocation[0]+1) {
            for(j in currLocation[1]-1..currLocation[1]+1) {
                val checking = intArrayOf(i, j)
                if(
                    // bound checks
                    i >= 0 && i < input.size &&
                    j >= 0 && j < input[0].length &&
                    // skip explored Locations
                    !(explored.any { it.contentEquals(checking) })
                ) {
                    if(isValidPath(currLocation, intArrayOf(i, j), currSteps > 1)) {
                        explored.add(intArrayOf(i, j))
                        findStepsToS(intArrayOf(i, j), currSteps + 1).let {
                            if (it != -1) {
                                return it
                            }
                        }
                    }
                }
            }
        }
        return -1
    }

    fun part1(): Int {
        // find S
        val currLocation = getStartCoords()

        return findStepsToS(currLocation, 0) / 2
    }

    fun part2(): Int {
        return 1
    }

    measureTime {
        // test if implementation meets criteria from the description, like:
        input = readInput("Day10_test")
        part1().let {
            println(it)
            check(it == 4)
        }

        input = readInput("Day10_test2")
        explored = arrayListOf()
        part1().let {
            println(it)
            check(it == 8)
        }
        input = readInput("Day10")
        explored = arrayListOf()
        part1().println()

        //val testInput2 = readInput("Day10_test")
        //check(part2(testInput2) == 1)

        // solution
        //part1(input).println()
        //part2(input).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}