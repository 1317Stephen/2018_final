# 2018_final

<class description>
    - Server6에서
 
1. Beacon: SERVER에서 정보 취득
2. BeaconDistanceCal: rssi & tx power 로 거리 계산
3. BeaconSql: DB에 저장
4. Location: (lagtude, longitude) 혹은 (x,y)
5. WriteHTML: db에 저장된 uuid, timestamp, 거리를 꺼내와서 html에 실시간을 적는다.
    http://ec2-18-216-177-151.us-east-2.compute.amazonaws.com/test.html 
6. FourCircleIntersection: 라즈베리4개 위치와 그에 따른 거리 4개  input으로 location(x,y) 계산
     

