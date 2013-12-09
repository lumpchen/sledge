package me.lumpchen.sledge.pdf.graphics;

public class Matrix {

	private double a;
	private double b;
	private double c;
	private double d;
	private double e;
	private double f;

	public Matrix(double a, double b, double c, double d, double e, double f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	public Matrix concate(Matrix m) {
		double[][] a = this.toArray();
		double[][] b = m.toArray();
		
		double[][] c = this.multiply(a, b);
		return this.toMatrix(c);
	}

	private double[][] toArray() {
		return new double[][]{{a, b, 0}, {c, d, 0}, {e, f, 1}};
	}
	
	private Matrix toMatrix(double[][] c) {
		return new Matrix(c[0][0], c[0][1], c[1][0], c[1][1], c[2][0], c[2][1]);
	}
	
	public double[][] multiply(double[][] A, double[][] B) {
		int mA = A.length;
		int nA = A[0].length;
		int mB = B.length;
		int nB = A[0].length;
		if (nA != mB) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		double[][] C = new double[mA][nB];
		for (int i = 0; i < mA; i++) {
			for (int j = 0; j < nB; j++) {
				for (int k = 0; k < nA; k++) {
					C[i][j] += (A[i][k] * B[k][j]);
				}
			}
		}
		return C;
	}
}
