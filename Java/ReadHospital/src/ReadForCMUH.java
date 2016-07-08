import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;


public class ReadForCMUH {
	static int  counter = 0;
	static int  numOfTerm = 18;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForCMUH()
	{
		for(int i = 0; i < numOfTerm; i++)
		{
			wardType[i] = "none";
			beds[i] = "none";
			occupancyBeds[i] = "none";
			noAvailableBeds[i] = "none";
		}
	}
	
	public static void main(String [] atrgs)
	{	
		try 
		{
			Parsing();
		} 
		catch (Exception e) 
		{
		}
	}
	
	public static void Parsing() throws Exception
	{	
		String url = "http://www.cmuh.cmu.edu.tw/web/guest/current_bed_availability"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		org.jsoup.nodes.Element table;
		String s = "";
		for(Element e : document.getAllElements()){
		     for(Node n: e.childNodes()){
		          if(n instanceof Comment){
		        	  s = ((Comment) n).getData(); 
		            }
		        }
		    }
		String[] token = s.split(">");
		String s2 = "";
		for(int i = 14; i < token.length; i++)
		{
			s2 = token[i];
			String[] tokens = s2.split("<");
			for(int j = 0; j < tokens.length; j++)
			{
				System.out.println(i + " " +  " token[ "+j+" ] = "+tokens[j]);
			}
		}
		
		
		
		
			
		
		
		
		/*String type = "";
		int j = 1;
		int index = 0;
		
		for(int i = 4; i < 79; i++)
		{
			table = document.select("table").get(0).select("td").get(i);
			
			if( i == 4 || i == 33 || i == 62)
			{
				type = table.text() + "_" ;
			}
			else
			{
				switch(j)
				{
				case 1:
					wardType[index] = type + table.text();
					break;
				case 2:
					beds[index] = table.text();
					break;
				case 3:
					occupancyBeds[index] = table.text();
					break;
				case 4:
				{
					noAvailableBeds[index] = table.text();
					j = 0;
					index++;
					break;
				}
				}
				j++;
			}
		}
		
		String s = "";
		System.out.println("---------中度-台中慈濟-----------");
		for(int i = 0; i < numOfTerm; i++)
		{
			s = wardType[i]  + " " + beds[i]  + " " +occupancyBeds[i]  + " " +noAvailableBeds[i];
			System.out.println(s);
		}	*/
	}

}  