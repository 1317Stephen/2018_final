// String message
//e0:e5:cf:23:b9:42, 11111111111111111111111111111111, 202, 4, -59, -53
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Beacon
{
        protected String message;
        protected String uuid;
        protected String macAddress;
        protected int rssi; // in dBm
        protected int calibratedRssi; // in dBm
        protected int calibratedDistance; // in m
        protected int transmissionPower; // in dBm
        protected float distance; // in m
//      protected Lacation location;
        protected String timestamp;
	protected String configuration;

	public Beacon()
	{
	}
        public Beacon(String message)
        {
                this.message=message;
                separateData();
//                this.timestamp=System.currentTimeMillis();

        }
        public void separateData()
        {

		String[] data;
                this.message=this.message.replace("'","");
                this.message=this.message.replace(" ","");

                data=this.message.split(",");
//		System.out.println(data[7]);
                this.uuid=data[1];

		this.rssi = Integer.parseInt(data[5]);
		this.transmissionPower = Integer.parseInt(data[4]);

//                this.rssi=Integer.parseInt(data[4]);
//                this.transmissionPower=Integer.parseInt(data[5]);

                this.macAddress=data[6];
//		this.configuration = data[8];
		this.timestamp = data[7];
        }


	
        public void setData(String message)
        {
                this.message=message;
        }
	public void setUuid(String uuid)
	{
		this.uuid=uuid;
	}
	public void setRssi(int rssi)
	{
		this.rssi=rssi;
	}
	public void setTransmissionPower(int transmissionPower)
	{
		this.transmissionPower=transmissionPower;
	}
	public void setTimestamp(String timestamp)
	{
		this.timestamp=timestamp;
	}
        public void setDistance(float distance)
        {
                this.distance=distance;
        }
        public void setMacAddress(String macAddress)
        {
                this.macAddress=macAddress;
        }
	
	
        public String getUuid()
        {
                return uuid;
        }
        public int getRssi()
        {
                return rssi;
        }
        public int getTransmissionPower()
        {
                return transmissionPower;
        }
        public String getTimestamp()
        {
                return timestamp;
        }
        public float getDistance()
        {
                return distance;
        }
        public String getMacAddress()
        {
                return macAddress;
        }
	public String getConfiguration()
	{
		return configuration;
	}
}





