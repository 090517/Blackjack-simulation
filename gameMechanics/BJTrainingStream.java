package gameMechanics;

import rules.Rules;
import strategy.InputStream;

public class BJTrainingStream {
	Board mainBoard;
	InputStream idealStrategy;
	double[] bets = {1.0, 1.0, 1.0, 1.0};
	boolean[] insuranceBets = {false, false, false, false};
	boolean textOn=false;
	Rules gameRules;
	
	public BJTrainingStream(Board mainBoard, InputStream idealStrategy, Rules gameRules, int numberOfHands) {
		this.mainBoard=mainBoard;
		this.idealStrategy=idealStrategy;
		this.gameRules=gameRules;
	}
	
	//takes in the count matrix from the board, along with the number of decks from the rules
	public double[] getBettingInputs() {
		return mainBoard.bettingMatrix();
	}
	
	public double[] getBettingIdeal() {
		return idealStrategy.roundOne(mainBoard);
	}
	
	public void passInputs() {
		mainBoard.roundOne(idealStrategy.roundOne(mainBoard), false);
	}
	
	public double[] getInsuranceInputs() {
		return mainBoard.insuranceMatrix();
	}
	
	public boolean[] getInsuranceIdeal() {
		return idealStrategy.roundTwo(mainBoard);
	}
	
	public boolean passInsurance() {
		mainBoard.roundTwo(idealStrategy.roundTwo(mainBoard), false);
		mainBoard.dealerBJRound();
		if (!roundEnded()) {
			mainBoard.startRoundThree();
			return true;
		}
		else return false;
	}
	
	public double[] getDecisionInputs(boolean simpleTraining) {
		return mainBoard.boardMatrix();	
	}
	
	public double[] getDecisionIdeal() {
		return idealStrategy.round3decisionMatrix(mainBoard);
	}
	
	public boolean passDecision() {
		return mainBoard.roundThreeFalseIfInvalid(idealStrategy.roundThree(mainBoard), false);
	}
	
	public boolean roundEnded() {
		return (mainBoard.round==0);
	}
}
