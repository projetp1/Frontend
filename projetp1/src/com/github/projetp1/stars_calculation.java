
package com.github.projetp1;
import java.lang.Math;

public class stars_calculation 
{
	static final double pi = Math.PI;
	static final double rad = pi/180.0;
	static final double deg = 180.0/pi;
	static final double AU = 149597870.7; // Astronomical Unit
	static final double number_of_day_in_one_year = 365.25;
	static final double number_of_day_in_one_month = 30.6001;//average of the day in one month about X000 years
	static final int initial_year = 4716;
	
	public int hms (int _hour,int _minute,int _second)
	{
		int sign;
		sign=(_hour<0 || _minute<0 || _second<0)?-1:1;
		return sign*(Math.abs(_hour)+Math.abs(_minute)/60+Math.abs(_second)/36000);
	}
	
	public double hms (double _hour,double _minute,double _second)
	{
		double sign;
		sign=(_hour<0 || _minute<0 || _second<0)?-1:1;
		return sign*(Math.abs(_hour)+Math.abs(_minute)/60.0+Math.abs(_second)/36000);
	}
	
	public int fraction_of_day(int _hour,int _minute,int _second)
	{
		return _hour/24 + _minute/(24*60) + _second/(24*60*60);
	}
	
	public double fraction_of_day(double _hour,double _minute,double _second)
	{
		return _hour/24.0 + _minute/(24.0*60.0) + _second/(24.0*60.0*60.0);
	}
	
	public double greg2julien (int _day,int _month,int _year,int _hour,int _minute,int _second)
	{
		if(_month<3)
		{
			_year--;
			_month+=12;
		}
		int l_c = (int)(_year/100);
		int l_b = 2-l_c+(int)(l_c/4);
		
		int l_t = fraction_of_day(_hour,_minute,_second);
		
		return (
				(int)(number_of_day_in_one_year*(_year+initial_year))+
				(int)(number_of_day_in_one_month*(_month+1))+
				_day+l_t+l_b-1524.5);
	}
	
	public double siderial_hour_angle(double l_HS)
	{
		return 2.0*pi*l_HS/fraction_of_day(23,56,4);
	}
	
	public double sideral_hour(int _day,int _month,int _year,int _hour,int _minute,int _second)
	{
		double l_JJ = greg2julien(_day,_month,_year,_hour,_minute,_second);
		double l_T = (l_JJ-2451545.0)/36525.0;
		double l_H1 = 24110.54841 + 8640184.812866*l_T + 0.093104*l_T*l_T - 0.0000062*l_T*l_T*l_T;
		double l_HSH = l_H1/3600.0;
		double l_HS = (l_HSH/24 - (int)(l_HSH/24))*24;
		return l_HS;
	}
}
