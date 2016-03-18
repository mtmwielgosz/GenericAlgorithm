import java.util.Random;

public class Population {
	
	private int numberOfIndividuals;
	private int nrOfChromosomes;
	private int nrOfTypes;
	private Individual[] wholePopulation;
	private int accIndex = 0;
	private Random ran = new Random();
	private double total = 0;
	private double[] rouletteWheel;
	
	public Population(int numberOfIndividuals, int numberOfChromosomesForEachIndividual, int numberOfTypesOfChromosomes, boolean randomCreate)
	{
		this.numberOfIndividuals = numberOfIndividuals;
		nrOfChromosomes = numberOfChromosomesForEachIndividual;
		nrOfTypes = numberOfTypesOfChromosomes;
		wholePopulation = new Individual[numberOfIndividuals];
		rouletteWheel = new double[numberOfIndividuals+1];
		
		if(randomCreate)
			for(int i = 0; i < numberOfIndividuals; i++)
			{
				wholePopulation[i] = new Individual(nrOfChromosomes, nrOfTypes, true);
				accIndex ++;
			}
	}
	
	public void add(Individual person)
	{
		if(accIndex < numberOfIndividuals)
		{
			wholePopulation[accIndex] = person;
			accIndex++;
		}
		else
			throw new IndexOutOfBoundsException("The population is outnumbered!");
	}
	
	public void insert(Individual person, int n)
	{
		if(n < numberOfIndividuals && accIndex < numberOfIndividuals)
		{
			for(int i = accIndex; i > n; i--)
			{
				wholePopulation[i] = wholePopulation[i-1];
			}
			wholePopulation[n] = person;
			accIndex++;
		}
		else
		{
			throw new IndexOutOfBoundsException("The population is outnumbered!");
		}
	}
	
	public Population selection() 
	{		
		Population nextGen = new Population(numberOfIndividuals, nrOfChromosomes, nrOfTypes, false);
		
////		System.out.println("Before sort:");
////		showPop(this);
//		
//		sort(wholePopulation, 0, wholePopulation.length - 1);
//		Individual temp;
//		for(int i = 0; i < wholePopulation.length/2; i++)
//		{
//			temp = wholePopulation[i];
//			wholePopulation[i] = wholePopulation[wholePopulation.length - i - 1];
//			wholePopulation[wholePopulation.length - i - 1] = temp;
//
//		}
//		
////		System.out.println("\nAfter sort:");
////		showPop(this);
//		
//		int len = wholePopulation.length;
//		int howManyDone = 0;
//		int border = len/4;
//		for(int i = 0; i < border; i++)
//		{
//			nextGen.add(wholePopulation[i]);
//			nextGen.add(wholePopulation[i]);
//			nextGen.add(wholePopulation[i]);
//			howManyDone += 3;
//		}
//		
//		while(howManyDone < len)
//		{
//			nextGen.add(wholePopulation[border]);
//			border ++;
//			howManyDone ++;
//		}
//		
////		System.out.println("\nbEFORE mix:");
////		showPop(nextGen);
//		mix(nextGen.wholePopulation);
////		System.out.println("\nAfter :");
////		showPop(nextGen);
			
	
		
// ----- start roulette wheel algorithm 
	
//		System.out.println("I am in selection, let me roullete ppl :)");
        rouletteWheel[0] = 0;
        for (int i = 0; i < numberOfIndividuals; i++) 
        {
        	rouletteWheel[i+1] = rouletteWheel[i] + wholePopulation[i].getGrade();
            total += wholePopulation[i].getGrade();
        }
        
        for(int i = 0; i < numberOfIndividuals; i++)
        {
        	int result = spin();
	        Individual ind = wholePopulation[result];
	 //     System.out.println("I've chosen person with grade: " + ind.getGrade());
	        nextGen.add(ind);
        }
// ----- end roulette wheel algorithm
       
        return nextGen;
	}
	
	private void sort(Individual[] p, int x, int y)
	{
//		private static void quicksort(int tablica[], int x, int y) {
		//			x = 0;
		//y = p.length - 1;
			 
			int i, j;
			double v; 
			Individual temp;
			 

			i = x;
			j = y;
			v = p[(x+y) / 2].getGrade();
			do {
				while (p[i].getGrade() < v)
					i++;
				while (v < p[j].getGrade())
					j--;
				if (i<=j) 
				{
					temp=p[i];
					p[i]=p[j];
					p[j]=temp;
					i++;
					j--;
				}
			}
			while (i<=j);
				if (x<j)
					sort(p,x,j);
				if (i<y)
					sort(p,i,y);
				
				
			
	}
	
	private void mix(Individual[] p)
	{
		Individual temp;
		for(int i = 0; i < p.length; i++)
		{
			int newPos = ran.nextInt(p.length);
			int newPos2 = ran.nextInt(p.length);
			temp = p[newPos];
			p[newPos] = p[newPos2];
			p[newPos2] = temp;
		}
	}
	
    private int spin() 
    {
        double r = ran.nextDouble() * total;
        int startScope = 0;
        int finishScope = numberOfIndividuals - 1;
        
        while (finishScope - startScope > 1) // Quick search algorithm is used here
        {
            int middle = (startScope + finishScope) / 2;
            if (rouletteWheel[middle] > r) 
            	finishScope = middle;
            else
            	startScope = middle;
        }
        return startScope;
    }
	
	public Population crossover(double posForFirstParent) throws Exception
	{
	//	System.out.println("I am in crossover now...");
		Population nextGen = new Population(numberOfIndividuals, nrOfChromosomes, nrOfTypes, false);
		for(int i = 0; i < numberOfIndividuals; i = i+2)
		{
			Individual parent1 = wholePopulation[i];
			Individual parent2 = wholePopulation[i+1];
			Individual child1 = new Individual(nrOfChromosomes, nrOfTypes, false);
			Individual child2 = new Individual(nrOfChromosomes, nrOfTypes, false);
			
			int[] forChild1 = new int[nrOfChromosomes];
			int[] forChild2 = new int[nrOfChromosomes];
			for(int j = 0; j < nrOfChromosomes; j++)
			{
				forChild1[j] = posForFirstParent > ran.nextInt() ? parent1.get(j) : parent2.get(j);
				forChild2[j] = posForFirstParent > ran.nextInt() ? parent1.get(j) : parent2.get(j);
			}
			
			child1.setChromosomes(forChild1);
			nextGen.add(child1);
			child2.setChromosomes(forChild2);
			nextGen.add(child2);
		}
		
		return nextGen;
	}
	
	public Population mutation(double posToMutate, double posForEachChrom) throws Exception
	{
	//	System.out.println("I am in mutation now...");
		Population nextGen = new Population(numberOfIndividuals, nrOfChromosomes, nrOfTypes, false);
		for(int i = 0; i < numberOfIndividuals; i++)
		{
			Individual parent = wholePopulation[i];
			Individual child = new Individual(nrOfChromosomes, nrOfTypes, false);
			int[] forChild = new int[nrOfChromosomes];
			
			if(yes(posToMutate))
			{
				for(int j = 0; j < nrOfChromosomes; j++)
					if(yes(posForEachChrom))
						forChild[j] = (parent.get(j) + ran.nextInt(nrOfTypes ) % (nrOfTypes - 1));
					else
						forChild[j] = parent.get(j);
				child.setChromosomes(forChild);
			}
			else
				child = parent;
			
			nextGen.add(child);
		}
		
		return nextGen;
	}
	
	private boolean yes(double possibility)
	{
		return possibility >= ran.nextDouble();
	}
	
	public Individual get(int i)
	{
		return wholePopulation[i];
	}
	
	public void showPop(Population p)
	{
		for(int i = 0; i < numberOfIndividuals; i++)
		{
	//		System.out.print(p.get(i).getGrade() + " ");
		}
	}
}
