package assignment_0;

public class doExperiments {

	public static void main(String[] args) {
		
		int width = 10;
		for(int p=72;p<90;p+=2)
		{
			double pval = p/100.0;
			//System.out.println(pval);
			border testBorder = new border(width,pval);
			testBorder.initialiseGrid();
			testBorder.changePvalGrid();
			//System.out.print(width);
			//System.out.print(",");
			//System.out.print(pval);
			//System.out.print(",");
			int sumTime = 0;
			for(int i=0; i<10; i++)
			{
				sumTime += testBorder.getTimeCross();
				//System.out.println(testBorder.getTimeCross());
				testBorder.resetTime();
			}
			int avgTime = sumTime/10;
			System.out.println(avgTime);
			//System.out.println(",");
		}
		System.out.println("done");
	}

}
