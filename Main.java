import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {
	//lampros vlachpoulos , AM 2948
	static ArrayList<HashMap<Integer, Integer>> allmaps =new ArrayList<>();
	public static void main(String args[]) throws IOException {

		String pathFile = "acs2015_census_tract_data.csv";
		LoaderCsv loader = new LoaderCsv(pathFile);
		char delimiter = ',';
		loader.load(delimiter);
		IncomeManager m = loader.getIncomeManager();
		m.sortIncomeValues();
		System.out.println(m.toString());
		int bins = 100;
		EquiWidth eq = new EquiWidth(m, bins);
		EquiDepth eqD = new EquiDepth(m, bins);
		System.out.println(eq.toString());
		System.out.println(eqD.toString());
		int a = 19000;
		int b = 55000;
		eq.estimate(a, b);
		eqD.estimate(a, b);
		eqD.actulResults(a, b);

		//calculateError(eq, eqD);

	}

	private static void calculateError(EquiWidth eq, EquiDepth eqD) {
		createMapBasicValuesForAB();
		int sumWidthBetter = 0;
		int sumDepthBetter = 0;
		int equal = 0;
		for (HashMap<Integer, Integer> map : allmaps) {
			//System.out.println(map);
			
			for (Entry<Integer, Integer> entry : map.entrySet()) {

				HashMap<Integer, Integer> pairs = createRandomRanges(entry);

				for (Entry<Integer, Integer> pair : pairs.entrySet()) {
					Integer key = pair.getKey();
					Integer value = pair.getValue();
					double estimateWIdth = eq.estimate(key, value);
					double estimateDepgth = eqD.estimate(key, value);
					int actual = eq.actulResults(key, value);

					double errorWidth = actual - estimateWIdth;
					double errorDepth = actual - estimateDepgth;

					if (errorWidth < 0) {
						errorWidth = errorWidth * (-1);
					}
					if (errorDepth < 0) {
						errorDepth = errorDepth * (-1);
					}
					if (errorWidth < errorDepth) {
						sumWidthBetter++;
					}
					if (errorWidth > errorDepth) {
						sumDepthBetter++;
					}
					if (errorWidth == errorDepth) {
						equal++;
					}

				}

			}
		}

		System.out.println("##########################\nFinal Results:\nEquiWidthBetter: " + sumWidthBetter + "\n"
				+ "EquiDepth: " + sumDepthBetter + "\nEqual: " + equal);

	}

	private static HashMap<Integer, Integer> createRandomRanges(Entry<Integer, Integer> entry) {
		int min = entry.getKey();
		int max = entry.getValue();

		int numOfRandomPairsAB = 500;
		SecureRandom random = new SecureRandom();
		HashMap<Integer, Integer> pairs = new HashMap<>();
		pairs.put(min, max);
		int standarApostash = 1000; // to random edine se mikro range a,b
		while (pairs.size() < numOfRandomPairsAB) {
			int randomNumber1 = random.nextInt(max - min + 1) + min;
			int randomNumber2 = standarApostash + random.nextInt(max - randomNumber1 + 1) + randomNumber1;
			pairs.put(randomNumber1, randomNumber2);

		}
		return pairs;
	}

	private static void createMapBasicValuesForAB() {
		HashMap<Integer, Integer> limits1 = new HashMap<>();
		HashMap<Integer, Integer> limits2 = new HashMap<>();
		HashMap<Integer, Integer> limits3 = new HashMap<>();
		HashMap<Integer, Integer> limits4 = new HashMap<>();
		HashMap<Integer, Integer> limits5 = new HashMap<>();
		HashMap<Integer, Integer> limits6 = new HashMap<>();
		HashMap<Integer, Integer> limits7 = new HashMap<>();
		HashMap<Integer, Integer> limits8 = new HashMap<>();
		HashMap<Integer, Integer> limits9 = new HashMap<>();
		HashMap<Integer, Integer> limits10 = new HashMap<>();
		HashMap<Integer, Integer> limits11 = new HashMap<>();
		HashMap<Integer, Integer> limits = new HashMap<>();
		
		createRangeAbPerNumber(limits1, 500);
		createRangeAbPerNumber(limits2,1000);
		createRangeAbPerNumber(limits3, 2500);
		createRangeAbPerNumber(limits4, 5000);
		createRangeAbPerNumber(limits5, 10000);
		createRangeAbPerNumber(limits6, 20000);
		createRangeAbPerNumber(limits7, 30000);
		createRangeAbPerNumber(limits8, 50000);
		createRangeAbPerNumber(limits9, 80000);
		createRangeAbPerNumber(limits10, 100000);
		createRangeAbPerNumber(limits11, 150000);
		
		limits.put(2611, 248750);
		limits.put(5000, 100000);
		limits.put(128000, 200000);
		limits.put(154365, 206545);
		limits.put(200000, 240000);
		limits.put(87239, 189000);
		
		allmaps.add(limits1);
		allmaps.add(limits2);
		allmaps.add(limits3);
		allmaps.add(limits4);
		allmaps.add(limits5);
		allmaps.add(limits6);
		allmaps.add(limits7);
		allmaps.add(limits8);
		allmaps.add(limits9);
		allmaps.add(limits10);
		allmaps.add(limits11);
		allmaps.add(limits);

	}

	private static void createRangeAbPerNumber(HashMap<Integer, Integer>limits, int num) {
		int i = 1;
		while (num * (i + 1) <= 248750) {
			limits.put(num * i, num * (i + 1));
			i++;
		}
	}
}
