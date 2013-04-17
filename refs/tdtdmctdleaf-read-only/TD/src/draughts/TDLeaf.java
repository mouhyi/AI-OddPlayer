package draughts;

public class TDLeaf extends TD {

	private double alpha;
	private double gamma;
	private double lambda;

	public TDLeaf(double[] initialWeights, double a, double b, double alphaLearningRate, double gammaDiscountParameter,
			double lambdaDecayParameter) {
		super(initialWeights, a, b);
		this.alpha = alphaLearningRate;
		this.gamma = gammaDiscountParameter;
		this.lambda = lambdaDecayParameter;
	}

	@Override
	public void updateWeights(GameData gameData) {

		for (int t = 0; t < gameData.evaluationFunctionFeatures.size() - 1; t++) {
			double delta = gamma * gameData.statesEvaluations.get(t + 1) - gameData.statesEvaluations.get(t);
			double[] eligibility = calculateEligibilityVector(gameData, t);
			for (int i = 0; i < getWeights().length; i++) {
				getWeights()[i] += alpha * delta * eligibility[i];
			}
		}
	}

	private double[] calculateEligibilityVector(GameData gameData, int t) {

		double[] result = new double[getWeights().length];
		if (t == 0) {
			for (int i = 0; i < result.length; i++) {
				result[i] = 0;
			}
			return result;
		}

		result = calculateEligibilityVector(gameData, t - 1);
		FeatureCalculations calculations = new FeatureCalculations(gameData.principalVariation.get(t),
				gameData.playerCheckersSort);
		double[] currFeatures = calculations.getEvaluationFunctionFeatures();
		for (int i = 0; i < result.length; i++) {
			result[i] *= gamma;
			result[i] *= lambda;
			result[i] += currFeatures[i];
		}
		return result;
	}
	
	@Override
	public double[] getWeigths() {
		return super.weights;
	}
}
