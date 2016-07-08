import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReadForTUNG {
	static int  numOfTerm = 16;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForTUNG()
	{
		for(int i = 0; i < numOfTerm; i++)
		{
			wardType[i] = "none";
			beds[i] = "none";
			occupancyBeds[i] = "none";
			noAvailableBeds[i] = "none";
		}
	}
		
	public void execute() 
	{	
		try 
		{
			Parsing();
		} 
		catch (Exception e) 
		{
		}
		insertToDatabase();
	}

	public  void Parsing() throws Exception
	{	
		String url = "http://www.sltung.com.tw/OPD/bmonline.htm"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
		org.jsoup.nodes.Element table = document.select("table").get(0);
		Elements rows = table.select("tr");
		org.jsoup.nodes.Element item;
		
		int index = 0;
    	for (int i = 1; i < rows.size()-1; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				item = document.select("table").get(0).select("tr").get(i).select("td").get(j);
				
				switch(j)
				{
				case 0:
					wardType[index] = item.text();
					break;
				case 1:
					beds[index] = item.text();
					break;
				case 2:
					occupancyBeds[index] = item.text();
					break;
				case 3:
				{
					noAvailableBeds[index] = item.text();
					index++;
					break;
				}
				}
			}
		}
    	
    	String s = "";
    	System.out.println("---------重度-童綜合醫院-----------");
		for(int i = 0; i < numOfTerm; i++)
		{
			s = wardType[i]  + " " + beds[i]  + " " +occupancyBeds[i]  + " " +noAvailableBeds[i];
			System.out.println(s);
		}	
	}
	public void insertToDatabase()
	{
		InsertToBedsInfoDatabase intoDatabase = new InsertToBedsInfoDatabase();
		for(int i = 0; i < numOfTerm; i++)
			intoDatabase.insertTable("童綜合醫療社團法人童綜合醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}
}
	
	


