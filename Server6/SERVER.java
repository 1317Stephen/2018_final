import java.util.Arrays;	
import java.lang.*;
import java.io.*;
import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class userThread extends Thread
{
	boolean isCalculate = false;
        Socket SS;
        int ID;
	String startTimeStamp;

        userThread(Socket SS, int ID)
        {
                this.SS = SS;
                this.ID = ID;
        }
	userThread(Socket SS, int ID, String startTimeStamp)
        {
                this.SS = SS;
                this.ID = ID;
		this.startTimeStamp = startTimeStamp;
        }
	userThread(Socket SS, int ID, String startTimeStamp, boolean isCalculate)
	{
		this.SS = SS;
                this.ID = ID;
                this.startTimeStamp = startTimeStamp;
		this.isCalculate = isCalculate;
	}
	public boolean getIsCalculate()
	{
		return this.isCalculate;
	}
	@Override
        public void run()
        {
		int i=0;
		int countBeacon = 0;
		int patternNumber=1;
                String message;
		float []locationDistances  = new float[4];
                String output="Welcome";
		
		Beacon test_beacon;
		BeaconSql beaconSql;
		Beacon test_beacon_inDB;
		
		String []uuid = new String[3];
		String []macAd = new String[4];

		float []getherDistances = new float[100];
		int distanceCount=0;
		float []getherDistancesAVE = new float[4];

		String beginTimeStamp = "";
		String endTimeStamp = "";
		String locationUUID = "";
		String locationTimeStamp = "";
		int timeInterval = 10;
		int currentSec = 0;
		int latestTimeWithLocation = 0;

	
		int uuidIndex = 0;
		uuid[0]="11111111111111111111111111111111";
		uuid[1]="22222222222222222222222222222222";
		uuid[2]="33333333333333333333333333333333";
		
		int raspberryIndex =0;
		macAd[0] = "no.1";
		macAd[1] = "no.2";
		macAd[2] = "no.3";
		macAd[3] = "no.4";

		double width= 8;
		double height= 10;
		Location location;
		DistanceAverage calculateDistanceAve;	
		FourCircleIntersection calculateLocation;

		CoordinateToArea coordinateAreaAlphabet;
	
		InputStream IS;
		OutputStream OS;
		BufferedReader in;
		PrintWriter out;
		
		RssiFilter rssiFilter;
		float []getheredRssi = new float[200];
		Beacon []getheredBeacon = new Beacon[200];
		
                try
                {
                        IS=SS.getInputStream();
                        OS=SS.getOutputStream();
                        in = new BufferedReader(new InputStreamReader(IS));
                        out = new PrintWriter(OS,true);

			beaconSql = new BeaconSql();


                        while ((message=in.readLine())!=null)
                        {
                                out.println(message);
				System.out.println("recv: "+message);
				
                                test_beacon=new Beacon(message);
                                test_beacon.setDistance(BeaconDistanceCal.calculateDistanceIndoor(test_beacon.getRssi(), test_beacon.getTransmissionPower()) );
		
				getheredBeacon[countBeacon] = test_beacon;

//				System.out.println(ID+":rssi in Beacon:"+test_beacon.getRssi());
//				System.out.println(ID+":distance in Beacon:"+test_beacon.getDistance());
				
				
				countBeacon++;
				
				if(countBeacon> 30)
				{
					this.isCalculate = true;			
					rssiFilter = new RssiFilter(getheredBeacon);
						
					rssiFilter.getherRssi();
					
					System.out.println("mean: "+ rssiFilter.calculateMeanRssi());
					System.out.println("var: "+ rssiFilter.calculateVaianceRssi());			
					for(i=0; i<countBeacon; i++)
					{

																System.out.println(ID+":rssi in Beacon:"+getheredBeacon[i].getRssi());
						System.out.println(ID+":distance in Beacon:"+getheredBeacon[i].getDistance());


						getheredRssi[i] = rssiFilter.calculateKalmanRssi(getheredBeacon[i].getRssi(), rssiFilter.calculateMeanRssi(), rssiFilter.calculateVaianceRssi() );
						getheredBeacon[i].setRssi((int)getheredRssi[i]);
						getheredBeacon[i].setDistance(BeaconDistanceCal.calculateDistanceIndoor(getheredBeacon[i].getRssi(), getheredBeacon[i].getTransmissionPower()));
					
						System.out.println(ID+":rssi in Beacon(F):"+getheredBeacon[i].getRssi());
						System.out.println(ID+":distance in Beacon(F):"+getheredBeacon[i].getDistance());
				
				
						beaconSql = new BeaconSql();
//						beaconSql.insertBeacon(getheredBeacon[i]);	
					}
				
					beginTimeStamp = getheredBeacon[0].getTimestamp();
					endTimeStamp = getheredBeacon[countBeacon-1].getTimestamp();
						
					countBeacon=0;
//					for(uuidIndex=0; uuidIndex<3; uuidIndex++)
//					{
						for(raspberryIndex=0; raspberryIndex<4; raspberryIndex++)
						{
							getherDistances = new float[150];
							getherDistances = beaconSql.getherDistances(uuid[uuidIndex], macAd[raspberryIndex], beginTimeStamp,  endTimeStamp );
		
							distanceCount = beaconSql.getDistanceCount();
							calculateDistanceAve = new DistanceAverage(getherDistances, distanceCount);
		
							getherDistancesAVE[raspberryIndex] = calculateDistanceAve.calculateAverageDistance();
							System.out.println("distance AVE("+raspberryIndex+"): " + getherDistancesAVE[raspberryIndex]);
		
						}
						calculateLocation = new FourCircleIntersection(width, height, getherDistancesAVE);
						calculateLocation.calcalateIntersection();
						location = new Location(calculateLocation.getIntersectionLocatoin().getLatitude(), calculateLocation.getIntersectionLocatoin().getLongitude() );

						System.out.println("locatoin:(" + location.getLatitude() + ", " + location.getLongitude() + ")");

						if( (!Double.isNaN(location.getLatitude()))  && (!Double.isNaN(location.getLongitude())) )
						{
							System.out.println("location != NaN" );
							coordinateAreaAlphabet = new CoordinateToArea(width, height, 3, 3);
							coordinateAreaAlphabet.settingArea();
		
							beaconSql.insertLocation( uuid[uuidIndex], endTimeStamp,  location, coordinateAreaAlphabet.coordinateToAreaNumber(location)+"" );
							beaconSql.updataIsCalculateLocationTrue( uuid[uuidIndex], endTimeStamp);
			
						}	
	
						this.isCalculate = false;
//					}
					
					
					
	
				}

				
			/*			
				System.out.println("start TimeStamp:"+this.startTimeStamp+", interval: "+timeInterval);
				currentSec = Integer.parseInt( test_beacon.getTimestamp().substring(16,18) );
				System.out.println("current: "+ test_beacon.getTimestamp() + ", current sec: " + test_beacon.getTimestamp().substring(16,18) +  ", to int: " + currentSec);

				if(!test_sql.getLatestTimeStampWithLocation().equals("") )
				{
					System.out.println("timeStamp(with Location): " + test_sql.getLatestTimeStampWithLocation());
					latestTimeWithLocation = Integer.parseInt( test_sql.getLatestTimeStampWithLocation().substring(16,18) );
				}

				if(  ((latestTimeWithLocation+ timeInterval )%60 <   currentSec) || (currentSec - latestTimeWithLocation < 0)  )
				{
					System.out.println("latestTimeWithLocation: "+ latestTimeWithLocation );
					while(uuidIndex < 3)
					{
						for(int i=0; i<4; i++)
						{
							getherDistances = new float[500];
																	if(latestTimeWithLocation == 0  )
							{
								getherDistances = test_sql.getherDistances(uuid[uuidIndex], macAd[i], this.startTimeStamp,  test_beacon.getTimestamp());
							}
							else
							{
								getherDistances = test_sql.getherDistances(uuid[uuidIndex], macAd[i], test_sql.getLatestTimeStampWithLocation(),  test_beacon.getTimestamp());
							}
										
							distanceCount = test_sql.getDistanceCount();
							System.out.println("uuid:" + uuid[uuidIndex] + ", rasp:" + macAd[i] + ", distance#:" + test_sql.getDistanceCount() );

																	calculateDistanceAve = new DistanceAverage(getherDistances, distanceCount);

							getherDistancesAVE[i] = calculateDistanceAve.calculateAverageDistance();
							System.out.println("AVE: " + calculateDistanceAve.calculateAverageDistance() );

						}
						
																calculateLocation = new FourCircleIntersection(width, height, getherDistancesAVE);
						calculateLocation.calcalateIntersection();
						location = new Location(calculateLocation.getIntersectionLocatoin().getLatitude(), calculateLocation.getIntersectionLocatoin().getLongitude() );
						System.out.println("Locatoin: ("+ location.getLatitude() + ", " + location.getLongitude() + ")");


						//Location timeStamp: currentTimeStamp(last time)
						//-> further more: setting the weight in timeStmap by number of distances
						if( (!Double.isNaN(location.getLatitude()))  && (!Double.isNaN(location.getLongitude())) )
						{
							coordinateAreaAlphabet = new CoordinateToArea(width, height, 3, 3);
							coordinateAreaAlphabet.settingArea();
							
							
							test_sql.insertLocation( uuid[uuidIndex], test_beacon.getTimestamp(),  location, coordinateAreaAlphabet.coordinateToAreaNumber(location)+"" );


							test_sql.updataIsCalculateLocationTrue( uuid[uuidIndex], test_beacon.getTimestamp() );
						}

						uuidIndex++;
					}
					uuidIndex=0;
				}

				for( uuidIndex = 0; uuidIndex<3; uuidIndex++)
				{
					if( test_sql.isBeaconOFF( uuid[uuidIndex]	, test_beacon.getTimestamp())  )
					{
						System.out.println("patten update(before): " + test_sql.latestPatternNumber() );
						test_sql.updateLocationPattern(  uuid[uuidIndex], test_sql.latestPatternNumber()+1 );
					}
				}
				uuidIndex=0;

				test_sql.sqlToHTML();		

			*/
                        }
			

			
			
			
			
			System.out.println("one");
                        System.out.println(ID + "> " + output);
                }
                catch (IOException e)
                {
                        System.out.println("--" + ID + " user OUT");
                }
        }
}

class connectThread extends Thread
{
        ServerSocket MSS;
        int count = 1;
	String startTimeStamp;
	boolean isCalculate = false;

        connectThread(ServerSocket MSS)
        {
                this.MSS = MSS;
        }
        @Override
        public void run()
        {
		boolean isCalculate = false;
		Socket serverSocket;
		userThread ust;
		BeaconSql beaconSql;
                try
                {
                        while (true)
                        {
                                serverSocket = MSS.accept();
                                System.out.println("--" + count + " user login");
				if(count==1)
				{
					beaconSql = new BeaconSql();
					this.startTimeStamp = beaconSql.getLatestTimeStamp();
					
				}
				
//				ust = new userThread(serverSocket, count);
				
//				ust = new userThread(serverSocket, count, startTimeStamp);
				ust = new userThread(serverSocket, count, startTimeStamp,isCalculate); 

				if(ust.getIsCalculate() == false)
				{
					ust.start();
				}

                                count++;
                        }
                }

                catch (IOException e)
                {
                        System.out.println("--SERVER CLOSE");
                }
//		catch (InterruptedException ex)
//		{
//			System.out.println("->fail sleep");
//		}
        }
	public String getStartTimeStamp()
	{
		return this.startTimeStamp;
	}
}

public class SERVER
{
        public static void main(String[] args)
        {
                Scanner input = new Scanner(System.in);
                ServerSocket MSS = null;
		connectThread cnt;
		int temp;
                try
                {
                        MSS = new ServerSocket();
                        MSS.bind(new InetSocketAddress(InetAddress.getLocalHost(), 7777));
                        System.out.println("--SERVER Waiting...");
                        cnt = new connectThread(MSS);
                        cnt.start();
                        temp = input.nextInt();
                }
                catch (Exception e)
                {
                        System.out.println(e);
                }
                try
                {
                        MSS.close();
                }
                catch (Exception e)
                {
                        System.out.println(e);
                }
        }
}







