class TicTacToe(private var humanPlayer: Player = P.X) {
    private val minimax: Minimax<String> = Minimax()
    private var board = TicTacToeBoard()
    private var toMove: Player = P.X

    private var aiScore: Int = 0
    private var humanScore: Int = 0
    private var draws: Int = 0

    enum class P: Player {
        X, O;

        override val maxPlayer: Player
            get() = X
        override val minPlayer: Player
            get() = O
    }

    fun play() {
        while(true) {
            board = TicTacToeBoard()
            toMove = P.X

            board.print()
            while(!board.isGameOver()) {
                if(toMove == humanPlayer) {
                    print("Put $toMove into: ")
                    board = board.execute(readLine()!!) as TicTacToeBoard
                } else {
                    val best = minimax.minimax(board, toMove)
                    println("Evaluation: ${best.value}")
                    println("AI put $toMove into ${best.key}")
                    board = board.execute(best.key) as TicTacToeBoard
                }

                board.print()
                toMove = toMove.other();
            }

            println("\n")
            if(board.hasWon("X")) println("X has won!")
            else if(board.hasWon("O")) println("O has won!")
            else println("Draw!")

            if(board.hasWon(humanPlayer.other().toString())) aiScore++
            else if(board.hasWon(humanPlayer.toString())) humanScore++     // haha, as if
            else draws++

            println("AI: $aiScore Human: $humanScore Draws: $draws")

            this.humanPlayer = humanPlayer.other()
        }
    }

    data class TicTacToeBoard(val field: List<String> = List(9) { (it + 1).toString() },
                              val toMove: Player = P.X): State<String> {
        override fun execute(action: String): State<String> {
            val clone = ArrayList(field)
            clone[Integer.parseInt(action) - 1] = toMove.toString()
            return TicTacToeBoard(clone, toMove.other())
        }

        override fun getPossibleActions(): List<String> {
            return field.filter{ Character.isDigit(it.toCharArray()[0]) }
        }

        fun hasWon(player: String): Boolean {
            //Horizontal
            return field.slice(0..2).all{ it == player } ||
                    field.slice(3..5).all{ it == player } ||
                    field.slice(6..8).all{ it == player }||
                    //Vertical
                    field.slice(setOf(0, 3, 6)).all{ it == player } ||
                    field.slice(setOf(1, 4, 7)).all{ it == player } ||
                    field.slice(setOf(2, 5, 8)).all{ it == player } ||
                    //Diagonal
                    field.slice(setOf(0, 4, 8)).all{ it == player } ||
                    field.slice(setOf(2, 4, 6)).all{ it == player }
        }

        override fun isGameOver(): Boolean = field.none{ Character.isDigit(it.toCharArray()[0]) } ||
                hasWon("X") || hasWon("O")

        override fun utility(): Double = if(hasWon("X")) 100.0 else if(hasWon("O")) -100.0 else 0.0

        fun print() {
            println("---\n" +
                    field.map{ " $it " }
                        .chunked(3)
                        .joinToString("\n") { it.toString() })
        }
    }
}

fun main() {
    TicTacToe().play()
}