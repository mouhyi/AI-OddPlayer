package draughts;

import java.util.List;

/*
 * Implementation of the  TDMC (as described in the Osaki, Shibahara Tajima and Kotani article)
 */
public class TDMC extends TD {
	double gammaDiscountRate = 0.5;
	double lambdaEligibilityRate = 0.9;
	double alfaLearningRate = 0.0001;
	int simNumber = 500;

	public TDMC(double[] weights, double gammaDiscountRate,
			double lambdaEligibilityRate, double alfaLearningRate, double a,
			double b) {
		super(weights, a, b);
		this.gammaDiscountRate = gammaDiscountRate;
		this.lambdaEligibilityRate = lambdaEligibilityRate;
		this.alfaLearningRate = alfaLearningRate;
	}

	public void updateWeights(GameData gameData) {
		// first step of the algorithm - calculate evaluationFunctionValues
		printDoubleTable(weights);
		double[] evaluationFunctionValues = calculateEvaluationFunctionValue(gameData.evaluationFunctionFeatures);
		printDoubleTable(evaluationFunctionValues);
		// calculate evaluation function gradient
		// first dimension - time moment, second dimension - weight derrivates
		double[][] evaulationFunctionGradient = computeEvaluationFunctionGradient(gameData.evaluationFunctionFeatures);

		double[] rewards = calculateMonteCarloWinningProbabilities(gameData,
				simNumber);

		double[] Rt = computeReturn(rewards);

		double[][] RTNStep = computeNStepReturn(rewards,
				evaluationFunctionValues);

		double[] RTLambda = computeLabdaReturn(RTNStep, Rt);
		updateWeights(RTLambda, evaulationFunctionGradient,
				evaluationFunctionValues);
	}

	/**
	 * Calculates gradient of the evaluation function for every moment of time.
	 * Return value: first dimension - time moment, second dimension - weight
	 * derrivate
	 */
	private double[][] computeEvaluationFunctionGradient(
			List<double[]> evaluationFunctionFeatures) {
		double[][] gradient = new double[evaluationFunctionFeatures.size()][getWeights().length];
		for (int i = 0; i < evaluationFunctionFeatures.size() - 1; i++) {
			for (int j = 0; j < getWeights().length; j++) {
				gradient[i][j] = evaluationFunctionFeatures.get(i)[j];
			}
		}
		return gradient;
	}

	private void printDoubleTable(double[] values) {
		String text = "";
		for (int i = 0; i < values.length; i++) {
			text += String.valueOf((values[i])) + " ";
		}
		System.out.println(text);
	}

	/**
	 * 
	 * @param returns
	 *            returns by Monte Carlo simulation for every time moment
	 * @return Rt return for every time moment
	 */
	private double[] computeReturn(double[] returns) {
		double[] Rt = new double[returns.length];
		for (int i = 0; i < returns.length; i++) {
			double value = 0.0;
			for (int j = i; j < returns.length; j++)
				value += Math.pow(lambdaEligibilityRate, j - i) * returns[j];
			Rt[i] = value;
		}
		return Rt;
	}

	/**
	 * Calculates the n-step return for each moment of time
	 * 
	 * @return first argument - time moment, n-step return (from t to T-1, size
	 *         od second dimension is (T-1-t))
	 */
	private double[][] computeNStepReturn(double[] rewards,
			double[] evaluationFunctionValues) {
		double[][] Rn = new double[rewards.length][];
		// foreach time moment
		for (int i = 0; i < rewards.length; i++) {
			// for t, t+1, t+2 .... t+n - build and fill table for different
			// values of n = (t;T)
			Rn[i] = new double[rewards.length - i - 1];
			for (int j = 1; j < rewards.length - i; j++) {

				double value = 0.0;
				// for given value of n calculate sum
				for (int k = 0; k < j; k++) {
					value += Math.pow(lambdaEligibilityRate, k)
							* rewards[k + i];
				}
				value += Math.pow(lambdaEligibilityRate, (j))
						* evaluationFunctionValues[j + i];
				Rn[i][j - 1] = value;
				printDoubleTable(Rn[i]);
			}
		}
		return Rn;
	}

	/**
	 * Returns the expected lambda return for each time moment
	 * 
	 * @param Rn
	 *            n-step return
	 * @param Rt
	 *            simple return
	 * @return
	 */
	private double[] computeLabdaReturn(double[][] Rn, double[] Rt) {
		double[] RtLambda = new double[Rt.length];
		for (int i = 0; i < RtLambda.length; i++) {
			double value = 0.0;
			for (int j = 0; j < Rt.length - i - 1; j++) {
				value += Math.pow(lambdaEligibilityRate, j) * Rn[i][j];
			}
			value += Math.pow(lambdaEligibilityRate, Rt.length - i) * Rt[i];
			RtLambda[i] = value;
		}
		return RtLambda;
	}

	/**
	 * Updating weights of evaluation function
	 */
	private void updateWeights(double[] RTLambda,
			double[][] evaluationFunctionGradient,
			double[] evluationFunctionValues) {
		for (int i = 0; i < getWeights().length; i++) {
			double value = 0.0;
			for (int j = 0; j < RTLambda.length; j++) {
				value += (RTLambda[j] - evluationFunctionValues[j])
						* evaluationFunctionGradient[j][i];
			}
			if (i == 0 || i == 1 || i == 8 || i == 9)
				getWeights()[i] = getWeights()[i] + alfaLearningRate * value
						* 0.1;
			else
				getWeights()[i] = getWeights()[i] + alfaLearningRate * value;
		}
		printDoubleTable(weights);
	}

	@Override
	public double[] getWeigths() {
		return super.weights;
	}
}
