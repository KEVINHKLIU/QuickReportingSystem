import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadForTAIC {
	static int  numOfTerm = 9;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForTAIC()
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
		String url = "http://www02.taic.mohw.gov.tw/bed/asp/bed.asp"; 
		Document document = Jsoup.connect(url)
			    .header("Big5", "gzip, deflate")
			    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
			    .get();
		//System.out.println(document);
		org.jsoup.nodes.Element table;
		wardType[0] = "急性一般病床";wardType[1] = "慢性精神病床";wardType[2] = "急性一般精神病床";
		wardType[3] = "慢性呼吸照護病床";wardType[4] = "加護病床";wardType[5] = "負壓隔離病床";
		wardType[6] = "亞急性呼吸照護病床";wardType[7] = "嬰兒病床";wardType[8] = "燒傷病床";
		
		int index = 0;
		int j = 1;
		int occupancyBedsNum = 0;
		int occupancyBedsNum1 = 0;
		int noAvailableBedsNum = 0;
		int noAvailableBedsNum1 = 0;
		for(int i = 3; i < 75; i++)
		{
			table = document.select("table").get(4).select("td").get(i);
			switch(j)
			{
			case 1:
			case 3:
			case 4:
				break;
			case 2:
				beds[index] = table.text();
				break;
			case 5:
				noAvailableBedsNum = Integer.parseInt(table.text());
				break;
			case 6:
				noAvailableBedsNum1 = Integer.parseInt(table.text());
				noAvailableBeds[index] = String.valueOf(noAvailableBedsNum + noAvailableBedsNum1);
				break;
			case 7:
				occupancyBedsNum = Integer.parseInt(table.text());
				break;
			case 8:
				occupancyBedsNum1 = Integer.parseInt(table.text());
				occupancyBeds[index] = String.valueOf(occupancyBedsNum + occupancyBedsNum1);
				j = 0;
				index++;
				break;
			}
			j++;
		}
		String s = "";
		System.out.println("---------中度-台中醫院-----------");
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
			intoDatabase.insertTable("衛生福利部臺中醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}

}  