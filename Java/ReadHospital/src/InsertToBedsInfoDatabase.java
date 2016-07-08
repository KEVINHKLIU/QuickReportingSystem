import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InsertToBedsInfoDatabase {
	  private Connection con = null; //Database objects 
	  //連接object 
	  private Statement stat = null; 
	  //執行,傳入之sql為完整字串 
	  private ResultSet rs = null; 
	  //結果集 
	  private PreparedStatement pst = null; 
	  //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
	  //先利用?來做標示 
	  
	  private String createdbSQL = "CREATE TABLE BedsInfo (" + 
	    "    id     INTEGER " + 
	    "  , hospital    VARCHAR(20) " + 
	    "  , wardType    VARCHAR(70) " + 
	    "  , totalBedsNum  VARCHAR(20)"+
	    "  , occupancyBedsNum    VARCHAR(30) " + 
	    "  , noAvailableBedsNum    VARCHAR(20)) " ; 
	
	  
	  private String insertdbSQL = "insert into BedsInfo(id,hospital,wardType,totalBedsNum,occupancyBedsNum,noAvailableBedsNum) " + 
	      "select ifNULL(max(id),0)+1,?,?,?,?,? FROM BedsInfo"; 
	  
	 // private String selectSQL = "select * from HospitalInfo "; 
	  
	  public InsertToBedsInfoDatabase() 
	  { 
	    try { 
	      Class.forName("com.mysql.jdbc.Driver"); 
	      //註冊driver 
	      con = DriverManager.getConnection( 
	      "jdbc:mysql://127.0.0.1/lab0123?useUnicode=true&characterEncoding=Big5", 
	      "Raymond","01230123"); 
	      //取得connection
	    } 
	    catch(ClassNotFoundException e) 
	    { 
	      System.out.println("DriverClassNotFound :"+e.toString());  //有可能會產生sqlexception 
	    }
	    catch(SQLException x) { 
	      System.out.println("Exception :"+x.toString()); 
	    } 
	    
	  } 
	  //建立table的方式 
	  //可以看看Statement的使用方式 
	  public void createTable() 
	  { 
	    try 
	    { 
	      stat = con.createStatement(); 
	      stat.executeUpdate(createdbSQL); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("CreateDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	  //新增資料 
	  //可以看看PrepareStatement的使用方式 
	  public void insertTable(String hospital, String wardType, String totalBedsNum, String occupancyBedsNum, String noAvailableBedsNum) 
	  { 
	    try 
	    { 
	      pst = con.prepareStatement(insertdbSQL); 
	      
	      pst.setString(1, hospital);      //病床種類
	      pst.setString(2, wardType);      //病床種類
	      pst.setString(3, totalBedsNum);  //總床數
	      pst.setString(4, occupancyBedsNum);   //佔床數
	      pst.setString(5, noAvailableBedsNum); //空床數
	
	      
	
	      pst.executeUpdate(); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("InsertDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	
	  //完整使用完資料庫後,記得要關閉所有Object 
	  //否則在等待Timeout時,可能會有Connection poor的狀況 
	  private void Close() 
	  { 
	    try 
	    { 
	      if(rs!=null) 
	      { 
	        rs.close(); 
	        rs = null; 
	      } 
	      if(stat!=null) 
	      { 
	        stat.close(); 
	        stat = null; 
	      } 
	      if(pst!=null) 
	      { 
	        pst.close(); 
	        pst = null; 
	      } 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("Close Exception :" + e.toString()); 
	    } 
	  } 
	  

	
}
