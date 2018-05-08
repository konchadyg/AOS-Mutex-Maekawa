import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckOverlap {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		boolean inCS = false;
		boolean oldStatus;
		boolean overlap = false;
		try 
		{
				String sCurrentLine;
				br = new BufferedReader(new FileReader("output.txt"));

				
				while ((sCurrentLine = br.readLine()) != null) {
					
					oldStatus = inCS;
					
					if(sCurrentLine.charAt(1) == '+')
					{inCS=true;
					}
					else
					{
						inCS=false;
					}
					
					if(oldStatus == inCS)
					{
						overlap = true;
						break;
					}
						
				}
				
				
				if(overlap)
				{
					System.out.println("There was an overlap in accessing the critical section");
				}
				else
				{
					System.out.println("There was no overlap in accessing the critical section");
				}

				
							
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try {
				if (br != null)br.close();
				}
			catch (IOException ex) {
				ex.printStackTrace();
				}
		}
		
		
	}

}
