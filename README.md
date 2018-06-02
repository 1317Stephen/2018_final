# 2018_final

class description
    - Server6에서
 
1. Beacon: SERVER에서 정보 취득
2. BeaconDistanceCal: rssi & tx power 로 거리 계산
3. BeaconSql: DB에 저장, 불러온다. 
4. Location: (lagtude, longitude) 혹은 (x,y)
5. WriteHTML: db에 저장된 uuid, timestamp, 거리를 꺼내와서 html에 실시간으로 적는다.
    http://ec2-18-216-177-151.us-east-2.compute.amazonaws.com/test.html 
6. FourCircleIntersection: 라즈베리4개 위치와 그에 따른 거리 4개를 받아서 location(x,y) 계산 (IMG_7406 파일: 수학적 설명 참조)
7. DistanceAverage: time interval마다 특정 라즈베리, uuid에 대해 모은 distances 평균내기
8. CoordinateToArea: 가상의 좌표 공간 만들기 ; 전체 가로, 세로 길이와, 가로 세로 별로 나눌 칸수 받아옴 -> 전체 좌표에 Sector(Map의 칸 하나 하나) 별로 알파벳 부여 -> coordinateToAreaNumber() 함수에서 Location 받아와서 Sector의 알파벳을 반환

>>>SERVER 클래스에서 beacon정보 취득해서  db에 넣은 후 html 파일에 쓴다(신호 하나 들어올 때마다(\n 마주할 때마다)


>>>SERVER: Client 에서 정보 받기 -> Beacon에 저장 -> DB에 저장 -> 만약 Location 찍은지 time interval 이상 지났으면 DB에서 특정 uuid, raspberry 마다 distances 모아서 평균 구하고, 구한 평균 4개로 Location 도출 -> Location을 DB에 저장
