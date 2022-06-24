# GameAI
Automatic AI for 2-player turn based games with small search spaces.

### What it does:
Follow the structure in the [Minimax.kt](https://github.com/danielbinder/GameAI/blob/main/src/main/kotlin/Minimax.kt) interfaces and you can have an automatic AI for your 2 player turn based game.
The AI only works for reasonably small search spaces, as there is currently nothing that includes a stopping condition and therefore the AI goes through the whole search space to find the best option (it would run endlessly for e.g. a chess game).
This is an algorithm that looks into the future i.e. looks at future game states, to see, if anyone has an advantage, and, to find the best move in order to gain that advantage.

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

What I care about (in no particular order):
* Readability
* Simplicity
* Modifiability
* Logical separation

Things to do:
* Alpha-Beta Pruning
* Enclosing this in a generic Monte Carlo Search Tree
* More examples

### Explanation
The minimax algorithm returns the objectively best move, meaning the move that is best, even when the other player makes the best response.
A game like TicTacToe is always a draw with best play, which means that you can't win against an AI implementing this algorithm.

[Minimax.kt](https://github.com/danielbinder/GameAI/blob/main/src/main/kotlin/Minimax.kt) is a simple and fairly generic implementation of the minimax algorithm.
To use it, implement the `State<Action>` Interface for your game state i.e. your game board.
Read the documentation of this file to understand how to use it and how it works.

[TicTacToe.kt](https://github.com/danielbinder/GameAI/blob/main/src/main/kotlin/TicTacToe.kt) is an example implementation of TicTacToe using the minimax algorithm.
Especially interesting for you might be the `TicTacToeBoard` Data Class that represents the `State<Action>` where an Action is just represented by a String.
The utility function is trivial:

`WIN = 100; LOSE = -100; DRAW (or uncertain) = 0`

I hope this illustrates how simple it can be to add the minimax algorithm to your existing project with very few lines of code that you mostly have anyway.
If the evaluation is anything other than '0' here, you have already lost and the further away you get from zero, the closer you are to a loss :)