package strategy;

import java.util.Arrays;

import gameMechanics.Board;
import gameMechanics.Decision;

public class CardCountingInputStream extends InputStream {
	public double[] roundOne(Board mainBoard) {

		if (trueCount(mainBoard) < 5) {
			return new double[] { mainBoard.gameRules.MINBET };
		} else {
			double[] output = new double[mainBoard.gameRules.MAXHANDS];
			Arrays.fill(output, mainBoard.gameRules.MAXBET);
			return output;
		}
	}

	public boolean[] roundTwo(Board mainBoard) {
		boolean[] trueOutput = new boolean[mainBoard.playerHands.size()];
		Arrays.fill(trueOutput, Boolean.TRUE);
		if (mainBoard.dealerHand.handCards.get(1).getBJValue() == 1) {
			if ((mainBoard.mainShoe.cardsLeft(10) / mainBoard.mainShoe.cardsLeft()) > .5) {
				return trueOutput;
			}
		} else if (mainBoard.dealerHand.handCards.get(1).getBJValue() == 10) {
			if ((mainBoard.mainShoe.cardsLeft(1) / mainBoard.mainShoe.cardsLeft()) > .5) {
				return trueOutput;
			}
		}

		Arrays.fill(trueOutput, Boolean.FALSE);
		return trueOutput;
	}

	public Decision roundThree(Board mainBoard) {
		return BasicStrategy.basicStrategyDecision(mainBoard);
	}

	public boolean continueGame(Board mainBoard, boolean textOn) {
		// TODO Auto-generated method stub
		return true;
	}

	public double[] round3decisionMatrix(Board mainBoard) {
		Decision decision = roundThree(mainBoard);

		switch (decision) {
		case HIT:
			return new double[] { 1.0, 0.0, 0.0, 0.0, 0.0 };
		case STAND:
			return new double[] { 0.0, 1.0, 0.0, 0.0, 0.0 };
		case DOUBLEDOWN:
			return new double[] { 0.0, 0.0, 1.0, 0.0, 0.0 };
		case SURRENDER:
			return new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
		case SPLIT:
			return new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		}
		return null;
	}

	public double trueCount(Board mainBoard) {
		int count = 0;

		for (int i = 2; i <= 6; i++) {
			count += mainBoard.cardCount[i - 1];
		}
		count -= mainBoard.cardCount[1 - 1];
		count -= mainBoard.cardCount[10 - 1];

		return (double) count / (mainBoard.mainShoe.cardsLeft()/mainBoard.gameRules.NUMDECKS);
	}
}
