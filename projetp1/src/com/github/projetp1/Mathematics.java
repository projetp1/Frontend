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
import java.util.Date;

public class Mathematics
{
	static private final double kpi = Math.PI;
	static private final double krad = kpi / 180.0;
	static private final double kdeg = 180.0 / kpi;
	static private final double kAU = 149597870.7; // Astronomical Unit

	static private final double knumber_of_day_in_one_year = 365.25;
	static private final double knumber_of_day_in_one_month = 30.6001;// average of the day in one
																		// month about X000 years
	static private final double kinitial_year = 4716.0;
	static private final double kadditionnal_year_of_gregorian_calendar = 1900.0;
	static private final double kadditionnal_month_of_Date_object = 1.0;
	static private final double kadditionnal_day_of_Date_object = 0.0;

	private double hour;
	private double minute;
	private double second;
	private double GMT;

	private double day;
	private double month;
	private double year;
	
	private double date_JulianCalendar;
	private double sideral_time;
	private double latitude;
	private double longitude;
	private double declination;
	private double ascension;

	private double azimuth;
	private double height;

	private double hour_angle_star;
	private double angle_sideral_time;
	private double angle_hour;
	private double angle; // angle_sideral_time+angle_our
	
	private double X;
	private double Y;

	/**
	 * Mathematics Constructor
	 * 
	 * @param Date _date : Date of the computer. Will be use to calculate the sideral Time
	 * @param double _dLat : It's the latitude of the star's pointer
	 * @param double _Lon : It's the longitude of the star's pointer
	 * @deprecated methods Date.get*()
	 */
	public Mathematics(Date _date, double _dLat, double _dLon,double _dDec,double _dAscension)
	{
		this.hour = _date.getHours();
		this.minute = _date.getMinutes();
		this.second = _date.getSeconds();

		String l_GMT = _date.toGMTString();
		String l_hour_GMT = l_GMT.substring(l_GMT.indexOf(':') - 2, l_GMT.indexOf(':'));
		this.GMT = this.hour - Integer.parseInt(l_hour_GMT);

		this.day = this.kadditionnal_day_of_Date_object + _date.getDate();
		this.month = this.kadditionnal_month_of_Date_object + _date.getMonth();
		this.year = this.kadditionnal_year_of_gregorian_calendar + _date.getYear();

		this.latitude = _dLat;
		this.longitude = _dLon;
		this.declination = _dDec;
		this.ascension = _dAscension;
	}
	
	/**
	 * calculate_all Calculates all the informations that the program needs
	 */
	public void calculate_all()
	{
		this.date_JulianCalendar = greg2julian(this.day, this.month, this.year, this.hour,this.minute, this.second);
		this.sideral_time = calculate_sideral_time(this.day, this.month, this.year, this.hour,this.minute, this.second);

		this.angle_sideral_time = calculate_sideral_hour_angle(this.sideral_time);
		this.angle_hour = calculate_hour_angle(this.hour, this.minute, this.GMT);
		this.angle = this.angle_hour + this.angle_sideral_time;

		this.hour_angle_star = this.angle - this.ascension + this.longitude;

		this.height = calculate_height(this.declination, this.latitude, this.hour_angle_star);
		this.azimuth = calculate_azimuth(this.declination, this.latitude, this.height,this.hour_angle_star);
	
		this.X = calculate_X(this.height,this.azimuth);
		this.Y = calculate_Y(this.height,this.azimuth);
	}

	/**
	 * get_all Give all the information of the values calculated
	 */
	public void get_all()
	{
		System.out.println("X : " + this.X);
		System.out.println("Y : " + this.Y);
		
		System.out.println("Hour : " + this.hour);
		System.out.println("Minute : " + this.minute);
		System.out.println("Seconde : " + this.second);
		System.out.println("GMT : " + this.GMT);
		System.out.println("Day : " + this.day);
		System.out.println("month : " + this.month);
		System.out.println("Year : " + this.year);

		System.out.println("-->" + this.day + "/" + this.month + "/" + this.year + "\t" + this.hour + ":" + this.minute + ":" + this.second);
		
		if(this.GMT>=0)
			System.out.print("GMT+" + this.GMT);
		else
			System.out.print("GMT-" + this.GMT);	

		System.out.println("Latitude : " + this.latitude);
		System.out.println("Longitude : " + this.longitude);
		System.out.println("Delination : " + this.declination);
		System.out.println("Ascension : " + this.ascension);

		System.out.println("Azimuth : " + this.azimuth);
		System.out.println("Height : " + this.height);

		System.out.println("Julian Date : " + this.date_JulianCalendar);
		System.out.println("Sideral Time : " + this.sideral_time);

		System.out.println("Hour Angle Star : " + this.hour_angle_star);
		System.out.println("Angle Sideral Time : " + this.angle_sideral_time);
		System.out.println("Angle Hour : " + this.angle_hour);
		System.out.println("Angle : " + this.angle);
	}

	static public double calculate_X(double _dHeight,double _dAzimuth)
	{
		double l_x=-((2.0/kpi)*_dHeight+1);
		
		return l_x*Math.cos(_dAzimuth);
	}
	
	static public double calculate_Y(double _dHeight,double _dAzimuth)
	{
		double l_y=-((2.0/kpi)*_dHeight+1);
		
		return l_y*Math.sin(_dAzimuth);
	}
	
	/** 
	 * hms
	 * Converts ° ' '' to degree
	 * @param double _hour : The hour of the value
	 * @param double _minute : The minute of the value
	 * @param double _second : The second of the value
	 * @return : Return a double that's the result
	 */
	static private double hms(double _hour, double _minute, double _second)
	{
		double sign;
		sign = (_hour < 0 || _minute < 0 || _second < 0) ? -1 : 1;
		return sign * (Math.abs(_hour) + Math.abs(_minute) / 60.0 + Math.abs(_second) / 36000);
	}

	/** 
	 * fraction_of_day
	 * Calculates the fraction of day about a time
	 * @param double _hour : The hour of the value
	 * @param double _minute : The minute of the value
	 * @param double _second : The second of the value
	 * @return : Return a double that's the result
	 */
	static private double fraction_of_day(double _hour, double _minute, double _second)
	{
		return _hour / 24 + _minute / (24 * 60) + _second / (24 * 60 * 60);
	}

	/**
	 * calculate_height Calculates the height of the fixed point
	 * 
	 * @param double _dec : The declination
	 * @param double _dLat : The latitude
	 * @param double _star_angle : The star's angle
	 * @return : Return a double that's the result
	 */
	static private double calculate_height(double _dec, double _dLat, double _star_angle)
	{
		double l_sinh = Math.sin(_dec) * Math.sin(_dLat) - Math.cos(_dec) * Math.cos(_dLat)
				* Math.cos(_star_angle);
		return Math.asin(l_sinh);
	}
	
	/** 
	 * calculate_azimu
	 * Calculates the azimuth
	 * @param double _dec : The declination
	 * @param double _lat : The latitude
	 * @param double _height : The height
	 * @param double _star_angle : The star's angle
	 * @return : Return a double that's the result
	 */
	static private double calculate_azimuth(double _dec, double _dLat, double _height, double _star_angle)
	{
		double l_cos_az = (Math.sin(_dec) - Math.sin(_dLat) * Math.sin(_height))
				/ (Math.cos(_dLat) * Math.cos(_height));
		double l_sin_a = (Math.cos(_dec) * Math.sin(_star_angle)) / Math.cos(_height);

		if (l_sin_a > 0)
			return Math.acos(l_cos_az);
		else
			return -Math.acos(l_cos_az);
	}

	/**
	 * greg2julian Converts a gregorian date to a julian date
	 * 
	 * @param double _day : The day
	 * @param double _month : The month
	 * @param double _year : The year
	 * @param double _hour : The hour
	 * @param double _minute : The minute
	 * @param double _seconde : The second
	 * @return : Return a double that's the result
	 */
	static private double greg2julian(double _day, double _month, double _year, double _hour,
			double _minute, double _second)
	{
		if (_month < 3)
		{
			_year--;
			_month += 12;
		}
		int l_c = (int) (_year / 100);
		int l_b = 2 - l_c + (int) (l_c / 4);

		int l_t = (int) (fraction_of_day(_hour, _minute, _second));

		return ((int) (knumber_of_day_in_one_year * (_year + kinitial_year))
				+ (int) (knumber_of_day_in_one_month * (_month + 1)) + _day + l_t + l_b - 1524.5);
	}

	/**
	 * calculate_sideral_hour_angle Calculates the sideral hour angle
	 * 
	 * @param double _sideral_time : The sideral time
	 * @return : Return a double that's the result
	 */
	static private double calculate_sideral_hour_angle(double _sideral_time)
	{
		return 2.0 * kpi * _sideral_time / hms(23, 56, 4);
	}

	/** 
	 * calculate_hour_angle
	 * Calculates the hours angle
	 * @param double _hour : The hour
	 * @param double _min : The minute
	 * @param double _GMT : The GMT zone
	 * @return : Return a double that's the result
	 */
	static private double calculate_hour_angle(double _hour, double _min, double _GMT)
	{
		return (_hour - 12 + _min / 60 - _GMT) * 2 * kpi * hms(23, 56, 4);
	}

	/** 
	 * calculate_sideral_time
	 * Calculates the sideral time
	 * @param double _day : The day
	 * @param double _month : The month
	 * @param double _year : The year
	 * @param double _hour : The hour
	 * @param double _min : The minute
	 * @param double _second : The second
	 * @return : Return a double that's the result
	 */
	static private double calculate_sideral_time(double _day, double _month, double _year, double _hour,double _minute, double _second)
	{
		double l_JJ = greg2julian(_day, _month, _year, _hour, _minute, _second);
		double l_T = (l_JJ - 2451545.0) / 36525.0;
		double l_H1 = 24110.54841 + 8640184.812866 * l_T + 0.093104 * l_T * l_T - 0.0000062 * l_T
				* l_T * l_T;
		double l_HSH = l_H1 / 3600.0;
		double l_HS = (l_HSH / 24 - (int) (l_HSH / 24)) * 24;

		return l_HS;
	}

	/**
	 * getHour getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getHour()
	{
		return hour;
	}

	/**
	 * getMinute getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getMinute()
	{
		return minute;
	}

	/**
	 * getSecond getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getSecond()
	{
		return second;
	}

	/**
	 * getGMT getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getGMT()
	{
		return GMT;
	}

	/**
	 * getDay getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getDay()
	{
		return day;
	}

	/**
	 * getMonth getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getMonth()
	{
		return month;
	}

	/**
	 * getYear getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getYear()
	{
		return year;
	}

	/**
	 * getDate_JulianCalendar getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getDate_JulianCalendar()
	{
		return date_JulianCalendar;
	}

	/**
	 * getSideral_time getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getSideral_time()
	{
		return sideral_time;
	}

	/**
	 * getLatitude getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * getLongitude getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * getDeclination getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getDeclination()
	{
		return declination;
	}

	/**
	 * getAscension getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAscension()
	{
		return ascension;
	}

	/**
	 * getAzimuth getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAzimuth()
	{
		return azimuth;
	}

	/**
	 * getHeight getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getHeight()
	{
		return height;
	}

	/**
	 * getHour_angle_star getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getHour_angle_star()
	{
		return hour_angle_star;
	}

	/**
	 * getAngle_sideral_time getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAngle_sideral_time()
	{
		return angle_sideral_time;
	}

	/**
	 * getAngle_hour getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAngle_hour()
	{
		return angle_hour;
	}

	/**
	 * getAngle getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAngle()
	{
		return angle;
	}
	
	/**
	 * getX getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getX()
	{
		return X;
	}

	/**
	 * getY getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getY()
	{
		return Y;
	}
}