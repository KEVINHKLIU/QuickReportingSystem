import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadForCHCK {
	static int  numOfTerm = 6;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForCHCK()
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
	
	public void Parsing() throws Exception
	{	
		String url = "http://www.ccgh.com.tw/html/webpage.aspx?pagetype=4&otherkind=B-0001&pageno=0"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		org.jsoup.nodes.Element table;
		int index = 0;
		int j = 1;
		for(int i = 0; i < 36; i++)
		{
			table = document.select("table").get(0).select("td").get(i);
			switch(j)
			{
			case 1:
				wardType[index] = table.text();
				break;
			case 2:
				beds[index] = table.text();
				break;
			case 3:
			case 4:
				break;
			case 5:
				occupancyBeds[index] = table.text();
				break;
			case 6:
			{
				noAvailableBeds[index] = table.text();
				j = 0;
				index++;
				break;
			}
			}
			j++;
		}
		String s = "";
		System.out.println("---------中度-澄清綜合醫院中港分院-----------");
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
			intoDatabase.insertTable("澄清綜合中港分院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  