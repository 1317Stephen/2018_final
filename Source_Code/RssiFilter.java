
public class RssiFilter
{
	private static float PROCESS_NOISE_DEFAULT = 0.008f;

	int beaconCount = 0;
	int []rssi;
	float meanRssi;
	float varianceRssi;
	Beacon []beacon;
	
	public RssiFilter(int []rssi)
	{
		this.rssi = rssi;
	}
	
	public RssiFilter(Beacon[] beacon)
	{
		this.beacon = beacon;
	}
	public void getherRssi()
	{
		int i=0;
		int maxGetheredSignal = 500;
		int []rssi = new int[500];
		Beacon []beacon = this.beacon;
		while(i< maxGetheredSignal)
		{
			if(beacon[i] == null)
			{
				break;
			}	
			rssi[i] = beacon[i].getRssi();
			i++;
		}
		this.beaconCount = i;
		this.rssi = rssi;
	}
	public float calculateVaianceRssi()
	{
		int i=0;
		float rssiMean =  calculateMeanRssi();
		float squaredSumRssi = 0;
		for(i=0; i<this.beaconCount; i++)
		{
			squaredSumRssi	 += Math.pow( (this.rssi[i]-rssiMean), 2 );
		}
		this.varianceRssi = squaredSumRssi / this.beaconCount;
		return squaredSumRssi / this.beaconCount;
	}
	public float calculateMeanRssi()
	{
		int i=0;
		float meanRssi = 0;
		int sumRssi = 0;
		for(i=0; i<this.beaconCount; i++)
		{
			sumRssi = sumRssi + this.rssi[i];
		}
		meanRssi = sumRssi/this.beaconCount;
		this.meanRssi = meanRssi;
		return meanRssi;
	}
	public float calculateKalmanRssi(int rssi, float meanRssi, float varianceRssi)
	{
		int i=0;

		float kalmanGain;
		float errorCovarianceRssi;
		float lastErrorCovarianceRssi = 1;
		float estimatedRssi = meanRssi;
		for(i=0; i<this.beaconCount; i++)
		{
			kalmanGain = lastErrorCovarianceRssi / (lastErrorCovarianceRssi + varianceRssi);
			estimatedRssi = estimatedRssi + (kalmanGain * (rssi - estimatedRssi) );
			errorCovarianceRssi = (1 - kalmanGain) * lastErrorCovarianceRssi;
			lastErrorCovarianceRssi = errorCovarianceRssi + PROCESS_NOISE_DEFAULT;
			
		}
		return estimatedRssi;
	}	
	
}
