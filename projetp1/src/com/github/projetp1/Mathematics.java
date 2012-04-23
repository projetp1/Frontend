/**=====================================================================*
| This file declares the following classes:
|    Mathematics
|
| Description of the class Mathematics :
|	  Use to calculate all the informations that the program needs
	  e.g : Coordinates, Magnitude, Direction ....
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @kAUtor : Diego Antognini
| @version : 1.0
|
|
 *========================================================================*/

package com.github.projetp1;

import java.lang.Math;
import java.util.Calendar;

public class Mathematics
{
	static private final double pi = Math.PI;
	static private final double deg2rad = pi / 180.0;
	static private final double rad2deg = 180.0 / pi;

	//Constants for Julian Date
	static private final double knumber_of_dDay_in_one_dYear = 365.25;
	static private final double knumber_of_dDay_in_one_dMonth = 30.6001;// average of the dDay in one dMonth about X000 dYears
	static private final double kinitial_dYear = 4716.0;
	
	//Constants for the Object Date
	static private final double kadditionnal_dYear_of_gregorian_calendar = 0.0;
	static private final double kadditionnal_dMonth_of_Date_object = 1.0;
	static private final double kadditionnal_dDay_of_Date_object = 0.0;

	private double dHour;
	private double dMinute;
	private double dSecond;
	private double dGMT;

	private double dDay;
	private double dMonth;
	private double dYear;
	
	private double dDate_JulianCalendar;
	private double dSideral_Time;
	private double dLatitude;
	private double dLongitude;
	private double dDeclination;
	private double dAscension;

	private double dAzimuth;
	private double dHeight;

	private double dHour_Angle_Star;
	private double dAngle_Sideral_Time;
	private double dAngle_Hour;
	private double dAngle; // dAngle_Sideral_Time+dAngle_our
	
	private double X;
	private double Y;

	/**
	 * Mathematics Constructor
	 * 
	 * @param Calendate _date : Date of the computer. Will be use to calculate the Sideral Time
	 * @param double _dLat : It's the Latitude of the star's pointer
	 * @param double _Lon : It's the Longitude of the star's pointer
	 * @param double _dDec : It's the Declination of the star
	 * @param double _dAsc : It's the Ascension of the star
	 */
	public Mathematics(Calendar _date, double _dLat, double _dLon,double _dDec,double _dAsc)
	{
		this.dHour = _date.get(Calendar.HOUR_OF_DAY);
		this.dMinute = _date.get(Calendar.MINUTE);
		this.dSecond = _date.get(Calendar.SECOND);

		//Calculate the GMT
		String l_GMT = _date.getTime().toGMTString();
		String l_hour_GMT = l_GMT.substring(l_GMT.indexOf(':') - 2, l_GMT.indexOf(':'));
		this.dGMT = this.dHour - Integer.parseInt(l_hour_GMT);

		this.dDay = Mathematics.kadditionnal_dDay_of_Date_object + _date.get(Calendar.DATE);
		this.dMonth = Mathematics.kadditionnal_dMonth_of_Date_object + _date.get(Calendar.MONTH);
		this.dYear = Mathematics.kadditionnal_dYear_of_gregorian_calendar + _date.get(Calendar.YEAR);

		this.dLatitude = _dLat;
		this.dLongitude = _dLon;
		this.dDeclination = _dDec;
		this.dAscension = _dAsc;
		
		calculate_all();
	}
	
	/**
	 * calculate_all 
	 * Calculates all the informations that the program needs
	 */
	private void calculate_all()
	{
		this.dDate_JulianCalendar = calculate_JulianDate(this.dDay, this.dMonth, this.dYear, this.dHour,this.dMinute, this.dSecond);
		this.dSideral_Time = calculate_dSideral_Time(this.dDay, this.dMonth, this.dYear, this.dHour,this.dMinute, this.dSecond);

		this.dAngle_Sideral_Time = calculate_sideral_hour_dAngle(this.dSideral_Time);
		this.dAngle_Hour = calculate_hour_angle(this.dHour, this.dMinute, this.dGMT);
		this.dAngle = this.dAngle_Hour + this.dAngle_Sideral_Time;

		this.dHour_Angle_Star = this.dAngle - this.dAscension + this.dLongitude;

		this.dHeight = calculate_height(this.dDeclination, this.dLatitude, this.dHour_Angle_Star);
		this.dAzimuth = calculate_azimuth(this.dDeclination, this.dLatitude, this.dHeight,this.dHour_Angle_Star);
	
		this.X = calculate_X(this.dHeight,this.dAzimuth);
		this.Y = calculate_Y(this.dHeight,this.dAzimuth);
	}

	/**
	 * get_all 
	 * Give all the informations of the values calculated
	 */
	public void get_all()
	{
		System.out.println("X : " + this.X);
		System.out.println("Y : " + this.Y);
		
		System.out.println("Hour : " + this.dHour);
		System.out.println("Minute : " + this.dMinute);
		System.out.println("Seconde : " + this.dSecond);
		System.out.println("GMT : " + this.dGMT);
		System.out.println("Day : " + this.dDay);
		System.out.println("Month : " + this.dMonth);
		System.out.println("Year : " + this.dYear);

		System.out.println("-->" + this.dDay + "/" + this.dMonth + "/" + this.dYear + "\t" + this.dHour + ":" + this.dMinute + ":" + this.dSecond);
		
		if(this.dGMT>=0)
			System.out.print("GMT+" + this.dGMT);
		else
			System.out.print("GMT-" + this.dGMT);	

		System.out.println("Latitude : " + this.dLatitude);
		System.out.println("Longitude : " + this.dLongitude);
		System.out.println("Delination : " + this.dDeclination);
		System.out.println("Ascension : " + this.dAscension);

		System.out.println("Azimuth : " + this.dAzimuth);
		System.out.println("Height : " + this.dHeight);

		System.out.println("Julian Date : " + this.dDate_JulianCalendar);
		System.out.println("Sideral Time : " + this.dSideral_Time);

		System.out.println("Hour dAngle Star : " + this.dHour_Angle_Star);
		System.out.println("Angle Sideral Time : " + this.dAngle_Sideral_Time);
		System.out.println("Angle Hour : " + this.dAngle_Hour);
		System.out.println("Angle : " + this.dAngle);
	}

	/**
	 * calculate_X
	 * Calculates the X value of a coordinate system of 2D. It's a projection.
	 * @param double _dHeight : The Height of the star, calculated with "calculate_Height()"
	 * @param double _dAzimuth : The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return : Return a double that contains the X coordinate
	 */
	static private double calculate_X(double _dHeight,double _dAzimuth)
	{
		double l_x=-((2.0/pi)*_dHeight+1);
		
		return l_x*cos(_dAzimuth);
	}
	
	/**
	 * calculate_Y
	 * Calculates the Y value of a coordinate system of 2D. It's a projection.
	 * @param double _dHeight : The Height of the star, calculated with "calculate_Height()"
	 * @param double _dAzimuth : The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return : Return a double that contains the Y coordinate
	 */
	static private double calculate_Y(double _dHeight,double _dAzimuth)
	{
		double l_y=-((2.0/pi)*_dHeight+1);
		
		return l_y*sin(_dAzimuth);
	}
	
	/** 
	 * hms
	 * Converts ° ' '' dAngle to degree
	 * @param double _dHour : The hour of the value
	 * @param double _dMinute : The Minute of the value
	 * @param double _dSecond : The Second of the value
	 * @return : Return a double that's the degree dAngle
	 */
	static private double hms(double _dHour, double _dMinute, double _dSecond)
	{
		double sign;
		sign = (_dHour < 0 || _dMinute < 0 || _dSecond < 0) ? -1 : 1;
		return sign * (Math.abs(_dHour) + Math.abs(_dMinute) / 60.0 + Math.abs(_dSecond) / 36000);
	}

	/** 
	 * fraction_of_day
	 * Calculates the fraction of day about a time
	 * @param double _dHour : The hour of the value
	 * @param double _dMinute : The Minute of the value
	 * @param double _dSecond : The Second of the value
	 * @return : Return a double that contains the value
	 */
	static private double fraction_of_day(double _dHour, double _dMinute, double _dSecond)
	{
		return _dHour / 24 + _dMinute / (24 * 60) + _dSecond / (24 * 60 * 60);
	}

	/**
	 * calculate_height 
	 * Calculates the dHeight of the fixed point
	 * @param double _dec : The Declination of the star
	 * @param double _dLat : The Latitude of the pointer's star
	 * @param double _star_angle : The hour angle star
	 * @return : Return a double that's the Height of the star
	 */
	static private double calculate_height(double _dec, double _dLat, double _star_angle)
	{
		double l_sinh = sin(_dec) * sin(_dLat) - cos(_dec) * cos(_dLat)* cos(_star_angle);
		return arcsin(l_sinh);
	}
	
	/** 
	 * calculate_azimuth
	 * Calculates the dAzimuth
	 * @param double _dec : The dDeclination of the star
	 * @param double _dLat : The dLatitude of the pointer's star
	 * @param double _dHeight : The dHeight of the star, calculated with "calculate_height()"
	 * @param double _star_angle : The star's dAngle
	 * @return : Return a double that's the dAzimuth of the star
	 */
	static private double calculate_azimuth(double _dec, double _dLat, double _dHeight, double _star_angle)
	{
		double l_cos_az = (sin(_dec) - sin(_dLat) * sin(_dHeight))/(cos(_dLat) * cos(_dHeight));
		double l_sin_a = (cos(_dec) * sin(_star_angle)) / cos(_dHeight);

		if (l_sin_a > 0)
			return arccos(l_cos_az);
		else
			return -arccos(l_cos_az);
	}

	/**
	 * calculate_JulianDate 
	 * Converts a gregorian date to a julian date
	 * @param double _dDay : The day
	 * @param double _dMonth : The month
	 * @param double _dYear : The year
	 * @param double _dHour : The hour
	 * @param double _dMinute : The minute
	 * @param double _dSeconde : The second
	 * @return : Return a double that's the result
	 */
	static private double calculate_JulianDate(double _dDay, double _dMonth, double _dYear, double _dHour,double _dMinute, double _dSecond)
	{
		if (_dMonth < 3)
		{
			_dYear--;
			_dMonth += 12;
		}
		int l_c = (int) (_dYear / 100);
		int l_b = 2 - l_c + (int) (l_c / 4);

		int l_t = (int) (fraction_of_day(_dHour, _dMinute, _dSecond));

		return ((int) (knumber_of_dDay_in_one_dYear * (_dYear + kinitial_dYear))
				+ (int) (knumber_of_dDay_in_one_dMonth * (_dMonth + 1)) + _dDay + l_t + l_b - 1524.5);
	}

	/**
	 * calculate_sideral_hour_dAngle Calculates the sideral hour angle
	 * 
	 * @param double _dSideral_Time : The Sideral time
	 * @return : Return a double that's the result
	 */
	static private double calculate_sideral_hour_dAngle(double _dSideral_Time)
	{
		return 2.0 * pi * _dSideral_Time / hms(23.0, 56.0, 4.0);
	}

	/** 
	 * calculate_hour_angle
	 * Calculates the hours dAngle
	 * @param double _dHour : The hour
	 * @param double _dMin : The minute
	 * @param double _dGMT : The GMT zone
	 * @return : Return a double that's the result
	 */
	static private double calculate_hour_angle(double _dHour, double _dMin, double _dGMT)
	{
		return (_dHour - 12 + _dMin / 60 - _dGMT) * 2 * pi / hms(23.0, 56.0, 4.0);
	}

	/** 
	 * calculate_dSideral_Time
	 * Calculates the sideral time
	 * @param double _dDay : The day
	 * @param double _dMonth : The month
	 * @param double _dYear : The year
	 * @param double _dHour : The hour
	 * @param double _min : The _minute
	 * @param double _dSecond : The second
	 * @return : Return a double that's the result
	 */
	static private double calculate_dSideral_Time(double _dDay, double _dMonth, double _dYear, double _dHour,double _dMinute, double _dSecond)
	{
		double l_JJ = calculate_JulianDate(_dDay, _dMonth, _dYear, _dHour, _dMinute, _dSecond);
		double l_T = (l_JJ - 2451545.0) / 36525.0;
		double l_H1 = 24110.54841 + 8640184.812866 * l_T + 0.093104 * l_T * l_T - 0.0000062 * l_T* l_T * l_T;
		double l_HSH = l_H1 / 3600.0;
		double l_HS = (l_HSH / 24.0 - (int) (l_HSH / 24.0)) * 24.0;

		return l_HS;
	}
	
	/**
	 * cos
	 * Calculate the cosinus of a number
	 * @param double _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double cos(double _dX)
	{
		return Math.cos(_dX);
	}
	
	/**
	 * sin
	 * Calculate the sinus of a number
	 * @param double _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double sin(double _dX)
	{
		return Math.sin(_dX);
	}
	
	/**
	 * arccos
	 * Calculate the ArcCosinus of a number
	 * @param double _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double arccos(double _dX)
	{
		return Math.acos(_dX);
	}
	
	/**
	 * arcsin
	 * Calculate the ArcSinus of a number
	 * @param double _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double arcsin(double _dX)
	{
		return Math.asin(_dX);
	}
	
	/**
	 * getdHour 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdHour()
	{
		return dHour;
	}

	/**
	 * get_dMinute 
	 * getter of the private value
	 * @return the private variable
	 */
	public double get_dMinute()
	{
		return dMinute;
	}

	/**
	 * getdSecond 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdSecond()
	{
		return dSecond;
	}

	/**
	 * getdGMT 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdGMT()
	{
		return dGMT;
	}

	/**
	 * getdDay 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdDay()
	{
		return dDay;
	}

	/**
	 * getdMonth 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdMonth()
	{
		return dMonth;
	}

	/**
	 * getdYear 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdYear()
	{
		return dYear;
	}

	/**
	 * getdDate_JulianCalendar 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdDate_JulianCalendar()
	{
		return dDate_JulianCalendar;
	}

	/**
	 * getdSideral_Time 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdSideral_Time()
	{
		return dSideral_Time;
	}

	/**
	 * getdLatitude 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdLatitude()
	{
		return dLatitude;
	}

	/**
	 * getdLongitude 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdLongitude()
	{
		return dLongitude;
	}

	/**
	 * getdDeclination 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdDeclination()
	{
		return dDeclination;
	}

	/**
	 * getdAscension 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdAscension()
	{
		return dAscension;
	}

	/**
	 * getdAzimuth 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdAzimuth()
	{
		return dAzimuth;
	}

	/**
	 * getdHeight 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdHeight()
	{
		return dHeight;
	}

	/**
	 * getdHour_Angle_Star 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdHour_Angle_Star()
	{
		return dHour_Angle_Star;
	}

	/**
	 * getdAngle_Sideral_Time 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdAngle_Sideral_Time()
	{
		return dAngle_Sideral_Time;
	}

	/**
	 * getdAngle_Hour 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdAngle_Hour()
	{
		return dAngle_Hour;
	}

	/**
	 * getdAngle 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getdAngle()
	{
		return dAngle;
	}
	
	/**
	 * getX 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getX()
	{
		return X;
	}

	/**
	 * getY 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getY()
	{
		return Y;
	}
}