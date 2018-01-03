package gameClasses;

import gameMechanics.Board;
import gameMechanics.Decision;
import rules.Rules;
import strategy.BasicStrategyInputStream;
import strategy.InputStream;

public class edgeCalculator {
	public static double calcEdge(InputStream strategy, int rounds) {
		boolean newtextOn=false;
		int monteCarloRounds=10000000;
		return playBSGame(strategy, newtextOn,  rounds,  rounds);
	}
	
	private static double playBSGame(InputStream strategy, boolean newtextOn, int monteCarloRounds, int monteCarloOutput) {
		Rules gameRules = new Rules();
		boolean outputOn = true;
		boolean trainingOn = false;
		return runNNGame(strategy, monteCarloRounds, newtextOn, gameRules, outputOn, trainingOn,
				monteCarloOutput, false, false);
	}
	
	public static double runNNGame(InputStream inputStream, int maxRounds, boolean textOn, Rules gameRules,
			boolean outputOn, boolean trainingOn, int numRoundsPrint, boolean simpleTraining,
			boolean AIOn) {

		final long startTime = System.currentTimeMillis();
		Board mainBoard = new Board(gameRules, textOn);
		int currentRound = 0;
		boolean gameOver = false;
		while (!gameOver) {
			currentRound++;
			// round 1 - placing bets and dealing cards
			mainBoard.roundOne(inputStream.roundOne(mainBoard), textOn);

			// INSURANCE ROUND -// check for blackjack, ends round if so
			if (gameRules.INSURANCE && mainBoard.insuranceCheck()) {
				mainBoard.roundTwo(inputStream.roundTwo(mainBoard), textOn);
			}

			if (mainBoard.dealerBJRound()) {
				mainBoard.startRoundThree();
				while (mainBoard.round == 3) {
					
					// if false 10 times, stand on hand.
					int count = 0;
					while ((!(mainBoard.roundThreeFalseIfInvalid(inputStream.roundThree(mainBoard), textOn)))) {
						count++;
						if (count == 1) {
							mainBoard.roundThreeFalseIfInvalid(Decision.STAND, textOn);
							break;
						}
					}
				}
			}

			// end game code
			mainBoard.payoutRound(textOn);
			boolean resetDeck = mainBoard.cleanupRound();

			if (currentRound == maxRounds) {
				gameOver = true;
				break;
			}  else gameOver = false;
		}
		return mainBoard.edgeCalc();
	}
}
