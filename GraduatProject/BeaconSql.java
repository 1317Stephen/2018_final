import java.sql.*;
import java.util.*;
//uuid, rssi, transmissionPower, timestamp, macAD
public class BeaconSql
{
        Connection conn;
        String USERNAME="root";
        String PASSWORD="koyean9401";
        String URL="jdbc:mysql://localhost/beacon";
//      String URL="jdbc:driver://ec2-18-216-177-151.us-east-2.compute.amazonaws.com:7777/beacon=root&password=koyean9401";



        public BeaconSql()
        {
                try
                {
                        System.out.println("try to Connect MYSQL");
                        Class.forName("com.mysql.jdbc.Driver");
                        conn=DriverManager.getConnection(URL, USERNAME, PASSWORD);
                        System.out.println("->Connection:MYSQL");
                }
                catch(ClassNotFoundException e)
                {
                        e.printStackTrace();
                        System.out.println("<Error>There's no Class! \b");
                }
                catch(SQLException e)
                {
                        e.printStackTrace();
                        System.out.println("<Error>Fail to Conneting SQL ! \b");
                }
        }
        public void insertBeacon(Beacon beacon)
        {
                String sql="insert into beacon values(?,?,?,?,?);";
                PreparedStatement pstmt=null;
                try
                {
                        System.out.println("try to insert in sql");
                        pstmt=conn.prepareStatement(sql);
                        pstmt.setString(1,beacon.getUuid());
                        pstmt.setInt(2,beacon.getRssi());
                        pstmt.setInt(3,beacon.getTransmissionPower());
                        pstmt.setFloat(4,beacon.getTimestamp());
                        pstmt.setString(5,beacon.getMacAddress());
                        pstmt.executeUpdate();
                }
                catch(SQLException e)
                {
                        e.printStackTrace();
                }
                finally
                {
                        try
                        {
                                if(pstmt !=null && !pstmt.isClosed())
                                {
                                        pstmt.close();
                                }
                        }
                        catch(SQLException e)
                        {
                                e.printStackTrace();
                        }
                }
        }

}


