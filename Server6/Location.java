public class Location {
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
 
    public Location(double latitude, double longitude, double altitude) {
        this(latitude, longitude);
        this.altitude = altitude;
    }

    public Location(Location location) {
        this(location.latitude, location.longitude, location.altitude);
  //      this.timestamp = location.timestamp;
    }
    /**
     * Calculates the distance between the current and the specified location in meters.
     * Elevation / altitude will be ignored.
     *
     * @return distance in meters
     */

/**
 * Shifts the current {@link #latitude} and {@link #longitude} based on the specified distance
 * and angle.
 *
 * @param distance in meters
 * @param angle    in degrees [0Â°-360Â°)
 **/

  

    /**
     * Creates a copy of the current instance and calls {@link #shift(double, double)} on that copy.
     *
     * @param distance in meters
     * @param angle    in degrees (0Â°-360Â°)
     */


    /**
     * Calculates the angle between two locations in degrees. The result ranges from [0,360),
     * rotating CLOCKWISE, 0 and 360 degrees represents NORTH, 90 degrees represents EAST. This is
     * also referred to as bearing.
     *
     * Calculation was derived from this <a href="http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/">
     * Bearing Calculation formula.</a>
     *
     * @param centerLocation Location we are rotating around.
     * @param targetLocation Location we want to calculate the angle to.
     * @return angle in degrees
     */


    /*
        Getter & Setter
     */

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
 //       this.timestamp = System.currentTimeMillis();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
   //     this.timestamp = System.currentTimeMillis();
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
    //    this.timestamp = timestamp;
    }
}

