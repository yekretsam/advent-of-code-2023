import kotlin.time.measureTime

fun main() {
    fun getStartCoords(input: List<String>): Pair<Int, Int> {
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, char ->
                if(char == 'S') {
                    return Pair(i, j)
                }
            }
        }
        throw IllegalStateException("S not found :(")
    }

    fun isValidPath(input: List<String>, start: Pair<Int, Int>, toCheck: Pair<Int, Int>, includeS: Boolean) : Boolean {
        val toCheckRelative = intArrayOf(
            toCheck.first - start.first,
            toCheck.second - start.second
        )

        // no diagonals
        if(toCheckRelative.any { it == 0 }){
            val currChar = input[start.first][start.second]
            val toCheckChar = input[toCheck.first][toCheck.second]
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

    fun part1(input: List<String>) : Int {
        val startLocation = getStartCoords(input)

        val explored: ArrayList<Pair<Int, Int>> = arrayListOf()
        var currLocations = arrayListOf<Pair<Int, Int>>()
        currLocations.add(Pair(startLocation.first, startLocation.second))
        var steps = 0

        while(true) {
            val newLocations = arrayListOf<Pair<Int, Int>>()
            steps++
            currLocations.forEach { currLocation ->
                for (i in currLocation.first - 1..currLocation.first + 1) {
                    for (j in currLocation.second - 1..currLocation.second + 1) {
                        val checking = Pair(i, j)
                        if (steps > 2 && input[currLocation.first][currLocation.second] == 'S') {
                            return steps
                        }
                        if (
                            // bound checks
                            i >= 0 && i < input.size &&
                            j >= 0 && j < input[0].length &&
                            // skip explored Locations
                            !(explored.any { it.first == checking.first && it.second == checking.second })
                        ) {
                            if (isValidPath(input, currLocation, Pair(i, j), steps > 2)) {
                                explored.add(checking)
                                newLocations.add(Pair(i, j))
                            }
                        }
                    }
                }
            }
            if(newLocations.size == 0) return steps-1
            currLocations = newLocations
        }
    }

    fun part2(): Int {
        return 1
    }

    measureTime {
        // test if implementation meets criteria from the description, like:
        val input = readInput("Day10_test")
        part1(input).let {
            println(it)
            check(it == 4)
        }
        val input1 = readInput("Day10_test2")
        part1(input1).let {
            println(it)
            check(it == 8)
        }

        val input2 = readInput("Day10")
        part1(input2).println()

        //val testInput2 = readInput("Day10_test")
        //check(part2(testInput2) == 1)

        // solution
        //part1(input).println()
        //part2(input).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}