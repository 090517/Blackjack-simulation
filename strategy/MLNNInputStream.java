package strategy;

import bjGame.Board;
import bjGame.Decision;
import neuralNet.MLNN;

public class MLNNInputStream extends InputStream {
	MLNN bjNN;

	public MLNNInputStream(MLNN newNN) {
		bjNN = newNN;
	}

	public double[] roundOne(Board mainBoard) {
		double[] output = { 1, 1, 1, 1 };
		return output;
	}

	//simple training to do
	public Decision roundThree(Board mainBoard) {
		
		return Decision.doubleArrayToDecision(bjNN.forwardProp(mainBoard.boardMatrix(true)));
	}

	public boolean continueGame(Board mainBoard, boolean textOn) {
		return (false);
	}
	
	public void updateNN(MLNN newNN) {
		bjNN=newNN;	
	}
}