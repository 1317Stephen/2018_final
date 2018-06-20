
//only  use> BeaconDistanceCal.calculateDistance();
//rssi&tx -> distance
public class BeaconDistanceCal 
{    
	public static final float PATH_LOSS_PARAMETER_INDOOR = 1.7f;
	public static final int SIGNAL_LOSS_AT_ONE_METER =-41;

	public static float calculateDistanceIndoor(float rssi, float txRssi) 
	{
		return (float) Math.pow(10, (txRssi - rssi) / (10 * PATH_LOSS_PARAMETER_INDOOR));
	}
}

