package bjGame;

public enum Decision {
	HIT, STAND, DOUBLEDOWN, SURRENDER, SPLIT;

	public static String decisionToString(Decision inputDecision) {
		//System.out.println(inputDecision.toString());
		switch (inputDecision) {
		case HIT:
			return "H  ";
		case STAND:
			return "S  ";
		case DOUBLEDOWN:
			return "DD ";
		case SURRENDER:
			return "Sr ";
		case SPLIT:
			return "Sp ";
		}			
		return null;
	}
	
	public static Decision doubleArrayToDecision(double[] input) {
		int max=0;
		for (int i = 1; i < input.length; i++) {
			//System.out.println(decision[i]);
		    if (input[i] > input[max]) {
		      max = i;
		    }
		}
		//System.out.println("Decision Array is " + decision.toString());
		
		switch (max) {
		case 0:
			return Decision.HIT;
		case 1:
			return Decision.STAND;
		case 2:
			return Decision.DOUBLEDOWN;
		case 3:
			return Decision.SURRENDER;
		case 4:
			return Decision.SPLIT;
		}
		System.out.println("Failed to send decision");
		return null;
	}
}
