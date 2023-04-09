# CPSC210 Personal Project

## Typing Speed Test

### *Introduction*
For this project, I would like to create a typing speed test
that is able to generate random words that allow user to test 
their own typing speed. This project allows anyone to put their
typing skills to a test. This project interests me 
because there is a high flexibility on how the project works,
as well as many possible functions you can add.
The project is also challenging as it requires to take in lots
of different user inputs.

***User Stories***:
- As a user, I want to be able to choose between different lengths
of the test
- As a user, I want to be able to view the previous high score
- As a user, I want to be able to distinguish correctness through
different colours
- As a user, I want to be able to view my WPM (words per minute) 
and CPM (characters per minute)
- As a user, I want to be able to add and view my previous speeds
and date the test was ran in a list to see my improvement
- As a user, I want to be able to save my stats
- As a user, I want to be able to load my stats from file
- As a user, on startup, I want to be able to decide to load saved stats from file or not
- As a user, after the game ends, I want to be able to decide to save stats to file or not
- As a user, I want to be able to choose the difficulty of the test
- (P3) As a user, I want to be able to add multiple stats to a history by playing the game
- (P3) As a user, I wanted to be prompted with the option to load stats from file when the application starts 
and prompted with the option to save stats to file when the application ends

***Instructions For Grader***:
- You can generate the first required action related to adding stats to history by playing the typing game
  (decimals like 0.1 is accepted when choosing playtime)
- You can generate the second required action related to removing stats from history by clicking history in main start
start screen, choosing a stat from table by clicking it and pressing delete
- You can locate my visual components by the data/images directory
- You can save the state by choosing yes at the save data screen after the results screen
- You can reload the state by choosing yes at the first screen when running the program


***Phase 4: Task 2***: <br />

Date: Sat Apr 08 01:35:19 PDT 2023 <br />
New Game Stat with length 0.3 mins played at 08/04/2023 01:35:14 added to History. <br />
Date: Sat Apr 08 01:35:36 PDT 2023 <br />
New Game Stat with length 0.1 mins played at 08/04/2023 01:35:33 added to History. <br />
Date: Sat Apr 08 01:35:48 PDT 2023 <br />
Game Stat with length 0.3 mins played at 08/04/2023 01:35:14 removed from History. <br />
Date: Sat Apr 08 01:36:26 PDT 2023 <br />
New Game Stat with length 0.5 mins played at 08/04/2023 01:36:24 added to History. <br />
Date: Sat Apr 08 01:36:31 PDT 2023 <br />
Game Stat with length 0.1 mins played at 08/04/2023 01:35:33 removed from History. <br />

***Phase 4: Task 3***: <br />
If I had more time on the project, I would refactor the use of JFrame in UI. <br />

Currently,  my UI design creates a new JFrame for every change of state, 
which will accumulate many unused JFrame objects as the program continues to be played.
 By using a single frame across all states, I can greatly reduce the memory usage of the game.
