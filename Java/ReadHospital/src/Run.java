import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Run extends TimerTask{
	static int  counter = 0;
	private ReadForTVGH TVGH = new ReadForTVGH();   //����-�x���a�`
	private ReadForCHSH CHSH = new ReadForCHSH();   //����-���s���
	private ReadForKTSL KTSL = new ReadForKTSL();   //����-���ШF��
	private ReadForKTDJ KTDJ = new ReadForKTDJ();	//����-���Фj��
	private ReadForTUNG TUNG = new ReadForTUNG();	//����-����
	private ReadForTZCH TZCH = new ReadForTZCH();	//����-�O��
	private ReadForFYH FYH = new ReadForFYH();	//����-�׭�
	private ReadForLISH LISH = new ReadForLISH();	//����-�L�s
	private ReadForTAFG TAFG = new ReadForTAFG();	//����-��x
	private ReadForLEDJ LEDJ = new ReadForLEDJ();	//����-�j�ҧ�
	private ReadForCHCH CHCH = new ReadForCHCH();	//����-��M
	private ReadForCHCK CHCK = new ReadForCHCK();	//����-��M����
	private ReadForJEAI JEAI = new ReadForJEAI();	//����-�j�����R
	private ReadForTAIC TAIC = new ReadForTAIC();	//����-�x����|
	private ReadForCHICHY CHICHY = new ReadForCHICHY();	//����-�M�u
	private ReadForDOSH DOSH = new ReadForDOSH();	//����-�F��
	public static void main(String [] atrgs)
	{
		Timer timer = new Timer();
		timer.schedule(new Run(), 0, 80000);
		System.out.println("�{�b�ɶ��G" + new Date());
		try 
		{
		   Thread.sleep(100000);
		}
		catch(InterruptedException e) 
		{
		}
	    timer.cancel();
	    timer.purge();
	    System.out.println("End testScheduleDateAndPeriod�G" + new Date() + "\n");
	}
	public void run() 
	{	
	
		counter++;
		try 
		{
			System.out.println("Task " + counter + " " + new Date());
			TVGH.execute();//����-�x���a�`
			System.out.println();
			CHSH.execute();//����-���s���
			System.out.println();
			TUNG.execute();//����-����
			System.out.println();
			KTSL.execute(); //����-���ШF��
			System.out.println();
			KTDJ.execute();//����-���Фj��
			System.out.println();
			TZCH.execute();//����-�O��
			System.out.println();
			FYH.execute();//����-�׭�
			System.out.println();
			LISH.execute();//����-�L�s
			System.out.println();
			TAFG.execute();//����-��x
			System.out.println();
			LEDJ.execute();//����-�j�ҧ�
			System.out.println();
			CHCH.execute();//����-��M
			System.out.println();
			CHCK.execute();	//����-��M����
			System.out.println();
			JEAI.execute(); //����-�j�����R
			System.out.println();
			TAIC.execute();//����-�x����|
			System.out.println();
			CHICHY.execute();//����-�M�u
			System.out.println();
			DOSH.execute();//����-�F��
		} 
		catch (Exception e) 
		{
		}
	}

}
