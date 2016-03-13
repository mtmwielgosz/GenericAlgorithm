import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Individual {

	private int nrOfChromosomes;
	private int nrOfTypes;
	private int[] chromosomes;
	private double grade;
	private Random ran = new Random();
	private int baads = 0;
	
	public Individual(int numberOfChromosomesForEachIndividual, int numberOfTypesOfChromosomes, boolean random)
	{
		nrOfChromosomes = numberOfChromosomesForEachIndividual;
		nrOfTypes = numberOfTypesOfChromosomes;
		chromosomes = new int[nrOfChromosomes];
		
		if(random)
			for(int i = 0; i < nrOfChromosomes; i++)
			{
				chromosomes[i] = ran.nextInt(nrOfTypes);
			}
	}
	
	public int get(int i)
	{
		return chromosomes[i];
	}
	
	public void setChromosomes(int[] chromosomes) throws Exception
	{
		if(chromosomes.length == this.chromosomes.length)
			this.chromosomes = chromosomes;
		else
			throw new Exception("Invalide number of chromosomes!");
	}

	public double getGrade() 
	{
		return grade;
	}

	public void setGrade(double grade) 
	{
		this.grade = grade;
	}
	
	public int getNrOfUniqueColors()
	{
		Set<Integer> colors = new HashSet<Integer>();
		
		for(int i = 0; i < nrOfChromosomes; i++)
		{
			colors.add(chromosomes[i]);
		}
		
		return colors.size();
	}

	public int getBaads() {
		return baads;
	}

	public void setBaads(int baads) {
		this.baads = baads;
	}
}
