# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys
import datetime
import time

import bluetooth._bluetooth as bluez
from socket import *

serverName='ec2-18-216-177-151.us-east-2.compute.amazonaws.com'
serverPort=7777

dev_id=0
try:
    sock=bluez.hci_open_dev(dev_id)
    print "ble thread started"

except:
	print "error accessing bluetooth device..."
    	sys.exit(1)

blescan.hci_le_set_scan_parameters(sock)
blescan.hci_enable_le_scan(sock)

clientSocket=socket(AF_INET, SOCK_STREAM)
print('socket created\n')
clientSocket.connect((serverName, serverPort))
print('connected\n')

beaconUUID1 = "1111111111";
beaconUUID2 = "2222222222";
beaconUUID3 = "3333333333";

while True:
    returnedList =blescan.parse_events(sock, 10)
    print "----------"
    for beacon in returnedList:
        if beaconUUID1 in beacon or beaconUUID2 in beacon or beaconUUID3 in beacon:
            timestamp = datetime.datetime.now()
            beaconString=repr(beacon)+",no.3,"+str(timestamp)+"\n"
            print beacon
            clientSocket.sendto(beaconString.encode(), (serverName, serverPort))
            time.sleep(2)

clientSocket.close()

