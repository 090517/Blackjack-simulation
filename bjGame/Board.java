package bjGame;

import java.util.ArrayList;
import java.util.ListIterator;

import rules.*;

public class Board {

	public Shoe mainShoe;
	public Hand dealerHand;
	public ArrayList<Hand> playerHands;
	public ArrayList<Card> trash;
	public double playerChipStack;
	int round = 0; // 0 new round, 1
	ListIterator<Hand> litr; // iterator to current hand
	public Hand currentHand;
	// RULES
	public Rules gameRules;
	boolean textOn;
	public int totalMoneyBet;
	int[] trashCount;

	// CONSTRUCTOR
	public Board(Rules newGameRules, boolean newTextOn) {
		textOn = newTextOn;
		gameRules = newGameRules;
		mainShoe = new Shoe(gameRules);
		playerChipStack = gameRules.CHIPSTACK;
		dealerHand = new Hand(0, gameRules);
		playerHands = new ArrayList<Hand>();
		trash = new ArrayList<Card>();
		round = 0;
		totalMoneyBet = 0;
		trashCount = new int[10];

		for (int i = 0; i < gameRules.INITIALNUMSHUFFLE; i++) {
			mainShoe.riffleShuffle();
		}

		if (textOn) {
			System.out.println("NEW BOARD CREATED. SHOES:" + gameRules.NUMDECKS + " MINBET:" + gameRules.MINBET
					+ " MAXBET:" + gameRules.MAXBET + " CHIPS:" + gameRules.CHIPSTACK);
		}
	}

	public static Board testBoard(Rules newGameRules) {
		Board output=new Board(newGameRules, false);
		output.addHand(0);
		output.litr = output.playerHands.listIterator();
		output.currentHand = output.litr.next();
		return output;
	}

	// ROUNDS 1-5

	public void roundOne(double[] bets, boolean textOn) {
		round = 1;
		try {
			for (double d : bets) {
				addHand(d);
			}
		} catch (ArithmeticException E) {
			if (textOn) {
				System.out.println("No More Money, cannot add new bets");
			}
		}
		// Iterating through hands, deleting invalids and deals card to valid
		litr = playerHands.listIterator();
		while (litr.hasNext()) {
			// first check if next hand has valid bet
			currentHand = litr.next();
			if ((currentHand.betSize < gameRules.MINBET) || (currentHand.betSize > gameRules.MAXBET)) {
				if (textOn) {
					System.out
							.println("ILLEGAL HAND BET DETECTED, HAND REMOVED. TOTAL " + playerHands.size() + " HANDS");
				}
				litr.remove();
			}
			// else add a card
			else {
				currentHand.addCard(mainShoe.deal());
			}
		}

		// Give dealer first card, then second r
		dealerHand.addCard(mainShoe.deal());
		for (Hand currentHand : playerHands) {
			currentHand.addCard(mainShoe.deal());
		}
		dealerHand.addCard(mainShoe.deal());
	}

	public void roundTwo(boolean[] insDecision, boolean textOn) {
		round = 2;
		if (insDecision != null) {
			for (int i = 0; i < insDecision.length; i++) {
				try {
					if (insDecision[i]) {
						addInsurance(playerHands.get(i));
					}
				} catch (ArithmeticException E) {
					if (textOn) {
						System.out.println("No More Money, cannot insure");
					}
					break;
				}
			}
		}
	}

	// check for blackjack, ends round if so
	public boolean dealerBJRound() {
		if (dealerHand.checkBlackJack() && gameRules.DEALERCHECKSFORBJ) {
			dealerBlackJackPayout();
			round = 0;
			return false;
		}
		return true;
	}

	public void dealerBlackJackPayout() {
		for (Hand tempHand : playerHands) {
			if (tempHand.checkBlackJack()) {
				this.push(tempHand);// push blackjackers
			}
			if (tempHand.insuranceBet) {
				playerChipStack += (tempHand.betSize); // insurance payout
			}
		}
	}

	public void startRoundThree() {
		round = 3;
		litr = playerHands.listIterator();
		currentHand = litr.next();
	}

	public boolean roundThreeFalseIfInvalid(Decision input, boolean textOn) {
		if (textOn) {
			System.out.println("CHOOSE TO " + input);
		}
		try {
			if (currentHand.checkBlackJack()) {
				currentHand = litr.next();
			}

			else if (input == Decision.HIT) {
				hit(currentHand);
				if (currentHand.checkBusted()) {
					if (textOn) {
						System.out.println("BUSTED: " + currentHand.handToString());
					}
					currentHand = litr.next();
				}
			}

			else if (input == Decision.STAND) {
				currentHand = litr.next();
			}

			else if (input == Decision.DOUBLEDOWN) {
				if (!currentHand.firstAction) {
					if (textOn) {
						System.out.println("Cannot Double, not first action");
					}
					return false;
				}

				else if (!currentHand.checkDouble(gameRules)) {
					if (textOn) {
						System.out.println("Cannot Double due to game rules");
					}
					return false;
				}

				try {
					doubleDown(currentHand);
					if (currentHand.checkBusted()) {
						if (textOn) {
							System.out.println("BUSTED: " + currentHand.handToString());
						}
					}
					currentHand = litr.next();
				} catch (ArithmeticException E) {
					if (textOn) {
						System.out.println("Cannot Double, not enough money");
					}
				}
			}

			else if (input == Decision.SURRENDER) {
				if (!currentHand.firstAction) {
					if (textOn) {
						System.out.println("Cannot SURRENDER, not first action");
						return false;
					}
				}
				currentHand.surrendered = true;
				currentHand = litr.next();
			}

			else if (input == Decision.SPLIT) {
				if (!currentHand.checkSplit(gameRules)) {
					if (textOn) {
						System.out.println("Cannot SPLIT, not VALID HAND");
						return false;
					}
				}

				try {
					boolean aceSplit = (currentHand.getAces() == 2);
					litr.add(splitHand(currentHand));

					// if two aces can't split again

					if (gameRules.ACESPLITONECARD && aceSplit) {
						if (textOn) {
							System.out.println("SPLIT ACES CAN'T HIT AGAIN");
						}
					} else {
						litr.previous(); // returns litr to right position
					}
				} catch (ArithmeticException E) {
					if (textOn) {
						System.out.println("Cannot Split, not enough money");
					}
				}
			}
		} catch (Exception NoSuchElementException) {
			round = 4;// ends round if next hand is empty causing nosuchelement
		}
		return true;
	}

	// End game functions

	public void payoutRound(boolean textOn) {

		if (textOn) {
			printBoard();
			System.out.println("---------------END ROUND--------------");
		}

		// Dealer finishes Hand NOTE CODE FOR SOFT OR HARD 17 BELOW!
		if (gameRules.DEALERHITSONSOFT17) {
			while ((dealerHand.checkSoft() && (dealerHand.getAceValue() < 18))
					|| (!dealerHand.checkSoft() && (dealerHand.getValue() < 17))) {
				dealerHand.addCard(mainShoe.deal());
			}
		} else {
			while ((dealerHand.checkSoft() && (dealerHand.getAceValue() < 17))
					|| (!dealerHand.checkSoft() && (dealerHand.getValue() < 17))) {
				dealerHand.addCard(mainShoe.deal());
			}
		}

		if (textOn) {
			System.out.println("Dealer Hand is: " + dealerHand.noBetString());
			// Payout to players
			if (dealerHand.getFinalHandValue() > 21) {
				System.out.println("DEALER BUSTED!");
			}
		}

		for (int i = 0; i < playerHands.size(); i++) {
			Hand tempHand = playerHands.get(i);
			if (tempHand.surrendered) {
				if (textOn) {
					System.out.println("HAND " + (i + 1) + " BET: " + tempHand.betSize + " SURRENDERED PAYOUT");
				}
				playerChipStack += (.5 * tempHand.betSize);
			} else if (tempHand.checkBlackJack()) {
				if (textOn) {
					System.out.println("HAND " + (i + 1) + " BET: " + tempHand.betSize + " BLACKJACK PAYOUT ");
				}
				playerChipStack += ((1 + gameRules.BJPAYOUT) * tempHand.betSize);
			} else if (tempHand.checkBusted()) {
				if (textOn) {
					System.out.println("HAND " + (i + 1) + " BET: " + tempHand.betSize + " BUSTED");
				}
			} else if ((tempHand.getFinalHandValue() > dealerHand.getFinalHandValue()) || dealerHand.checkBusted()) {
				if (textOn) {
					System.out.println("HAND " + (i + 1) + " BET: " + tempHand.betSize + " NORMAL PAYOUT");
				}
				playerChipStack += (2 * tempHand.betSize);
			} else if (tempHand.getFinalHandValue() == dealerHand.getFinalHandValue()) {
				if (textOn) {
					System.out.println("HAND " + (i + 1) + " BET: " + tempHand.betSize + " PUSH");
				}
				push(tempHand);
			} else {
				if (textOn) {
					System.out.println("HAND " + (i + 1) + " BET: " + tempHand.betSize + " LOST");
				}
			}
		}
		// Total ending value
		if (textOn) {
			System.out.println("Player has:" + playerChipStack);
		}
	}

	public boolean cleanupRound() {
		// putting a copy of all cards in trash

		// deletes hand, clears player hands

		for (Hand tempHand : playerHands) {
			trashCopyHand(tempHand);
		}
		trashCopyHand(dealerHand);
		playerHands.clear();
		dealerHand.handClear();
		round = 0;

		if ((mainShoe.mainShoe.size() * 100 / (gameRules.NUMDECKS * 60)) <= (gameRules.RESHUFFLEAFTER)) {
			mainShoe.reset();
			trash.clear();
			for (int i = 0; i < gameRules.INITIALNUMSHUFFLE; i++) {
				mainShoe.riffleShuffle();
			}
			return true;
		}
		return false;
	}

	// METHODS

	public boolean insuranceCheck() {
		return (dealerHand.handCards.get(1).getValue() == 1);
	}

	public Hand prevHand() {
		return playerHands.get(litr.previousIndex() - 1);
	}

	public Hand nextHand() {
		return playerHands.get(litr.nextIndex());
	}

	// Print Methods

	public void printBoard() {
		// Dealer Card
		System.out.println("----------------BOARD-----------------");
		System.out.println("CHIPS: " + playerChipStack);
		System.out.println(dealerHand.dealerUpCard());

		// Player Cards
		for (int i = 0; i < playerHands.size(); i++) {
			System.out.println(handToStringWithIndex(playerHands.get(i), i));
		}
	}

	public static String handToStringWithIndex(Hand tempHand, int handIndex) {
		return ("HAND " + (handIndex + 1) + " " + tempHand.handToString());
	}

	public String printCurrentHand() {
		return ("HAND " + litr.previousIndex() + " " + currentHand.handToString());
	}

	// PRIVATE METHODS

	private void push(Hand payoutHand) {
		playerChipStack += payoutHand.betSize;
	}

	private void trashCopyHand(Hand tempHand) {
		for (Card tempCard : tempHand.handCards) {
			trash.add(tempCard);
			trashCount[tempCard.getBJValue() - 1] = trashCount[tempCard.getBJValue() - 1] + 1;
		}
	}

	// blackjack actions

	public void addInsurance(Hand tempHand) {
		gambleChipStack(tempHand.betSize * .5);
		tempHand.addInsurance();
	}

	public void hit(Hand tempHand) {
		tempHand.addCard(mainShoe.deal());
		tempHand.firstAction = false;
	}

	public void doubleDown(Hand tempHand) {
		gambleChipStack(tempHand.betSize);
		tempHand.addCard(mainShoe.deal());
		tempHand.betSize = tempHand.betSize * 2;
		tempHand.firstAction = false;
	}

	public Hand splitHand(Hand tempHand) {
		gambleChipStack(tempHand.betSize);
		Hand newHand = tempHand.splitHand(gameRules);
		newHand.addCard(mainShoe.deal());
		tempHand.addCard(mainShoe.deal());
		return newHand;
	}

	public void addHand(double betAmmount) {
		gambleChipStack(betAmmount);
		playerHands.add(new Hand(betAmmount, gameRules));
	}

	public void gambleChipStack(double betAmmount) {
		if ((betAmmount > playerChipStack) && (!gameRules.ALLOWNEGATIVE)) {
			throw new ArithmeticException("No More Money");
		} else {
			totalMoneyBet += betAmmount;
			playerChipStack -= betAmmount;
		}
	}

	public double[] boardMatrix(boolean simpleTraining) {
		if (simpleTraining) {
			double[] output = new double[22];
			output[0] = dealerHand.handCards.get(1).getBJValue();
			for (int i = 0; i < currentHand.handCards.size(); i++) {
				output[i + 1] = currentHand.handCards.get(i).getBJValue();
			}
			return output;
		} else {
			double[] output = new double[1358];

			// 0-9 copying from trashcount array
			for (int i = 0; i < trashCount.length; i++) {
				output[i] = trashCount[i];
			}

			output[10] = mainShoe.cardsLeft();
			output[11] = dealerHand.handCards.get(1).getBJValue();
			output[12] = playerChipStack;
			output[13] = round;
			output[14] = litr.previousIndex();

			// maximum numbner of cards 21, max number of hands MAXHANDS*2^maxsplit=
			// 8*2^3=8*8

			int i = 0, j = 0;

			for (Hand tempHand : playerHands) {
				for (Card tempCard : tempHand.handCards) {
					output[14 + (j * 21) + (i + 1)] = tempCard.getBJValue();
					i++;
				}
				i = 0;
				j++;
			}
			return output;
		}
	}
	
	public double edgeCalc() {
		return (playerChipStack * 100 / totalMoneyBet);
	}
}