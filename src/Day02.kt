fun main() {

    data class BagDraw(var red: Int, var green: Int, var blue: Int)
    data class Game(var id: Int, val draws: ArrayList<BagDraw>)

    fun gameForInput(str: String) : Game {
        val result = Game(0, arrayListOf())

        val regex = "(\\d+):((?:(?:\\d+[a-z]+)+,*;*)+)".toRegex()
        val regexResult = regex.find(str.replace(" ", ""))
        if(regexResult != null) {
            result.id = regexResult.groupValues[1].toInt()
            val drawStrings = regexResult.groupValues[2]
            drawStrings.split(';').forEach { bagDraw ->
                val currBagDraw = BagDraw(0,0,0)
                bagDraw.split(',').forEach { bagDrawPerColor ->
                    val match = "(\\d+)([a-z]+)".toRegex().find(bagDrawPerColor)
                    if(match != null) {
                        val n = match.groupValues[1].toInt()
                        val color = match.groupValues[2]
                        when(color) {
                            "red" -> currBagDraw.red = n
                            "green" -> currBagDraw.green = n
                            "blue" -> currBagDraw.blue = n
                        }
                    }
                }
                result.draws.add(currBagDraw)
            }
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val game = gameForInput(line)
            game.draws.forEach { draw ->
                if(draw.red > 12 || draw.green > 13 || draw.blue > 14) {
                    return@sumOf 0
                }
            }
            return@sumOf game.id
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val game = gameForInput(line)
            var minRed = 0
            var minGreen = 0
            var minBlue = 0
            game.draws.forEach { draw ->
                minRed = minRed.coerceAtLeast(draw.red)
                minGreen = minGreen.coerceAtLeast(draw.green)
                minBlue = minBlue.coerceAtLeast(draw.blue)
            }
            return@sumOf minRed * minGreen * minBlue
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)

    // solution
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}