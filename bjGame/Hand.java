package bjGame;

import java.util.ArrayList;

import rules.Rules;

public class Hand {
	public ArrayList<Card> handCards;
	public double betSize;
	public boolean insuranceBet;
	public boolean surrendered;
	public boolean aceSplit;
	public int splitNum;
	public boolean splitAble;
	public boolean firstAction;

	public Hand(double newbetSize, Rules gameRules) {
		handCards = new ArrayList<Card>();
		betSize = newbetSize;
		insuranceBet = false;
		surrendered = false;
		aceSplit = false;
		splitNum = 0;
		splitAble = gameRules.ALLOWSPLITTING;
		firstAction = true;
	}

	//Change methods
	public void addCard(Card newCard) {
		handCards.add(newCard);
	}

	public void newBet(double newBetSize) {
		betSize = newBetSize;
	}
	
	public void handClear() {
		handCards.clear();
	}

	public Hand splitHand(Rules gameRules) {
		// check hand can be split
		if (checkSplit(gameRules)) {
			Hand output = new Hand(betSize, gameRules);
			output.addCard(handCards.get(1));
			splitNum++;
			if (output.handCards.get(0).getBJValue()==1) {
				aceSplit=true;
			}
			handCards.remove(1);
			return output;
		} else {
			throw new ArithmeticException("cannot split hand");
		}
	}

	public void doubleDown(Card nextCard) {
		handCards.add(nextCard);
		betSize = betSize * 2;
	}
	
	public void addInsurance() {
		insuranceBet = true;
	}

	//String Functions

	public String handToString() {
		return ("BET " + betSize + " " + noBetString());
	}

	public String dealerUpCard() {
		return "DEALER CARD: " + handCards.get(1).cardString();
	}

	public String noBetString() {
		String output = new String("HANDCARDS: ");
		for (Card tempCard : handCards) {
			output += (tempCard.cardString() + ". ");
		}
		output += ("TOTAL: " + getFinalHandValue());
		return output;
	}
	
	// Get functions

	public int getFinalHandValue() {
		if (this.getAceValue() <= 21) {
			return this.getAceValue();
		}
		return this.getValue();
	}
	
	public int getValue() {
		int answer = 0;
		for (Card tempCard : handCards) {
			answer += tempCard.getBJValue();// All non facevalue cards.
		}
		return answer;
	}

	public int getAceValue() {
		if (checkAce() == false) {
			return this.getValue();
		}
		return (this.getValue() + 10);

	}

	public int getAces() {
		int numAces = 0;
		for (Card tempCard : handCards) {
			if (tempCard.getValue() == 1) {
				numAces++;
			}
		}
		return numAces;
	}

	//Boolean Checks
	
	public boolean checkSoft() {
		if (getFinalHandValue() != getValue()) {
			return true;
		}
		return false;
	}
	
	public boolean checkBusted() {
		return (getFinalHandValue() > 21);
	}
	
	public boolean checkSplit(Rules gameRules) {
		return ((handCards.size() == 2) && (handCards.get(0).getBJValue() == handCards.get(1).getBJValue())
				&& splitAble && (splitNum<gameRules.MAXSPLIT));
	}

	public boolean checkDouble(Rules gameRules){
		boolean splitCheck = ((splitNum==0)||(gameRules.ALLOWDOUBLEAFTERSPLIT));
		boolean acesSplitCheck = ((!aceSplit)||gameRules.ALLOWDOUBLEAFTERACESPLIT);
		return (gameRules.ALLOWDOUBLEDOWNONANY && splitCheck && acesSplitCheck);
	}
	
	public boolean checkAce() {
		for (Card tempCard : handCards) {
			if (tempCard.getValue() == 1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkBlackJack() {
		return (checkAce() && (handCards.size() == 2) && (getAceValue() == 21));
	}

	public void setHand(int firstHandCard, int secondHandCard) {
		handCards.clear();
		handCards.add(new Card(firstHandCard, 0));
		handCards.add(new Card(secondHandCard, 0));
	}
	
	public void setHand(int firstHandCard, int secondHandCard, int thirdHandCard) {
		handCards.clear();
		handCards.add(new Card(firstHandCard, 0));
		handCards.add(new Card(secondHandCard, 0));
		handCards.add(new Card(thirdHandCard, 0));
	}
}