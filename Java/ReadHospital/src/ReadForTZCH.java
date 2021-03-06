import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadForTZCH {
	static int  counter = 0;
	static int  numOfTerm = 18;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForTZCH()
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
		String url = "http://app.tzuchi.com.tw/tzuchi/AdmBedQry/BedQry.aspx?Hosp=TC"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		org.jsoup.nodes.Element table;
		String type = "";
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
		}	
	}
	public void insertToDatabase()
	{
		InsertToBedsInfoDatabase intoDatabase = new InsertToBedsInfoDatabase();
		for(int i = 0; i < numOfTerm; i++)
			intoDatabase.insertTable("臺中慈濟醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  
