public class Polynomial {

	double[] coefficients;
	
	public Polynomial() {
		coefficients = new double[1];
	}
	public Polynomial(double[] array) {
		coefficients = new double[array.length];
		for(int i=0; i<array.length; i++) {
			coefficients[i] = array[i];
		}
	}
	Polynomial add(Polynomial p) {

		int maxLength = Math.max(coefficients.length, p.coefficients.length);
		double[] array = new double[maxLength];
		for (int i = 0; i < maxLength; i++) {
			if(i < coefficients.length) {
				array[i] += coefficients[i];
			}
			if(i < p.coefficients.length) {
				array[i] += p.coefficients[i];
			}
		}
		Polynomial result = new Polynomial(array);
		return result;
	}
	
	double evaluate(double value) {
		double result = 0;
		int counter = 0;
		for(double coef:coefficients) {
			result += coef*Math.pow(value, counter);
			counter++;
		}
		return result;
	}
	
	boolean hasRoot(double root) {
		return evaluate(root) == 0;
	}
}