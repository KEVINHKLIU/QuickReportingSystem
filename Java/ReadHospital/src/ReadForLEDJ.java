import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadForLEDJ {
	static int  counter = 0;
	static int  numOfTerm = 9;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForLEDJ()
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
		String url = "http://www.leehospital.com.tw/%E5%B0%B1%E9%86%AB%E9%A0%88%E7%9F%A5/reminder05-1.asp"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
		
		org.jsoup.nodes.Element table;
		int index = 0;
		int j = 1;
		for(int i = 5; i < 49; i++)
		{
			table = document.select("table").get(7).select("td").get(i);
			switch(j)
			{
			case 1:
				wardType[index] = table.text();
				wardType[index] = wardType[index].trim();
				break;
			case 2:
				beds[index] = table.text();
				beds[index] = beds[index].trim(); 
				break;
			case 3:
				occupancyBeds[index] = table.text();
				occupancyBeds[index] = occupancyBeds[index].trim();
				break;
			case 4:
				noAvailableBeds[index] = table.text();
				noAvailableBeds[index] = noAvailableBeds[index].trim();
				break;
			case 5:
			{
				j = 0;
				index++;
				break;
			}
			}
			j++;
		}
		String s = "";
		System.out.println("---------中度-大甲李綜合醫院-----------");
		for(int i = 0; i < numOfTerm; i++)
		{
			s = wardType[i] + " " + beds[i]  + " " +occupancyBeds[i]  + " " +noAvailableBeds[i];
			System.out.println(s);
		}	
	}
	public void insertToDatabase()
	{
		InsertToBedsInfoDatabase intoDatabase = new InsertToBedsInfoDatabase();
		for(int i = 0; i < numOfTerm; i++)
			intoDatabase.insertTable("大甲李綜合醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  