import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Beacon
{
	protected int minor;
        protected String message;
        protected String uuid;
        protected String macAddress;
        protected int rssi;
        protected int calibratedRssi; 
        protected int calibratedDistance; 
        protected int transmissionPower;
        protected float distance; 
        protected String timestamp;
	protected String configuration;

	public Beacon()
	{
	}
        public Beacon(String message)
        {
                this.message=message;
                separateData();
        }
        public void separateData()
        {

		String[] data;
                this.message=this.message.replace("'","");
                this.message=this.message.replace(" ","");

                data=this.message.split(",");
                this.uuid=data[1];

		this.minor = Integer.parseInt( data[3] );
		this.rssi = Integer.parseInt(data[5]);
		this.transmissionPower = Integer.parseInt(data[4]);

                this.macAddress=data[6];
		this.timestamp = data[7];
        }


	public void setMinor(int minor)
	{
		this.minor = minor;
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
	public int getMinor()
	{
		return this.minor;
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





