package strategy;

import gameMechanics.*;

public class BasicStrategy {

	private final static int S = 1, H = 2, Dh = 3, Ds = 4, SP = 5, SU = 6;

	private final static byte[][] fourDeckSplit = { { SP, SP, SP, SP, SP, SP, SP, SP, SP, SP }, // 11
			{ S, S, S, S, S, S, S, S, S, S }, // 10
			{ SP, SP, SP, SP, SP, S, SP, SP, S, S }, // 9
			{ SP, SP, SP, SP, SP, SP, SP, SP, SP, SP }, // 8
			{ SP, SP, SP, SP, SP, SP, H, H, H, H }, // 7
			{ SP, SP, SP, SP, SP, H, H, H, H, H }, // 6
			{ Dh, Dh, Dh, Dh, Dh, Dh, Dh, Dh, H, H }, // 5
			{ H, H, H, SP, SP, H, H, H, H, H }, // 4
			{ SP, SP, SP, SP, SP, SP, H, H, H, H }, // 3
			{ SP, SP, SP, SP, SP, SP, H, H, H, H } // 2
	};

	private final static byte[][] fourDeckSoft = { { S, S, S, S, S, S, S, S, S, S }, // A10
			{ S, S, S, S, S, S, S, S, S, S }, // A9
			{ S, S, S, S, Ds, S, S, S, S, S }, // A8
			{ Ds, Ds, Ds, Ds, Ds, S, S, H, H, H }, // A7
			{ H, Dh, Dh, Dh, Dh, H, H, H, H, H }, // A6
			{ H, H, Dh, Dh, Dh, H, H, H, H, H }, // A5
			{ H, H, Dh, Dh, Dh, H, H, H, H, H }, // A4
			{ H, H, H, Dh, Dh, H, H, H, H, H }, // A3
			{ H, H, H, Dh, Dh, H, H, H, H, H }, // A2
			{ SP, SP, SP, SP, SP, SP, SP, SP, SP, SP } // A1
	};

	private final static byte[][] fourdeckHard = { { S, S, S, S, S, S, S, S, S, S }, // 21
			{ S, S, S, S, S, S, S, S, S, S }, // 20
			{ S, S, S, S, S, S, S, S, S, S }, // 19
			{ S, S, S, S, S, S, S, S, S, S }, // 18
			{ S, S, S, S, S, S, S, S, S, S }, // 17
			{ S, S, S, S, S, H, H, SU, SU, SU }, // 16
			{ S, S, S, S, S, H, H, H, SU, H }, // 15
			{ S, S, S, S, S, H, H, H, H, H }, // 14
			{ S, S, S, S, S, H, H, H, H, H }, // 13
			{ H, H, S, S, S, H, H, H, H, H }, // 12
			{ Dh, Dh, Dh, Dh, Dh, Dh, Dh, Dh, Dh, Dh }, // 11
			{ Dh, Dh, Dh, Dh, Dh, Dh, Dh, Dh, H, H }, // 10
			{ H, Dh, Dh, Dh, Dh, H, H, H, H, H }, // 9
			{ H, H, H, H, H, H, H, H, H, H }, // 8
			{ H, H, H, H, H, H, H, H, H, H }, // 7
			{ H, H, H, H, H, H, H, H, H, H }, // 6
			{ H, H, H, H, H, H, H, H, H, H }, // 5
			{ H, H, H, H, H, H, H, H, H, H }, // 4
			{ H, H, H, H, H, H, H, H, H, H }, // 3
			{ H, H, H, H, H, H, H, H, H, H }, // 2
			{ H, H, H, H, H, H, H, H, H, H }, // 1
	};

	public static Decision basicStrategyDecision(Board mainBoard) {
		Hand dealerHand = mainBoard.dealerHand;
		Hand currentHand = mainBoard.currentHand;

		int dealerUpCard = dealerHand.handCards.get(0).getBJValue();
		if (dealerUpCard == 1) {
			dealerUpCard = 11;
		}

		int firstCardValue = currentHand.handCards.get(0).getBJValue();

		int secondCardValue = currentHand.handCards.get(1).getBJValue();

		int totalValue = currentHand.getFinalHandValue();

		/*
		 * System.out.println("first hand card is"+ firstCardValue);
		 * System.out.println("second hand card is"+ secondCardValue);
		 * System.out.println("total hand card is"+ totalValue);
		 */
		if (currentHand.checkSplit(mainBoard.gameRules)) {
			return splitFourDeck(firstCardValue, dealerUpCard, secondCardValue, totalValue, currentHand);
		} else if (currentHand.checkSoft()) {
			return softFourDeck(firstCardValue, dealerUpCard, secondCardValue, totalValue, currentHand);
		}
		else {
			return hardFourDeck(firstCardValue, dealerUpCard, secondCardValue, totalValue, currentHand);
		}

	}

	private static Decision splitFourDeck(int firstCardValue, int dealerUpCard, int secondCardValue, int totalValue, Hand currentHand) {
		if (firstCardValue == 1) {
			return Decision.SPLIT;
		}

		else if (firstCardValue == 10) {
			return Decision.STAND;
		}

		else if (firstCardValue == 9) {
			if (dealerUpCard == 7 || dealerUpCard == 10 || dealerUpCard == 11) {
				return Decision.STAND;
			} else {
				return Decision.SPLIT;
			}
		}

		else if (firstCardValue == 8) {
			return Decision.SPLIT;
		}

		else if (firstCardValue == 7) {
			if (dealerUpCard >= 8) {
				return Decision.HIT;
			} else {
				return Decision.SPLIT;
			}
		}

		else if (firstCardValue == 6) {
			if (dealerUpCard >= 7) {
				return Decision.HIT;
			} else {
				return Decision.SPLIT;
			}
		}

		else if (firstCardValue == 5) {
			if (dealerUpCard >= 10) {
				return Decision.HIT;
			} else {
				return Decision.DOUBLEDOWN;
			}
		}

		else if (firstCardValue == 4) {
			if (dealerUpCard == 5 || dealerUpCard == 6) {
				return Decision.SPLIT;
			} else {
				return Decision.HIT;}
		}

		else if (firstCardValue == 3) {
			if (dealerUpCard >= 8) {
				return Decision.HIT;
			} else
				{return Decision.SPLIT;}
		}

		else if (firstCardValue == 2) {
			if (dealerUpCard >= 8) {
				return Decision.HIT;
			} else
				{return Decision.SPLIT;}
		} else {
			error(firstCardValue, dealerUpCard, secondCardValue, totalValue, currentHand);
			return Decision.HIT;
		}
	}



	private static Decision softFourDeck(int firstCardValue, int dealerUpCard, int secondCardValue, int totalValue, Hand currentHand) {
		if (totalValue >= 20) {
			return Decision.STAND;
		}

		else if (totalValue == 19) {
			if (dealerUpCard == 6 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.STAND;
		}

		else if (totalValue == 18) {
			if (dealerUpCard >= 9) {
				return Decision.STAND;
			} else if (currentHand.firstAction && dealerUpCard <= 6) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.STAND;
		}

		else if (totalValue == 17) {
			if (dealerUpCard >= 3 && dealerUpCard <= 6 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.HIT;
		} else if (totalValue == 16) {
			if (dealerUpCard >= 4 && dealerUpCard <= 6 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.HIT;
		} else if (totalValue == 15) {
			if (dealerUpCard >= 4 && dealerUpCard <= 6 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.HIT;
		} else if (totalValue == 14) {
			if (dealerUpCard >= 5 && dealerUpCard <= 6 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.HIT;
		} else if (totalValue == 13) {
			if (dealerUpCard >= 5 && dealerUpCard <= 6 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.HIT;
		} 
		else if (totalValue == 12) {
			//need to double check
			return Decision.HIT;
		}
		else {
			error(firstCardValue, dealerUpCard, secondCardValue, totalValue, currentHand);
			return Decision.HIT;
		}
	}

	private static Decision hardFourDeck(int firstCardValue, int dealerUpCard, int secondCardValue, int totalValue, Hand currentHand) {
		if (totalValue >= 17) {
			return Decision.STAND;
		} else if (totalValue == 16) {
			if (dealerUpCard <= 6) {
				return Decision.STAND;
			} else if (dealerUpCard >= 9 && currentHand.firstAction) {
				return Decision.SURRENDER;
			} else
				return Decision.STAND;
		} else if (totalValue == 15) {
			if (dealerUpCard <= 6) {
				return Decision.STAND;
			} else if (dealerUpCard == 8 && currentHand.firstAction) {
				return Decision.SURRENDER;
			} else
				return Decision.STAND;
		} else if (totalValue == 14 || totalValue == 13) {
			if (dealerUpCard <= 6) {
				return Decision.STAND;
			} else
				return Decision.HIT;
		} else if (totalValue == 12) {
			if (dealerUpCard <= 6 && dealerUpCard >= 4) {
				return Decision.STAND;
			} else
				return Decision.HIT;
		}

		else if (totalValue == 11) {
			if (currentHand.firstAction) {
			return Decision.DOUBLEDOWN;
			}
			else return Decision.HIT;
		} else if (totalValue == 10) {
			if (dealerUpCard >= 10||!currentHand.firstAction) {
				return Decision.HIT;
			} else
				return Decision.DOUBLEDOWN;
		} else if (totalValue == 9) {
			if (dealerUpCard <= 6 && dealerUpCard >= 3 && currentHand.firstAction) {
				return Decision.DOUBLEDOWN;
			} else
				return Decision.HIT;
		} else if (totalValue<=8&&totalValue>=5) {
			return Decision.HIT;
		}
		else if (totalValue==4) {
			//need to double check
			return Decision.HIT;
		}
		else {
			error(firstCardValue, dealerUpCard, secondCardValue, totalValue, currentHand);
			return Decision.HIT;
		}
	}

	
	private static void error(int firstCardValue, int dealerUpCard, int secondCardValue, int totalValue, Hand currentHand) {
		System.out.println("Did not find setting");
		System.out.println("Dealer Card:" + dealerUpCard + " Current Hand Cards" + currentHand.handToString() + " Total Value:" + totalValue);		
		System.out.println("First Card :" + firstCardValue + " Second Card:" + secondCardValue);	
	}
}
