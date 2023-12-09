import kotlin.time.measureTime


fun main() {
    fun gcd(a: Long, b: Long): Long = if(b == 0L) a else gcd(b, a % b)
    fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a,b)

    fun getLCM(values: List<Long>) : Long {
        return values.fold(1L) { a: Long, b: Long ->
            lcm(a, b)
        }
    }

    fun solution(input: List<String>, evalStart : (String) -> Boolean): Long {
        val instructions = input[0].map { it == 'L' }
        val nodeMappings = input.subList(2, input.size).associate { line ->
            val entries = line.replace('=', ',').filter { it.isLetter() || it.isDigit() || it == ',' }.split(',')
            entries[0] to entries.subList(1, 3)
        }

        val allCurrNodes = nodeMappings.filter { evalStart(it.key) }.map { it.key }
        val allResults: ArrayList<Long> = arrayListOf()
        allCurrNodes.forEach { it ->
            var nodeResult = 0L
            var instructionIndex = 0
            var currNode = it
            while(!currNode.endsWith('Z')) {
                currNode = if(instructions[instructionIndex]) {
                    nodeMappings[currNode]!![0]
                } else {
                    nodeMappings[currNode]!![1]
                }
                instructionIndex = (instructionIndex+1) % instructions.size
                nodeResult++
            }
            allResults.add(nodeResult)
        }

        return getLCM(allResults)
    }

    measureTime {
        val input = readInput("Day08")

        // Part1
        val evalStartPart1: (String) -> Boolean = { str ->
            str == "AAA"
        }
        val testInput = readInput("Day08_test")
        check(solution(testInput, evalStartPart1) == 2L)
        val testInput2 = readInput("Day08_test2")
        check(solution(testInput2, evalStartPart1) == 6L)
        solution(input, evalStartPart1).println()

        // Part2
        val evalStartPart2: (String) -> Boolean = { str ->
            str.endsWith('A')
        }
        val testInput3 = readInput("Day08_test3")
        check(solution(testInput3, evalStartPart2) == 6L)
        solution(input, evalStartPart2).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}