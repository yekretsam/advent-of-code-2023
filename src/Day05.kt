import kotlin.time.measureTime

data class Mapping(val source: Long, val destination: Long, val range: Long)

class Almanac() {
    private lateinit var seeds: List<Long>
    private var mappings: ArrayList<List<Mapping>> = arrayListOf()

    constructor(input: List<String>) : this() {
        var i = 0
        while(i < input.size) {
            if(input[i].startsWith("seeds: ")) {
                this.seeds = input[i].substring(7).split(" ").map { it.toLong() }
            } else if(input[i].isNotBlank()) {
                i++
                val newMapping = arrayListOf<Mapping>()
                while(i < input.size && input[i].isNotBlank()) {
                    newMapping.add(
                        input[i].split(" ").run {
                            Mapping(
                                this[1].toLong(),
                                this[0].toLong(),
                                this[2].toLong()
                            )
                        }
                    )
                    i++
                }
                mappings.add(newMapping)
            }
            i++
        }
    }

    private fun findInMappings(toFind: Long, mapping: List<Mapping>) : Long {
        mapping.forEach { currMapping ->
            if(toFind in currMapping.source..currMapping.source+currMapping.range) {
                return currMapping.destination + toFind - currMapping.source
            }
        }
        return toFind
    }

    private fun getLocationForSeed(seed: Long) : Long {
        return mappings.fold(seed) { toFind, mapping ->
            findInMappings(toFind, mapping)
        }
    }

    fun computeLowestLocationNumber() = seeds.minOf { seed ->
        getLocationForSeed(seed)
    }

    fun computeLowestLocationNumberUnfolded() : Long {
        var minLocation = Long.MAX_VALUE
        for(seedIndex in seeds.indices step 2) {
            for(seed in seeds[seedIndex]..<seeds[seedIndex]+seeds[seedIndex+1]) {
                minLocation = minLocation.coerceAtMost(getLocationForSeed(seed))
            }
        }
        return minLocation
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        return Almanac(input).computeLowestLocationNumber()
    }

    fun part2(input: List<String>): Long {
        return Almanac(input).computeLowestLocationNumberUnfolded()
    }

    measureTime {
        // test if implementation meets criteria from the description, like:
        val testInput = readInput("Day05_test")
        check(part1(testInput) == 35L)
        val testInput2 = readInput("Day05_test")
        check(part2(testInput2) == 46L)

        // solution
        val input = readInput("Day05")
        part1(input).println()
        part2(input).println()
    }.also { println("This took us ${it.inWholeMilliseconds} ms") }
}