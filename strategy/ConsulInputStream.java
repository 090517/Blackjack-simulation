package strategy;

import java.util.Scanner;

import bjGame.Board;
import bjGame.Decision;
import bjGame.Hand;

public class ConsulInputStream extends InputStream {
	Scanner scanner = new Scanner(System.in);

	public double[] roundOne(Board mainBoard) {
		System.out.println("NUMBER OF HANDS?");
		boolean invalidInput = true;
		int numHands = -1;
		while (invalidInput) {
			try {
				numHands = Integer.parseInt(scanner.nextLine());
				if ((numHands > mainBoard.gameRules.MAXHANDS) || (numHands < 1)) {
					System.out.println("INVALID NUMBER OF HANDS - BETWEEN 1 TO " + mainBoard.gameRules.MAXHANDS + ".");
				} else {
					invalidInput = false;
				}
			} catch (Exception e) {
				System.out.println("INVALID INPUT, NOT AN INTEGER");
			}
		}

		double[] bets = new double[numHands];
		for (int i = 0; i < numHands; i++) {
			System.out.println("HAND " + (i + 1) + " BET?");
			double bet = 0;
			invalidInput = true;
			while (invalidInput) {
				try {
					bet = Double.parseDouble(scanner.nextLine());
					if ((bet > mainBoard.gameRules.MAXBET) || (bet < mainBoard.gameRules.MINBET)) {
						System.out.println("INVALID BET, MUST BE BETWEEN " + mainBoard.gameRules.MAXBET + " and "
								+ mainBoard.gameRules.MINBET);
					} else {
						bets[i] = bet;
						invalidInput = false;
					}
				} catch (Exception e) {
					System.out.println("INVALID INPUT, NOT AN INTEGER");
				}
			}
		}
		return bets;
	}

	public boolean[] roundTwo(Board mainBoard) {
		boolean[] output = new boolean[mainBoard.playerHands.size()];
		System.out.println("DEALER SHOWS ACE.");
		for (int i = 0; i < mainBoard.playerHands.size(); i++) {
			System.out.println("CURRENT " + handToStringWithIndex(mainBoard.playerHands.get(i), i) + " INSURANCE?");
			if (yesNo()) {
				output[i] = true;
			} else {
				output[i] = false;
			}
		}
		return output;
	}

	public Decision roundThree(Board mainBoard) {
		String input = scanner.nextLine();
		if (input.matches("hit|Hit|HIT|h|H")) {
			return Decision.HIT;
		} else if (input.matches("stand|STAND|Stand|S|std|STD")) {
			return Decision.STAND;
		} else if (input.matches("doubledown|DOUBLEDOWN|DD|dd|Double|double")) {
			return Decision.DOUBLEDOWN;
		} else if (input.matches("split|SPLIT|Sp|SP")) {
			return Decision.SPLIT;
		} else if (input.matches("surrender|Surrender|Surr|surr|sur|Sur")) {
			return Decision.SURRENDER;
		}
		return null;
	}

	public boolean continueGame(Board mainBoard, boolean textOn) {
		return yesNo();
	}

	private boolean yesNo() {
		while (true) {
			String input = scanner.nextLine();
			if (input.matches("y|Y|yes|Yes|YES")) {
				return true;
			} else if (input.matches("n|N|No|NO|no")) {
				return false;
			} else {
				System.out.println("INVALID INPUT MUST BE YES/NO");
			}
		}
	}

	private static String handToStringWithIndex(Hand tempHand, int handIndex) {
		return ("HAND " + (handIndex) + " " + tempHand.handToString());
	}
}
