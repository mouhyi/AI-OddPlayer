

1. Inside AutomatedTester.java, change the constructor calls to match the AI classes you want to test (lines 55, 56).

2. test.properties contains all the parameters you might want to set - 
   IMPORTANT: Ensure that the names you give to the players correspond to the arguments you pass to the parent class (Player) constructor for each of the AIs you're testing. For example, if your AI class is named MyAIPlayer but in its constructor MyAIPlayer() you do super("ub3rpwn4g3"); and your opponent is OddRandomPlayer (in the constructor of which there is a super("OddRandomPlayer") call) then you should set player1=ub3rpwn4g3 and player2=OddRandomPlayer (or the other way around, it doesn't matter since they get shuffled) to get the correct results in stats.txt.

3. Run AutomatedTester to play the amount of games specified in test.properties.

4. Run ResultsGenerator to generate stats. Your results should be in logs/stats.txt. 
Between tests that you run, statistics for your latest run will be being appended to the end of this file (basically the statistics summarize the contents of outcomes.txt -  which AI player won more times and how many times the "odd" player won).
