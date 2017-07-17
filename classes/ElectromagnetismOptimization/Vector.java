package EM;

import java.util.Arrays;

public class Vector {
	
	static double[] plus(double[] vector, double num) {
		double[] newVector = new double[vector.length];
		
		for (int i = 0; i < newVector.length; i++) {
			newVector[i] = vector[i] + num;
		}
		
		return newVector;
	}
	
	static double[] plus(double[] vector1, double[] vector2) {
		double[] newVector = new double[vector1.length];
		
		for (int i = 0; i < newVector.length; i++) {
			newVector[i] = vector1[i] + vector2[i];
		}
		
		return newVector;
	}
	
	
	
	
	static double[] minus(double vector[], double num) {
		double[] newVector = new double[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
			newVector[i] = vector[i] - num;
		}
		
		return newVector;
	}
	
	static double[] minus(double[] vector1, double[] vector2) {
		double[] newVector = new double[vector1.length];
		
		for (int i = 0; i < newVector.length; i++) {
			newVector[i] = vector1[i] - vector2[i];
		}
		
		return newVector;
	}
	
	
	
	static double[] times(double vector[], double num) {
		double[] newVector = new double[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
			newVector[i] = vector[i] * num;
		}
		
		return newVector;
	}
	
	static double[] times(double[] vector1, double[] vector2) {
		double[] newVector = new double[vector1.length];
		
		for (int i = 0; i < newVector.length; i++) {
			newVector[i] = vector1[i] * vector2[i];
		}
		
		return newVector;
	}
	
	
	
	static double[] div(double vector[], double num) {
		double[] newVector = new double[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
			newVector[i] = vector[i] / num;
		}
		
		return newVector;
	}
	
	static double[] div(double[] vector1, double[] vector2) {
		double[] newVector = new double[vector1.length];
		
		for (int i = 0; i < newVector.length; i++) {
			newVector[i] = vector1[i] / vector2[i];
		}
		
		return newVector;
	}
	
	
	static double min(double vector[]) {
		double minValue = vector[0];
		
		for (int i = 1; i < vector.length; i++) {
			if (vector[i] < minValue) {
				minValue = vector[i];
			}
		}
		
		return minValue;
	}
	
	static double max(double vector[]) {
		double maxValue = vector[0];
		
		for (int i = 1; i < vector.length; i++) {
			if (vector[i] > maxValue) {
				maxValue = vector[i];
			}
		}
		
		return maxValue;
	}
	
	static double length(double[] vector) {
		double result = 0;
		for (int i = 0; i < vector.length; i++) {
			result += vector[i] * vector[i];
		}
		return Math.sqrt(result);
	}
	
	static double[] copy(final double[] vector) {
		return Arrays.copyOf(vector, vector.length);
	}
}
