# Group04_2021

## Dice Chess!

### How to run:

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
    * After the game you will find a new file in the root project folder ```data.csv``` which will track for you what
      type of game you played and who won (KPI's).
2. To interactively play against an AI choose as White Player ```Human```, then choose as black player an AI agent and
   start the game as a ```Single Game```.
3. See two agents compete against each other
    * Chose two agents as ```White``` and ```Black``` player.
    * Simulate a ```Single Game``` and set a ```Delay``` to follow the moves visually, or run a ```Simulation``` with
      x ```ÌIterations```, after the simulation you will find a csv file ```aiVsAi``` with the KPI's in the root project
      folder.

KPI's:

* agent played as white
* agent played as black
* which agent won
* number of moves in that game