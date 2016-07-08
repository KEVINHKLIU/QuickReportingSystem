import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ReadForTAFG {
	static int  counter = 0;
	static int  numOfTerm = 10;
	static int j = 1;
	static int index = 0;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForTAFG()
	{
		for(int i = 0; i < numOfTerm; i++)
		{
			wardType[i] = "none";
			beds[i] = "none";
			occupancyBeds[i] = "none";
			noAvailableBeds[i] = "none";
		}
	}
	
	public  void execute()
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
		String url = "http://web-reg-server.803.org.tw/TRE/sickbednum/803bed.asp"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
		String bedName = "";
		org.jsoup.nodes.Element table;
		for(int i = 7; i < 62; i++)
		{
			if( i== 11 || i== 20 || i== 27 || i== 32 || i== 37 || i== 42 || i== 47 || i== 52 || i== 58)
			{
			
			}
			else
			{
				table = document.select("table").get(0).select("td").get(i);
				if( i == 36)
					bedName = "特殊病床_";
				if( i == 19)
					bedName = "一般病床_急性病床_";
				if( i == 7 || i == 8 || i == 9 )
					bedName = bedName + table.text() + "_";
				else if( i == 24 || i == 25)
				{
					if( i == 24)
						bedName = "";
					bedName = bedName + table.text() + "_";
				}	
				else if( i == 56)
					bedName = "特殊病床_" + table.text() + "_";
				else
					assign(table.text(),bedName);
			}
		
				
		
		}
		String s = "";
		System.out.println("---------中度-國軍台中總醫院-----------");
		for(int i = 0; i < numOfTerm; i++)
		{
			s = wardType[i]  + " " + beds[i]  + " " +occupancyBeds[i]  + " " +noAvailableBeds[i];
			System.out.println(s);
		}	
	}
	public void assign(String s, String bedName)
	{
		
		switch(j)
		{
		case 1:
			wardType[index] = bedName + s;
			break;
		case 2:
			beds[index] = s;
			break;
		case 3:
			occupancyBeds[index] = s;
			break;
		case 4:
		{
			noAvailableBeds[index] = s;
			j = 0;
			index++;
			break;
		}
		}
		j++;
	}
	public void insertToDatabase()
	{
		InsertToBedsInfoDatabase intoDatabase = new InsertToBedsInfoDatabase();
		for(int i = 0; i < numOfTerm; i++)
			intoDatabase.insertTable("國軍臺中總醫院附設民眾診療服務處", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  