import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ReadForCHICHY {
	static int  counter = 0;
	static int  numOfTerm = 4;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForCHICHY()
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
		String url = "http://www.ching-chyuan.com.tw/Registration/Select/frmBed.aspx"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		org.jsoup.nodes.Element table;
		int index = 0;
		int j = 1;
		for(int i = 0; i < 16; i++)
		{
			table = document.select("table").get(2).select("td").get(i);
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
		System.out.println("---------輕度-清泉醫院-----------");
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
			intoDatabase.insertTable("清泉醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  