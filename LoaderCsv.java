import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class LoaderCsv {
	//lampros vlachpoulos , AM 2948
	private File readFile;
	private IncomeManager incomeManager;

	public LoaderCsv(String pathFile) {
		super();
		this.readFile = new File(pathFile);
		this.incomeManager = new IncomeManager();
	}

	public void load(char delimiter) throws IOException {
		String path = readFile.getAbsolutePath();
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader br = new BufferedReader(fileReader);
			String line = br.readLine();
			int foundFirstTimeDelimiter = 0;
			int positionField = findPositionIncomeValues(line, null, delimiter, foundFirstTimeDelimiter);

			while (line != null) {
				line = br.readLine();
				if (line == null) {
					break;
				}
				String dataPerLine = line;
				ArrayList<String> tokensPerLine = new ArrayList<String>();
				foundFirstTimeDelimiter = 0;
				splitLineWithDelimiter(dataPerLine, tokensPerLine, delimiter, foundFirstTimeDelimiter);
				String valueField = tokensPerLine.get(positionField);
				Income income = new Income(valueField);
				incomeManager.addIncome(income);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private int findPositionIncomeValues(String dataPerLine, ArrayList<String> listWithTokensPerLine, char delimiter,
			int foundFirstTimeDelimiter) {
		ArrayList<String> listFeilds = new ArrayList<String>();

		splitLineWithDelimiter(dataPerLine, listFeilds, delimiter, foundFirstTimeDelimiter);
		String field = "Income";
		int positionField = listFeilds.indexOf(field);
		return positionField;
	}

	private void splitLineWithDelimiter(String dataPerLine, ArrayList<String> listWithTokensPerLine, char delimiter,
			int foundFirstTimeDelimiter) {

		String token = "";
		for (int i = 0; i < dataPerLine.length(); i++) {
			if (dataPerLine.charAt(i) != delimiter) {
				if (i + 1 == dataPerLine.length()) {
					token = token + dataPerLine.charAt(i);
					listWithTokensPerLine.add(token);
					foundFirstTimeDelimiter = 0;
					break;
				}
				token = token + dataPerLine.charAt(i);
				foundFirstTimeDelimiter = 0;
			} else if (dataPerLine.charAt(i) == delimiter) {
				if (foundFirstTimeDelimiter == 1) {
					if (i == dataPerLine.length() - 1) {
						token = "" + dataPerLine.charAt(i);
						if (token.equals(",")) {
							listWithTokensPerLine.add("Not Info");
						} else {
							listWithTokensPerLine.add(token);
						}
					}
					listWithTokensPerLine.add("Not Info");
					token = "";
				}
				if (foundFirstTimeDelimiter % 2 == 0) {
					foundFirstTimeDelimiter = 1;
					listWithTokensPerLine.add(token);
					token = "";
				}
			}
		}
	}

	public IncomeManager getIncomeManager() {
		return incomeManager;
	}
}
