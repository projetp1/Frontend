 /**=====================================================================*
 | This file declares the following classes:
 |    Mathematique
 |
 | Description of the class Mathematique :
 |	  Use to calcul all the informations that the program needs
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

public class Mathematic 
{
	static private final double kpi = Math.PI;
	static private final double krad = kpi/180.0;
	static private final double kdeg = 180.0/kpi;
	static private final double kAU = 149597870.7; // Astronomical Unit
	
	static private final double knumber_of_day_in_one_year = 365.25;
	static private final double knumber_of_day_in_one_month = 30.6001;//average of the day in one month about X000 years
	static private final double kinitial_year = 4716.0;
	static private final double kadditionnal_year_of_gregorian_calendar = 1900.0;
	static private final double kadditionnal_monthe_of_Date_object = 1.0;
	static private final double kadditionnal_day_of_Date_object = 0.0;
	
	private double hour;
	private double minute;
	private double second;
	private double GMT;
	
	private double day;
	private double monthe;
	private double year;
	
	private double date_JulianCalendar;
	private double sideral_time;
	private double latitude;
	private double longitude;
	private double declination;
	private double ascension;
	private double azimut;
	private double height;
	
	private double hour_angle_star;
	private double angle_sideral_time;
	private double angle_hour;
	private double angle; //angle_sideral_time+angle_our
	
	/** 
	 * Mathematique
	 * Constructor
	 * @param Date _date : Date of the computer. Will be use to calculate the sideral Time
	 * @param double _lat : It's the latitude of the star's pointeur
	 * @param double _lon : It's the longitude of the star's pointeur
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	public Mathematic(Date _date,double _lat, double _lon)
	{
		this.hour = _date.getHours();
		this.minute = _date.getMinutes();
		this.second = _date.getSeconds();
		
		String l_GMT = _date.toGMTString();
	    String l_hour_GMT = l_GMT.substring(l_GMT.indexOf(':')-2,l_GMT.indexOf(':'));
	    this.GMT = this.hour-Integer.parseInt(l_hour_GMT);
		
	    this.day = this.kadditionnal_day_of_Date_object+_date.getDate();
	    this.monthe = this.kadditionnal_monthe_of_Date_object+_date.getMonth();
	    this.year = this.kadditionnal_year_of_gregorian_calendar+_date.getYear();
	    
		this.latitude = _lat;
		this.longitude = _lon;
	}
	
	/** 
	 * calculate_all
	 * Calculs all the informations that the program needs
	 */
	public void calculate_all()
	{
		this.declination = calculate_declination(this.latitude,this.longitude);
		this.ascension = calculate_ascension();
		
		this.date_JulianCalendar = 	greg2julien(this.day,this.monthe,this.year,this.hour,this.minute,this.second);
		this.sideral_time = calculate_sideral_time(this.day,this.monthe,this.year,this.hour,this.minute,this.second);
		
		this.angle_sideral_time = calculate_siderial_hour_angle(this.sideral_time);
		this.angle_hour = calculate_hour_angle(this.hour,this.minute,this.GMT);
		this.angle = this.angle_hour+this.angle_sideral_time;
		
		this.hour_angle_star = this.angle - this.ascension + this.longitude;
		
		this.height = calculate_height(this.declination,this.latitude,this.hour_angle_star);
		this.azimut = calculate_azimut(this.declination,this.latitude,this.height,this.hour_angle_star);
	}
	
	/** 
	 * get_all
	 * Give all the information of the values calculated
	 */
	public void get_all()
	{
		System.out.println("Hour : " + this.hour);
		System.out.println("Minute : " + this.minute);
		System.out.println("Seconde : " + this.second);
		System.out.println("GMT : " + this.GMT);
		System.out.println("Day : " + this.day);
		System.out.println("Monthe : " + this.monthe);
		System.out.println("Year : " + this.year);
		System.out.println("-->" + this.day + "/" + this.monthe + "/" + this.year + "\t" + this.hour + ":" + this.minute + ":" + this.second);
		
		if(this.GMT>=0)
			System.out.print("GMT+" + this.GMT);
		else
			System.out.print("GMT-" + this.GMT);	
		
		System.out.println("Latitude : " + this.latitude);
		System.out.println("Longitude : " + this.longitude);
		System.out.println("Delination : " + this.declination);
		System.out.println("Ascension : " + this.ascension);
		System.out.println("Azimut : " + this.azimut);
		System.out.println("Height : " + this.height);
		
		System.out.println("Julian Date : " + this.date_JulianCalendar);
		System.out.println("Sideral Time : " + this.sideral_time);
		
		System.out.println("Hour Angle Star : " + this.hour_angle_star);
		System.out.println("Angle Sideral Time : " + this.angle_sideral_time);
		System.out.println("Angle Hour : " + this.angle_hour);
		System.out.println("Angle : " + this.angle);
	}
	
	/** 
	 * calculate_declination
	 * Calculates the declination of a position(longitude+latitude)
	 * @param double _lat : It's the latitude of the star's pointer
	 * @param double _lon : It's the longitude of the star's pointer
	 * @return : Return a double that's the result
	 */
	private double calculate_declination(double _lat,double _lon)
	{
		return 0;
	}
	
	/** 
	 * calculate_ascension
	 * Calculates the 
	 * @param Date _date : Date of the computer. Will be use to calculate the sideral Time
	 * @param double _lat : It's the latitude of the star's pointeur
	 * @param double _lon : It's the longitude of the star's pointeur
	 * @return : Return a boolean with true if the regex has found the string or return false
	 */
	private double calculate_ascension()
	{
		return 0;
	}
	
	private double hms (double _hour,double _minute,double _second)
	{
		double sign;
		sign=(_hour<0 || _minute<0 || _second<0)?-1:1;
		return sign*(Math.abs(_hour)+Math.abs(_minute)/60.0+Math.abs(_second)/36000);
	}
	
	private double fraction_of_day(double _hour,double _minute,double _second)
	{
		return _hour/24 + _minute/(24*60) + _second/(24*60*60);
	}
	
	private double calculate_height(double _dec,double _lat,double _star_angle)
	{
		double l_sinh = Math.sin(_dec)*Math.sin(_lat)-Math.cos(_dec)*Math.cos(_lat)*Math.cos(_star_angle);
		return Math.asin(l_sinh);
	}
	
	private double calculate_azimut(double _dec,double _lat,double _height,double _star_angle)
	{
		double l_cos_az = (Math.sin(_dec)-Math.sin(_lat)*Math.sin(_height))/(Math.cos(_lat)*Math.cos(_height));
		double l_sin_a = (Math.cos(_dec)*Math.sin(_star_angle))/Math.cos(_height);
		
		if(l_sin_a > 0)
			return Math.acos(l_cos_az);
		else
			return -Math.acos(l_cos_az);
				
	}

	private double greg2julien (double _day,double _month,double _year,double _hour,double _minute,double _second)
	{
		if(_month<3)
		{
			_year--;
			_month+=12;
		}
		int l_c = (int)(_year/100);
		int l_b = 2-l_c+(int)(l_c/4);
		
		int l_t = (int)(fraction_of_day(_hour,_minute,_second));
		
		return (
				(int)(knumber_of_day_in_one_year*(_year+kinitial_year))+
				(int)(knumber_of_day_in_one_month*(_month+1))+
				_day+l_t+l_b-1524.5);
	}
	
	private double calculate_siderial_hour_angle(double _sideral_time)
	{
		return 2.0*kpi*_sideral_time/fraction_of_day(23,56,4);
	}
	
	private double calculate_hour_angle(double _hour,double _min,double _GMT)
	{
		return (_hour-12+_min/60-_GMT)*2*kpi*fraction_of_day(23,56,4);
	}
	
	private double calculate_sideral_time(double _day,double _month,double _year,double _hour,double _minute,double _second)
	{
		double l_JJ = greg2julien(_day,_month,_year,_hour,_minute,_second);
		double l_T = (l_JJ-2451545.0)/36525.0;
		double l_H1 = 24110.54841 + 8640184.812866*l_T + 0.093104*l_T*l_T - 0.0000062*l_T*l_T*l_T;
		double l_HSH = l_H1/3600.0;
		double l_HS = (l_HSH/24 - (int)(l_HSH/24))*24;
		return l_HS;
	}

	public double getHour() {
		return hour;
	}
	
	public double getMinute() {
		return minute;
	}
	
	public double getSecond() {
		return second;
	}
	
	public double getGMT() {
		return GMT;
	}
	
	public double getDay() {
		return day;
	}
	
	public double getMonthe() {
		return monthe;
	}
	
	public double getYear() {
		return year;
	}
	
	public double getDate_JulianCalendar() {
		return date_JulianCalendar;
	}
	
	public double getSideral_time() {
		return sideral_time;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getDeclination() {
		return declination;
	}
	
	public double getAscension() {
		return ascension;
	}
	
	public double getAzimut() {
		return azimut;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getHour_angle_star() {
		return hour_angle_star;
	}
	
	public double getAngle_sideral_time() {
		return angle_sideral_time;
	}
	
	public double getAngle_hour() {
		return angle_hour;
	}
	
	public double getAngle() {
	return angle;
}

	
	public static double getkpi() {
		return kpi;
	}

	
	public static double getkrad() {
		return krad;
	}

	
	public static double getkdeg() {
		return kdeg;
	}
	

	public static double getkAU() {
		return kAU;
	}
	

	public static double getNumberOfDayInOneYear() {
		return knumber_of_day_in_one_year;
	}
	

	public static double getNumberOfDayInOneMonth() {
		return knumber_of_day_in_one_month;
	}
	

	public static double getInitialYear() {
		return kinitial_year;
	}
	

	public static double getAdditionnalYearOfGregorianCalendar() {
		return kadditionnal_year_of_gregorian_calendar;
	}
	

	public static double getAdditionnalMontheOfDateObject() {
		return kadditionnal_monthe_of_Date_object;
	}
	

	public static double getAdditionnalDayOfDateObject() {
		return kadditionnal_day_of_Date_object;
	}

}