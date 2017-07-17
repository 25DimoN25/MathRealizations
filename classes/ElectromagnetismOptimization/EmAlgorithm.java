package EM;
import java.util.Arrays;
import java.util.Locale;

import static EM.Vector.*;

public class EmAlgorithm {
	
	int m = 5;						//	количество генерируемых, разбрасываемых точек
	
	
	double[] l = {-1000, -1000, -1000};		//	левые  границы аргументов (размерность n)
	double[] u = { 1000,  1000,  1000};		//	правые границы аргументов (размерность n)
	
	
	int MAX_ITER = 100;				//	максимальное количество итераций общего поиска
	
	
	int LS_ITER = 100;					//	максимальное количество итераций локального поиска
	double d = 0.1;						//	"размер шага" локального поиска [0 .. 1]
	
	
	int n = 2;							//	размерность функции
	double f(double ... x) {			//	оптимизируемая функция
//		return 5 * ((x[0] + 3) * (x[0] + 3)) + 20;	//x = -3
		return ((x[0] + 50) * (x[0] + 50)) + ((x[1] - 25) * (x[1] - 25));	//x = -50, 25
//		return 10*(x[0] + 5)*(x[0] + 5) + 4*(x[1] + 8)*(x[1] + 8) + 5*(x[2] + 10)*(x[2] + 10);	//x = -5, 8, -10
	};
	
		
	int best = 0;						//	идекс оптимума
	double[][] x = new double[m][n];	//	положение частиц
	double[]   q = new double[m];		// 	заряды частиц
	double[][] F = new double[m][n];	//	силы частиц
	
	
		
	
	void start() {
		Locale.setDefault(Locale.US);
		System.out.print("{");
		
		//инициализация популяции
		initialize();
		for (int i = 1; i <= MAX_ITER; i++) {
			//локальный поиск
			local();
			//вычисление сумарных сил, действующих на каждую частицу
			calculateForces();
			//перемещение частиц
			move();
			
//			System.out.printf("i=%4d f=%.9f%n", i, f(x[best]));
			System.out.printf("{%d, %.9f}, ", i, f(x[best]));
		}

		System.out.print("};");
		//вывод полученного результата
//		System.out.println("f" + Arrays.toString(x[best]) + " = " + f(x[best]));
	}
	
	/**
	 * Разбрасываем случайным образом точки
	 */
	void initialize() {
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < n; k++) {
				double lambda = U();
				x[i][k] = l[k] + lambda * (u[k] - l[k]);	//каждая точка будет находиться в "левая граница + случайная часть диапазона"
			}
		}
	}

	/**
	 * Локальный, стохастический поиск (сбор информации об окружении точек)
	 */
	void local() {
		double[] y = new double[n]; 
		
		
		double length = d * max(minus(u, l)); 	// максимальная длина из всех диапазонов
		
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < n; k++) {
				int counter = 0;
				
				double lambda1 = U();
				while (counter < LS_ITER) {
					
					y = copy(x[i]);
					double lambda2 = U();
					
					if (lambda1 > 0.5) {
						y[k] = y[k] + lambda2 * length;
					} else {
						y[k] = y[k] - lambda2 * length;
					}
					
					if ( f(y) < f(x[i]) ) {		//цикл while до тех пор, пока не выполнится это условие или не достигнется макс кол-во операций
						x[i] = copy(y);
						counter = LS_ITER - 1;
					}
					
					counter += 1;
				}
				
			}
		}
		best = calculateBest();
	}

	/**
	 * Вычисление суммарных сил
	 */
	void calculateForces() {
		double sum = 0;
		for (int k = 0; k < m; k++) {
			sum += f(x[k]) - f(x[best]);
		}
		
		for (int i = 0; i < m; i++) {
			q[i] = Math.exp(											//	расчитываем заряд каждой точки
						-n * (f(x[i]) - f(x[best])) / sum
					);
			
			F[i] = new double[n];	// = 0
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if (i != j) {
					double[] xjxi = minus(x[j], x[i]);	// x_j - x_i
					double[] right = times(		// правая часть выражения
										xjxi, 
										(q[i] * q[j]) / (length(xjxi) * length(xjxi)) // q_i * q_j / |x_j - x_i|^2
									 );
					
					if ( f(x[j]) < f(x[i]) ) {
						F[i] = plus(F[i], right);					
					} else {
						F[i] = minus(F[i], right);
					}
				}
			}
		}
	}

	/**
	 * Передвижение частиц
	 */
	void move() {
		for (int i = 0; i < m; i++) {
			 if (i != best) {
				 
				 double lambda = U();
				 F[i] = div(F[i], length(F[i]));
				 
				 for (int k = 0; k < n; k++) {
					 
					 if( F[i][k] > 0) {
						 x[i][k] = x[i][k] + lambda * F[i][k] * (u[k] - x[i][k]);
					 } else {
						 x[i][k] = x[i][k] + lambda * F[i][k] * (x[i][k] - l[k]);
					 }
					 
				 }
			 }
		}
	}

	
	int calculateBest() {
		int iBest = 0;
		double[] xBest = x[0];
		
		for (int i = 1; i < m; i++) {

			if (f(x[i]) < f(xBest)) {
				xBest = x[i];
				iBest = i;
			}
			
		}
		return iBest;
	}

	/**
	 * Случайное число между 0 и 1
	 */
	double U() {
		return Math.random();
	}
	

	
	public static void main(String[] args) {
		new EmAlgorithm().start();
	}
}
