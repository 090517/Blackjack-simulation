import gameClasses.edgeCalculator;
import strategy.BasicStrategyInputStream;
import strategy.InputStream;

public class Main {
	public static void main(String[] args) {
		System.out.println(edgeCalculator.calcEdge((InputStream) new BasicStrategyInputStream(), 10000000));
		System.out.println(edgeCalculator.calcEdge((InputStream) new strategy.CardCountingInputStream(), 10000000));
	}
}
