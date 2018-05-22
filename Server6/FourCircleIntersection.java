// input: 4 raspberry location, 4 distance, 1 timestamp, 
// output: 


public class FourCircleIntersection
{
	//coordinate: just imitational coordinates of received locations: ((0,0), (0,a), (b,0), (a,b)) 
	double width;
	double height;
	long timestamp;
	float[] distances;
//	Coordinate []fourInputCoordinates;
	Location[] fourInputLocations;
//	Coordinate intersectionCoordinate;
	Location intersectionLocation;
	
/*	public static void main(String[] args)
	{
		float []dist = new float[4];
		Location []loc = new Location[4];
		FourCircleIntersection testIntersection;
		
		loc[0] = new Location(0,0);
		loc[1] = new Location(20,0);
		loc[2] = new Location(0,15);
		loc[2] = new Location(20,15);
		
		dist[0]=3;
		dist[1]=5;
		dist[2]=3;
		dist[3]=2;
		testIntersection = new FourCircleIntersection(loc, dist);
		testIntersection.setHeight(5);
		testIntersection.setWidth(8);
//		testIntersection.settingSquareMap();	
		testIntersection.calcalateIntersection();
		System.out.println("("+testIntersection.getIntersectionLocatoin().getLatitude() + "," + testIntersection.getIntersectionLocatoin().getLongitude() + ")");
	}
*/
	public FourCircleIntersection(double width, double height, float[] distances)
	{
		this.width = width;
		this.height = height;
		this.distances = distances;
	}
	public FourCircleIntersection(Location[] fourInputLocations, float[] distances)
	{
		this.fourInputLocations = fourInputLocations;
		this.distances = distances;
//		this.timestamp = timestamp;
	}
	
	public void settingSquareMap()
	{
		double height = 0;
		double width = 0; 
		//case of straight input data
//		fourInputCoordinates = new Coordinate[4];
//		fourInputCoordinates[0] = new Coordinate(0,0);
		for(int i=1; i<4; i++)
		{
			if(fourInputLocations[0].getLatitude() == fourInputLocations[i].getLatitude() )
			{
				height = Math.abs( fourInputLocations[0].getLongitude() - fourInputLocations[i].getLatitude() );
			}
			if(fourInputLocations[0].getLongitude() == fourInputLocations[i].getLatitude() )
			{
				width = Math.abs(  fourInputLocations[0].getLatitude() - fourInputLocations[i].getLatitude() );
			}
		}
		setHeight(height);
		setWidth(width);
	}
	public void calcalateIntersection()
	{
		double x_1 = 0, x_2 = 0;
		double y_1 = 0, y_2 = 0;
		Location intersectionLocation = new Location();
		
		x_1 = ( Math.pow(distances[2], 2) - Math.pow(distances[3],2) + Math.pow(width, 2) ) / (2 * width);
		x_2 = ( Math.pow(distances[0], 2) - Math.pow(distances[1],2) + Math.pow(width, 2) ) / (2 * width);
		y_2 = ( Math.pow(distances[1], 2) - Math.pow(distances[3],2) + Math.pow(height, 2) ) / (2 * width);
		y_2 = ( Math.pow(distances[0], 2) - Math.pow(distances[2],2) + Math.pow(height, 2) ) / (2 * width);
		intersectionLocation.setLatitude( (x_1+x_2)/2);
		intersectionLocation.setLongitude( (y_1+y_2)/2 );
		
		this.intersectionLocation = intersectionLocation;
	}
	
	
	
	public void setHeight(double height)
	{
		this.height = height;
	}
	public void setWidth(double width)
	{
		this.width = width;
	}
	
	public Location getIntersectionLocatoin()
	{
		return intersectionLocation;
	}
	
}
