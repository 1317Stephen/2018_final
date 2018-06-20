import java.sql.*;
import java.util.*;

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
                        Class.forName("com.mysql.jdbc.Driver");
                        conn=DriverManager.getConnection(URL, USERNAME, PASSWORD);
			this.stmt = conn.createStatement();
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
	public float[] getherDistancesFromMinor(String raspberryNumber, int minor)
	{
		int distanceIndex = 0;
		float []distances = new float[500];
		
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		ResultSet rs;
		String sql = sb.append("select distance from beacon where raspberryNumber= '").append(raspberryNumber).append("' and minor = ").append(minor).append(" and isCalculateLocation = false order by timeStamp DESC;").toString();
		
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				distances[distanceIndex] = rs.getFloat("distance");
				distanceIndex++;
				if(distanceIndex > 499)
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
        public void insertBeacon(Beacon beacon)
        {
                String sql="insert into beacon values(?,?,?,?,?,?,?,?);";
                PreparedStatement pstmt=null;
                try
                {
                        pstmt=conn.prepareStatement(sql);
                        pstmt.setString(1,beacon.getUuid());
                        pstmt.setInt(2,beacon.getRssi());
                        pstmt.setInt(3,beacon.getTransmissionPower());
                        pstmt.setString(4,beacon.getTimestamp());
                        pstmt.setString(5,beacon.getMacAddress());
			pstmt.setFloat(6,beacon.getDistance());
			pstmt.setBoolean(7,false);
			pstmt.setInt(8, beacon.getMinor());
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
		String sql = sb.append("update location set pattern=").append(patternNumber).append(" where uuid='").append(uuid).append("' and pattern = 0;").toString();
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
	public boolean isBeaconOFF(String uuid, String currentTimeStamp)
	{
		int i=0;
		int sec=0;
		int timeInterval = 0;
		int offTimeInterval = 5;
		boolean isBeaconOff = false;
		String lastTimeStamp = "";
		String []lastTwoTimeStamp = new String[3];
		int lastTwoSecond[] = new int[3];
		StringBuilder sb = new StringBuilder();
		
		String sql = sb.append("select timeStamp from beacon where uuid ='").append(uuid).append("' order by timeStamp DESC;").toString();
		
		PreparedStatement pstmt = null;
		ResultSet rs;
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
				
			while( rs.next())
			{
				lastTimeStamp = rs.getString("timeStamp");
				if( lastTimeStamp != "" )
				{
					break;
				}					
			
			}	
			if( lastTimeStamp != "")
			{	
				timeInterval = Integer.parseInt(currentTimeStamp.substring(13,15)) - Integer.parseInt(lastTimeStamp.substring(13,15));
	
				if( timeInterval  > offTimeInterval )
				{
					//case : 56 - 50
					isBeaconOff = true;
				}
				else if( (timeInterval<0) &&  ( (60 + timeInterval)>offTimeInterval)  )
				{
					//case: 0 - 55
					isBeaconOff = true;
				}
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
			pstmt.setDouble(2, location.getLatitude());
			pstmt.setDouble(3, location.getLongitude());
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
	public float[] getherRssiFromMinor(String raspberryNumber, int minor)
	{
		int distanceIndex = 0;
		float []distances = new float[500];
		
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		ResultSet rs;
		String sql = sb.append("select rssi from beacon where raspberryNumber= '").append(raspberryNumber).append("' and minor = ").append(minor).append(" and isCalculateLocation = false order by timeStamp DESC;").toString();
		
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				distances[distanceIndex] = rs.getFloat("rssi");
				distanceIndex++;
				if(distanceIndex > 499)
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
	
	public float[] getherDistances(String uuid, String raspberryNumber, String startTimeStamp, String endTimeStamp )
	{
		int distanceIndex = 0;
		float []distances = new float[500];
		
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
				distances[distanceIndex] = rs.getFloat("distance");
				
				distanceIndex++;
				if(distanceIndex > 499)
				{
					break;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		this.distanceCount = distanceIndex;
		return distances;
	}
	public int getDistanceCount()
	{
		return this.distanceCount;
	}
	public float getherFourDistances(String raspberryNumber, int beaconCount)
	{
		float sumDistance = 0;
		float minDistance = 0;
		int countFour=0;
		int i=0;
		float []distances = new float[beaconCount];
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("select distance,isCalculateLocation from beacon where raspberryNumber='").append(raspberryNumber).append("' order by timeStamp DESC;").toString();
		PreparedStatement pstmt = null;
		ResultSet rs;
		try
		{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				distances[i] = rs.getFloat("distance");
				sumDistance = sumDistance + distances[i];
				i++;
				if( (i >= beaconCount)  )
				{
					break;
				}
			}
			
		}
		 catch(SQLException e)
		{
			e.printStackTrace();
		}	
		minDistance = sumDistance / (i-1);
		return minDistance; 
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
			write_html.writeHead();
			write_html.writeBody("uuid &nbsp;&nbsp;&nbsp; rssi &nbsp;&nbsp;&nbsp; distance <br />");
			while(re.next())
			{		
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


}




