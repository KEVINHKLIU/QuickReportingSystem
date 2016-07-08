import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InsertToHospitalInfoDatabase {
	  private Connection con = null; //Database objects 
	  //連接object 
	  private Statement stat = null; 
	  //執行,傳入之sql為完整字串 
	  private ResultSet rs = null; 
	  //結果集 
	  private PreparedStatement pst = null; 
	  //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
	  //先利用?來做標示 
	  
	  private String dropdbSQL = "DROP TABLE HospitalInfo "; 
	  
	  private String createdbSQL = "CREATE TABLE HospitalInfo (" + 
	    "    id     INTEGER " + 
	    "  , hName    VARCHAR(20) " + 
	    "  , hClassify  VARCHAR(20)"+
	    "  , hAddress    VARCHAR(30) " + 
	    "  , emergencyInfo    VARCHAR(20) " + 
	    "  , burnBeds    VARCHAR(20) " + 
	    "  , WomenChild24h    VARCHAR(20) " + 
	    "  , noAdmitted    VARCHAR(100)) "; 
	  
	  private String insertdbSQL = "insert into HospitalInfo(id,hName,hClassify,hAddress,emergencyInfo,burnBeds,WomenChild24h,noAdmitted) " + 
	      "select ifNULL(max(id),0)+1,?,?,?,?,?,?,? FROM HospitalInfo"; 
	  
	 // private String selectSQL = "select * from HospitalInfo "; 
	  
	  public InsertToHospitalInfoDatabase() 
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
	  public void insertTable(String hName, String hClassify, String hAddress, String emergencyInfo, String burnBeds, String WomenChild24h, String noAdmitted) 
	  { 
	    try 
	    { 
	      pst = con.prepareStatement(insertdbSQL); 
	      
	      pst.setString(1, hName);      //醫院名稱
	      pst.setString(2, hClassify);  //分級
	      pst.setString(3, hAddress);   //地址
	      pst.setString(4, emergencyInfo); //急診資訊
	      pst.setString(5, burnBeds);	//燒傷病床
	      pst.setString(6, WomenChild24h);	//24婦幼
	      pst.setString(7, noAdmitted);//無法收治限制
	      
	
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
	  //刪除Table, 
	  //跟建立table很像 
	  public void dropTable() 
	  { 
	    try 
	    { 
	      stat = con.createStatement(); 
	      stat.executeUpdate(dropdbSQL); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("DropDB Exception :" + e.toString()); 
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
