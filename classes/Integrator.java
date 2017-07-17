import java.util.Random;
import java.util.function.Function;
import static java.lang.Math.*;

public class Integrator {

	
	static double integrate(Function<Double, Double> f, double minValue, double maxValue,
			double from, double to,	int iterations) {
		
		int total = iterations;
		int upperInside = 0, lowerInside = 0;
		
		Random rd = new Random();
		double x, y;
		
		for (int i = 0; i < total; i++) {			
			x = (rd.nextDouble() * (to-from)) + from;
			y = (rd.nextDouble() * (maxValue-minValue)) + minValue;
			
			if ( y > 0 && y <= f.apply(x) ) upperInside++;
			if ( y < 0 && y >= f.apply(x) ) lowerInside++;
			
		}
		
		double s = (to-from)*(maxValue-minValue);
		
		return (double) s*(upperInside-lowerInside)/total;
	}
	
		
	public static void main(String[] arg) {
		System.out.println(	integrate(x -> sin(x*x-x*x*x), -1, 1, 0, 3, 100_000_000)	);
	}
}