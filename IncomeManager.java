import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IncomeManager {
	//lampros vlachpoulos , AM 2948
	private ArrayList<Income> listIncomes;

	public IncomeManager() {
		this.listIncomes = new ArrayList<Income>();

	}

	public void addIncome(Income income) {
		if (income.getIncomeValue() != income.errorCode) {
			this.listIncomes.add(income);
		}
		// System.out.println("Incomes me errorCode den ginontai insert sth lista ");

	}

	public void sortIncomeValues() {
		Collections.sort(this.listIncomes, new Comparator<Income>() {
			@Override
			public int compare(Income c1, Income c2) {
				return Double.compare(c1.getIncomeValue(), c2.getIncomeValue());
			}
		});

	}

	public Income getMaxValueIncome() {
		int lastPositionInList = this.listIncomes.size() - 1;
		Income income = this.listIncomes.get(lastPositionInList);
		return income;
	}

	public Income getMinValueIncome() {
		int firtstPositionInList = 0;
		Income income = this.listIncomes.get(firtstPositionInList);
		return income;
	}

	public int getIncomeValuesLoaded() {

		return this.listIncomes.size();
	}

	public ArrayList<Income> getListIncome() {

		return this.listIncomes;
	}

	public String toString() {

		String result = getIncomeValuesLoaded() + " valid income values\n" + "minimum income = "
				+ getMinValueIncome().getIncomeValue() + " maximum income = " + getMaxValueIncome().getIncomeValue()
				+ "\n";
		return result;
	}

}
