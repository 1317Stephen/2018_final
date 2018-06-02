

public class DistanceAverage
{
	int distanceCount; 
	float []getherDistances;
	
	public DistanceAverage(float []getherDistances, int distanceCount)
	{
		this.getherDistances = getherDistances;
		this.distanceCount = distanceCount;
	}
	
	public float calculateAverageDistance()
	{
		float averageDistance = 0;
		float sumDistances = 0;
		for(int i=0; i<distanceCount; i++)
		{
			sumDistances = sumDistances + getherDistances[i];
		}
		averageDistance = sumDistances / this.distanceCount;
		return averageDistance;
	}
	
	
}