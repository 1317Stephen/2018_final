//http://sime.tistory.com/83
//http://goppa.tistory.com/entry/MySQL-%EC%A1%B0%ED%9A%8C-%EC%93%B0%EA%B8%B0-%EC%88%98%EC%A0%95-%EC%82%AD%EC%A0%9C
import java.io.*;
import java.sql.*;
import java.util.*;
//uuid, rssi, transmissionPower, timestamp, macAD
public class BeaconSql
{
        Connection conn;
        String USERNAME="root";
        String PASSWORD="koyean9401";
        String URL="jdbc:mysql://localhost/beacon";

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
                String sql="insert into beacon values(?,?,?,?,?,?,?);";
                PreparedStatement pstmt=null;
                try
                {
                        System.out.println("try to insert in sql");
                        pstmt=conn.prepareStatement(sql);
                        pstmt.setString(1,beacon.getUuid());
                        pstmt.setInt(2,beacon.getRssi());
                        pstmt.setInt(3,beacon.getTransmissionPower());
                        pstmt.setString(4,beacon.getTimestamp());
                        pstmt.setString(5,beacon.getMacAddress());
			pstmt.setFloat(6,beacon.getDistance());
			pstmt.setString(7,beacon.getConfiguration());
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
	public void sqlToHTML()
	{
		WriteHTML write_html=new WriteHTML();
		String sql="select * from beacon;";
		PreparedStatement pstmt=null;
		ResultSet re, re_uuid, re_rssi;
		
		try
		{
			pstmt=conn.prepareStatement(sql);
			re=pstmt.executeQuery();
//			re_uuid=pstmt.executeQuery();
//			re_rssi=pstmt.executeQuery();
			write_html.writeHead();
			write_html.writeBody("uuid &nbsp;&nbsp;&nbsp; rssi &nbsp;&nbsp;&nbsp; distance <br />");
			while(re.next())
//(re_uuid.next() && re_rssi.next() )
//&& re[2].next() && re[3].next() && re[4].next() && re[5].next() )
			{
//				System.out.println("UUID");
//				System.out.println(re.getString("uuid"));				
				write_html.writeBody(re.getString("uuid"));				
				write_html.writeBody(re.getInt("rssi")+"");
				write_html.writeBody(re.getString("timeStamp")+"");
				write_html.writeBody(re.getFloat("distance")+"");
				write_html.writeBody("<br />");
			}
			write_html.writeTail();
			write_html.closeBufWriter();
			
		}
		catch(SQLException e)
		{
		}
	}
	public void selectAll()
	{
//		String tableField="UUID	Rssi		Tx		TimeStamp	MacAd		Distance \n";
		PreparedStatement pstmt=null;
		WriteHTML write_html=new WriteHTML();
		ResultSet rs;
		String table="Beacon";
		StringBuilder sb = new StringBuilder();
		String sql=sb.append("select * from"+table).append(";").toString();
		
		try
		{
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			System.out.println("before write loop");
			while(rs.next())
			{
//				write_html.writeHtml("UUID");
//				write_html=new WriteHTML("\t");
//				write_html=new WriteHTML(rs.getString("uuid"));
				
				
				
			}
			
		}
		catch(SQLException e)
		{
		}
	}
/*	public Beacon selectOne(long timeStamp )
	{
		String sql="select * from beacon where timeStamp = ?;";
		PreparedStatement pstmt=null;
		ResultSet rs;
		Beacon re = new Beacon();
		
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setFloat(4,timeStamp);
			rs=pstmt.executeQuery();
			
			if( rs.next() )
			{
				re.setUuid(rs.getString("uuid"));
				re.setRssi(rs.getInt("rssi"));
				re.setTransmissionPower(rs.getInt("transmissionPower"));
				re.setTimestamp((long) rs.getFloat("timeStamp"));
				re.setMacAddress(rs.getString("macAddress"));
			}
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
		return re;
	}
*///	public List<Beacon> selectSameMacAd(String macAddress)
//	{
//		String sql=
//	}
	

}



