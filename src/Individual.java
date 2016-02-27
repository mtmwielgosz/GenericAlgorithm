import java.util.Random;

public class Individual {

	private int number = 8;
	private int[] chromosomes = new int[number];
	private double grade;
	
	public Individual(boolean random)
	{
		if(random)
			for(int i = 0; i < number; i++)
			{
				Random ran = new Random();
				chromosomes[i] = ran.nextInt(10);
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
	
	public int getNumOfChromosomes()
	{
		return number;
	}

}
