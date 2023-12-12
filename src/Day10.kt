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

    fun isValidPath(input: List<String>, start: Pair<Int, Int>, toCheckRelative: Pair<Int, Int>, includeS: Boolean) : Boolean {
        // no diagonals
        if(toCheckRelative.first == 0 || toCheckRelative.second == 0){
            val currChar = input[start.first][start.second]
            val toCheckChar = input[start.first + toCheckRelative.first][start.second + toCheckRelative.second]
            if(toCheckRelative.first == -1) { // top
                if(currChar == '|' || currChar == 'L' || currChar == 'J' || currChar == 'S') {
                    return toCheckChar == '|' || toCheckChar == '7' || toCheckChar == 'F' || (includeS && toCheckChar == 'S')
                }
            } else if(toCheckRelative.first == 1) { // bottom
                if(currChar == '|' || currChar == '7' || currChar == 'F' || currChar == 'S') {
                    return toCheckChar == '|' || toCheckChar == 'L' || toCheckChar == 'J' || (includeS && toCheckChar == 'S')
                }
            } else if(toCheckRelative.second == -1) { // left
                if(currChar == '-' || currChar == 'J' || currChar == '7' || currChar == 'S') {
                    return toCheckChar == '-' || toCheckChar == 'F' || toCheckChar == 'L' || (includeS && toCheckChar == 'S')
                }
            } else if(toCheckRelative.second == 1) { // right
                if(currChar == '-' || currChar == 'F' || currChar == 'L' || currChar == 'S') {
                    return toCheckChar == '-' || toCheckChar == 'J' || toCheckChar == '7' || (includeS && toCheckChar == 'S')
                }
            }
            return false
        } else {
            return false
        }
    }

    fun floodMap(newMap: Array<CharArray>) : Array<CharArray> {
        var currLocationsToCheck: List<Pair<Int, Int>> = listOf(Pair(0,0))
        while(currLocationsToCheck.isNotEmpty()) {
            val newLocations: ArrayList<Pair<Int, Int>> = arrayListOf()
            currLocationsToCheck.forEach { currLocationToCheck ->
                for (i in currLocationToCheck.first - 1..currLocationToCheck.first + 1) {
                    for (j in currLocationToCheck.second - 1..currLocationToCheck.second + 1) {
                        if (
                            // bound checks
                            i >= 0 && i < newMap.size &&
                            j >= 0 && j < newMap[0].size &&
                            // skip explored Locations
                            newMap[i][j] != 'X' && newMap[i][j] != 'O')
                        {
                            newMap[i][j] = 'O'
                            newLocations.add(Pair(i,j))
                        }
                    }
                }
            }
            currLocationsToCheck = newLocations
        }

        return newMap
    }

    fun part1(input: List<String>) : Int {
        val startLocation = getStartCoords(input)

        val explored: ArrayList<Pair<Int, Int>> = arrayListOf()
        var currLocations = arrayListOf<Pair<Int, Int>>()
        currLocations.add(Pair(startLocation.first, startLocation.second))
        var steps = 0

        var newMap: Array<CharArray> = Array(input.size * 3) {
            CharArray(input[0].length * 3)
        }

        var lastLocation: Pair<Int, Int>? = startLocation
        while(true) {
            val newLocations = arrayListOf<Pair<Int, Int>>()
            steps++
            currLocations.forEach { currLocation ->
                // update my map
                for (i in currLocation.first - 1..currLocation.first + 1) {
                    for (j in currLocation.second - 1..currLocation.second + 1) {
                        val checking = Pair(i, j)
                        if (
                            // bound checks
                            i >= 0 && i < input.size &&
                            j >= 0 && j < input[0].length &&
                            // skip explored Locations
                            !(explored.any { it.first == checking.first && it.second == checking.second })
                        ) {
                            val toCheckRelative = Pair(
                                i - currLocation.first,
                                j - currLocation.second
                            )
                            if (isValidPath(input, currLocation, toCheckRelative, steps > 2)) {
                                val newMapX = currLocation.first * 3
                                val newMapY = currLocation.second * 3
                                newMap[newMapX][newMapY] = 'X'
                                if(lastLocation != null) {
                                    val lastRelative = Pair(
                                        lastLocation!!.first - currLocation.first,
                                        lastLocation!!.second - currLocation.second
                                    )
                                    if(lastRelative.first == -1 || toCheckRelative.first == -1) {
                                        newMap[newMapX-1][newMapY] = 'X'
                                    }
                                    if(lastRelative.first == 1 || toCheckRelative.first == 1) {
                                        newMap[newMapX+1][newMapY] = 'X'
                                    }
                                    if(lastRelative.second == -1 || toCheckRelative.second == -1) {
                                        newMap[newMapX][newMapY-1] = 'X'
                                    }
                                    if(lastRelative.second == 1 || toCheckRelative.second == 1) {
                                        newMap[newMapX][newMapY+1] = 'X'
                                    }

                                    if(newLocations.size == 0) {
                                        explored.add(checking)
                                        newLocations.add(Pair(i, j))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(newLocations.size == 0) {
                newMap = floodMap(newMap)
                var insideCnt = 0
                newMap.forEachIndexed { rowIndex, row ->
                    row.forEachIndexed { columnIndex, columns ->
                        if(rowIndex%3==0 && columnIndex%3==0) {
                            if(newMap[rowIndex][columnIndex] != 'X' && newMap[rowIndex][columnIndex] != 'O') {
                                print('I')
                                insideCnt++
                            } else {
                                print(newMap[rowIndex][columnIndex])
                            }
                        }
                    }
                    if(rowIndex%3==0)
                    println()
                }
                println("InsideCnt: $insideCnt")
                return (steps-1) / 2
            }
            lastLocation = currLocations[0]
            currLocations = newLocations
        }
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
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}