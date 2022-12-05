import kotlin.math.ceil

typealias Stack = List<Crate>

@JvmInline
value class Crate(val value: String) {
    override fun toString() = "<$value>"
}

data class Instr(
    val amount: Int,
    val from: Int,
    val to: Int,
)

fun main() {
    val (rawStacks, rawInstructions) = readInput("Day05")
        .filter { it.isNotBlank() }
        .partition { !it.startsWith("move") } // split input file into two halves
    val stackCount = ceil(rawStacks.maxBy { it.length }.length / 4f).toInt()
    val stacks: List<Stack> = rawStacks
        .subList(0, rawStacks.size - 1)
        .map { it.windowed(size = 4, step = 4, partialWindows = true) }
        .map { it + (if (it.size < stackCount) List(stackCount - it.size) { null } else emptyList()) } // make sure all columns have the same size
        .swapRowsAndColumns() // consider stacks rather than horizontal layers of crates
        .map { stack ->
            stack
                .filterNotNull()
                .filter { it.isNotBlank() } // remove empty cells
                .map { Crate(it[1].toString()) }
        }
//        .onEach { println(it) }
    val instructions = rawInstructions
        .map { it.substring(5).split(" from ", " to ") }
        .map { it.map { it.toInt() } }
        .map { Instr(amount = it[0], from = it[1], to = it[2]) }
//        .onEach { println(it) }

    determineTopCrates(instructions, stacks, keepOrder = false)
        .also { printP1(it.joinToString("") { it.value }) }
    determineTopCrates(instructions, stacks, keepOrder = true)
        .also { printP2(it.joinToString("") { it.value }) }
}

fun determineTopCrates(instructions: List<Instr>, initialStacks: List<Stack>, keepOrder: Boolean): List<Crate> = instructions.fold(initialStacks) { acc, instruction ->
    val fromCrate = acc[instruction.from - 1]
    val movableCrates = fromCrate.subList(0, instruction.amount)
        .let { if (!keepOrder) it.reversed() else it }

    val newFromCrate = fromCrate.subList(instruction.amount, fromCrate.size)
    val newToCrate = acc[instruction.to - 1].toMutableList()
        .also { it.addAll(0, movableCrates) }

    acc.mapIndexed { i, stack ->
        when (i) {
            instruction.from - 1 -> newFromCrate
            instruction.to - 1 -> newToCrate
            else -> stack
        }
    }
}.map { it.first() }

fun <T> List<List<T>>.swapRowsAndColumns() = (0 until first().size)
    .map { row -> indices.map { col -> this[col][row] } }