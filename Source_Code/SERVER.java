import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	int countBeacon;
	Beacon []getheredBeacon;
	
	
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
	public int getCountBeacon()
	{
		return this.countBeacon;
	}
	public Beacon[] getGetheredBeacon()
	{
		return this.getheredBeacon;
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
		
		Beacon beacon;
		BeaconSql beaconSql;
		
		
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

		double width= 7;
		double height= 6.2;
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
		
		int countTime = 1;
		int calculationTime = 5;
		int calculationInterval = 3;
		
		int serverBeginSec = 0;
                try
                {
                        IS=SS.getInputStream();
                        OS=SS.getOutputStream();
                        in = new BufferedReader(new InputStreamReader(IS));
                        out = new PrintWriter(OS,true);

			beaconSql = new BeaconSql();
			

			while (  ( message=in.readLine())  !=null  )
			{

				out.println(message);
				System.out.println("recv: "+message);
				
				beacon = new Beacon(message);
				getheredBeacon[countBeacon] = beacon;
				
				this.countBeacon = countBeacon;
				this.getheredBeacon = getheredBeacon;
				countBeacon++;
				
				
				currentSec = Integer.parseInt( beacon.getTimestamp().substring(16,18) );
				
				if( this.startTimeStamp.length()==0 || this.startTimeStamp.length()==1)
				{
					serverBeginSec = 0;
				}
				else
				{
					serverBeginSec = Integer.parseInt( this.startTimeStamp.substring(16,18) );
				}
				
				if( currentSec%10 == 9 )  
				{
					rssiFilter = new RssiFilter(getheredBeacon);
						
					rssiFilter.getherRssi();
					
					System.out.println("mean: "+ rssiFilter.calculateMeanRssi());
					System.out.println("var: "+ rssiFilter.calculateVaianceRssi());
					
					for(i=0; i<countBeacon; i++)
					{
						getheredRssi[i] = rssiFilter.calculateKalmanRssi(getheredBeacon[i].getRssi(), rssiFilter.calculateMeanRssi(), rssiFilter.calculateVaianceRssi() );
						getheredBeacon[i].setRssi((int)getheredRssi[i]);

						if( getheredBeacon[i].getRssi() < -80 )
                                                {
                                                        getheredBeacon[i].setTransmissionPower((int)rssiFilter.calculateMeanRssi());
                                                }
				
						getheredBeacon[i].setDistance(BeaconDistanceCal.calculateDistanceIndoor(getheredBeacon[i].getRssi(), getheredBeacon[i].getTransmissionPower()));
						if( getheredBeacon[i].getDistance() > 9.3 )
						{
							getheredBeacon[i].setDistance((float)9.3);
						}
						if( countBeacon > 10 )
						{
							beaconSql.insertBeacon(getheredBeacon[i]);	
						}
					}
					for(raspberryIndex=0; raspberryIndex<4; raspberryIndex++)
					{
						getherDistancesAVE[raspberryIndex] = beaconSql.getherFourDistances(macAd[raspberryIndex], countBeacon);	
					}
					calculateLocation = new FourCircleIntersection(width, height, getherDistancesAVE);
					calculateLocation.calcalateIntersection();
					location = new Location(calculateLocation.getIntersectionLocatoin().getLatitude(), calculateLocation.getIntersectionLocatoin().getLongitude() );

					if( (!Double.isNaN(location.getLatitude()))  && (!Double.isNaN(location.getLongitude())) )
					{
						coordinateAreaAlphabet = new CoordinateToArea(width, height, 3, 3);
						coordinateAreaAlphabet.settingArea();
		
						if( location.getLatitude() <0 )
						{
							location.setLatitude(0);
						}
						if( location.getLongitude() <0 )
						{
							location.setLongitude(0);
						}
						if( location.getLatitude() >= width )
						{
							location.setLatitude(width-0.1);
						}
						if( location.getLongitude() >= height )
						{
							location.setLongitude(height-0.1);
						}
					

						beaconSql.insertLocation( uuid[uuidIndex], beacon.getTimestamp(),  location, coordinateAreaAlphabet.coordinateToAreaNumber(location)+"" );
						beaconSql.updataIsCalculateLocationTrue( uuid[uuidIndex], beacon.getTimestamp());
			
					}


					countBeacon = 0;
				}
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
	int startSec = 0;

        connectThread(ServerSocket MSS)
        {
                this.MSS = MSS;
        }
        @Override
        public void run()
        {

		Timestamp beginTimestamp ;
		Timestamp currentTimestamp;
		boolean isCalculate = false;
		Socket serverSocket;
		userThread ust;
		BeaconSql beaconSql;
		long startSec = 0;
		long currentSec = 0;
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

					beginTimestamp = new Timestamp(System.currentTimeMillis());
					startSec = beginTimestamp.getTime();

				}

				currentTimestamp = new Timestamp(System.currentTimeMillis());
				currentSec = currentTimestamp.getTime();

				if( ( (currentSec-startSec)%10) != 9 )
				{
					ust = new userThread(serverSocket, count, startTimeStamp,isCalculate); 
					ust.start();
				}

                                count++;
                        }
                }

                catch (IOException e)
                {
                        System.out.println("--SERVER CLOSE");
                }
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








