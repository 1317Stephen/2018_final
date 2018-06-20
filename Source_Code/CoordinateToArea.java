

public class CoordinateToArea
{
	int widthDivision;
	int heightDivision;
	int areaNumber;
	char [][]areaSector;
	double width;
	double height;
	
	public CoordinateToArea(double width, double height, int widthDivision, int heightDivision)
	{
		this.width = width;
		this.height = height;
		this.widthDivision = widthDivision;
		this.heightDivision = heightDivision;
		this.areaNumber = areaNumber * heightDivision;
	}

	public void settingArea()
	{
		//unicode: 65 == A
		/* 		areaSector 						<-> areaAlphabet
		areaSector[0][2]   areaSector[1][2]   areaSector[2][2]		G H I		 
		areaSector[0][1]    areaSector[1][1]   areaSector[2][1]		D E F
		areaSector[0][0]   areaSector[1][0]  areaSector[2][0]		A B C
		*/
		int i=0, j=0;
		int unicodeInt = 65; 
		char [][]areaSector;
		areaSector = new char[this.widthDivision][this.heightDivision];
		
		for(i=0; i<this.heightDivision; i++)
		{
			for(j=0; j<this.widthDivision; j++)
			{
				areaSector[j][i]= (char)unicodeInt;
				unicodeInt++;
			}
		}
		this.areaSector = areaSector;
	}
	public char coordinateToAreaNumber(Location location)
	{
		int widthNumber=0;
		int heightNumber=0;
		widthNumber = (int)( location.getLatitude() / (this.width/this.widthDivision) );
		heightNumber = (int)( location.getLongitude() / (this.height/this.heightDivision) );
		return this.areaSector[widthNumber][heightNumber];
	}
	
	
}
