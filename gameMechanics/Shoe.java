package gameMechanics;

import java.util.ArrayList;
import java.util.Random;

import rules.Rules;

public class Shoe {
	public ArrayList<Card> mainShoe;
	Rules gameRules;
	private static int numSuits = 4;
	private static int numValues = 13;
	private Random ranNumGen;

	public Shoe(Rules newGameRules) {
		gameRules=newGameRules;
		mainShoe = new ArrayList<Card>();
		this.fill();
		ranNumGen = new Random(System.currentTimeMillis());
	}

	public void riffleShuffle() {
		ArrayList<Card> pileA = new ArrayList<Card>();
		ArrayList<Card> pileB = new ArrayList<Card>();
		while (mainShoe.size() != 0) {
			Boolean pileATrue = ranNumGen.nextBoolean();
			if (pileATrue) {
				pileA.add(mainShoe.get(0));
				mainShoe.remove(0);
			} else {
				pileB.add(mainShoe.get(0));
				mainShoe.remove(0);
			}
		}
		mainShoe.addAll(pileA);
		mainShoe.addAll(pileB);
	}

	public Card deal() {
		Card output = mainShoe.get(0);
		mainShoe.remove(0);
		return output;
	}

	public void reset() {
		mainShoe.clear();
		this.fill();
	}

	public void shoePrint() {
		for (Card tempCard : mainShoe) {
			System.out.println(tempCard.cardString());
		}
	}

	public int cardsLeft() {
		return (mainShoe.size());
	}

	public int tensLeft() {
		return cardsLeft(10);
	}

	public int acesLeft() {
		return cardsLeft(1);
	}

	public int cardsLeft(int value) {
		int output = 0;
		for (Card tempCard : mainShoe) {
			if (tempCard.getBJValue() == value) {
				output++;
			}
		}
		return output;
	}

	private void fill() {
		for (int i = 0; i < gameRules.NUMDECKS; i++) {
			for (int value = 1; value <= numValues; value++) {
				for (int suit = 0; suit < numSuits; suit++) {
					mainShoe.add(new Card(value, suit));
				}
			}
		}
	}
}
