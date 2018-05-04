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
        protected long timestamp;

        public Beacon(String message)
        {
                this.message=message;
                separateData();
                this.timestamp=System.currentTimeMillis();

        }
        public void separateData()
        {
//              String[] all_data;
                String[] data;

//              all_data=this.message.split("\n");
//              for(int i=0; i<all_data.length; i++)
//              {
//                      all_data[i]=all_data[i].replace("'","");
//                      data[i]=all_data[i].split(",");
//                      this

                this.message=this.message.replace("'","");
                this.message=this.message.replace(" ","");
//              all_data=this.message.split("'");

//              for(i=0; i<all_data.length; i++)
//              {
//              data=all_data[i].split(",");
//              this.uuid=data[1];
//              this.rssi=Integer.parseInt(data[4]);
//              this.transmissionPower=Integer.parseInt(data[5]);
//              }
                data=this.message.split(",");
                this.uuid=data[1];
                this.rssi=Integer.parseInt(data[4]);
                this.transmissionPower=Integer.parseInt(data[5]);

                this.macAddress=data[6];
        }



        public void setData(String message)
        {
                this.message=message;
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
        public long getTimestamp()
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

}




