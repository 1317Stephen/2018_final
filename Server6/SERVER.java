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
        Beacon test_beacon;
        BeaconSql test_sql;
	Beacon test_beacon_inDB;

        userThread(Socket SS, int ID)
        {
                this.SS = SS;
                this.ID = ID;
        }
	@Override
        public void run()
        {
                String message;
                String output="Welcome";
                try
                {
                        InputStream IS=SS.getInputStream();
                        OutputStream OS=SS.getOutputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(IS));
                        PrintWriter out = new PrintWriter(OS,true);

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

				System.out.println("write-server");
				test_sql.sqlToHTML();				
				
//				test_beacon_inDB=test_sql.selectOne(test_beacon.getTransmissionPower());
//				System.out.println("In DB(tx):"+ test_beacon_inDB.getTransmissionPower());


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
        int Count = 1;

        connectThread(ServerSocket MSS)
        {
                this.MSS = MSS;
        }
        @Override
        public void run()
        {
                try
                {
                        while (true)
                        {
                                Socket SS = MSS.accept();
                                System.out.println("--" + Count + " user login");
                                userThread ust = new userThread(SS, Count);
                                ust.start();
                                Count++;
                        }
                }

                catch (IOException e)
                {
                        System.out.println("--SERVER CLOSE");
                }
        }
}

public class SERVER
{
        public static void main(String[] args)
        {
                Scanner input = new Scanner(System.in);
                ServerSocket MSS = null;
                try
                {
                        MSS = new ServerSocket();
                        MSS.bind(new InetSocketAddress(InetAddress.getLocalHost(), 7777));
                        System.out.println("--SERVER Waiting...");
                        connectThread cnt = new connectThread(MSS);
                        cnt.start();
                        int temp = input.nextInt();
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





