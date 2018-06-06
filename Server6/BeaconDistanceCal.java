
//only  use> BeaconDistanceCal.calculateDistance();
//rssi&tx -> distance
public class BeaconDistanceCal 
{    
	public static final float PATH_LOSS_PARAMETER_INDOOR = 3.0f;
	public static final int SIGNAL_LOSS_AT_ONE_METER =-41;

	public static float calculateDistanceIndoor(float rssi, float txRssi) 
	{
		return (float) Math.pow(10, (txRssi - rssi) / (10 * PATH_LOSS_PARAMETER_INDOOR));
	}

/*	public static float calculateDistanceWithoutAltitudeDeltaToFloor(Beacon beacon, float rssi) 
	{
		double altitude = beacon.getLocation().getAltitude();
		float distance = calculateDistanceTo(beacon, rssi);
		
		if (altitude > 0 && distance > (altitude * 2)) 
		{
			double delta = Math.pow(distance, 2) - Math.pow(altitude, 2);
			return (float) Math.sqrt(delta);
		} 
		else
		{
			return distance;
		}
	}
*/}

