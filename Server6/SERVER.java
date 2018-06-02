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
	@Override
        public void run()
        {
		int patternNumber=1;
		FourCircleIntersection calculateLocation;
                String message;
		float []locationDistances  = new float[4];
                String output="Welcome";
		
		Beacon test_beacon;
		BeaconSql test_sql;
		Beacon test_beacon_inDB;
		
		String []uuid = new String[3];
		String []macAd = new String[4];
		float []getherDistances = new float[100];
		int distanceNumber=0;
		String beginTimeStamp = "";
		String endTimeStamp = "";
		String locationUUID = "";
		String locationTimeStamp = "";
		int timeInterval = 5;
		int currentSec = 0;
		int latestTimeWithLocation = 0;

	
		int uuidIndex = 0;
		uuid[0]="11111111111111111111111111111111";
		uuid[1]="22222222222222222222222222222222";
		uuid[2]="33333333333333333333333333333333";
		
		
		macAd[0] = "no.1";
		macAd[1] = "no.2";
		macAd[2] = "no.3";
		macAd[3] = "no.4";
	
		InputStream IS;
		OutputStream OS;
		BufferedReader in;
		PrintWriter out;
                try
                {
                        IS=SS.getInputStream();
                        OS=SS.getOutputStream();
                        in = new BufferedReader(new InputStreamReader(IS));
                        out = new PrintWriter(OS,true);

                        while ((message=in.readLine())!=null)
                        {
                                out.println(message);
				System.out.println("recv: "+message);
				
                                test_beacon=new Beacon(message);
                                test_beacon.setDistance(BeaconDistanceCal.calculateDistanceIndoor(test_beacon.getRssi(), test_beacon.getTransmissionPower()) );
				System.out.println(ID+":uuid in Beacon:"+test_beacon.getUuid());
				System.out.println(ID+":distance in Beacon:"+test_beacon.getDistance());
				
				test_sql=new BeaconSql();
                                test_sql.insertBeacon(test_beacon);
				System.out.println("insert DB");
				
				System.out.println("start TimeStamp:"+this.startTimeStamp+", interval: "+timeInterval);
				currentSec = Integer.parseInt( test_beacon.getTimestamp().substring(16,18) );
				System.out.println("current: "+ test_beacon.getTimestamp() + ", current sec: " + test_beacon.getTimestamp().substring(16,18) +  ", to int: " + currentSec);

				if(test_sql.getLatestTimeStampWithLocation()!= "" )
				{
					System.out.println("timeStamp(with Location): " + test_sql.getLatestTimeStampWithLocation());
					latestTimeWithLocation = Integer.parseInt( test_sql.getLatestTimeStampWithLocation().substring(17,19) );
				}
				if(  (latestTimeWithLocation+5)%60 <   currentSec  )
				{
					System.out.println("latestTimeWithLocation: "+ latestTimeWithLocation );
					while(uuidIndex < 4)
					{
						for(int i=0; i<4; i++)
						{
							getherDistances = new float[100];
							getherDistances = test_sql.getherDistances(uuid[uuidIndex], macAd[i], test_sql.getLatestTimeStampWithLocation(),  test_beacon.getTimestamp(), distanceNumber );
							System.out.println("uuid:" + uuid[uuidIndex] + ", rasp:" + macAd[i] + ", distance#:" + distanceNumber);
							
						}
						
						
						uuidIndex++;
					}
					uuidIndex=0;
				}

				

				
				test_sql.sqlToHTML();				
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
	
        connectThread(ServerSocket MSS)
        {
                this.MSS = MSS;
        }
        @Override
        public void run()
        {
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
				
				ust = new userThread(serverSocket, count, startTimeStamp);
                                ust.start();
				

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






