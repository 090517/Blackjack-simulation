package strategy;

import gameMechanics.Board;
import gameMechanics.Decision;

public class BasicStrategyInputStream extends InputStream {
	
	public double[] roundOne(Board mainBoard) {
		double[] output = {1,1,1,1};
		return output;
	}

	public Decision roundThree(Board mainBoard) {
		return BasicStrategy.basicStrategyDecision(mainBoard);
	}

	public boolean continueGame(Board mainBoard, boolean textOn) {
		return (false);
	}
}
