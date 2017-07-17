package EM;
import java.util.Arrays;
import java.util.Locale;

import static EM.Vector.*;

public class EmAlgorithm {
	
	int m = 5;						//	���������� ������������, �������������� �����
	
	
	double[] l = {-1000, -1000, -1000};		//	�����  ������� ���������� (����������� n)
	double[] u = { 1000,  1000,  1000};		//	������ ������� ���������� (����������� n)
	
	
	int MAX_ITER = 100;				//	������������ ���������� �������� ������ ������
	
	
	int LS_ITER = 100;					//	������������ ���������� �������� ���������� ������
	double d = 0.1;						//	"������ ����" ���������� ������ [0 .. 1]
	
	
	int n = 2;							//	����������� �������
	double f(double ... x) {			//	�������������� �������
//		return 5 * ((x[0] + 3) * (x[0] + 3)) + 20;	//x = -3
		return ((x[0] + 50) * (x[0] + 50)) + ((x[1] - 25) * (x[1] - 25));	//x = -50, 25
//		return 10*(x[0] + 5)*(x[0] + 5) + 4*(x[1] + 8)*(x[1] + 8) + 5*(x[2] + 10)*(x[2] + 10);	//x = -5, 8, -10
	};
	
		
	int best = 0;						//	����� ��������
	double[][] x = new double[m][n];	//	��������� ������
	double[]   q = new double[m];		// 	������ ������
	double[][] F = new double[m][n];	//	���� ������
	
	
		
	
	void start() {
		Locale.setDefault(Locale.US);
		System.out.print("{");
		
		//������������� ���������
		initialize();
		for (int i = 1; i <= MAX_ITER; i++) {
			//��������� �����
			local();
			//���������� �������� ���, ����������� �� ������ �������
			calculateForces();
			//����������� ������
			move();
			
//			System.out.printf("i=%4d f=%.9f%n", i, f(x[best]));
			System.out.printf("{%d, %.9f}, ", i, f(x[best]));
		}

		System.out.print("};");
		//����� ����������� ����������
//		System.out.println("f" + Arrays.toString(x[best]) + " = " + f(x[best]));
	}
	
	/**
	 * ������������ ��������� ������� �����
	 */
	void initialize() {
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < n; k++) {
				double lambda = U();
				x[i][k] = l[k] + lambda * (u[k] - l[k]);	//������ ����� ����� ���������� � "����� ������� + ��������� ����� ���������"
			}
		}
	}

	/**
	 * ���������, �������������� ����� (���� ���������� �� ��������� �����)
	 */
	void local() {
		double[] y = new double[n]; 
		
		
		double length = d * max(minus(u, l)); 	// ������������ ����� �� ���� ����������
		
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
					
					if ( f(y) < f(x[i]) ) {		//���� while �� ��� ���, ���� �� ���������� ��� ������� ��� �� ����������� ���� ���-�� ��������
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
	 * ���������� ��������� ���
	 */
	void calculateForces() {
		double sum = 0;
		for (int k = 0; k < m; k++) {
			sum += f(x[k]) - f(x[best]);
		}
		
		for (int i = 0; i < m; i++) {
			q[i] = Math.exp(											//	����������� ����� ������ �����
						-n * (f(x[i]) - f(x[best])) / sum
					);
			
			F[i] = new double[n];	// = 0
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if (i != j) {
					double[] xjxi = minus(x[j], x[i]);	// x_j - x_i
					double[] right = times(		// ������ ����� ���������
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
	 * ������������ ������
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
	 * ��������� ����� ����� 0 � 1
	 */
	double U() {
		return Math.random();
	}
	

	
	public static void main(String[] args) {
		new EmAlgorithm().start();
	}
}
