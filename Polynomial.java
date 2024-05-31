import java.io.*;

public class Polynomial {
	double[] coefficients;
	int[] exponents;
	
	public Polynomial() {
		coefficients = new double[1];
		exponents = new int[1];
	}
	public Polynomial(double[] coeffArray, int[] expArray) {
		coefficients = coeffArray;
		exponents = expArray;
	}
	Polynomial add(Polynomial p) {
		//p1.add(p2);
		if(p == null || p.coefficients.length != p.exponents.length) {
			return null;
		}
		int commonExp = 0;
		for(int a: exponents) {
			for(int b: p.exponents) {
				if(a == b) {
					commonExp++;
				}
			}
		}
		int size = coefficients.length + p.coefficients.length - commonExp;
		int counter = 0;
		int[] exp = new int[size];
		double[] coef = new double[size];
		
		for(int i=0; i<coefficients.length; i++, counter++) {
			exp[counter] = exponents[i];
			coef[counter] = coefficients[i];
		}
		for(int i=0; i<p.coefficients.length; i++) {
			for(int j=0; j<counter; j++) {
				if(p.exponents[i] == exp[j]) {
					coef[j] += p.coefficients[i];
					break;
				}
				else if(j == counter - 1) {
					coef[counter] = p.coefficients[i];
					exp[counter] = p.exponents[i];
					counter++;
					break;
				}
			}	
		}
		Polynomial sum = new Polynomial(coef, exp);
		return sum.clean();
	}
	Polynomial clean() {
		int counter = coefficients.length;
		for(double a: coefficients) {
			if (a == 0) {
				counter--;
			}
		}
		
		double[] newCoef = new double[counter];
		int[] newExp = new int[counter];
		for(int i=0, j=0; i<coefficients.length && j< newCoef.length; i++) {
			if (coefficients[i] != 0) {
				 newCoef[j] = coefficients[i];
				 newExp[j] = exponents[i];
				 j++;
			}
		}
		Polynomial result = new Polynomial(newCoef, newExp);
		return result;
	}
	
	double evaluate(double value) {
		double result = 0;
		for(int i=0; i<exponents.length; i++) {
			result += coefficients[i]*Math.pow(value, exponents[i]); 
		}
		return result;
	}
	
	boolean hasRoot(double root) {
		return evaluate(root) == 0;
	}
	
	Polynomial multiply(Polynomial p) {
		if(p == null || p.coefficients.length != p.exponents.length) {
			return null;
		}
		Polynomial result = new Polynomial();
		Polynomial temp = new Polynomial();
		Polynomial temp2 = new Polynomial();
		
		for(int i=0; i<exponents.length; i++) {
			temp.coefficients[0] = coefficients[i];
			temp.exponents[0] = exponents[i];
			for(int j=0; j<p.exponents.length; j++) {
				temp2.coefficients[0] = p.coefficients[j] * temp.coefficients[0];
				temp2.exponents[0] = p.exponents[j] + temp.exponents[0];
				result = result.add(temp2);
			}
		}
		return result;
	}
	
	public Polynomial(File file) {	
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String poly = bufferedReader.readLine().replace("-", "+-");
			if(poly.startsWith("+-")) {
				poly = poly.substring(1);
			}
			
			String[] terms  = poly.split("\\+");
			double[] coef = new double[terms.length];
			int[] exp = new int[terms.length];
			int counter = 0;
			
			for(String a: terms) {
				//Note:
				//coefToExp[0] is coefficient
				//coefToExp[1] is exponent
				//Possibility: x, -x, 1, -1, -1x1, 1x1 
				if(a.contains("x")) {
					String[] coefToExp = a.split("x");
			
					//Case 1: x
					if(coefToExp.length == 0 || coefToExp[0].isEmpty()) {
						coef[counter] = 1.0;
					}
					//Case 2: -x
					else if(coefToExp[0].equals("-")) {
						coef[counter] = -1.0;
					}
					//Case 3: ax
					else {
						coef[counter] = Double.parseDouble(coefToExp[0]);
					}
					
					if(coefToExp.length > 1 && !coefToExp[1].isEmpty()) {
						exp[counter] = Integer.parseInt(coefToExp[1]);
					}
					else {
						exp[counter] = 1;
					}	
				}
				else {
					coef[counter] = Double.parseDouble(a);
					exp[counter] = 0;
				}
				
				counter++;
			}
			Polynomial temp = (new Polynomial(coef, exp)).clean();
			coefficients = temp.coefficients;
			exponents = temp.exponents;
			bufferedReader.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void saveToFile(String fileName) {
		try {
			//File does not exist? 
			//File exists and writable?
			File file = new File(fileName);
			file.createNewFile();
			if(!file.canWrite()) {
				return;
			}
			String output = "";
			for(int i=0; i<coefficients.length; i++) {
				output += "+" + coefficients[i];
				if(exponents[i] >= 1) {
					output += "x";
				}
				if(exponents[i] > 1) {
					output += exponents[i];
				}
				if(i == 0) {
					output = output.substring(1);
				}
			}
			output = output.replace("+-", "-");
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
			bufferedWriter.write(output);
			bufferedWriter.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}