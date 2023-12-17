import kotlin.time.measureTime

data class Coordinate(val x: Int, val y: Int)

fun main() {
    fun rangeBetween(start: Int, end: Int) = if (start > end) start downTo end else start .. end

    fun solution(input: ArrayList<String>, expansionSize: Long = 2L): Long {
        // get all galaxy coordinates
        val galaxyCoords = arrayListOf<Coordinate>()
        input.forEachIndexed { rowIndex, row ->
            if(row.contains('#')) {
                row.forEachIndexed { columnIndex, column ->
                    if (column == '#') {
                        galaxyCoords.add(Coordinate(columnIndex, rowIndex))
                    }
                }
            }
        }

        // get expandes row/column indices
        val expandedRowIndices = arrayListOf<Int>()
        val expandedColumnIndices = arrayListOf<Int>()
        // rows
        for(i in input.indices) {
            if(!input[i].contains('#')) {
                expandedRowIndices.add(i)
            }
        }
        // columns
        var emptyColumn : Boolean
        for(i in input[0].indices) {
            emptyColumn = true
            input.forEach {
                if(it[i] == '#') {
                    emptyColumn = false
                    return@forEach
                }
            }
            if(emptyColumn) {
                expandedColumnIndices.add(i)
            }
        }

        // calculate all the things
        var result = 0L
        for(i in 0..<galaxyCoords.size-1) {
            for(j in i+1..<galaxyCoords.size) {
                var distance = 0L
                val start = galaxyCoords[i]
                val end = galaxyCoords[j]

                // x distance
                for(xPos in rangeBetween(start.x, end.x)) {
                    if(xPos != start.x) {
                        distance += if (expandedColumnIndices.contains(xPos)) {
                            expansionSize
                        } else {
                            1
                        }
                    }
                }

                // yDistance
                for(yPos in rangeBetween(start.y, end.y)) {
                    if(yPos != start.y) {
                        distance += if (expandedRowIndices.contains(yPos)) {
                            expansionSize
                        } else {
                            1
                        }
                    }
                }

                result += distance
            }
        }

        return result
    }

    measureTime {
        // test if implementation meets criteria from the description
        val testInput = readInput("Day11_test")
        solution(ArrayList(testInput)).let {
            println(it)
            check(it == 374L)
        }
        solution(ArrayList(testInput), 10L).let {
            println(it)
            check(it == 1030L)
        }
        solution(ArrayList(testInput), 100L).let {
            println(it)
            check(it == 8410L)
        }

        val input = readInput("Day11")
        // part 1
        solution(ArrayList(input)).println()
        // part 2
        solution(ArrayList(input), 1000000L).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}