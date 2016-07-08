import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InsertToBedsInfoDatabase {
	  private Connection con = null; //Database objects 
	  //�s��object 
	  private Statement stat = null; 
	  //����,�ǤJ��sql������r�� 
	  private ResultSet rs = null; 
	  //���G�� 
	  private PreparedStatement pst = null; 
	  //����,�ǤJ��sql���w�x���r��,�ݭn�ǤJ�ܼƤ���m 
	  //���Q��?�Ӱ��Х� 
	  
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
	      //���Udriver 
	      con = DriverManager.getConnection( 
	      "jdbc:mysql://127.0.0.1/lab0123?useUnicode=true&characterEncoding=Big5", 
	      "Raymond","01230123"); 
	      //���oconnection
	    } 
	    catch(ClassNotFoundException e) 
	    { 
	      System.out.println("DriverClassNotFound :"+e.toString());  //���i��|����sqlexception 
	    }
	    catch(SQLException x) { 
	      System.out.println("Exception :"+x.toString()); 
	    } 
	    
	  } 
	  //�إ�table���覡 
	  //�i�H�ݬ�Statement���ϥΤ覡 
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
	  //�s�W��� 
	  //�i�H�ݬ�PrepareStatement���ϥΤ覡 
	  public void insertTable(String hospital, String wardType, String totalBedsNum, String occupancyBedsNum, String noAvailableBedsNum) 
	  { 
	    try 
	    { 
	      pst = con.prepareStatement(insertdbSQL); 
	      
	      pst.setString(1, hospital);      //�f�ɺ���
	      pst.setString(2, wardType);      //�f�ɺ���
	      pst.setString(3, totalBedsNum);  //�`�ɼ�
	      pst.setString(4, occupancyBedsNum);   //���ɼ�
	      pst.setString(5, noAvailableBedsNum); //�ŧɼ�
	
	      
	
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
	
	  //����ϥΧ���Ʈw��,�O�o�n�����Ҧ�Object 
	  //�_�h�b����Timeout��,�i��|��Connection poor�����p 
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
