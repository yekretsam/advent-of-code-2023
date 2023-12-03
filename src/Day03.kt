fun main() {
    // UTIL

    fun Char.isSymbol() = this != '.' && !this.isDigit()

    fun checkAdjacentLines(input: List<String>, validate: (String, String, String) -> Int) : Int {
        var result = 0

        if(input.isEmpty()) return 0

        var i = 0
        val dummyLine = "".padStart(input[0].length, '.')
        while(i < input.size) {
            val beforeLine = if(i == 0) {
                dummyLine
            } else {
                input[i-1]
            }
            val nextLine = if(i == input.size-1) {
                dummyLine
            } else {
                input[i+1]
            }

            result += validate(
                beforeLine,
                input[i],
                nextLine
            )

            i++
        }

        return result
    }

    // PART 1

    fun getPart1Sum(beforeLine: String, currLine: String, nextLine: String) : Int {
        var result = 0

        var index = 0
        while(index < currLine.length) {
            if(currLine[index].isDigit()) {
                var strNumber = ""
                var hasSymbol = false
                while(index < currLine.length && currLine[index].isDigit()) {
                    strNumber += currLine[index]

                    if(!hasSymbol) {
                        if(index-1 > 0) {
                            hasSymbol = beforeLine[index-1].isSymbol() || currLine[index-1].isSymbol() || nextLine[index-1].isSymbol()
                        }

                        if(!hasSymbol) {
                            hasSymbol = beforeLine[index].isSymbol() || currLine[index].isSymbol() || nextLine[index].isSymbol()
                        }
                    }

                    index++
                }
                if(!hasSymbol && index < currLine.length) {
                    hasSymbol = beforeLine[index].isSymbol() || currLine[index].isSymbol() || nextLine[index].isSymbol()
                }

                if(hasSymbol) result += strNumber.toInt()
            }

            index++
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return checkAdjacentLines(input) { beforeLine, currLine, nextLine ->
            getPart1Sum(beforeLine = beforeLine, currLine = currLine, nextLine = nextLine)
        }
    }

    // PART 2

    fun getAdjacentNumbers(str: String, indexToCheck: Int) : ArrayList<Int> {
        val result = arrayListOf<Int>()

        var i = indexToCheck
        // find starting point
        while(i-1 >= 0 && str[i-1].isDigit()) {
            i--
        }

        // find numbers
        var currNumber = ""
        while(i <= indexToCheck+1) {
            if(!str[i].isDigit()) {
                if(currNumber.isNotEmpty()) {
                    result.add(currNumber.toInt())
                    currNumber = ""
                }
                i++
            } else {
                while(i < str.length && str[i].isDigit()) {
                    currNumber += str[i]
                    i++
                }
            }
        }
        if(currNumber.isNotEmpty()) {
            result.add(currNumber.toInt())
        }

        return result
    }

    fun getPart2Sum(beforeLine: String, currLine: String, nextLine: String): Int {
        var result = 0

        var index = 0
        while(index < currLine.length) {
            if(currLine[index].isSymbol()) {
                val numbers = arrayListOf<Int>()

                numbers.apply {
                    addAll(getAdjacentNumbers(beforeLine, index))
                    addAll(getAdjacentNumbers(currLine, index))
                    addAll(getAdjacentNumbers(nextLine, index))
                }

                if(numbers.size == 2) result += numbers[0] * numbers[1]
            }

            index++
        }

        return result
    }

    fun part2(input: List<String>): Int {
        return checkAdjacentLines(input) { beforeLine, currLine, nextLine ->
            getPart2Sum(beforeLine = beforeLine, currLine = currLine, nextLine = nextLine)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    val testInput2 = readInput("Day03_test")
    check(part2(testInput2) == 467835)

    // solution
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}