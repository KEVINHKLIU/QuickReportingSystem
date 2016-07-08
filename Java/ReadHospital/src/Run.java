import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Run extends TimerTask{
	static int  counter = 0;
	private ReadForTVGH TVGH = new ReadForTVGH();   //重度-台中榮總
	private ReadForCHSH CHSH = new ReadForCHSH();   //重度-中山醫學
	private ReadForKTSL KTSL = new ReadForKTSL();   //重度-光田沙鹿
	private ReadForKTDJ KTDJ = new ReadForKTDJ();	//重度-光田大甲
	private ReadForTUNG TUNG = new ReadForTUNG();	//重度-童綜
	private ReadForTZCH TZCH = new ReadForTZCH();	//中度-慈濟
	private ReadForFYH FYH = new ReadForFYH();	//中度-豐原
	private ReadForLISH LISH = new ReadForLISH();	//中度-林新
	private ReadForTAFG TAFG = new ReadForTAFG();	//中度-國軍
	private ReadForLEDJ LEDJ = new ReadForLEDJ();	//中度-大甲李
	private ReadForCHCH CHCH = new ReadForCHCH();	//中度-澄清
	private ReadForCHCK CHCK = new ReadForCHCK();	//中度-澄清中港
	private ReadForJEAI JEAI = new ReadForJEAI();	//中度-大里仁愛
	private ReadForTAIC TAIC = new ReadForTAIC();	//中度-台中醫院
	private ReadForCHICHY CHICHY = new ReadForCHICHY();	//輕度-清泉
	private ReadForDOSH DOSH = new ReadForDOSH();	//輕度-東勢
	public static void main(String [] atrgs)
	{
		Timer timer = new Timer();
		timer.schedule(new Run(), 0, 80000);
		System.out.println("現在時間：" + new Date());
		try 
		{
		   Thread.sleep(100000);
		}
		catch(InterruptedException e) 
		{
		}
	    timer.cancel();
	    timer.purge();
	    System.out.println("End testScheduleDateAndPeriod：" + new Date() + "\n");
	}
	public void run() 
	{	
	
		counter++;
		try 
		{
			System.out.println("Task " + counter + " " + new Date());
			TVGH.execute();//重度-台中榮總
			System.out.println();
			CHSH.execute();//重度-中山醫學
			System.out.println();
			TUNG.execute();//重度-童綜
			System.out.println();
			KTSL.execute(); //重度-光田沙鹿
			System.out.println();
			KTDJ.execute();//重度-光田大甲
			System.out.println();
			TZCH.execute();//中度-慈濟
			System.out.println();
			FYH.execute();//中度-豐原
			System.out.println();
			LISH.execute();//中度-林新
			System.out.println();
			TAFG.execute();//中度-國軍
			System.out.println();
			LEDJ.execute();//中度-大甲李
			System.out.println();
			CHCH.execute();//中度-澄清
			System.out.println();
			CHCK.execute();	//中度-澄清中港
			System.out.println();
			JEAI.execute(); //中度-大里仁愛
			System.out.println();
			TAIC.execute();//中度-台中醫院
			System.out.println();
			CHICHY.execute();//輕度-清泉
			System.out.println();
			DOSH.execute();//輕度-東勢
		} 
		catch (Exception e) 
		{
		}
	}

}
