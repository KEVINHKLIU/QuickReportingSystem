import java.util.Scanner; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class ReadForHospitalInfo 
{
	private static int numOfTerm = 17;
	private static String[] hospitalName =  new String[numOfTerm];
	private static String[] hospitalClassification=  new String[numOfTerm];
	private static String[] hospitalAddress=  new String[numOfTerm];
	private static String[] emergencyInfo=  new String[numOfTerm]; 
	private static String[] burnBeds=  new String[numOfTerm];
	private static String[] WomenChild24h=  new String[numOfTerm];
	private static String[] noAdmitted=  new String[numOfTerm]; 
	
	public ReadForHospitalInfo()
	{
		for(int i = 0; i < numOfTerm; i++)
		{
			hospitalName[i] = "none";			//醫院名稱
			hospitalClassification[i] = "none";	//等級分級
			hospitalAddress[i] = "none";		//醫院地址
			emergencyInfo[i] = "none";			//急診資訊
			burnBeds[i] = "none";				//燙傷病床
			WomenChild24h[i] = "none";			//24H婦兒
			noAdmitted[i] = "none";				//無法收治限制
		}
	}
	
	public static void main(String[] args)   
	{
		InsertToHospitalInfoDatabase intoDatabase = new InsertToHospitalInfoDatabase();
		readFromText();
		readForTVGH();
		readForCMUH();
		readForCHSH();
		readForTUNG();
		readForKTSL();
		readForKTDJ();
		intoDatabase.dropTable(); 
		intoDatabase.createTable(); 
		for(int i = 0; i < numOfTerm; i++)
		{
		   
			intoDatabase.insertTable(hospitalName[i], hospitalClassification[i], hospitalAddress[i], 
		    		         emergencyInfo[i], burnBeds[i], WomenChild24h[i], noAdmitted[i]); 
		
		}
		
		output();
	}

	public static void readFromText()
	{
		Scanner fileIn = null;  //先定義初始值
		String line;
		try   //檔案讀寫的函示
		{
			fileIn = new Scanner(new FileInputStream("HospitalInfo.txt")); //去讀檔案,我的檔案名稱是teamInfo.txt
		}
		catch (FileNotFoundException e)    //假如找不到檔案
		{
			System.out.println("File Not Found!!");  //就印出File Not Found!!
			System.exit(0);  //並且不會繼續執行程式
		}
	
		for(int i = 0; i < numOfTerm; i++)
		{
			line = fileIn.nextLine();
			String[] token = line.split("#");
			/*for(int j = 0; j < token.length; j++)
				System.out.println(" token[ "+j+" ] = "+token[j]);*/
			hospitalName[i] = token[0];
			hospitalClassification[i] = token[1];	
			hospitalAddress[i] = token[2];		
			emergencyInfo[i] = token[3];		
			burnBeds[i] = token[4];				
			WomenChild24h[i] = token[5];			
			noAdmitted[i] = token[6];			
		}
	}
	
	public static void readForTVGH()
	{
		try 
		{

			String url = "http://www.vghtc.gov.tw/GipOpenWeb/wSite/sp?xdUrl=/wSite/query/Doctor/GetEmgBedInform.jsp&ctNode=55658&mp=1"; 
			Document document = Jsoup.connect(url)
				    .header("UTF-8", "gzip, deflate")
				    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
				    .get();
			org.jsoup.nodes.Element table;
			table = document.select("table").get(0).select("td").get(4);
			emergencyInfo[0] = table.text();
			
		} 
		catch (Exception e) 
		{
		}
	}
	
	public static void readForCMUH()
	{

		try 
		{
			String url = "http://61.66.117.28/EmrCount/Default.aspx"; 
			Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
			org.jsoup.nodes.Element table ;
			table = document.select("table").get(0).select("td").get(4);
			emergencyInfo[1] = table.text();
			
		} 
		catch (Exception e) 
		{
		}
	}
	
	public static void readForCHSH()
	{
	
		try 
		{
			String url = "http://www.csh.org.tw/ER/index.aspx"; 
			Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
			org.jsoup.nodes.Element table ;
			table = document.select("table").get(4).select("td").get(0).select("span").get(1);
			if(table.text().equals("未通報"))
				emergencyInfo[2] = "否";
			else
				emergencyInfo[2] = "是";
			
		} 
		catch (Exception e) 
		{
		}
	}
	
	public static void readForTUNG()
	{

		try 
		{
			String url = "http://www.sltung.com.tw/BED/bed.html"; 
			Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
			org.jsoup.nodes.Element table ;
		
			table = document.select("span").get(0);
			if(table.text().substring(2, 15).equals("是否已向119通報滿床：否"))
				emergencyInfo[3] = "否";
			else
				emergencyInfo[3] = "是";
		} 
		catch (Exception e) 
		{
		}
	}
	
	public static void readForKTSL()
	{
	
		try 
		{
			String url = "http://www.ktgh.com.tw/BednoInfo_Show.asp?CatID=80&ModuleType=Y"; 
			Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
			org.jsoup.nodes.Element table ;
		
			table = document.select("table").get(6).select("td").get(39); 
			if(table.text().equals("●"))
				emergencyInfo[4] = "否";
			else
				emergencyInfo[4] = "是";
			
		} 
		catch (Exception e) 
		{
		}
	}
	
	public static void readForKTDJ()
	{

		try 
		{
			String url = "http://www.ktgh.com.tw/BednoInfo_Show.asp?CatID=81&ModuleType=Y"; 
			Document document = Jsoup.parse(new URL(url).openStream(), "Big5", url);
			org.jsoup.nodes.Element table ;
		
			table = document.select("table").get(6).select("td").get(39); 
			if(table.text().equals("●"))
				emergencyInfo[5] = "否";
			else
				emergencyInfo[5] = "是";
			
		} 
		catch (Exception e) 
		{
		}
		
	}
	
	public static void output()
	{
		for(int i = 0; i < numOfTerm; i++)
		{
			System.out.println("醫院名稱: " + hospitalName[i]);
			System.out.println("醫院分級: " + hospitalClassification[i]);
			System.out.println("醫院地址: " + hospitalAddress[i]);
			System.out.println("急診資訊: " + emergencyInfo[i]);
			System.out.println("燒傷病床: " + burnBeds[i]);
			System.out.println("24H婦幼: " + WomenChild24h[i]);
			System.out.println("無法收治限制: " + noAdmitted[i]);
			System.out.println("**********************************");
		}	
	}

}
