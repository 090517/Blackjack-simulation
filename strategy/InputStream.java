package strategy;

import java.util.Arrays;
import bjGame.Board;
import bjGame.Decision;
import neuralNet.BJNeuralNet;
import rules.Rules;

public class InputStream {

	public double[] roundOne(Board mainBoard) {
		return null;
	}

	public boolean[] roundTwo(Board mainBoard) {
		boolean[] trueOutput = new boolean[mainBoard.playerHands.size()];
		Arrays.fill(trueOutput, Boolean.TRUE);
		if (mainBoard.dealerHand.handCards.get(1).getBJValue() == 1) {
			if ((mainBoard.mainShoe.cardsLeft(10) / mainBoard.mainShoe.cardsLeft()) > .5) {
				System.out.println("insuranceBought");
				return trueOutput;
			}
		} else if (mainBoard.dealerHand.handCards.get(1).getBJValue() == 10) {
			if ((mainBoard.mainShoe.cardsLeft(1) / mainBoard.mainShoe.cardsLeft()) > .5) {
				System.out.println("insuranceBought");
				return trueOutput;
			}
		}
		return null;
	}

	public Decision roundThree(Board mainBoard) {
		System.out.println("ERROR ROUND 3 NULL POINTER");
		return null;
	}

	public boolean continueGame(Board mainBoard, boolean textOn) {
		// TODO Auto-generated method stub
		return true;
	}

	public double[] round3decisionMatrix(Board mainBoard) {

		double[] output = null;
		Decision decision=roundThree(mainBoard);
		//System.out.println("Decision is:" + Decision.decisionToString(decision));
		//false for simple training below, becareful
		
		switch (decision) {
		case HIT:
			//System.out.println("hit");
			output = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0 };
			break;
		case STAND:
			//System.out.println("Stand");
			output = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0 };
			break;
		case DOUBLEDOWN:
			//System.out.println("double down");
			output = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0 };
			break;
		case SURRENDER:
			//System.out.println("Surrender");
			output = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
			break;
		case SPLIT:
			//System.out.println("split");
			output = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
			break;
		}
		return output;
	}

	public void printStrategy() {
		Board testBoard=Board.testBoard(new Rules());
		// splits
		System.out.println("Splits - Dealer cards top, Player cards down");
		System.out.println("\t1  2  3  4  5  6  7  8  9  10");
		for (int playerCard = 10; playerCard >= 1; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(playerCard, playerCard);
				System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}
		// Softs
		System.out.print("\n");
		System.out.println("Softs - Dealer cards top, Player cards down");
		System.out.println("\t1  2  3  4  5  6  7  8  9  10");
		for (int playerCard = 10; playerCard >= 1; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(1, playerCard);
				System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}
		// Hards
		System.out.print("\n");
		System.out.println("Hards - Dealer cards top, Player cards down");
		System.out.println("\t1  2  3  4  5  6  7  8  9  10");
		for (int playerCard = 21; playerCard >= 13; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(6,6,(playerCard-12));
				System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}
		for (int playerCard = 12; playerCard >= 3; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(1,1,(playerCard-2));
				System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}


	}

	public void printDifference() {
		Board testBoard=Board.testBoard(new Rules());
		InputStream BSStream = new BasicStrategyInputStream();
		// splits
		System.out.println("Splits - Dealer cards top, Player cards down");
		System.out.println("\t1  2  3  4  5  6  7  8  9  10");
		for (int playerCard = 10; playerCard >= 1; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(playerCard, playerCard);
				if (Decision.decisionToString(roundThree(testBoard)).equals(Decision.decisionToString(BSStream.roundThree(testBoard))))
				{
					System.out.print("-  ");
				}
				else System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}
		// Softs
		System.out.print("\n");
		System.out.println("Softs - Dealer cards top, Player cards down");
		System.out.println("\t1  2  3  4  5  6  7  8  9  10");
		for (int playerCard = 10; playerCard >= 1; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(1, playerCard);
				if (Decision.decisionToString(roundThree(testBoard)).equals(Decision.decisionToString(BSStream.roundThree(testBoard))))
				{
					System.out.print("-  ");
				}
				else System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}
		// Hards
		System.out.print("\n");
		System.out.println("Hards - Dealer cards top, Player cards down");
		System.out.println("\t1  2  3  4  5  6  7  8  9  10");
		for (int playerCard = 21; playerCard >= 13; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(6,6,(playerCard-12));
				if (Decision.decisionToString(roundThree(testBoard)).equals(Decision.decisionToString(BSStream.roundThree(testBoard))))
				{
					System.out.print("-  ");
				}
				else System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}
		for (int playerCard = 12; playerCard >= 3; playerCard--) {
			System.out.print(Integer.toString(playerCard) + "\t"); 
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				testBoard.dealerHand.setHand(dealerCard, dealerCard);
				testBoard.currentHand.setHand(1,1,(playerCard-2));
				if (Decision.decisionToString(roundThree(testBoard)).equals(Decision.decisionToString(BSStream.roundThree(testBoard))))
				{
					System.out.print("-  ");
				}
				else System.out.print(Decision.decisionToString(roundThree(testBoard)));
			}
			System.out.print("\n");
		}

	}

	public void updateNN() {
		// TODO Auto-generated method stub
		
	}
}