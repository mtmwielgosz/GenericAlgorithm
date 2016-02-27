public class Main {

	static boolean stopCondition = false;
	static double minGrade = 45;
	static int maxGenerations = 10;
	static int numberOfPopulation = 1000; 		// number of population must be even
	static double posForFirstParent = 0.6; 		// >
	static double posToMutate = 0.8;			// possibilities have to be between 0 and 1
	static double posForEachChrom = 0.3;		// <
	static int basicOfPower = 2; 				
	static int runIndex = 0;
	
	public static void main(String[] args) throws Exception 
	{	
		Population[] pop = new Population[maxGenerations];
		pop[runIndex] = new Population(numberOfPopulation, true);
		evaluate(pop[runIndex]);
		
		while(++runIndex < maxGenerations && !stopCondition)
		{
			pop[runIndex] = pop[runIndex - 1].selection().crossover(posForFirstParent).mutation(posToMutate, posForEachChrom);
			evaluate(pop[runIndex]);
		}
	}
	
	public static void evaluate(Population p)
	{
		System.out.println("I am evaluating run " + runIndex + "..."); 
		int sum = 0;
		double wholeSum = 0;
		for(int i = 0; i < numberOfPopulation; i++)
		{
			for(int j = 0; j < p.get(i).getNumOfChromosomes(); j++)
				sum += p.get(i).get(j);
			
			p.get(i).setGrade(basicOfPower ^ sum); 
			wholeSum += sum;
			sum = 0;
		}
		
		stopCondition = wholeSum / numberOfPopulation > minGrade;
		
		System.out.println("Run " + runIndex + ": " + (wholeSum / numberOfPopulation) + " > " + minGrade);
		
	}

}
