package rules;

/*
RULE	EFFECT
---------double down rules----------
Double down on 10, 11 -
Double down on 9 
Double down on 8
Double down any hard count
Double down after split 
Double down on soft count 
Soft Double A9 or A8 only
Ace Counts as 1 after double
Double down on three cards
Double on any number of cards
Redouble
Soft Double Down counts as hard hand
Discard Double
Hit after Double Down

*/

public class Rules {
	
	//SHOE RULES;
	public final int NUMDECKS = 6;
	public final int INITIALNUMSHUFFLE = 12;
	public final int RESHUFFLEAFTER=50;
	
	//PLAYER BET RULES
	public final int CHIPSTACK = 0;
	public final int MINBET = 1;
	public final int MAXBET = 100*MINBET;
	public final int MAXHANDS = 8;
	public final boolean ALLOWNEGATIVE = true;
	
	//PLAYER CHOICE RULES
	public final boolean SURRENDER = true;
	public final boolean INSURANCE = true;
		
	//BLACKJACK RULES
	public final double BJPAYOUT = 1.5;
	
	//DEALER PLAY RULES
	public final boolean DEALERHITSONSOFT17 = true;

	//INSURANCE RULES
	public final boolean DEALERCHECKSFORBJ = true;
	
	//SURRENDER RULES
	public final boolean LATESURRENDER = false;
	public final boolean EARLYSURRENDER = true;
	
	//SPLITRULES
	public final boolean ALLOWSPLITTING=true;
	public final boolean NODOUBLEAFTERSPLIT = true;
	public final int MAXSPLIT = 3;
	public final boolean ACESPLITONECARD=false;
	
	//DOUBLE DOWN RULES
	public final boolean ALLOWDOUBLEDOWNONANY = true;
	public final boolean ALLOWDOUBLEAFTERSPLIT = true;
	public final boolean ALLOWDOUBLEAFTERACESPLIT = true;
	public final int MAXACESSPLIT = 1;
	//OTHER RULES
}
