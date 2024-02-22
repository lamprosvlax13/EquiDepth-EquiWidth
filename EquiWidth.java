
import java.util.ArrayList;

//lampros vlachpoulos , AM 2948
public class EquiWidth {

	private IncomeManager incomeManager;

	private int bins;
	private double maximumValueIncome;
	private double minimumValueIncome;
	private double binSize;
	private ArrayList<ArrayList<Double>> pairsList;
	private int[] ScoreInPair;
	private double estimetedResult;

	public EquiWidth(IncomeManager incomeManager, int bins) {
		super();
		this.incomeManager = incomeManager;
		this.bins = bins;
		this.maximumValueIncome = 0;
		this.minimumValueIncome = 0;
		this.binSize = 0;
		this.pairsList = new ArrayList<ArrayList<Double>>();
		this.ScoreInPair = new int[this.bins];
		this.estimetedResult = 0;
		calculatePairs();
		findValuesInPair();

	}

	public void calculatePairs() {
		initializeMaxValueIncome();
		initializeMinValueIcome();
		initializeBinSize();
		double minimumValueIncomeCopy = this.minimumValueIncome;
		this.pairsList = new ArrayList<ArrayList<Double>>();
		int numBins = 0;
		while (numBins < this.bins) {
			ArrayList<Double> pair = createPair(minimumValueIncomeCopy);
			this.pairsList.add(pair);
			minimumValueIncomeCopy = minimumValueIncomeCopy + this.binSize;
			numBins++;
		}
		// lastBin
		if (pairsList.size() < this.bins) {
			ArrayList<Double> pair = new ArrayList<Double>();
			double minInPair = minimumValueIncomeCopy - this.binSize;
			double MaxInPair = maximumValueIncome;
			pair.add(minInPair);
			pair.add(MaxInPair);
			this.pairsList.add(pair);
		}
	}

	private ArrayList<Double> createPair(double minimumValueIncomeCopy) {
		ArrayList<Double> pair = new ArrayList<Double>();
		double minInPair = minimumValueIncomeCopy;
		double MaxInPair = (minimumValueIncomeCopy + this.binSize);
		pair.add(minInPair);
		pair.add(MaxInPair);
		return pair;
	}

	private void initializeBinSize() {
		this.binSize = (this.maximumValueIncome - this.minimumValueIncome) / this.bins;
		// System.out.println(binSize);
	}

	private void initializeMinValueIcome() {
		Income incomeWithMinValue = this.incomeManager.getMinValueIncome();
		this.minimumValueIncome = incomeWithMinValue.getIncomeValue();
	}

	private void initializeMaxValueIncome() {

		Income incomeWithMaxValue = this.incomeManager.getMaxValueIncome();
		this.maximumValueIncome = incomeWithMaxValue.getIncomeValue();
	}

	public void findValuesInPair() {

		ArrayList<Income> listWithAllIncomes = this.incomeManager.getListIncome();
		this.ScoreInPair = new int[this.bins];
		for (Income income : listWithAllIncomes) {
			int PositionInPair = findIndexBin(income.getIncomeValue());
			if (PositionInPair == this.bins) {
				PositionInPair -= 1;
			}
			this.ScoreInPair[PositionInPair] += 1;
		}
	}

	private int findIndexBin(double incomeValue) {
		int PositionInPair = (int) Math.floor((incomeValue - this.minimumValueIncome) / (double) binSize);
		if (PositionInPair < 0) {
			// System.out.println(
			// "Error den yparxei sto prwto bin ka9ws h min timh einai megaluterh apo to
			// search\nMetraw apo prwto bin ");
			return -1;
		}

		return PositionInPair;
	}

	public String toString() {
		String result = "equiwidth:\n";
		int numberPair = 0;
		int i = 0;
		for (ArrayList<Double> pair : pairsList) {

			result = result + i + "range: [" + pair.get(0) + "," + pair.get(1) + "), numtuples: "
					+ ScoreInPair[numberPair] + "\n";
			numberPair++;
			i++;
		}
		return result;
	}

	private double calculateNumtuplesPerBin(ArrayList<Double> rangePair, int positionInPair, int positionInPairMaxValue,
			int positionInPairMinValue, double range) {
		double maxValuePair = rangePair.get(positionInPairMaxValue);
		double minValuePair = rangePair.get(positionInPairMinValue);
		double pososto = 0.0;
		if (maxValuePair != minValuePair) {
			pososto = (range) / (maxValuePair - minValuePair);
		}
		double score = ScoreInPair[positionInPair] * pososto;
		return score;
	}

	public double estimate(int a, int b) {

		int positionInPairA = findIndexBin(a);
		int positionInPairB = findIndexBin(b);
		double numberOfRecords;
		double result = 0;

		// kai ta duo mesa
		// if (positionInPairA != -1 && positionInPairB < this.bins && positionInPairB
		// !=-1) {
		if ((positionInPairA >= 0 && positionInPairA < this.bins) && positionInPairB >= 0
				&& positionInPairB < this.bins) {

			// an einai sto idio bin
			if (positionInPairA == positionInPairB) {
				// result = calculateInsideRecors(positionInPairA, positionInPairB, scoreA +
				// scoreB);
				ArrayList<Double> rangeA = pairsList.get(positionInPairA);
				double range = b - a;
				double scoreA2 = calculateNumtuplesPerBin(rangeA, positionInPairA, 1, 0, range);
				this.estimetedResult = scoreA2;
				System.out.println("equiwidth estimated results: " + this.estimetedResult);
				return this.estimetedResult;
			}
			double scoreA = recordsForAIfInsideTotalRange(a, positionInPairA);
			double scoreB = recordsForBIfInsideTotalRange(b, positionInPairB);

			result = calculateInsideRecors(positionInPairA, positionInPairB, scoreA + scoreB);

			this.estimetedResult = result;
			System.out.println("equiwidth estimated results: " + this.estimetedResult);
			return this.estimetedResult;

		}
		// A etkosPisw
		// if (positionInPairA == -1 && positionInPairB < this.bins &&
		// positionInPairB>=0) {
		if (positionInPairA == -1 && positionInPairB >= 0 && positionInPairB < bins) {
			positionInPairA = -1;
			// A

			double scoreB = recordsForBIfInsideTotalRange(b, positionInPairB);

			result = calculateInsideRecors(positionInPairA, positionInPairB, scoreB);
			this.estimetedResult = result;
			System.out.println("equiwidth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
		}
		// B>=
		// if (positionInPairA != -1 && positionInPairB >= this.bins && positionInPairA
		// < bins ) {
		if ((positionInPairA >= 0 && positionInPairA < bins) && positionInPairB >= bins) {
			double scoreA = recordsForAIfInsideTotalRange(a, positionInPairA);
			positionInPairB = this.bins;
			numberOfRecords = calculateInsideRecors(positionInPairA, positionInPairB, scoreA);
			result = numberOfRecords;
			this.estimetedResult = result;
			System.out.println("equiwidth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
		}
		// A , B EKTOS
		// if (positionInPairA == -1 && positionInPairB >= this.bins) {
		if (positionInPairA == -1 && positionInPairB >= bins) {
			positionInPairA = -1; // 0;
			positionInPairB = bins;// bins - 1;
			numberOfRecords = calculateInsideRecors(positionInPairA, positionInPairB, 0);
			result = numberOfRecords;
			this.estimetedResult = result;
			System.out.println("equiwidth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
		}
		/*
		 * if (positionInPairA == -1 && positionInPairB == -1) {
		 * System.out.println("mikroteea kai ta 2");
		 * 
		 * } if (positionInPairA >= bins && positionInPairB >= bins) {
		 * System.out.println("megalytera kai ta 2"); }
		 */
		this.estimetedResult = result;
		System.out.println("equiwidth estimated results: " + this.estimetedResult);
		return this.estimetedResult;
	}

	private double recordsForBIfInsideTotalRange(int b, int positionInPairB) {
		int positionInPairMaxValue = 1;
		int positionInPairMinValue = 0;
		double range;
		ArrayList<Double> rangeB = pairsList.get(positionInPairB);
		double[] pairB = new double[2];
		pairB[0] = b;
		pairB[1] = rangeB.get(positionInPairMinValue);
		range = pairB[0] - pairB[1];
		double scoreB = calculateNumtuplesPerBin(rangeB, positionInPairB, positionInPairMaxValue,
				positionInPairMinValue, range);
		return scoreB;
	}

	private double recordsForAIfInsideTotalRange(int a, int positionInPairA) {
		int positionInPairMaxValue = 1;
		int positionInPairMinValue = 0;
		ArrayList<Double> rangeA = pairsList.get(positionInPairA);
		double[] pairA = new double[2];
		pairA[0] = a;
		pairA[1] = rangeA.get(positionInPairMaxValue);
		double range = pairA[1] - pairA[0];
		double scoreA = calculateNumtuplesPerBin(rangeA, positionInPairA, positionInPairMaxValue,
				positionInPairMinValue, range);
		return scoreA;
	}

	private double calculateInsideRecors(int positionInPairA, int positionInPairB, double numberOfRecords) {
		int firstBinInside = positionInPairA + 1;
		int lastBinInside = positionInPairB - 1;
		while (firstBinInside <= lastBinInside) {
			numberOfRecords = numberOfRecords + ScoreInPair[firstBinInside];
			firstBinInside++;
		}
		return numberOfRecords;
	}

	public int actulResults(int a, int b) {
		int sum = 0;
		for (Income in : incomeManager.getListIncome()) {
			if (in.getIncomeValue() >= a && in.getIncomeValue() < b) {
				sum++;
			}
		}
		System.out.println("Actual is " + sum);
		return sum;
	}

}
