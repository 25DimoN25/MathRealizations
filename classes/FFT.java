package fft;

import java.util.Locale;

public class FFT {


	public static Complex[] fft(Complex[] x) {
		int n = x.length;


		if (n == 1)
			return new Complex[] { x[0] };

		// проверка того, что n=2^x
		if (n % 2 != 0) {
			throw new RuntimeException("n is not a power of 2");
		}

		// преобразование четных
		Complex[] even = new Complex[n / 2];
		for (int k = 0; k < n / 2; k++) {
			even[k] = x[2 * k];
		}
		Complex[] q = fft(even);

		// преобразование нечетных
		Complex[] odd = even; // new Complex[n / 2];
		for (int k = 0; k < n / 2; k++) {
			odd[k] = x[2 * k + 1];
		}
		Complex[] r = fft(odd);

		// слияние
		Complex[] y = new Complex[n];
		for (int k = 0; k < n / 2; k++) {
			double kth = -2 * k * Math.PI / n;
			Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
			y[k] = q[k].plus(wk.times(r[k]));			//	qk + Wk*rk
			y[k + n / 2] = q[k].minus(wk.times(r[k]));	//	qk - Wk*rk
		}
		return y;
	}

	

	public static Complex[] ifft(Complex[] x) {
		int n = x.length;
		Complex[] y = new Complex[n];

		//	берем сопряжение
		for (int i = 0; i < n; i++) {
			y[i] = x[i].conjugate();
		}

		//	БПФ
		y = fft(y);

		//	обратно сопряжение
		for (int i = 0; i < n; i++) {
			y[i] = y[i].conjugate().scale(1.0 / n);
		}

		return y;
	}


	
	public static void show(Complex[] x, String title) {
		System.out.println(title);
		System.out.println("-------------------");
		for (int i = 0; i < x.length; i++) {
			System.out.println(x[i]);
		}
		System.out.println();
	}

	
	
	
	static Complex[] minusIPow = {
			new Complex(1, 0),	//-i^0
			new Complex(0, -1),	//-i^1
			new Complex(-1, 0),	//-i^2
			new Complex(0, 1)	//-i^3
	};
	
	public static void main(String[] args) {
		int n = 16;
		Complex[] x = new Complex[n];


		for (int i = 0; i < n; i++) {
			x[i] = new Complex(i, 0);
		}
		show(x, "x");

		Complex[] y = fft(x);
		show(y, "бпф");
		
		Complex[] z = ifft(y);
		show(z, "ибпф");
		
		
		
		// res[k]=z[k]*(-i)^k
		System.out.println("Сдвиг");
		Complex[][] res = new Complex[4][n];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < n; j++) {
				res[i][j] = z[j].times(minusIPow[i * j % 4]);
			}
//			res[i] = ifft(res[i]);
		}
		
		Locale.setDefault(Locale.US);
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < 4; i++) {
				System.out.println(res[i][j]);
//				System.out.printf("%.5f, ", res[i][j].re());
			}
			System.out.println();
		}


		
		

	}
}

















class Complex {
	private final double re; // the real part
	private final double im; // the imaginary part

	// create a new object with the given real and imaginary parts
	public Complex(double real, double imag) {
		re = real;
		im = imag;
	}

	// return a string representation of the invoking Complex object
	public String toString() {
		if (im == 0)
			return re + "";
		if (re == 0)
			return im + "i";
		if (im < 0)
			return re + " - " + (-im) + "i";
		return re + " + " + im + "i";
	}

	// return abs/modulus/magnitude
	public double abs() {
		return Math.hypot(re, im);
	}

	// return angle/phase/argument, normalized to be between -pi and pi
	public double phase() {
		return Math.atan2(im, re);
	}

	// return a new Complex object whose value is (this + b)
	public Complex plus(Complex b) {
		Complex a = this; // invoking object
		double real = a.re + b.re;
		double imag = a.im + b.im;
		return new Complex(real, imag);
	}

	// return a new Complex object whose value is (this - b)
	public Complex minus(Complex b) {
		Complex a = this;
		double real = a.re - b.re;
		double imag = a.im - b.im;
		return new Complex(real, imag);
	}

	// return a new Complex object whose value is (this * b)
	public Complex times(Complex b) {
		Complex a = this;
		double real = a.re * b.re - a.im * b.im;
		double imag = a.re * b.im + a.im * b.re;
		return new Complex(real, imag);
	}

	// return a new object whose value is (this * alpha)
	public Complex scale(double alpha) {
		return new Complex(alpha * re, alpha * im);
	}

	// return a new Complex object whose value is the conjugate of this
	public Complex conjugate() {
		return new Complex(re, -im);
	}

	// return a new Complex object whose value is the reciprocal of this
	public Complex reciprocal() {
		double scale = re * re + im * im;
		return new Complex(re / scale, -im / scale);
	}

	// return the real or imaginary part
	public double re() {
		return re;
	}

	public double im() {
		return im;
	}

	// return a / b
	public Complex divides(Complex b) {
		Complex a = this;
		return a.times(b.reciprocal());
	}

	// return a new Complex object whose value is the complex exponential of
	// this
	public Complex exp() {
		return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
	}

	// return a new Complex object whose value is the complex sine of this
	public Complex sin() {
		return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
	}

	// return a new Complex object whose value is the complex cosine of this
	public Complex cos() {
		return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
	}

	// return a new Complex object whose value is the complex tangent of this
	public Complex tan() {
		return sin().divides(cos());
	}

	public static Complex plus(Complex a, Complex b) {
		double real = a.re + b.re;
		double imag = a.im + b.im;
		Complex sum = new Complex(real, imag);
		return sum;
	}

	public boolean equals(Object x) {
		if (x == null)
			return false;
		if (this.getClass() != x.getClass())
			return false;
		Complex that = (Complex) x;
		return (this.re == that.re) && (this.im == that.im);
	}

	public int hashCode() {
		return java.util.Objects.hash(re, im);
	}
}