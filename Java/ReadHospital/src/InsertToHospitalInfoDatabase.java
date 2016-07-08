import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InsertToHospitalInfoDatabase {
	  private Connection con = null; //Database objects 
	  //�s��object 
	  private Statement stat = null; 
	  //����,�ǤJ��sql������r�� 
	  private ResultSet rs = null; 
	  //���G�� 
	  private PreparedStatement pst = null; 
	  //����,�ǤJ��sql���w�x���r��,�ݭn�ǤJ�ܼƤ���m 
	  //���Q��?�Ӱ��Х� 
	  
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
	  public void insertTable(String hName, String hClassify, String hAddress, String emergencyInfo, String burnBeds, String WomenChild24h, String noAdmitted) 
	  { 
	    try 
	    { 
	      pst = con.prepareStatement(insertdbSQL); 
	      
	      pst.setString(1, hName);      //��|�W��
	      pst.setString(2, hClassify);  //����
	      pst.setString(3, hAddress);   //�a�}
	      pst.setString(4, emergencyInfo); //��E��T
	      pst.setString(5, burnBeds);	//�N�˯f��
	      pst.setString(6, WomenChild24h);	//24����
	      pst.setString(7, noAdmitted);//�L�k���v����
	      
	
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
	  //�R��Table, 
	  //��إ�table�ܹ� 
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
