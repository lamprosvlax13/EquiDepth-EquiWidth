
class Income {
	//lampros vlachpoulos , AM 2948
	private double incomeValue;

	static final Double errorCode = -100.0;

	public Income(String incomeValue) {

		if (incomeValue.equals("Not Info")) {
			this.incomeValue = errorCode;
		} else {
			this.incomeValue = Double.parseDouble(incomeValue);
		}

	}

	public double getIncomeValue() {
		return incomeValue;
	}
}