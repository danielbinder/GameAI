# GameAI
 Automatic AI for 2 Player Games

### What it does:
Follow the structure in the [Minimax.kt](https://github.com/danielbinder/GameAI/blob/main/src/main/kotlin/Minimax.kt) interfaces and you can have an automatic AI for your 2 player turn based game. The AI only works for reasonably small search spaces, as there is currently nothing that includes a stopping condition and therefore the AI goes through the whole search space to find the best option (it would run endlessly for e.g. a chess game). This is an algorithm that looks into the future i.e. looks at future game states, to see, if anyone has an advantage, and, to find the best move in order to gain that advantage. This also means, that this algorithm does NOT help you, if you know the objectivly correct evaluation from every possible current game state already.

### I want to use and/or adapt this project
Go for it - I tried to make everything as readable and modifiable as possible.
Check out the explanation below, and the license in the 'LICENSE' file.
Regardless of the license, it would be cool if you somehow mentioned, that you got this code from here :)

### Contributions
If you want to understand the project, read the explanation below.
Here are some things to put at the start of the title to help me sort messages:
* [F]...Feature proposal
* [B]...Bug
* [C]...Code (to put in) - needs to be in current style!

Note: Be as short and precise as possible!

What I do care about (in no particular order):
* Readability
* Simplicity
* Modifiability
* Logical separation

Things to do:
* Alpha-Beta Pruning
* Enclosing this in a generic Monte Carlo Search Tree
* More examples

### Explanation
The minimax algorithm returns the objectivly best move, meaning the move that is best, even when the other player makes the best response. A game like TicTacToe is always a draw with best play, which means that you can't win against an AI implementing this algorithm.

#### [Minimax.kt](https://github.com/danielbinder/GameAI/blob/main/src/main/kotlin/Minimax.kt) is a simple and fairly generic implementation of the minimax algorithm.

##### interface State<Action>
The State<Action> interface represents a game state, e.g. your playing field. 'Action' is the type corrisponding to an action in your game world i.e. a class representing a players' (or the AI's) action. In order to use the minimax algorithm, you have to implement the following methods:

fun getPossibleActions(): List<Action><br/>
This returns a list with all actions possible in the current state. It needs to return at least one action for the algorithm to work properly. I could easily change this, but then you'd have to figure out the best move from all action-evaluation pairs yourself, and I wanted to keep usage as simple as possible.
 
fun execute(action: Action): State<Action><br/>
This takes and action and executes it. Returned is a new state that this action produced.
 
fun isGameOver(): Boolean<br/>
Returns 'true' if the game is over.

Up to this point, you should already have everything, or at least something similar that is easy to change into the method you need. Now let's talk about how a generic AI can even function.

fun utility(): Double<br/>
A generic AI can't know what's important in your game world and can't know what variables to use and how heavily these variables influence your world. This is why we need some sort of utility function. This function can return a fairly simple world view e.g. WIN = 100.0; LOSS (i.e. other player won) = -100.0; UNCERTAIN = 0. This information should be easily extractable from your game world, after all, you need to know when someone has won anyways. The uncertainty element can be in there, since the whole search space is traversed and the minimax algorithm chooses the biggest or smallest value anyway, so we can use something in the middle to represent equality or uncertainty.
 
 ##### interface Player
This represents a player in your game (both the AI and humans are considered to be players). This can be as simple as 'X' and 'O' in TicTacToe or as complex as you want it to be, but it must have a 'maxPlayer' and 'minPlayer' attribute. The maxPlayer is the one who is favoured when the evaluation is positive or high, the minPlayer is favoured when the evaluation is negative or low. You can choose whomever to be the max- or minPlayer.
 
##### class Minimax<Action> (val futureValueWorthPolicy: ((Double) -> Double) = { it * 0.5 })
This is the AI-algorithm itself. The futureVAlueWorthPolicy determines how much future values are worth compared to current values. The idea behind this is, that immediate actions should be worth more than future actions with the same evaluation. By default, actions one step in the future with the same evaluation are worth half of current actions (this reduction might be too high for your case). Future actions are worth more, the higher you choose the number the evaluation is multiplied with e.g. 'it * 0.9' means Future actions have 90% worth of current actions. You can give this any function that takes a Double and returns a Double.

fun minimax(state: State<Action>, toMove: Player): Map.Entry<Action, Double><br/>
This is the core. It takes the current game state and which players' move is next i.e. the player to move next. It will return a Map Entry with the best action as key and the action's evaluation as value.

#### [TicTacToe.kt](https://github.com/danielbinder/GameAI/blob/main/src/main/kotlin/TicTacToe.kt)
This is an example implementation of TicTacToe using the minimax algorithm. Especially interesting for you might be the P enum that represents the 2 players 'X' and 'O' and the TicTacToeBoard class that represents the State<Action> where an action is just represented by a String. I hope this illustrates how simple it can be to add the minimax algorithm to your existing project with very few lines of code. If the evaluation is anything other than '0', you have already lost and the further away from zero, the closer you are to a loss :)
