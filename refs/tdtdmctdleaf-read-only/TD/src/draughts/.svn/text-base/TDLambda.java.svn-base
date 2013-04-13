package draughts;

public class TDLambda extends TD {

	private double alpha;
	private double gamma;
	private double lambda;
	private Boolean useMC;
	private int simNumber = 1000;

	public TDLambda(double[] initialWeights, double a, double b,
			double alphaLearningRate, double gammaDiscountParameter,
			double lambdaDecayParameter, Boolean useMonteCarloRewards) {
		super(initialWeights, a, b);
		this.alpha = alphaLearningRate;
		this.gamma = gammaDiscountParameter;
		this.lambda = lambdaDecayParameter;
		useMC = useMonteCarloRewards;
	}

	@Override
	public void updateWeights(GameData gameData) {
		double[] rewards = null;
		if (useMC)
			rewards = calculateMonteCarloWinningProbabilities(gameData,
					simNumber);
		for (int t = 0; t < gameData.evaluationFunctionFeatures.size() - 1; t++) {
			if (useMC) {
				double reward = (t == gameData.evaluationFunctionFeatures
						.size() - 2 ? 0 : rewards[t + 1] - rewards[t]);
				reward = 0.01 * reward;
				double delta = (reward) + gamma
						* gameData.statesEvaluations.get(t + 1)
						- gameData.statesEvaluations.get(t);
				double[] eligibility = calculateEligibilityVector(gameData, t);
				for (int i = 0; i < getWeights().length; i++) {
					getWeights()[i] += alpha * delta * eligibility[i];
				}
			} else {
				double delta = gamma * gameData.statesEvaluations.get(t + 1)
						- gameData.statesEvaluations.get(t);
				double[] eligibility = calculateEligibilityVector(gameData, t);
				for (int i = 0; i < getWeights().length; i++) {
					getWeights()[i] += alpha * delta * eligibility[i];
				}

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
		double[] currFeatures = gameData.evaluationFunctionFeatures.get(t);
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
