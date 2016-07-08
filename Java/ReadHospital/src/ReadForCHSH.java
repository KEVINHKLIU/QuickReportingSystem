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
		Scanner fileIn = null;  //���w�q��l��
		String line = "";
		try   //�ɮ�Ū�g�����
		{
			fileIn = new Scanner(new FileInputStream("���s��Ǥj��.txt")); //�hŪ�ɮ�,�ڪ��ɮצW�٬OteamInfo.txt
		}
		catch (FileNotFoundException e)    //���p�䤣���ɮ�
		{
			System.out.println("File Not Found!!");  //�N�L�XFile Not Found!!
			System.exit(0);  //�åB���|�~�����{��
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
		System.out.println("---------����-���s��Ǥj�Ǫ��]��|-----------");
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
			intoDatabase.insertTable("���s��Ǥj�Ǫ��]��|", wardType[i], beds[i], occupancyBeds[i], noAvailableBeds[i]);
	}
	

}
