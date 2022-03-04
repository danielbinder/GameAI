/**
 * Action ... Action type
 *
 * getPossibleActions()     Returns all legal Actions in this State (MUST contain at least 1 possible actions)
 * execute()                Executes an action and returns the resulting State
 * isGameOver()             Returns if the Game is over
 * utility()                Returns an evaluation of the current state
 *                          This can be very primitive, since the whole state space is searched by the algorithm
 *                          e.g. won -> 100; lost -> -100; uncertain -> 0
 */
sealed interface State<Action> {
    fun getPossibleActions(): List<Action>
    fun execute(action: Action): State<Action>
    fun isGameOver(): Boolean
    fun utility(): Double
}

/**
 * This is the acting entity.
 *
 * If e.g. your Player is represented by a String like 'X' in TikTacToe, you need to
 *      extend the String class AND
 *      implement this interface
 *
 * maxPlayer        The player for whom high evaluation values are good
 * minPlayer        The player for whom low evaluation values are good
 * other()          Returns the other player
 */
sealed interface Player {
    val maxPlayer: Player
    val minPlayer: Player
    fun other(): Player = if(this == maxPlayer) minPlayer else maxPlayer
}

/**
 * Implements the minimax algorithm for your State and Player
 *
 * Action ... Action type
 *
 * futureValueWorthPolicy       Determines how much future values are worth compared to current values.
 *                              The idea behind this is, that immediate actions should be worth more than
 *                              future actions with the same evaluation.
 *                              By default, actions one step in the future with the same evaluation
 *                              are worth half of current actions (this reduction might be too high for your case).
 *                              Future actions are worth more, the higher you choose
 *                              the number the evaluation is multiplied with
 *                              e.g. it * 0.9 means Future actions have 90% worth of current actions.
 *
 * minimax(state, toMove)       Returns a Map entry consisting of the best action as key, and it's evaluation as value.
 *      state                   The current Board state
 *      toMove                  The Player to move
 */
class Minimax<Action> (val futureValueWorthPolicy: ((Double) -> Double) = { it * 0.5 }) {
    fun minimax(state: State<Action>, toMove: Player): Map.Entry<Action, Double> {
        return if(toMove == toMove.maxPlayer) {
            state.getPossibleActions()
                .associateWith { minValue(state.execute(it), toMove.other()) }
                .maxWithOrNull { x, y -> x.value.compareTo(y.value) }!!
        } else {
            state.getPossibleActions()
                .associateWith{ maxValue(state.execute(it), toMove.other()) }
                .minWithOrNull{x, y -> x.value.compareTo(y.value)}!!
        }
    }

    private fun maxValue(state: State<Action>, toMove: Player): Double {
        if(state.isGameOver()) return state.utility()
        var v: Double = -99999.0

        for(a in state.getPossibleActions()) {
            val result = minValue(state.execute(a), toMove.other())
            if(result > v) v = result
        }

        return futureValueWorthPolicy(v)
    }

    private fun minValue(state: State<Action>, toMove: Player): Double {
        if(state.isGameOver()) return state.utility()
        var v: Double = +99999.0

        for(a in state.getPossibleActions()) {
            val result = maxValue(state.execute(a), toMove.other())
            if(result < v) v = result
        }

        return futureValueWorthPolicy(v)
    }
}