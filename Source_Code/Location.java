public class Location 
{
	public static double VALUE_NOT_SET = 0;

	private double latitude = VALUE_NOT_SET;
	private double longitude = VALUE_NOT_SET;
	private double altitude = VALUE_NOT_SET;

	private long timestamp;


	public Location()
	{
	}
	public Location(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;	
		this.altitude = 0;
	}
 
	public Location(double latitude, double longitude, double altitude) 
	{
		this(latitude, longitude);
		this.altitude = altitude;
	}

	public Location(Location location) 
	{
		this(location.latitude, location.longitude, location.altitude);

	}

	public double getLatitude() 
	{
		return latitude;
	}

	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}

	public double getLongitude() 
	{
		return longitude;
	}

	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}

	public double getAltitude() 
	{
		return altitude;
	}

	public void setAltitude(double altitude) 
	{
		this.altitude = altitude;
	}

	public long getTimestamp() 
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
	}
}

