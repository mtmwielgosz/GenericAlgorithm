import java.util.Random;

public class Population {
	
	private int number;
	private Individual[] wholePopulation;
	private int accIndex = 0;
	private Random ran = new Random();
	private double total = 0;
	private double[] rouletteWheel;
	
	public Population(int number, boolean randomCreate)
	{
		this.number = number;
		wholePopulation = new Individual[number];
		rouletteWheel = new double[number+1];
		
		if(randomCreate)
			for(int i = 0; i < number; i++)
			{
				wholePopulation[i] = new Individual(true);
				accIndex ++;
			}
	}
	
	public void add(Individual person)
	{
		if(accIndex < number)
		{
			wholePopulation[accIndex] = person;
			accIndex++;
		}
		else
			throw new IndexOutOfBoundsException("The population is outnumbered!");
	}
	
	public void insert(Individual person, int n)
	{
		if(n < number && accIndex < number)
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

		System.out.println("I am in selection, let me roullete ppl :)");
		Population nextGen = new Population(number, false);
		
// ----- start roulette wheel algorithm is used here
	
        rouletteWheel[0] = 0;
        for (int i = 0; i < number; i++) 
        {
        	rouletteWheel[i+1] = rouletteWheel[i] + wholePopulation[i].getGrade();
            total += wholePopulation[i].getGrade();
        }
        
        for(int i = 0; i < number; i++)
        {
        	nextGen.add(wholePopulation[spin()]);
        }
        
// ----- end roulette wheel algorithm
        
// ----- start bandwidth coloring algorithm - two people who will be crossed-over can't have same 1st and last chromosome 
        
		System.out.println("I am coloring graph...");
        Population afterBandwith = new Population(number, false);
        afterBandwith.add(nextGen.wholePopulation[0]);
        boolean[] assigned = new boolean[number];
        assigned[0] = true;
        int numberOfAssigned = 1;
        
        while(numberOfAssigned < number)
        {
        	for(int i = 1; i < number; i++)
        	{
        		for(int j = 0; j < numberOfAssigned && !assigned[i]; j++)
        		{
        			if(afterBandwith.wholePopulation[j+1] != null) 
        				if(afterBandwith.wholePopulation[j+1].get(0) == nextGen.wholePopulation[i].get(0))
        				{
        					System.out.println("chuj");
        					continue;
        				}
        			if(afterBandwith.wholePopulation[j].get(0) != nextGen.wholePopulation[i].get(0))
        			{
        				afterBandwith.insert(nextGen.wholePopulation[i], j+1);
        				assigned[i] = true;
        				numberOfAssigned ++;
        				System.out.println("Assigned:" + assigned[i] + ", " + i + ". from nextGen to " + j+1 +" afterBandwidth");
        			}
        			System.out.println("here1");
        		}

        	}
        }
        
        return afterBandwith;
	}
	
    private int spin() 
    {
        double r = ran.nextDouble() * total;
        int startScope = 0;
        int finishScope = number - 1;
        
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
		System.out.println("I am in crossover now...");
		Population nextGen = new Population(number, false);
		for(int i = 0; i < number; i = i+2)
		{
			Individual parent1 = wholePopulation[i];
			Individual parent2 = wholePopulation[i+1];
			Individual child1 = new Individual(false);
			Individual child2 = new Individual(false);
			
			int[] forChild1 = new int[parent1.getNumOfChromosomes()];
			int[] forChild2 = new int[parent1.getNumOfChromosomes()];
			for(int j = 0; j < parent1.getNumOfChromosomes(); j++)
			{
				forChild1[j] = yes(posForFirstParent) ? parent1.get(0) : parent2.get(0);
				forChild2[j] = yes(posForFirstParent) ? parent1.get(0) : parent2.get(0);
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
		System.out.println("I am in mutation now...");
		Population nextGen = new Population(number, false);
		for(int i = 0; i < number; i++)
		{
			Individual parent = wholePopulation[i];
			Individual child = new Individual(false);
			int[] forChild = new int[parent.getNumOfChromosomes()];
			
			if(yes(posToMutate))
			{
				for(int j = 0; j < parent.getNumOfChromosomes(); j++)
					if(yes(posForEachChrom))
						forChild[j] = (parent.get(j) + ran.nextInt(10)) % 9;
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
	
}
