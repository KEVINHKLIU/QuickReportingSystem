import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class ReadForKTSL{
	
	static int  numOfTerm = 17;
	private static String[] wardType =  new String[numOfTerm];		//�f�����O
	private static String[] beds = new String[numOfTerm];			//�f�ɼ�
	private static String[] occupancyBeds = new String[numOfTerm];	//���ɼ�
	private static String[] noAvailableBeds = new String[numOfTerm];//�ŧɼ�
	
	public ReadForKTSL()
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
		

	public  void Parsing() throws  Exception
	{	
	
		
		String url = "http://www.ktgh.com.tw/BednoInfo_Show.asp?CatID=80&ModuleType=Y"; 
		Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
		org.jsoup.nodes.Element table ;
		
		
		int j = 0;
		int index = 0;
		index = 0;
		table = document.select("table").get(6).select("td").get(46);
		wardType[index] = table.text();
		table = document.select("table").get(6).select("td").get(47);
		beds[index] = table.text();
		table = document.select("table").get(6).select("td").get(48);
		occupancyBeds[index] = table.text();
		table = document.select("table").get(6).select("td").get(49);
		noAvailableBeds[index] = table.text();
		
		j = 0;
		index = 1;
		int type = 1;
		for(int i = 52; i < 67; i++)
		{
			j++;
			if(j != 4)
			{
				table = document.select("table").get(6).select("td").get(i);
				switch(type)
				{
				case 1:
					wardType[index] = "��ʦ��t�B�f�� Acute Partially Insured Room_" + table.text();
					break;
				case 2:
					beds[index] = table.text();
					break;
				case 3:
					occupancyBeds[index] = table.text();
					break;
				case 4:
					noAvailableBeds[index] = table.text();
					break;
				}
				index++;
		    	if(j == 3)
		    	{
		    		index = 1;
		    		type++;
		    	}
			}
			else
				j = 0;
		}
		
		j = 0;
		index = 4;
		for(int i = 67; i < 111; i++)
	    {
			j++;
			
			table = document.select("table").get(6).select("td").get(i);
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
	    	case 4:{
	    		noAvailableBeds[index] = table.text();
	    		 j = 0;
	    		 index++;
	    		 break;
	    	}
	    	}
	    }
		index = 15;
		j = 0;
		type = 1;
		for(int i = 113; i < 124; i++)
		{
			j++;
			if(j != 3)
			{
				table = document.select("table").get(6).select("td").get(i);
				switch(type)
				{
				case 1:
					wardType[index] = "�C�ʦ��t�B�f�� Chronic Beds_" + table.text();
					
					break;
				case 2:
					beds[index] = table.text();
				
					break;
				case 3:
					occupancyBeds[index] = table.text();
					break;
				case 4:
					noAvailableBeds[index] = table.text();
					break;
				}
				index++;
		    	if(j == 2)
		    	{
		    		index = 15;
		    		type++;
		    	}
			}
			else
				j = 0;
		}
		
		String s = "";
		System.out.println("---------����-���к�X��|(�F��)-----------");
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
			intoDatabase.insertTable("���к�X��|(�F���|��)", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}
}
	
	


