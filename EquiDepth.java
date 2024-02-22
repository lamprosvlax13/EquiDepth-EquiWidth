import java.util.ArrayList;


//lampros vlachpoulos , AM 2948
public class EquiDepth {

	private IncomeManager incomeManager;
	private int bins;
	private double maximumValueIncome;
	private double minimumValueIncome;
	private int numberOfIncomeValuesPerBin;
	private ArrayList<ArrayList<Double>> pairsList;
	private int[] ScoreInPair;
	private double estimetedResult;
	// private double binSize;

	public EquiDepth(IncomeManager incomeManager, int bins) {
		super();
		this.incomeManager = incomeManager;
		this.bins = bins;
		this.maximumValueIncome = 0;
		this.minimumValueIncome = 0;

		this.pairsList = new ArrayList<ArrayList<Double>>();
		this.ScoreInPair = new int[this.bins];
		this.estimetedResult = 0;
		calculatePairs();
		findValuesInPair();
	}

	public void calculatePairs() {

		initializeMaxValueIncome();
		initializeMinValueIncome();
		ArrayList<Income> listIncomeValues = this.incomeManager.getListIncome();
		int sumOfIncomeValues = listIncomeValues.size();
		initializeNuberOfIncomeValuesPerBin(sumOfIncomeValues);
		this.pairsList = new ArrayList<ArrayList<Double>>();
		int currentBin = 1;
		int position = 1;
		double minValuePair = this.minimumValueIncome;
		int maxValuePairPosition = this.numberOfIncomeValuesPerBin * position;
		double maxValuePair = listIncomeValues.get(maxValuePairPosition - 1).getIncomeValue(); // -1 ###########EDWWWWW

		while (currentBin < this.bins) {
			maxValuePair = createPair(listIncomeValues, position, minValuePair);
			minValuePair = maxValuePair;
			currentBin++;
			position++;
		}

		if (pairsList.size() < this.bins) {
			ArrayList<Double> pair = new ArrayList<Double>();
			maxValuePair = this.maximumValueIncome;
			pair.add(minValuePair);
			pair.add(maxValuePair);
			this.pairsList.add(pair);

		}

		/*
		 * // paralagh gia na xanw to teleutaio if (pairsList.size() < this.bins) {
		 * ArrayList<Double> pair = new ArrayList<Double>(); maxValuePair =
		 * this.maximumValueIncome; pair.add(minValuePair);
		 * pair.add(listIncomeValues.get((bins*numberOfIncomeValuesPerBin)
		 * -1).getIncomeValue()); this.pairsList.add(pair); }
		 */
	}

	private double createPair(ArrayList<Income> listIncomeValues, int position, double minValuePair) {
		int maxValuePairPosition;
		double maxValuePair;
		ArrayList<Double> pair = new ArrayList<Double>();
		maxValuePairPosition = (this.numberOfIncomeValuesPerBin * position);

		maxValuePair = listIncomeValues.get(maxValuePairPosition).getIncomeValue();

		pair.add(minValuePair);
		pair.add(maxValuePair);
		pairsList.add(pair);
		return maxValuePair;
	}

	public void findValuesInPair() {

		this.ScoreInPair = new int[this.bins];
		for (int i = 0; i < this.ScoreInPair.length; i++) {
			ScoreInPair[i] = this.numberOfIncomeValuesPerBin;
			if (i == ScoreInPair.length - 1) {
				ScoreInPair[i] = incomeManager.getListIncome().size() - (this.bins - 1) * numberOfIncomeValuesPerBin;
			}
		}
	}

	private void initializeNuberOfIncomeValuesPerBin(int sizeOfIncomeValues) {
		this.numberOfIncomeValuesPerBin = sizeOfIncomeValues / this.bins;
	}

	private void initializeMinValueIncome() {
		this.minimumValueIncome = this.incomeManager.getMinValueIncome().getIncomeValue();
	}

	private void initializeMaxValueIncome() {
		this.maximumValueIncome = this.incomeManager.getMaxValueIncome().getIncomeValue();
	}

	public String toString() {
		String result = "equidepth:\n";
		int i = 0;
		for (ArrayList<Double> pair : pairsList) {
			result = result + i + "range: [" + pair.get(0) + "," + pair.get(1) + "), numtuples: " + this.ScoreInPair[i]
					+ "\n";
			i++;
		}
		return result;

	}

	public double estimate(int a, int b) {

		ArrayList<Integer> indexesOfPair = new ArrayList<Integer>();
		int positionPair = 0;
		double pososto = 0;
		double pleiades = 0;
		int positionFirstPair = 0;
		int positionLastPair = pairsList.size() - 1;
		ArrayList<Double> firstPair = pairsList.get(positionFirstPair);
		ArrayList<Double> LastPair = pairsList.get(positionLastPair);
		int leftValuePositionFirstPair = 0;
		int rigthValuePositionLastPair = 1;
		// e3w to A
		if (a < firstPair.get(leftValuePositionFirstPair) && b < LastPair.get(rigthValuePositionLastPair)
				&& b >= firstPair.get(leftValuePositionFirstPair)) {

			int positionPairWhoHaveInsideB = binarySearch(b);
			positionPair = -1;
			indexesOfPair.add(positionPair);
			pleiades = calculateRecordsForB(b, indexesOfPair, pleiades, positionPairWhoHaveInsideB);
			pleiades = calculateInsideRecords(indexesOfPair, pleiades);
			this.estimetedResult = pleiades;
			System.out.println("equidepth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
			// return pleiades;

		}
		// kai ta duo mesa
		if (a >= firstPair.get(leftValuePositionFirstPair) && a < LastPair.get(rigthValuePositionLastPair)
				&& b < LastPair.get(rigthValuePositionLastPair) && b >= firstPair.get(leftValuePositionFirstPair)) {

			int positionPairWhoHaveInsideA = binarySearch(a);
			int positionPairWhoHaveInsideB = binarySearch(b);
			;

			if (positionPairWhoHaveInsideA == positionPairWhoHaveInsideB) {

				ArrayList<Double> rangeA = pairsList.get(positionPairWhoHaveInsideA);
				pososto = (b - a) / (rangeA.get(1) - rangeA.get(0));
				pleiades = (pososto * ScoreInPair[positionPairWhoHaveInsideA]);
				this.estimetedResult = pleiades;
				System.out.println("equidepth estimated results: " + this.estimetedResult);
				return this.estimetedResult;

			}

			pleiades = calculateRecordsForA(a, indexesOfPair, pleiades, positionPairWhoHaveInsideA);
			pleiades = calculateRecordsForB(b, indexesOfPair, pleiades, positionPairWhoHaveInsideB);
			pleiades = calculateInsideRecords(indexesOfPair, pleiades);

			this.estimetedResult = pleiades;
			System.out.println("equidepth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
		}
		// e3w to B
		if (a >= firstPair.get(leftValuePositionFirstPair) && a <= LastPair.get(rigthValuePositionLastPair)
				&& b >= LastPair.get(rigthValuePositionLastPair)) {

			int positionPairWhoHaveInsideA = binarySearch(a);

			pleiades = calculateRecordsForA(a, indexesOfPair, pleiades, positionPairWhoHaveInsideA);
			positionPair = pairsList.size();
			indexesOfPair.add(positionPair);
			pleiades = calculateInsideRecords(indexesOfPair, pleiades);
			this.estimetedResult = pleiades;
			System.out.println("equidepth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
		}
		if (a < firstPair.get(leftValuePositionFirstPair) && b > LastPair.get(rigthValuePositionLastPair)) {
			indexesOfPair.add(-1);
			indexesOfPair.add(100);
			pleiades = calculateInsideRecords(indexesOfPair, pleiades);
			this.estimetedResult = pleiades;
			System.out.println("equidepth estimated results: " + this.estimetedResult);
			return this.estimetedResult;
		}

		System.out.println("equidepth estimated results: " + this.estimetedResult);
		return this.estimetedResult;
	}

	private double calculateRecordsForA(int a, ArrayList<Integer> indexesOfPair, double pleiades, int positionPair) {

		double pososto;
		indexesOfPair.add(positionPair);
		ArrayList<Double> pairWhoHaveInsideA = pairsList.get(positionPair);
		pososto = (pairWhoHaveInsideA.get(1) - a) / (pairWhoHaveInsideA.get(1) - pairWhoHaveInsideA.get(0));
		pleiades = pleiades + (pososto * ScoreInPair[positionPair]);
		return pleiades;
	}

	private double calculateRecordsForB(int b, ArrayList<Integer> indexesOfPair, double pleiades, int positionPair) {

		double pososto;
		indexesOfPair.add(positionPair);
		ArrayList<Double> pairWhoHaveInsideB = pairsList.get(positionPair);
		pososto = (b - pairWhoHaveInsideB.get(0)) / (pairWhoHaveInsideB.get(1) - pairWhoHaveInsideB.get(0));
		pleiades = pleiades + (pososto * ScoreInPair[positionPair]);
		return pleiades;
	}

	private double calculateInsideRecords(ArrayList<Integer> indexesOfPair, double pleiades) {
		int positionMinimumPair = indexesOfPair.get(0) + 1;
		int positionMaximumPair = indexesOfPair.get(1) - 1;
		while (positionMinimumPair <= positionMaximumPair) {

			pleiades = pleiades + ScoreInPair[positionMinimumPair];
			positionMinimumPair++;
		}

		return pleiades;

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

	public int binarySearch(int value) {
		int left = 0;
		int right = pairsList.size() - 1;
		int mid = 0;
		while (left <= right) {
			mid = left + (right - left) / 2;
			ArrayList<Double> pairMid = pairsList.get(mid);

			if (pairMid.get(0) <= value && value < pairMid.get(1)) { // svhse to = sto pairMid.get(1) an xreiastei. !
				return mid;
			} else if (pairMid.get(0) < value) { /// 1 htan
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}

		return mid;
	}

}
