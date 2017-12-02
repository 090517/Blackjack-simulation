package bjGame;

import activationFunctions.*;
import neuralNet.MLNN;
import rules.Rules;
import strategy.BasicStrategyInputStream;
import strategy.ConsulInputStream;
import strategy.InputStream;
import strategy.MLNNInputStream;

public class BJGame {

	public static void main(String[] args) {

		;
		// settings
		boolean consulInput = false;
		Rules gameRules = new Rules();
		boolean textOn = false;
		boolean outputOn = true;

		InputStream inputStream;
		if (consulInput) {
			inputStream = new ConsulInputStream();
		} else {
			inputStream = new BasicStrategyInputStream();
		}

		int hiddenLayers=3;
		int[] hiddenArray = new int[hiddenLayers];
		hiddenArray[0] = 23;
		hiddenArray[1] = 23;
		hiddenArray[2] = 23;

		ActivationFunction[] AF = new ActivationFunction[4];
		for (int i = 0; i < AF.length; i++) {
			AF[i] = new Sigmoid();
		}
		
		//double leakRate=.01;
		//AF[1]=new LeakyRelu(leakRate);

		boolean[] bias = new boolean[4];
		for (int i = 0; i < AF.length; i++) {
			bias[i] = true;
		}
		
		double learningRate = .1;

		// int[] nnHiddenArrangment, int inputs, int outputs, ActivationFunction[]
		// AFArray, boolean[] bias

		MLNN MLNNtest = new MLNN(hiddenArray, 22, 5, AF, bias, learningRate);
		// MLNN MLNNtest=MLNN.readNN("MLNNtest");

		// Game Code, starting game on empty board

		// int trainingRounds = Integer.MAX_VALUE;
		int trainingRounds =1000;
		int trainingRoundsPrint = 10;
		boolean trainingOnly = true;
		boolean simpleTraining = true;
		
		// runNNGame(inputStream, trainingRounds, textOn, gameRules, outputOn, MLNNtest, trainingOnly, trainingRoundsPrint, simpleTraining);
		
		System.out.println("---------START---------");

		
		trainBS(MLNNtest, trainingRounds, trainingRoundsPrint);
		InputStream MLInput=new MLNNInputStream(MLNNtest);
		
		System.out.println("---------FINAL---------");
		MLNNtest.plotErrors();
		
		inputStream.printStrategy();
		MLInput.printDifference();
		
		System.out.println("least error strat");
		MLNNtest.loadBest();
		//aMLInput.updateNN();
		MLInput.printDifference();

		MLNNtest.saveNN("MLNNtest22Sigmoid3LayerOvernight");
	}

	private static void printInputStreamStrategy(InputStream inputStream, boolean simpleTraining) {
		inputStream.printStrategy();
	}

	public static void playTextGame() {
		Rules gameRules = new Rules();
		boolean textOn = true;
		boolean outputOn = true;
		InputStream inputStream = new ConsulInputStream();
		boolean trainingOn = false;
		int numRoundsPrint = 100;
		runNNGame(inputStream, Integer.MAX_VALUE, textOn, gameRules, outputOn, new MLNN(), trainingOn, numRoundsPrint,
				false);
	}

	private static void playBSGame(boolean newtextOn, int monteCarloRounds, int monteCarloOutput) {
		Rules gameRules = new Rules();
		boolean outputOn = true;
		InputStream inputStream = new BasicStrategyInputStream();
		boolean trainingOn = false;
		runNNGame(inputStream, monteCarloRounds, newtextOn, gameRules, outputOn, new MLNN(), trainingOn,
				monteCarloOutput, false);
	}

	public static void trainNN(MLNN nNinput, double[] input, double[] expected) {
		nNinput.forwardProp(input, expected);
		nNinput.backProp(expected);
	}

	public static void trainBS(MLNN trainingNeural, int rounds, int roundPrint) {
		Board testBoard = Board.testBoard(new Rules());
		InputStream BSInput = new BasicStrategyInputStream();

		for (int i = 0; i < rounds; i++) {
			for (int playerCard = 10; playerCard >= 1; playerCard--) {
				for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
					testBoard.dealerHand.setHand(dealerCard, dealerCard);
					testBoard.currentHand.setHand(playerCard, playerCard);
					trainNN(trainingNeural, testBoard.boardMatrix(true), BSInput.round3decisionMatrix(testBoard));
				}
			}
			// Softs

			for (int playerCard = 10; playerCard >= 1; playerCard--) {
				for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
					testBoard.dealerHand.setHand(dealerCard, dealerCard);
					testBoard.currentHand.setHand(1, playerCard);
					trainNN(trainingNeural, testBoard.boardMatrix(true), BSInput.round3decisionMatrix(testBoard));
				}
			}

			// Hards
			for (int playerCard = 21; playerCard >= 13; playerCard--) {
				for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
					testBoard.dealerHand.setHand(dealerCard, dealerCard);
					testBoard.currentHand.setHand(6, 6, (playerCard - 12));
					trainNN(trainingNeural, testBoard.boardMatrix(true), BSInput.round3decisionMatrix(testBoard));
				}
			}
			for (int playerCard = 12; playerCard >= 3; playerCard--) {
				for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
					testBoard.dealerHand.setHand(dealerCard, dealerCard);
					testBoard.currentHand.setHand(1, 1, (playerCard - 2));
					trainNN(trainingNeural, testBoard.boardMatrix(true), BSInput.round3decisionMatrix(testBoard));
				}
			}
			if (i%roundPrint==0&&i!=0) {
				System.out.println(trainingNeural.getError());
			}
			if (i==1005) {
				System.out.println("trim test");
				System.out.println(trainingNeural.getError());
				double[][] weights = new double[23][23];
				for (int ii=0; ii<23; ii++) {
					for (int j=0; j<23; j++) {
						weights[ii][j]=0;
					}
				}
				//trainingNeural.setWeights(1, weights);
				//trainingNeural.setWeights(2, weights);

				trainingNeural.trimNeurons(-3);
				System.out.println("trim End");
			}
		}
	}

	public static void runNNGame(InputStream inputStream, int maxRounds, boolean textOn, Rules gameRules,
			boolean outputOn, MLNN NNinput, boolean trainingOn, int numRoundsPrint, boolean simpleTraining) {

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
					if (trainingOn) {
						trainNN(NNinput, mainBoard.boardMatrix(simpleTraining),
								inputStream.round3decisionMatrix(mainBoard));
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
				System.out.println("Current Round: " + currentRound + " Edge calculated as: " + mainBoard.edgeCalc()
						+ " NN Error:" + NNinput.getError());
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
