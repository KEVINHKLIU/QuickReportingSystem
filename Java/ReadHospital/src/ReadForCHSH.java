import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class ReadForCHSH {
	static int  numOfTerm = 6;
	private static String[] wardType =  new String[numOfTerm];
	private static String[] beds = new String[numOfTerm];
	private static String[] occupancyBeds = new String[numOfTerm];
	private static String[] noAvailableBeds = new String[numOfTerm];
	
	public ReadForCHSH()
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
		readFromText();
		insertToDatabase();
	}
	
	public  void readFromText()
	{
		Scanner fileIn = null;  //先定義初始值
		String line = "";
		try   //檔案讀寫的函示
		{
			fileIn = new Scanner(new FileInputStream("中山醫學大學.txt")); //去讀檔案,我的檔案名稱是teamInfo.txt
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
			wardType[i] = token[0];
			beds[i] = token[1];	
			occupancyBeds[i] = token[2];		
			noAvailableBeds[i] = token[3];				
		}
		String s = "";
		System.out.println("---------重度-中山醫學大學附設醫院-----------");
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
			intoDatabase.insertTable("中山醫學大學附設醫院", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}
	

}
