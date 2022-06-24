import kotlin.random.Random

/**
 * Action ... Action type       i.e. data type of Actions
 *
 * getPossibleActions()         Returns all legal Actions in this State (MUST contain at least 1 possible actions)
 * execute()                    Executes an action and returns the resulting State (MUST be a new State)
 * isGameOver()                 Returns if the game is over
 * utility()                    Returns an evaluation of the current state
 *                              This can be very primitive, since the whole state space is searched by the algorithm
 *                              e.g. won -> 100; lost -> -100; uncertain -> 0
 *
 * ALREADY IMPLEMENTED METHODS:
 *
 * futureValueWorthPolicy(v)    Determines how much future values are worth compared to current values.
 *                              The idea behind this is, that immediate actions should be worth more than
 *                              future actions with the same evaluation.
 *                              By default, actions one step in the future with the same evaluation
 *                              are worth half of current actions (this reduction might be too high for your case).
 *                              Future actions are worth more, the higher you choose
 *                              the number the evaluation is multiplied with
 *                              e.g. v * 0.9 means Future actions have 90% worth of current actions.
 *
 * best(maxPlayerToMove, difficulty, thresholds)
 *                              Returns a Pair consisting of the best action as first, and it's evaluation as second.
 *      maxPlayerToMove         If the maxPlayer is the next to move (the one that benefits from positive evaluation)
 *      difficulty              The difficulty of the AI (perfect play by default)
 *      thresholds              A list of percentage thresholds for playing the best move for medium and hard difficulty
 *                              FREE is always the worst move
 *                              EASY is always a random move
 *                              MEDIUM is thresholds[0]     (default: 50%)
 *                              HARD is thresholds[1]       (default: 80%)
 *                              IMPOSSIBLE always the best move
 *
 * eval(maxPlayerToMove)        Returns the evaluation of the current position
 */
sealed interface State<Action> {
    fun getPossibleActions(): List<Action>
    fun execute(action: Action): State<Action>
    fun isGameOver(): Boolean
    fun utility(): Double

    // ONLY CHANGE THE FOLLOWING IF YOU KNOW WHAT YOU'RE DOING
    fun futureValueWorthPolicy(v: Double): Double = v * 0.5

    fun best(maxPlayerToMove: Boolean, difficulty: Difficulty = Difficulty.IMPOSSIBLE): Pair<Action, Double> =
        applyDifficulty(maxPlayerToMove, difficulty)

    fun eval(maxPlayerToMove: Boolean): Double = minimax(maxPlayerToMove).second

    private fun minimax(maxPlayerToMove: Boolean): Pair<Action, Double> {
        return if(maxPlayerToMove)
                    getPossibleActions()
                        .shuffled()         // to get different best move each time
                        .associateWith { minValue(execute(it)) }
                        .maxWithOrNull { x, y -> x.value.compareTo(y.value) }!!
                        .toPair()
                else
                    getPossibleActions()
                        .shuffled()         // to get different best move each time
                        .associateWith { maxValue(execute(it)) }
                        .minWithOrNull() { x, y -> x.value.compareTo(y.value) }!!
                        .toPair()
    }

    private fun maxValue(state: State<Action>): Double {
        if(state.isGameOver()) return state.utility()
        var v: Double = -99999.0

        for(a in state.getPossibleActions()) {
            val result = minValue(state.execute(a))
            if(result > v) v = result
        }

        return futureValueWorthPolicy(v)
    }

    private fun minValue(state: State<Action>): Double {
        if(state.isGameOver()) return state.utility()
        var v: Double = +99999.0

        for(a in state.getPossibleActions()) {
            val result = maxValue(state.execute(a))
            if(result < v) v = result
        }

        return futureValueWorthPolicy(v)
    }

    private fun applyDifficulty(maxPlayerToMove: Boolean,
                                difficulty: Difficulty,
                                thresholds: List<Int> = listOf(50, 80)): Pair<Action, Double> {
        val randomAction: Action = getPossibleActions()[Random.nextInt(getPossibleActions().size)]
        val best = minimax(maxPlayerToMove)

        return when(difficulty) {
            Difficulty.FREE -> Pair(minimax(!maxPlayerToMove).first, eval(maxPlayerToMove))
            Difficulty.EASY -> Pair(randomAction, best.second)
            Difficulty.MEDIUM ->
                if(Random.nextInt() > thresholds[0]) Pair(randomAction, best.second) else minimax(maxPlayerToMove)
            Difficulty.HARD ->
                if(Random.nextInt() > thresholds[1]) Pair(randomAction, best.second) else minimax(maxPlayerToMove)
            Difficulty.IMPOSSIBLE -> minimax(maxPlayerToMove)
        }
    }
}

enum class Difficulty {
    FREE, EASY, MEDIUM, HARD, IMPOSSIBLE
}
