import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadForTVGH {
	static int  numOfTerm = 12;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForTVGH()
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
		String url = "http://www.vghtc.gov.tw/GipOpenWeb/wSite/sp?xdUrl=/wSite/query/portal/ADMService.jsp&ctNode=244&idPath=214_219_244&mp=3"; 
		Document document = Jsoup.connect(url)
			    .header("UTF-8", "gzip, deflate")
			    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
			    .get();
		org.jsoup.nodes.Element table;
		int index = 0;
		int j = 1;
		for(int i = 2; i < 74; i++)
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
		System.out.println("---------重度-台中榮總-----------");
		for(int i = 0; i < numOfTerm; i++)
		{
			s = wardType[i]  + " " + beds[i]  + " " +occupancyBeds[i]  + " " +noAvailableBeds[i];
			System.out.println(s);
		}	

	}

	public void insertToDatabase()
	{
		InsertToBedsInfoDatabase intoDatabase = new InsertToBedsInfoDatabase();
		intoDatabase.createTable();
		for(int i = 0; i < numOfTerm; i++)
			intoDatabase.insertTable("臺中榮民總醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}
	
	
	
}  