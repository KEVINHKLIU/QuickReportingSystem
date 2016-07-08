import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadForJEAI {
	static int  numOfTerm = 6;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForJEAI()
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
		String url = "http://www.jah.org.tw/sickbed/index2.asp?m=99&m1=5&m2=457"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
		org.jsoup.nodes.Element table;
		int index = 0;
		int j = 1;
		for(int i = 41; i < 65; i++)
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
		String s = "";
		System.out.println("---------中度-大里仁愛醫院-----------");
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
			intoDatabase.insertTable("大里仁愛醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  