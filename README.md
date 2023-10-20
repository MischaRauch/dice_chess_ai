# Maastricht University Project - 2nd year | 1st Semester
During the Bachelor Data Science and Artificial Intelligence

## Group04_2021

### Dice Chess!

Dice Chess takes the age-old game of chess and adds an exciting twist by introducing dice rolls as a game mechanic. You can play this dynamic and unpredictable version of chess against another human player, or you can challenge our sophisticated AI opponents, each powered by a different strategy.

### Essay:
For a detailed understanding of the research and methodologies employed in this project, you can refer to the [essay](https://drive.google.com/file/d/1AESOxEysk00gxioMHD3dP_pScGXvBtKM/view?usp=share_link).

#### How to run:

1. Unzip the source code project folder.
2. Navigate to the project root folder ``BestDiceChess``on the command line.
3. If you have gradle installed, run the project using ```gradle run``` ( or ```./gradle run```) if you don't have
   gradle install it.
4. If you don't have gradle installed, try running ``gradlew run`` ( or ```./gradlew run```).
5. If step 4. does not work open the project with your favorite IDE (IntelliJ, VSCode,...).
6. Make sure when you open the root folder (BestDiceChess) to trust this project.
7. Run the inbuild ```run``` command of gradles applications via your IDE.

### Setting up a game:

1. Play against your best buddy by selecting ```Human``` as ```White``` and ```Black``` player.
2. To interactively play against an AI choose as White Player ```Human```, then choose as black player an AI agent and
   start the game as a ```Single Game```.
3. See two agents compete against each other
    * Chose two agents as ```White``` and ```Black``` player.
    * Simulate a ```Single Game``` and set a ```Delay``` to follow the moves visually, or run a ```Simulation``` with
      x ```ÌIterations```, after multi run simulation you will find two csv files in the root project folder. One
      called ```multiGame.csv``` with the end game results and one file called ```multiStats.csv``` with detailed
      information about every move. As long as these files are not beeing deleted all simulations will append to the
      existing file. Which KPI's can be seen in which file is listed below.

### KPI's: 

**MultiGame CSV**
* agent played as white
* agent played as black
* average time needed to calculate a move for agent in white (in nanoseconds)
* average time needed to calculate a move for agent in black (in nanoseconds)
* total game time (in nanoseconds)
* which agent won
* number of moves in that game
* the number of pieces left on the board from agent in white, in the following order [Pawn,Knight,Bishop,Rook,Queen,King]
* the number of pieces left on the board from agent in black, in the following order [Pawn,Knight,Bishop,Rook,Queen,King]
* the evaluation score based on the pieces which are left on the board for white
* the evaluation score based on the pieces which are left on the board for black 

**MultiStats CSV**

Each row in this file represents two moves. One move from the White agent and one move from the Black agent. Each move tracks the following:
* Which algorithm played
* How long it took the algorithm to calculate that move (in nanoseconds)
* If a capture was performed, and if so which piece from the opponent was captured
* the remaining pieces of its own side, in the following order [Pawn,Knight,Bishop,Rook,Queen,King]
