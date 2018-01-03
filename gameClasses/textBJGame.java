package gameClasses;

import gameMechanics.Board;
import gameMechanics.Decision;
import rules.Rules;
import strategy.BasicStrategyInputStream;
import strategy.ConsulInputStream;
import strategy.InputStream;

public class textBJGame {
	public static void main(String[] args) {
		playTextGame();		
	}

	public static void playTextGame() {
		Rules gameRules = new Rules();
		boolean textOn = true;
		boolean outputOn = true;
		InputStream inputStream = new ConsulInputStream();
		boolean trainingOn = false;
		int numRoundsPrint = 100;
		runNNGame(inputStream, Integer.MAX_VALUE, textOn, gameRules, outputOn, trainingOn, numRoundsPrint,
				false, false);
	}

	private static void playBSGame(boolean newtextOn, int monteCarloRounds, int monteCarloOutput) {
		Rules gameRules = new Rules();
		boolean outputOn = true;
		InputStream inputStream = new BasicStrategyInputStream();
		boolean trainingOn = false;
		runNNGame(inputStream, monteCarloRounds, newtextOn, gameRules, outputOn, trainingOn,
				monteCarloOutput, false, false);
	}

	public static void runNNGame(InputStream inputStream, int maxRounds, boolean textOn, Rules gameRules,
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

			if (textOn) {
				mainBoard.printBoard();
				System.out.println("--------------START ROUND " + currentRound + "-------------");
			}

			// INSURANCE ROUND -// check for blackjack, ends round if so
			if (gameRules.INSURANCE && mainBoard.insuranceCheck()) {
				mainBoard.roundTwo(inputStream.roundTwo(mainBoard), textOn);
			}

			if (mainBoard.dealerBJRound()) {
				mainBoard.startRoundThree();
				while (mainBoard.round == 3) {
					if (textOn) {
						System.out.println("CURRENT " + mainBoard.printCurrentHand());
						if (mainBoard.currentHand.checkBlackJack()) {
							System.out.println("BLACKJACK!");
						} else if (mainBoard.currentHand.checkSplit(gameRules)) {
							System.out.println("ACTIONS ARE SURRENDER, HIT, STAND, DOUBLEDOWN, SPLIT:");
						} else if (mainBoard.currentHand.firstAction) {
							System.out.println("ACTIONS ARE SURRENDER, HIT, STAND, DOUBLEDOWN:");
						} else {
							System.out.println("ACTIONS ARE HIT, STAND");
						}
					}

					// if false 10 times, stand on hand.
					int count = 0;
					while ((!(mainBoard.roundThreeFalseIfInvalid(inputStream.roundThree(mainBoard), textOn)))) {
						count++;
						if (count == 1) {
							if (textOn) {
								System.out.println("INVALID ACTION, STANDING ON HAND");
							}
							mainBoard.roundThreeFalseIfInvalid(Decision.STAND, textOn);
							break;
						}
					}
				}
			}

			// end game code
			mainBoard.payoutRound(textOn);
			boolean resetDeck = mainBoard.cleanupRound();
			if (resetDeck && textOn) {
				System.out.println("ShuffledDeck");
			}
			if ((outputOn) && (currentRound % numRoundsPrint == 0)) {
				System.out.print("Current Round: " + currentRound + " Edge calculated as: " + mainBoard.edgeCalc());
				System.out.println();
			}

			if (currentRound == maxRounds) {
				gameOver = true;
				break;
			} else if ((mainBoard.playerChipStack < mainBoard.gameRules.MINBET)
					&& (!mainBoard.gameRules.ALLOWNEGATIVE)) {
				if (textOn) {
					System.out.println("Out of Money.  Game Over");
				}
				gameOver = true;
				break;
			} else {
				if (textOn) {
					System.out.println("ROUND END. NEXT ROUND?");
				}
				gameOver = inputStream.continueGame(mainBoard, textOn);
			}
		}
		if (outputOn) {
			System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime));
			System.out.println(
					"EXITING GAME.  FINAL STACK VALUE:" + mainBoard.playerChipStack + " ROUNDS:" + currentRound);
			System.out.println("Edge calculated as: " + mainBoard.edgeCalc());
		}
	}
}
