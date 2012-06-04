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
import java.util.TimeZone;
import java.util.logging.Logger;

public class Mathematics
{
	static private final double pi = Math.PI;
	static private final double D2R = pi / 180.0;
	static private final double R2D = 180.0 / pi;

	//Constants for Julian Date
	static private final double kNumberOfDayInOneYear = 365.25;
	static private final double kNumberOfDayInOneMonth = 30.6001;// average of the dDay in one dMonth about X000 dYears
	static private final double kInitialYear = 4716.0;
	
	//Constants for the Object Date
	static private final double kAdditionnalYearOfGregorianCalendar = 0.0;
	static private final double kAdditionnalMonthOfDateObject = 1.0;
	static private final double kAdditionnalDayOfDateObject = 0.0;

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
	
	private double dX;
	private double dY;
	
	/**
	 * Mathematics Constructor
	 * 
	 * @param _date : Date of the computer. Will be use to calculate the Sideral Time
	 * @param _dLat : It's the Latitude of the star's pointer
	 * @param _dLon : It's the Longitude of the star's pointer
	 */
	public Mathematics(Calendar _date, double _dLat, double _dLon)
	{
		calculateDateTime(_date);

		this.dLatitude = _dLat;
		this.dLongitude = _dLon;
		
		this.dDate_JulianCalendar = calculate_JulianDate(this.dDay, this.dMonth, this.dYear, this.dHour,this.dMinute, this.dSecond);
		this.dSideral_Time = calculateSideralTime(this.dDay, this.dMonth, this.dYear, this.dHour,this.dMinute, this.dSecond);

		this.dAngle_Sideral_Time = calculateSideralHourAngle(this.dSideral_Time);
		this.dAngle_Hour = calculateHourAngle(this.dHour, this.dMinute, this.dGMT);
		this.dAngle = this.dAngle_Hour + this.dAngle_Sideral_Time;
	}
	
	/**
	 * calculateAll 
	 * Calculates all the informations that the program needs
	 * @param _dDec : It's the Declination of the star
	 * @param _dAsc : It's the Ascension of the star
	 */
	public void calculateAll(double _dDec,double _dAsc)
	{
		this.dDeclination = _dDec;
		this.dAscension = _dAsc;

		this.dHour_Angle_Star = this.dAngle - this.dAscension + this.dLongitude;

		this.dHeight = calculate_height(this.dDeclination, this.dLatitude, this.dHour_Angle_Star);
		this.dAzimuth = calculate_azimuth(this.dDeclination, this.dLatitude, this.dHeight,this.dHour_Angle_Star);
	
		this.dX = calculateX(this.dHeight,this.dAzimuth);
		this.dY = calculateY(this.dHeight,this.dAzimuth);
	}

	/**
	 * calculatePositionSun
	 * Calculates the sun's declination and ascencion and uses calculateAll()
	 */
	public void calculatePositionSun()
	{
		//http://www.cppfrance.com/codes/CALCUL-POSITION-SOLEIL-DECLINAISON-ANGLE-HORAIRE-ALTITUDE-AZIMUT_31774.aspx
		double g=357.529+0.98560028*this.dDate_JulianCalendar;
		double q=280.459+0.98564736*this.dDate_JulianCalendar;
		double l=q+1.915*sin(g*D2R)+0.020*sin(2*g*D2R);
		double e=23.439-0.00000036*this.dDate_JulianCalendar;

		this.dAscension = arctan(cos(e*D2R)*sin(l*D2R)/cos(l*D2R))*(R2D);
		if(cos(l*D2R)<0)
			this.dAscension = 12.0+this.dAscension;
		else if(cos(l*D2R)>0 && sin(l*D2R)<0)
			this.dAscension = this.dAscension+24.0;
		this.dDeclination = arcsin(sin(e*D2R)*sin(l*D2R))*R2D;

		calculateAll(this.dDeclination,this.dAscension);
	}
	
	/**
	 * getAll 
	 * Give all the informations of the values calculated
	 */
	private void getAll()
	{
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.info("X : " + this.dX);
		log.info("Y : " + this.dY);
		
		log.info("Hour : " + this.dHour);
		log.info("Minute : " + this.dMinute);
		log.info("Seconde : " + this.dSecond);
		log.info("GMT : " + this.dGMT);
		log.info("Day : " + this.dDay);
		log.info("Month : " + this.dMonth);
		log.info("Year : " + this.dYear);

		log.info("-->" + this.dDay + "/" + this.dMonth + "/" + this.dYear + "\t" + this.dHour + ":" + this.dMinute + ":" + this.dSecond);
		
		if(this.dGMT>=0)
			log.info("GMT+" + this.dGMT);
		else
			log.info("GMT-" + this.dGMT);	

		log.info("Latitude : " + this.dLatitude);
		log.info("Longitude : " + this.dLongitude);
		log.info("Delination : " + this.dDeclination);
		log.info("Ascension : " + this.dAscension);

		log.info("Azimuth : " + this.dAzimuth);
		log.info("Height : " + this.dHeight);

		log.info("Julian Date : " + this.dDate_JulianCalendar);
		log.info("Sideral Time : " + this.dSideral_Time);

		log.info("Hour dAngle Star : " + this.dHour_Angle_Star);
		log.info("Angle Sideral Time : " + this.dAngle_Sideral_Time);
		log.info("Angle Hour : " + this.dAngle_Hour);
		log.info("Angle : " + this.dAngle);
	}
	
	/**
	 * calculateDateTime
	 * Calculates the informations from the date and the time
	 * @param _date : Use a calendar for calculate the GTM hour
	 */
	public void calculateDateTime(Calendar _date)
	{
		this.dHour = _date.get(Calendar.HOUR_OF_DAY);
		this.dMinute = _date.get(Calendar.MINUTE);
		this.dSecond = _date.get(Calendar.SECOND);

		this.dGMT = calculateHourGMT(_date);
		this.dDay = Mathematics.kAdditionnalDayOfDateObject + _date.get(Calendar.DATE);
		this.dMonth = Mathematics.kAdditionnalMonthOfDateObject + _date.get(Calendar.MONTH);
		this.dYear = Mathematics.kAdditionnalYearOfGregorianCalendar + _date.get(Calendar.YEAR);
	}
	
	/**
	 * calculateHourGMT
	 * Gives the hour from HMT
	 * @param _cal : Use a calendar for calculate the GTM hour
	 * @return : Return an int that's the hour GMT
	 */
	static private int calculateHourGMT(Calendar _cal)
	{
	    TimeZone l_t = _cal.getTimeZone();
	    
	    return l_t.getOffset(_cal.getTimeInMillis())/1000/3600;
	}
	
	/**
	 * calculateX
	 * Calculates the X value of a coordinate system of 2D. It's a projection.
	 * @param _dHeight : The Height of the star, calculated with "calculate_Height()"
	 * @param _dAzimuth : The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return : Return a double that contains the X coordinate
	 */
	static private double calculateX(double _dHeight,double _dAzimuth)
	{
		double l_x = 1*((-2.0/pi)*_dHeight+1);
		
		return -l_x*sin(_dAzimuth);
		//return (cos(_dHeight)*sin(_dAzimuth)/(sin(_dHeight)+1));
	}
	
	/**
	 * calculateY
	 * Calculates the Y value of a coordinate system of 2D. It's a projection.
	 * @param _dHeight : The Height of the star, calculated with "calculate_Height()"
	 * @param _dAzimuth : The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return : Return a double that contains the Y coordinate
	 */
	static private double calculateY(double _dHeight,double _dAzimuth)
	{
		double l_y = 1*((-2.0/pi)*_dHeight+1);
		
		return -l_y*cos(_dAzimuth);
		//return (cos(_dHeight)*cos(_dAzimuth)/(sin(_dHeight)+1));
	}
	
	/** 
	 * hms
	 * Converts ° ' '' dAngle to degree
	 * @param _dHour : The hour of the value
	 * @param _dMinute : The Minute of the value
	 * @param _dSecond : The Second of the value
	 * @return : Return a double that's the degree dAngle
	 */
	static private double hms(double _dHour, double _dMinute, double _dSecond)
	{
		double sign;
		sign = (_dHour < 0 || _dMinute < 0 || _dSecond < 0) ? -1 : 1;
		return sign * (Math.abs(_dHour) + Math.abs(_dMinute) / 60.0 + Math.abs(_dSecond) / 36000);
	}

	/** 
	 * fractionOfDay
	 * Calculates the fraction of day about a time
	 * @param _dHour : The hour of the value
	 * @param _dMinute : The Minute of the value
	 * @param _dSecond : The Second of the value
	 * @return : Return a double that contains the value
	 */
	static private double fractionOfDay(double _dHour, double _dMinute, double _dSecond)
	{
		return _dHour / 24 + _dMinute / (24 * 60) + _dSecond / (24 * 60 * 60);
	}

	/**
	 * calculate_height 
	 * Calculates the dHeight of the fixed point
	 * @param _dec : The Declination of the star
	 * @param _dLat : The Latitude of the pointer's star
	 * @param _star_angle : The hour angle star
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
	 * @param _dec : The dDeclination of the star
	 * @param _dLat : The dLatitude of the pointer's star
	 * @param _dHeight : The dHeight of the star, calculated with "calculate_height()"
	 * @param _star_angle : The star's dAngle
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
	 * @param _dDay : The day
	 * @param _dMonth : The month
	 * @param _dYear : The year
	 * @param _dHour : The hour
	 * @param _dMinute : The minute
	 * @param _dSeconde : The second
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

		int l_t = (int) (fractionOfDay(_dHour, _dMinute, _dSecond));

		return ((int) (kNumberOfDayInOneYear * (_dYear + kInitialYear))
				+ (int) (kNumberOfDayInOneMonth * (_dMonth + 1)) + _dDay + l_t + l_b - 1524.5);
	}

	static public double calculateAngleCompass(double _dX,double _dY,double _dZ)
	{
		return 0;
	}

	static public double calculateAngleInclinometer(double _dX,double _dY,double _dZ)
	{
		return 0;
	}
	
	static public double picLon2Lon(double _duLon,char _cHemisphere)
	{
		return 0;
	}
	
	static public double picLat2Lat(double _duLat,char _cHemisphere)
	{
		return 0;
	}
	
	/**
	 * calculateSideralHourAngle Calculates the sideral hour angle
	 * 
	 * @param _dSideral_Time : The Sideral time
	 * @return : Return a double that's the result
	 */
	static private double calculateSideralHourAngle(double _dSideral_Time)
	{
		return 2.0 * pi * _dSideral_Time / hms(23.0, 56.0, 4.0);
	}

	/** 
	 * calculateHourAngle
	 * Calculates the hours dAngle
	 * @param _dHour : The hour
	 * @param _dMin : The minute
	 * @param _dGMT : The GMT zone
	 * @return : Return a double that's the result
	 */
	static private double calculateHourAngle(double _dHour, double _dMin, double _dGMT)
	{
		return (_dHour - 12 + _dMin / 60 - _dGMT) * 2 * pi / hms(23.0, 56.0, 4.0);
	}

	/** 
	 * calculateSideralTime
	 * Calculates the sideral time
	 * @param _dDay : The day
	 * @param _dMonth : The month
	 * @param _dYear : The year
	 * @param _dHour : The hour
	 * @param _min : The _minute
	 * @param _dSecond : The second
	 * @return : Return a double that's the result
	 */
	static private double calculateSideralTime(double _dDay, double _dMonth, double _dYear, double _dHour,double _dMinute, double _dSecond)
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
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double cos(double _dX)
	{
		return Math.cos(_dX);
	}
	
	/**
	 * sin
	 * Calculate the sinus of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double sin(double _dX)
	{
		return Math.sin(_dX);
	}
	
	/**
	 * arccos
	 * Calculate the ArcCosinus of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double arccos(double _dX)
	{
		return Math.acos(_dX);
	}
	
	/**
	 * arcsin
	 * Calculate the ArcSinus of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double arcsin(double _dX)
	{
		return Math.asin(_dX);
	}
	
	/**
	 * tan
	 * Calculate the tan of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double tan(double _dX)
	{
		return Math.tan(_dX);
	}

	/**
	 * arctan
	 * Calculate the ArcTan of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static private double arctan(double _dX)
	{
		return Math.atan(_dX);
	}
	
	/**
	 * getHour 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getHour()
	{
		return dHour;
	}

	/**
	 * getMinute 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getMinute()
	{
		return dMinute;
	}

	/**
	 * getSecond 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getSecond()
	{
		return dSecond;
	}

	/**
	 * getGMT 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getGMT()
	{
		return dGMT;
	}

	/**
	 * getDay 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getDay()
	{
		return dDay;
	}

	/**
	 * getMonth 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getMonth()
	{
		return dMonth;
	}

	/**
	 * getYear 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getYear()
	{
		return dYear;
	}

	/**
	 * getDate_JulianCalendar 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getDate_JulianCalendar()
	{
		return dDate_JulianCalendar;
	}

	/**
	 * getSideral_Time 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getSideral_Time()
	{
		return dSideral_Time;
	}

	/**
	 * getLatitude 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getLatitude()
	{
		return dLatitude;
	}

	/**
	 * getLongitude 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getLongitude()
	{
		return dLongitude;
	}

	/**
	 * getDeclination 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getDeclination()
	{
		return dDeclination;
	}

	/**
	 * getAscension 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getAscension()
	{
		return dAscension;
	}

	/**
	 * getAzimuth 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getAzimuth()
	{
		return dAzimuth;
	}

	/**
	 * getHeight 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getHeight()
	{
		return dHeight;
	}

	/**
	 * getHour_Angle_Star 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getHour_Angle_Star()
	{
		return dHour_Angle_Star;
	}

	/**
	 * getAngle_Sideral_Time 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getAngle_Sideral_Time()
	{
		return dAngle_Sideral_Time;
	}

	/**
	 * getAngle_Hour 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getAngle_Hour()
	{
		return dAngle_Hour;
	}

	/**
	 * getAngle 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getAngle()
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
		return dX;
	}

	/**
	 * getY 
	 * getter of the private value
	 * @return the private variable
	 */
	public double getY()
	{
		return dY;
	}
}