class TicTacToe(private var humanPlayer: Player = Player.X) {
    private var board = TicTacToeBoard()

    private var aiScore: Int = 0
    private var humanScore: Int = 0
    private var draws: Int = 0

    fun play() {
        while(true) {
            board = TicTacToeBoard()

            // MAIN GAME LOOP //
            board.print()
            while(!board.isGameOver()) {
                if(board.toMove() == humanPlayer) {
                    println("Evaluation: ${board.eval(board.toMove().isMaxPlayer())}")
                    print("Put ${board.toMove()} into: ")
                    board = board.execute(readLine()!!) as TicTacToeBoard
                } else {
                    val aiMove = board.best(board.toMove().isMaxPlayer())       // this is where the magic happens
                    println("Evaluation: ${aiMove.second}")
                    println("AI put ${board.toMove()} into ${aiMove.first}")
                    board = board.execute(aiMove.first) as TicTacToeBoard
                }

                board.print()
            }

            // MANAGE RESULT //
            println("\n")
            if(board.hasWon("X")) println("X has won!")
            else if(board.hasWon("O")) println("O has won!")
            else println("Draw!")

            if(board.hasWon(humanPlayer.other().toString())) aiScore++
            else if(board.hasWon(humanPlayer.toString())) humanScore++     // haha, as if
            else draws++

            println("AI: $aiScore Human: $humanScore Draws: $draws")

            this.humanPlayer = humanPlayer.other()
            Thread.sleep(2000)      // so players have time to read the result
        }
    }

    enum class Player {
        X, O;
        val maxPlayer: Player
            get() = X
        val minPlayer: Player
            get() = O
        fun isMaxPlayer(): Boolean = this == maxPlayer
        fun isMinPlayer(): Boolean = this == minPlayer
        fun other(): Player = if(this == maxPlayer) minPlayer else maxPlayer
    }

    data class TicTacToeBoard(val field: List<String> = List(9) { (it + 1).toString() }): State<String> {
        override fun getPossibleActions(): List<String> = field.filter{ Character.isDigit(it.toCharArray()[0]) }

        override fun execute(action: String): State<String> {
            val clone = ArrayList(field)
            clone[Integer.parseInt(action) - 1] = toMove().toString()
            return TicTacToeBoard(clone)
        }

        override fun isGameOver(): Boolean = field.none{ Character.isDigit(it.toCharArray()[0]) } ||
                hasWon("X") || hasWon("O")

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

        override fun utility(): Double = if(hasWon("X")) 100.0 else if(hasWon("O")) -100.0 else 0.0

        fun toMove(): Player =
            if(field.count{ Character.isDigit(it.toCharArray()[0]) } % 2 == 0) Player.O else Player.X

        fun print() {
            println(field.map{ " $it " }
                        .chunked(3)
                        .joinToString("\n") { it.toString() })
        }
    }
}

fun main() {
    TicTacToe().play()
}