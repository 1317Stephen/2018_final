import java.sql.*;
import java.util.*;
//uuid, rssi, transmissionPower, timestamp, macAD
public class BeaconSql
{
        Connection conn;
        String USERNAME="root";
        String PASSWORD="koyean9401";
        String URL="jdbc:mysql://localhost/beacon";
	String locationUUID="";
	String locationTimeStamp="";
	String []locationDistances = new String[4];
	Statement stmt = null;
	int distanceCount = 0;
	
        public BeaconSql()
        {
                try
                {
                        System.out.println("try to Connect MYSQL");
                        Class.forName("com.mysql.jdbc.Driver");
                        conn=DriverManager.getConnection(URL, USERNAME, PASSWORD);
			this.stmt = conn.createStatement();
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
			pstmt.setBoolean(7,false);
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
	public void updateLocationPattern(String uuid, int patternNumber)
	{
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("update location set pattern=").append(patternNumber).append(" where uuid='").append(uuid).append("' and pattern != 0;").toString();
		PreparedStatement pstmt = null;
		ResultSet re;
		
		try
		{
			stmt.executeUpdate(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public int latestPatternNumber()
	{
		int patternNumber=0;
		String sql = "select pattern from location order by pattern DESC";
		PreparedStatement pstmt = null;
		ResultSet rs;	
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				patternNumber = rs.getInt("pattern");
			}
			
		}
		catch(SQLException e)
                {
                        e.printStackTrace();
                        System.out.println("<Error>Fail to Conneting SQL ! \b");
                }
		return patternNumber;
	}
	public boolean isBeaconOFF(String uuid)
	{
		int i=0;
		int sec=0;
		boolean isBeaconOff = false;
		String []lastTwoTimeStamp = new String[3];
		int lastTwoSecond[] = new int[3];
		String sql= "select timeStamp from location order by timeStamp;";
		PreparedStatement pstmt = null;
		ResultSet rs;
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
				
			while( rs.next())
			{
				lastTwoTimeStamp[i] = rs.getString("timeStamp");
//				lastTwoTimeStamp[1] = rs.getString("timeStamp");
				i++;
				if(i==2)break;
			}
			
				lastTwoSecond[0] = Integer.parseInt( lastTwoTimeStamp[0].substring(16,18) );
				lastTwoSecond[1] = Integer.parseInt( lastTwoTimeStamp[1].substring(16,18) );
				System.out.println("sec1:"+lastTwoSecond[0]+", sec2: "+lastTwoSecond[1]);
				sec = lastTwoSecond[1] - lastTwoSecond[0];
				System.out.println("sec:"+sec);
				if(sec > 5 || sec < 0)
				{
					System.out.print("off:");
					System.out.println(sec);
					isBeaconOff = true;
				}
			
			return isBeaconOff;
		}
                catch(SQLException e)
                {
                        e.printStackTrace();
                        System.out.println("<Error>Fail to Conneting SQL ! \b");
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
		return isBeaconOff;
	}
	public void insertLocation(String uuid, String timeStamp, Location location, String area)
	{
		String sql="insert into location(uuid, x, y, timeStamp, pattern, area) values(?,?,?,?,?,?);";
		PreparedStatement pstmt=null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uuid);
			System.out.println("uuid in location");
			pstmt.setDouble(2, location.getLatitude());
			System.out.println("x in location");
			pstmt.setDouble(3, location.getLongitude());
			System.out.println("y in location");
			pstmt.setString(4, timeStamp);
			pstmt.setInt(5, 0);
			pstmt.setString(6, area);
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
	public String getLocationUUID()
	{
		return locationUUID;
	}
	public String getLocationTimeStamp()
	{
		return locationTimeStamp;
	}
	public String[] getLocationDistances()
	{
		return locationDistances;
	}
	public void updataIsCalculateLocationTrue(String uuid, String timeStamp)
	{
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("update beacon set isCalculateLocation=1 where uuid='").append(uuid).append("' and timeStamp='").append(timeStamp).append("';").toString();
		PreparedStatement pstmt = null;
		ResultSet re;
		try
		{
			stmt.executeUpdate(sql);
			
		}
		 catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getLatestTimeStampWithLocation()
	{
		PreparedStatement pstmt;
		ResultSet rs;
		
		String latestTimeStamp = "";
		String sql = "select timeStamp from beacon where isCalculateLocation=true order by timeStamp DESC;";
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				latestTimeStamp = rs.getString("timeStamp");
				if(latestTimeStamp != "")
				{
					break;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return latestTimeStamp;
	}
	public String getLatestTimeStamp()
	{
		PreparedStatement pstmt;
		ResultSet rs;
		
		String latestTimeStamp = new String();
		String sql = "select timeStamp from beacon order by timeStamp DESC;";
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				latestTimeStamp = rs.getString("timeStamp");
				if(latestTimeStamp != null)
				{
					break;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return latestTimeStamp;
	}
	void int getDistanceCount()
	{
		return this.distanceCount;
	}
	public float[] getherDistances(String uuid, String raspberryNumber, String startTimeStamp, String endTimeStamp)
	{
		int distanceIndex = 0;
		float []distances = new float[100];
		
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		ResultSet rs;
		String sql = sb.append("select timeStamp, distance from beacon where ").append("uuid = '").append(uuid).append("' and raspberryNumber = '").append(raspberryNumber).append("' and").append(" timeStamp > '").append(startTimeStamp).append("' and timeStamp <= '").append(endTimeStamp).append("';").toString();
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();	
			while(rs.next())
			{
				System.out.println("distance: " + rs.getFloat("distance") );
				distances[distanceIndex] = rs.getFloat("distance");
				
				distanceIndex++;
				if(distanceIndex >100)
				{
					break;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		Systme.out.println("distanceIndex: " + distanceIndex);
		this.distanceCount = distanceIndex;
		return distances;
	}
	
	public float[] getherFourDistances(String uuid, String timeStamp)
	{
		int countFour=0;
		int i=0;
		float []distances = new float[4];
		StringBuilder sb = new StringBuilder();
//		String sql = sb.append("select distance from beacon where uuid='").append(uuid).append("' and timeStamp='").append(timeStamp).append("';").toString();

		String sql = sb.append("select distance from beacon where uuid='").append(uuid).append("' order by timeStamp DESC;").toString();
		PreparedStatement pstmt = null;
		ResultSet rs;
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				distances[i] = rs.getFloat("distance");
				i++;
				if(i>3)
				{
					break;
				}
			}
			
		}
		 catch(SQLException e)
		{
			e.printStackTrace();
		}	
		return distances; 
	}
	

	public boolean is4RaspberrySignalDB()
{
	int i=0;
	boolean isFourSignal = false;
	String sql = "select  uuid,timeStamp, distance, count(*) from beacon where  isCalculateLocation=false group by uuid, timeStamp having count(*)=4 order by timeStamp;";
	PreparedStatement pstmt = null;
	ResultSet rs;
	try
	{
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if(rs.next())
		{
			this.locationUUID = rs.getString("uuid");
			this.locationTimeStamp =rs.getString("timeStamp");
			System.out.println("time:"+locationTimeStamp);	
			isFourSignal = true;
			i++;
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
	return isFourSignal;
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
				write_html.writeBody(re.getBoolean("isCalculateLocation")+"");
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

}




