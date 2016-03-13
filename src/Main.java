import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Main {

	static boolean stopCondition = false;
	static int maxGenerations = 1000;
	static int numberOfPopulation = 800; 		// number of population must be even
	static double posForFirstParent = 0.6; 		// >
//	static double posToMutate = 0.1;			// possibilities have to be between 0 and 1
//	static double posForEachChrom = 0.01;		// <
//	static double posToMutate = 0.1;			// possibilities have to be between 0 and 1
//	static double posForEachChrom = 0.02;		// <
//	static double posToMutate = 0.3;			// possibilities have to be between 0 and 1
//	static double posForEachChrom = 0.025;		// <
	static double posToMutate = 0.5;			// possibilities have to be between 0 and 1
	static double posForEachChrom = 0.05;		// <
	static int numberOfChromosomesForEachIndividual = 120;
	static int numberOfTypesOfChromosomes = 110;
	static int numberOfPeaks = numberOfChromosomesForEachIndividual;
	static int[][] graph = new int[numberOfPeaks][numberOfPeaks];
	static int valueOfGrade = 2;
	static int punishment = numberOfTypesOfChromosomes / 10;
	static int runIndex = 0;
	static double minGrade = 45;
	static Random ran = new Random();
	static Individual bestOfTheBests;
	static String parametersInfo = "index;ocenaSrednia;maxOcena;kolory;zakres;generacji: " + maxGenerations + ";liczba populacji: " + numberOfPopulation + 
			";prawd mutacji: " + posToMutate * posForEachChrom + ";prawd krzyzowania: " + posForFirstParent;
	static String[] eachGenInfo = new String[maxGenerations];
	static String[] offEachGenInfo = new String[maxGenerations];
//	static String[] bestInfo = new String[maxGenerations];
	
	public static void main(String[] args) throws Exception 
	{	
//		graph[0][1] = 3;
//		graph[1][2] = 5;
//		graph[1][3] = 2;
//		graph[2][3] = 4;
//		graph[3][4] = 4;
//		graph[1][5] = 2;
		
		getGraph("D:\\Files\\GEOM" + numberOfChromosomesForEachIndividual + ".txt", numberOfPeaks);
		
		Long now = (new Date()).getTime();
		while(!stopCondition)
		{
			Population[] pop = new Population[maxGenerations];
			pop[runIndex] = new Population(numberOfPopulation, numberOfChromosomesForEachIndividual, numberOfTypesOfChromosomes, true);
			evaluate(pop[runIndex]);

			stopCondition = true;
			while(++runIndex < maxGenerations)
			{
				pop[runIndex] = pop[runIndex - 1].selection().crossover(posForFirstParent).mutation(posToMutate, posForEachChrom);
				evaluate(pop[runIndex]);
			}
			if(bestOfTheBests.getBaads() == 0)
			{
				stopCondition = false;
				offEachGenInfo = eachGenInfo.clone();
			}
			bestOfTheBests = null;
			numberOfTypesOfChromosomes --;
			runIndex = 0;
		}
		Long then = (new Date()).getTime();
		toFile("GEM"+ numberOfChromosomesForEachIndividual + "_pop" + numberOfPopulation + "_posMut" + posToMutate * posForEachChrom + "_posCross" + posForFirstParent + "_DOUBLED.csv", (then - now) / 1000);
	}
	
	public static void evaluate(Population p)
	{
	//	System.out.println("I am evaluating run " + runIndex + "..."); 
		showGraph();
		
		int sum = 0;
		int goods = 0;
		int baads = 0;
		double grade = 1;
		double diff = 0;
		int[] nrOfColors = new int[numberOfPopulation];
		for(int i = 0; i < numberOfPopulation; i++)
		{
			grade = 1;
			for(int j = 0; j < graph.length; j++)
				for(int k = 0; k < graph[j].length; k++)
				{
					int color1 = p.get(i).get(j);
					int color2 = p.get(i).get(k);
					
	//				System.out.println(j + "c[" + color1 + "] -> " + graph[j] + "c[" + color2 + "] . . . ");
					if(graph[j][k] > 0 && j != k)
					{
					//	System.out.println("Comparing:" + j + " with " + k);
						if(Math.abs(color1 - color2) > graph[j][k])
						{
							//diff = Math.pow(2, (Math.abs(color1 - color2) / 10));
							diff = Math.pow(2, punishment);
							grade += diff;
		//					System.out.println("Good! Extra " + valueOfGrade + " points! Now: " +sum );
							goods ++;
				//			System.out.println("GOOD, color1: " + color1 + ", color2: " +color2 + ", diff:" + graph[j][k]);
						}
						else
						{
							diff = Math.pow(2, punishment);
							grade -= diff;
		//					System.out.println("Baad! Minus " + valueOfGrade + " points! Now: " +sum );
							baads ++;
		//					System.out.println("BAAD, color1: " + color1 + ", color2: " +color2 + ", diff:" + graph[j][k]);
							p.get(i).setBaads(p.get(i).getBaads() + 1);
						}
					}
					
				}
			
				nrOfColors[i] = p.get(i).getNrOfUniqueColors(); 	
				grade = (grade / p.get(i).getNrOfUniqueColors());
				grade = grade / ((p.get(i).getBaads() + 1) * 2);
					
//				grade -= p.get(i).getNrOfUniqueColors() * 9000 * valueOfGrade;
				if(grade < 1)
					grade = 1;
//				
				grade *= 100;
				p.get(i).setGrade(grade); 
				sum += grade;
///			System.out.println("Number " + i + " got grade: " + grade + ", nrOfColors: " + p.get(i).getNrOfUniqueColors());
		}
		
	//	System.out.println("Run " + runIndex + ": " + sum / numberOfPopulation +", colors: " + min(nrOfColors) + ", goods: " + goods + ", baads: " +baads);
		Individual best = getBest(p);
		if(bestOfTheBests == null)
			bestOfTheBests = best;
		else
			if(best.getGrade() > bestOfTheBests.getGrade())
				bestOfTheBests = best;
	//	System.out.println("Best individual: colors: " + bestOfTheBests.getNrOfUniqueColors() + ", grade: " + bestOfTheBests.getGrade() + ", baads:" + bestOfTheBests.getBaads() + ", typesOfChromosomes: " + numberOfTypesOfChromosomes );
		showInd(bestOfTheBests);
		eachGenInfo[runIndex] = runIndex + ";" + sum / numberOfPopulation + ";" + bestOfTheBests.getGrade() + ";" + bestOfTheBests.getNrOfUniqueColors() + ";" + numberOfTypesOfChromosomes;
		
	}
	
	public static int min(int[] tab)
	{
		int min = tab[0];
		for(int i = 1; i < tab.length; i++)
		{
			if(min > tab[i])
				min = tab[i];
		}
		return min;
	}
	
	public static Individual getBest(Population p)
	{
		Individual best = p.get(0);
		for(int i = 1; i < numberOfPopulation; i++)
		{
			if(p.get(i).getGrade() > best.getGrade())
				best = p.get(i);
		}
		
		return best;
	}
	
	public static void showGraph()
	{
	//	System.out.print("Graph: { ");
		for(int i = 0; i < graph.length; i++)
			for(int j = 0; j < graph[i].length; j++)
		{
	//		System.out.print("[" + i + ", " + j + ": " + graph[i][j] + "] ");
		}
	//	System.out.println("}");
	}
	
	public static void getGraph(String path, int nrOfPeaks) throws FileNotFoundException
	{
		File file = new File(path);
		Scanner sc = new Scanner(file);
		String line = "";
		graph = new int[nrOfPeaks][nrOfPeaks];
		int edge = 0;
		int toEdge = 0;
		int minDiff = 0;
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			String[] edgeTab = line.split(" ");
//			System.out.println(edgeTab[0]);
			if(edgeTab[0].equals("e") && edgeTab[1] != edgeTab[2])
			{
//				System.out.println("I am IN!!!");
				edge = Integer.parseInt(edgeTab[1]) - 1;
				toEdge = Integer.parseInt(edgeTab[2]) - 1;
				minDiff = Integer.parseInt(edgeTab[3]);
			
				graph[edge][toEdge] = minDiff;
			}
		}
		sc.close();
	}
	
	public static void showInd(Individual ind)
	{
		for(int i = 0; i < numberOfChromosomesForEachIndividual; i++)
		{
	//		System.out.println(i + "->" + ind.get(i) + " | ");
		}
	}
	
	public static void toFile(String name, Long time) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("D:\\Files\\Results\\GEOM" + numberOfChromosomesForEachIndividual + "\\" + name, "UTF-8");
		writer.println(parametersInfo + "; czas [sekund]: " + time);
		for(String line: offEachGenInfo)
		{
			writer.println(line);
		}
		
		writer.close();
	}

}
