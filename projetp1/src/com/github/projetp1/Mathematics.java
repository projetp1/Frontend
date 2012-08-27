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

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

public class Mathematics
{
	// Source http://www.voidware.com/moon_phase.htm
	// Adapted in Java by Diego Antognini
	public static class TimePlace
	{
		public int year;
		public int month;
		public int day;
		public double hour;
	}

	// Source http://www.voidware.com/moon_phase.htm
	// Adapted in Java by Diego Antognini
	public final static class RefObject<T>
	{
		public T argvalue;

		public RefObject(T refarg)
		{
			argvalue = refarg;
		}
	}

	// Source http://www.voidware.com/moon_phase.htm
	// Adapted in Java by Diego Antognini
	public static class GlobalMembers
	{
		public static void JulianToDate(TimePlace now, double jd)
		{
			int jdi;
			int b;
			int c;
			int d;
			int e;
			int g;
			int g1;

			jd += 0.5;
			jdi = (int) jd;
			if (jdi > 2299160)
			{
				int a = (int) ((jdi - 1867216.25) / 36524.25);
				b = jdi + 1 + a - a / 4;
			}
			else
				b = jdi;

			c = b + 1524;
			d = (int) ((c - 122.1) / 365.25);
			e = (int) (365.25 * d);
			g = (int) ((c - e) / 30.6001);
			g1 = (int) (30.6001 * g);
			now.day = c - e - g1;
			now.hour = (jd - jdi) * 24.0;
			if (g <= 13)
				now.month = g - 1;
			else
				now.month = g - 13;
			if (now.month > 2)
				now.year = d - 4716;
			else
				now.year = d - 4715;
		}

		public static double Julian(int year, int month, double day)
		{
			int a;
			int b = 0;
			int c;
			int e;
			if (month < 3)
			{
				year--;
				month += 12;
			}
			if (year > 1582 || (year == 1582 && month > 10)
					|| (year == 1582 && month == 10 && day > 15))
			{
				a = year / 100;
				b = 2 - a + a / 4;
			}
			c = (int) (365.25 * year);
			e = (int) (30.6001 * (month + 1));
			return b + c + e + day + 1720994.5;
		}

		public static double sun_position(double j)
		{
			double n;
			double x;
			double e;
			double l;
			double dl;
			double v;
			int i;

			n = 360 / 365.2422 * j;
			i = (int) (n / 360);
			n = n - i * 360.0;
			x = n - 3.762863;
			if (x < 0)
				x += 360;
			x *= (3.1415926535897932384626433832795 / 180.0);
			e = x;
			do
			{
				dl = e - .016718 * sin(e) - x;
				e = e - dl / (1 - .016718 * cos(e));
			} while (Math.abs(dl) >= (1e-12));
			v = 360 / 3.1415926535897932384626433832795 * Math.atan(1.01686011182 * tan(e / 2));
			l = v + 282.596403;
			i = (int) (l / 360);
			l = l - i * 360.0;
			return l;
		}

		public static double moon_position(double j, double ls)
		{
			double ms;
			double l;
			double mm;
			double n;
			double ev;
			double sms;
			double ae;
			double ec;
			int i;

			/* ls = sun_position(j) */
			ms = 0.985647332099 * j - 3.762863;
			if (ms < 0)
				ms += 360.0;
			l = 13.176396 * j + 64.975464;
			i = (int) (l / 360);
			l = l - i * 360.0;
			if (l < 0)
				l += 360.0;
			mm = l - 0.1114041 * j - 349.383063;
			i = (int) (mm / 360);
			mm -= i * 360.0;
			n = 151.950429 - 0.0529539 * j;
			i = (int) (n / 360);
			n -= i * 360.0;
			ev = 1.2739 * sin((2 * (l - ls) - mm) * (3.1415926535897932384626433832795 / 180.0));
			sms = sin(ms * (3.1415926535897932384626433832795 / 180.0));
			ae = 0.1858 * sms;
			mm += ev - ae - 0.37 * sms;
			ec = 6.2886 * sin(mm * (3.1415926535897932384626433832795 / 180.0));
			l += ev + ec - ae + 0.214 * sin(2 * mm * (3.1415926535897932384626433832795 / 180.0));
			l = 0.6583 * sin(2 * (l - ls) * (3.1415926535897932384626433832795 / 180.0)) + l;
			return l;
		}

		public static double moon_phase(int year, int month, int day, double hour,
				RefObject<Integer> ip)
		{
			double j = Julian(year, month, day + hour / 24.0) - 2444238.5;
			double ls = sun_position(j);
			double lm = moon_position(j, ls);

			double t = lm - ls;
			if (t < 0)
				t += 360;
			ip.argvalue = (int) ((t + 22.5) / 45) & 0x7;
			return (1.0 - cos((lm - ls) * (3.1415926535897932384626433832795 / 180.0))) / 2;
		}

		public static void nextDay(RefObject<Integer> y, RefObject<Integer> m,
				RefObject<Integer> d, double dd)
		{
			TimePlace tp = new TimePlace();
			double jd = Julian(y.argvalue, m.argvalue, d.argvalue);

			jd += dd;
			JulianToDate(tp, jd);

			y.argvalue = tp.year;
			m.argvalue = tp.month;
			d.argvalue = tp.day;
		}
	}

	static private final double pi = Math.PI;

	// Constants for Julian Date
	static private final double kNumberOfDayInOneYear = 365.25;
	static private final double kNumberOfDayInOneMonth = 30.6001;// average of the dDay in one
																	// dMonth about X000 dYears
	static private final double kInitialYear = 4716.0;

	// Constants for the Object Date
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
	 * @param _date
	 *            Date of the computer. Will be use to calculate the Sideral Time
	 * @param _dLat
	 *            It's the Latitude of the star's pointer
	 * @param _dLon
	 *            It's the Longitude of the star's pointer
	 */
	public Mathematics(Calendar _date, double _dLat, double _dLon)
	{
		calculateDateTime(_date);

		this.dLatitude = Math.toRadians(_dLat);
		this.dLongitude = Math.toRadians(_dLon);

		this.dDate_JulianCalendar = calculate_JulianDate(this.dDay, this.dMonth, this.dYear,
				this.dHour, this.dMinute, this.dSecond);
		this.dSideral_Time = calculateSideralTime(this.dDay, this.dMonth, this.dYear, this.dHour,
				this.dMinute, this.dSecond);

		this.dAngle_Sideral_Time = calculateSideralHourAngle(this.dSideral_Time);
		this.dAngle_Hour = calculateHourAngle(this.dHour, this.dMinute, this.dGMT);
		this.dAngle = this.dAngle_Hour + this.dAngle_Sideral_Time;
	}

	/**
	 * Calculates all the informations that the program needs
	 * 
	 * @param _dDec
	 *            It's the Declination of the star
	 * @param _dAsc
	 *            It's the Ascension of the star
	 */
	public void calculateAll(double _dDec, double _dAsc)
	{
		this.dDeclination = Math.toRadians(_dDec);
		this.dAscension = _dAsc * 2 * pi / 24.0;

		this.dHour_Angle_Star = this.dAngle - this.dAscension + this.dLongitude;

		this.dHeight = calculate_height(this.dDeclination, this.dLatitude, this.dHour_Angle_Star);
		this.dAzimuth = calculate_azimuth(this.dDeclination, this.dLatitude, this.dHeight,
				this.dHour_Angle_Star);

		this.dX = calculateX(this.dHeight, this.dAzimuth);
		this.dY = calculateY(this.dHeight, this.dAzimuth);
	}

	/**
	 * Number - Math.floor(Number)
	 * 
	 * @param double x The number
	 * @return double Result of the operation
	 */
	private double Frac(double x)
	{
		return x - Math.floor(x);
	}

	/**
	 * Calculates the sun's declination and ascension and uses calculateAll()
	 */
	public void calculatePositionSun()
	{
		// Thank to Patrick Ellenberger for the translating C++ to Java with the book
		// "Astronomy on the Personal Computer"

		double T = (this.dDate_JulianCalendar - 2451545.0) / 36525.0;
		double eps = Math.toRadians(23.43929111);
		double L, M;
		double pi2 = 2.0 * pi;
		M = pi2 * Frac(0.993133 + 99.997361 * T);
		L = pi2
				* Frac(0.7859453 + M / pi2 + (6893.0 * sin(M) + 72.0 * sin(2.0 * M) + 6191.2 * T)
						/ 1296.0e3);

		double S = sin(-eps);
		double C = cos(-eps);

		double mat[][] = new double[3][3];

		mat[0][0] = 1.0;
		mat[1][0] = 0.0;
		mat[2][0] = 0.0;
		mat[0][1] = 0.0;
		mat[1][1] = +C;
		mat[2][1] = -S;
		mat[0][2] = 0.0;
		mat[1][2] = +S;
		mat[2][2] = +C;

		double phi = L;
		double theta = 0.0;
		// double r = 0.0;

		double vec[] = new double[3];
		vec[0] = L;
		vec[1] = 0.0;
		vec[2] = 1.0;

		double cosEl = cos(theta);
		vec[0] = 1 * cos(phi) * cosEl;
		vec[1] = 1 * sin(phi) * cosEl;
		vec[2] = 1 * sin(theta);

		double e_sun[] = new double[3];

		for (int i = 0; i < 3; i++)
		{
			double Scalp = 0.0;

			for (int j = 0; j < 3; j++)
				Scalp += mat[i][j] * vec[j];

			e_sun[i] = Scalp;
		}

		double rhoSqr = e_sun[0] * e_sun[0] + e_sun[1] * e_sun[1];
		// double m_r = Math.sqrt(rhoSqr + e_sun[2] * e_sun[2]);

		if ((e_sun[0] == 0.0) && (e_sun[1] == 0.0))
			phi = 0.0;
		else
			phi = Math.atan2(e_sun[1], e_sun[0]);

		if (phi < 0.0)
			phi += 2.0 * pi;

		double rho = Math.sqrt(rhoSqr);
		if ((e_sun[2] == 0.0) && (rho == 0.0))
			theta = 0.0;
		else
			theta = Math.atan2(e_sun[2], rho);

		this.dAscension = phi * 24.0 / (2 * pi);
		this.dDeclination = Math.toDegrees(theta);

		this.calculateAll(this.dDeclination, this.dAscension);
	}

	/**
	 * Calculates the moon's declination and ascension and uses calculateAll()
	 */
	public void calculatePositionMoon()
	{
		// Thank to Patrick Ellenberger for the translating C++ to Java with the book
		// "Astronomy on the Personal Computer"

		double T = (this.dDate_JulianCalendar - 2451545.0) / 36525.0;
		double eps = Math.toRadians(23.43929111);
		double Arcs = Math.toDegrees(3600.0);

		double pi2 = 2.0 * pi;
		double L_0, l, ls, F, D, dL, S, h, N, l_Moon, b_Moon;
		L_0 = Frac(0.606433 + 1336.855225 * T);
		l = pi2 * Frac(0.374897 + 1325.552410 * T);
		ls = pi2 * Frac(0.993133 + 99.997361 * T);
		D = pi2 * Frac(0.827361 + 1236.853086 * T);
		F = pi2 * Frac(0.259086 + 1342.227825 * T);

		dL = +22640 * sin(l) - 4586 * sin(l - 2 * D) + 2370 * sin(2 * D) + 769 * sin(2 * l) - 668
				* sin(ls) - 412 * sin(2 * F) - 212 * sin(2 * l - 2 * D) - 206 * sin(l + ls - 2 * D)
				+ 192 * sin(l + 2 * D) - 165 * sin(ls - 2 * D) - 125 * sin(D) - 110 * sin(l + ls)
				+ 148 * sin(l - ls) - 55 * sin(2 * F - 2 * D);
		S = F + (dL + 412 * sin(2 * F) + 541 * sin(ls)) / Arcs;
		h = F - 2 * D;
		N = -526 * sin(h) + 44 * sin(l + h) - 31 * sin(-l + h) - 23 * sin(ls + h) + 11
				* sin(-ls + h) - 25 * sin(-2 * l + F) + 21 * sin(-l + F);
		l_Moon = pi2 * Frac(L_0 + dL / 1296.0e3);
		b_Moon = (18520.0 * sin(S) + N) / Arcs;

		double Srot = sin(-eps);
		double Crot = cos(-eps);

		double mat[][] = new double[3][3];

		mat[0][0] = 1.0;
		mat[1][0] = 0.0;
		mat[2][0] = 0.0;
		mat[0][1] = 0.0;
		mat[1][1] = +Crot;
		mat[2][1] = -Srot;
		mat[0][2] = 0.0;
		mat[1][2] = +Srot;
		mat[2][2] = +Crot;

		double phi = l_Moon;
		double theta = b_Moon;
		// double r = 0.0;

		double vec[] = new double[3];
		vec[0] = l_Moon;
		vec[1] = b_Moon;
		vec[2] = 1.0;

		double cosEl = cos(theta);
		vec[0] = 1 * cos(phi) * cosEl;
		vec[1] = 1 * sin(phi) * cosEl;
		vec[2] = 1 * sin(theta);

		double e_Moon[] = new double[3];

		for (int i = 0; i < 3; i++)
		{
			double Scalp = 0.0;

			for (int j = 0; j < 3; j++)
			{
				Scalp += mat[i][j] * vec[j];
			}

			e_Moon[i] = Scalp;
		}

		double rhoSqr = e_Moon[0] * e_Moon[0] + e_Moon[1] * e_Moon[1];
		// double m_r = Math.sqrt(rhoSqr + e_Moon[2] * e_Moon[2]);

		if ((e_Moon[0] == 0.0) && (e_Moon[1] == 0.0))
			phi = 0.0;
		else
			phi = Math.atan2(e_Moon[1], e_Moon[0]);

		if (phi < 0.0)
			phi += 2.0 * pi;

		double rho = Math.sqrt(rhoSqr);
		if ((e_Moon[2] == 0.0) && (rho == 0.0))
			theta = 0.0;
		else
			theta = Math.atan2(e_Moon[2], rho);

		this.dAscension = phi * 24.0 / (2 * pi);
		this.dDeclination = Math.toDegrees(theta);

		this.calculateAll(this.dDeclination, this.dAscension);
	}

	/**
	 * Give a percentage that represents the moon's brightness
	 * 
	 * @param _yesterday
	 *            True if you want the moon's brightness for yesterday
	 * @return double Return a percentage
	 */
	public double getMoonBrightness(boolean _yesterday)
	{
		// Source : http://www.voidware.com/moon_phase.htm
		// Adapted in Java and modified by Diego Antognini
		int y = (int) this.dYear;
		int m = (int) this.dMonth;
		int d = (int) this.dDay;

		if (_yesterday)
			if (d > 1)
				d--;
			else
			{
				if (m == 1)
				{
					d = 31;
					m = 12;
					y--;
				}
				else
				{
					m--;
					if (m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12)
						d = 31;
					else if (m == 2)
						if (isLeapYear(y))
							d = 29;
						else
							d = 28;
					else
						d = 30;
				}
			}

		double h;
		double step = 1;
		int begun = 0;

		double pmax = 0;
		double pmin = 1;
		double plast = 0;
		double brightness = 0;
		double p;
		int ip = 0;

		for (h = 0; h < 24; h += step)
		{
			RefObject<Integer> tempRef_ip = new RefObject<Integer>(ip);
			p = GlobalMembers.moon_phase(y, m, d, h, tempRef_ip);
			ip = tempRef_ip.argvalue;

			if (begun != 0)
			{
				if (p > plast && p > pmax)
					pmax = p;
				if (p < plast && p < pmin)
					pmin = p;
				if (h == 16)
					brightness = Math.floor(p * 1000 + 0.5) / 10;
			}
			else
				begun = 1;
			plast = p;
		}
		RefObject<Integer> tempRef_y = new RefObject<Integer>(y);
		RefObject<Integer> tempRef_m = new RefObject<Integer>(m);
		RefObject<Integer> tempRef_d = new RefObject<Integer>(d);
		GlobalMembers.nextDay(tempRef_y, tempRef_m, tempRef_d, 1.0);
		y = tempRef_y.argvalue;
		m = tempRef_m.argvalue;
		d = tempRef_d.argvalue;
		return brightness;
	}

	/**
	 * Calculates the informations from the date and the time
	 * 
	 * @param _date
	 *            Use a calendar for calculate the GTM hour
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
	 * Give the new coordinate of a star with a rotation
	 * @param _dX
	 * 			X coordinate
	 * @param _dY
	 * 			Y coordinate
	 * @param _dAngle
	 * 			Angle for the rotation
	 * @return
	 */
	static public double[] getNewXYRotation(double _dX,double _dY,double _dAngle)
	{		
		return new double[] {
				cos(Math.toRadians(_dAngle)) * _dX - sin(Math.toRadians(_dAngle)) * _dY ,
				sin(Math.toRadians(_dAngle)) * _dX + cos(Math.toRadians(_dAngle)) * _dY
		};
	}
	
	/**
	 * Give the origin's coordinates
	 * 
	 * @Param _dPitch
	 * 			Pic's pitch (degree, [-90;90] -90=top)
	 * @Param _dAzimuth
	 * 			Pic's Azimuth (degree, [-180;180] 0=North)
	 * 
	 * @return Double[] first case contains the X and the second one the y
	 */
	static public double[] getOrigin(double _dPitch,double _dAzimuth)
	{
		double l_dX = 0.0, l_dY = 0.0;
		
		if(_dPitch > 0)
			l_dY*=-1.0;
		
		l_dX = sin(Math.toRadians(_dAzimuth));
		l_dY = sin(Math.toRadians(_dPitch))*-1.0;
		
		return new double[] {
				l_dX,
				l_dY
				};
	}
	
	/**
	 * Say if the year is leap or not
	 * 
	 * @param _year
	 *            Year to check
	 * @return True if yes or false
	 */
	static public boolean isLeapYear(int _year)
	{
		if (_year % 4 == 0)
			if (_year % 100 == 0)
				if (_year % 400 == 0)
					return true;
				else
					return false;
			else
				return true;
		else
			return false;
	}

	/**
	 * Gives the hour from HMT
	 * 
	 * @param _cal
	 *            Use a calendar for calculate the GTM hour
	 * @return Return an int that's the hour GMT
	 */
	static public int calculateHourGMT(Calendar _cal)
	{
		TimeZone l_t = _cal.getTimeZone();

		return l_t.getOffset(_cal.getTimeInMillis()) / 1000 / 3600;
	}

	/**
	 * Calculates the X value of a coordinate system of 2D. It's a projection.
	 * 
	 * @param _dHeight
	 *            The Height of the star, calculated with "calculate_Height()"
	 * @param _dAzimuth
	 *            The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return A double that contains the X coordinate
	 */
	static public double calculateX(double _dHeight, double _dAzimuth)
	{
		double l_x = -1 * ((-2.0 / pi) * _dHeight + 1);

		return l_x * sin(_dAzimuth);
	}

	/**
	 * Calculates the Y value of a coordinate system of 2D. It's a projection.
	 * 
	 * @param _dHeight
	 *            The Height of the star, calculated with "calculate_Height()"
	 * @param _dAzimuth
	 *            The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return : Return a double that contains the Y coordinate
	 */
	static public double calculateY(double _dHeight, double _dAzimuth)
	{
		double l_y = 1 * ((-2.0 / pi) * _dHeight + 1);

		return l_y * cos(_dAzimuth);
	}

	/**
	 * Converts ° ' '' dAngle to degree
	 * 
	 * @param _dHour
	 *            The hour of the value
	 * @param _dMinute
	 *            The Minute of the value
	 * @param _dSecond
	 *            The Second of the value
	 * @return A double that's the degree dAngle
	 */
	static public double hms(double _dHour, double _dMinute, double _dSecond)
	{
		double sign;
		sign = (_dHour < 0 || _dMinute < 0 || _dSecond < 0) ? -1 : 1;
		return sign * (Math.abs(_dHour) + Math.abs(_dMinute) / 60.0 + Math.abs(_dSecond) / 36000);
	}

	/**
	 * Calculates the fraction of day about a time
	 * 
	 * @param _dHour
	 *            The hour of the value
	 * @param _dMinute
	 *            The Minute of the value
	 * @param _dSecond
	 *            The Second of the value
	 * @return A double that contains the value
	 */
	static public double fractionOfDay(double _dHour, double _dMinute, double _dSecond)
	{
		return _dHour / 24 + _dMinute / (24 * 60) + _dSecond / (24 * 60 * 60);
	}

	/**
	 * Calculates the dHeight of the fixed point
	 * 
	 * @param _dec
	 *            The Declination of the star
	 * @param _dLat
	 *            The Latitude of the pointer's star
	 * @param _star_angle
	 *            The hour angle star
	 * @return A double that's the Height of the star
	 */
	static public double calculate_height(double _dec, double _dLat, double _star_angle)
	{
		double l_sinh = sin(_dec) * sin(_dLat) - cos(_dec) * cos(_dLat) * cos(_star_angle);
		return arcsin(l_sinh);
	}

	/**
	 * Calculates the dAzimuth
	 * 
	 * @param _dec
	 *            The dDeclination of the star
	 * @param _dLat
	 *            The dLatitude of the pointer's star
	 * @param _dHeight
	 *            The dHeight of the star, calculated with "calculate_height()"
	 * @param _star_angle
	 *            The star's dAngle
	 * @return A double that's the dAzimuth of the star
	 */
	static public double calculate_azimuth(double _dec, double _dLat, double _dHeight,
			double _star_angle)
	{
		double l_cos_az = (sin(_dec) - sin(_dLat) * sin(_dHeight)) / (cos(_dLat) * cos(_dHeight));
		double l_sin_a = cos(_dec) * sin(_star_angle) / cos(_dHeight);

		if (l_sin_a > 0)
			return arccos(l_cos_az);
		else
			return -arccos(l_cos_az);
	}

	/**
	 * Converts a gregorian date to a julian date
	 * 
	 * @param _dDay
	 *            The day
	 * @param _dMonth
	 *            The month
	 * @param _dYear
	 *            The year
	 * @param _dHour
	 *            The hour
	 * @param _dMinute
	 *            The minute
	 * @param _dSeconde
	 *            The second
	 * @return A double that's the result
	 */
	static public double calculate_JulianDate(double _dDay, double _dMonth, double _dYear,
			double _dHour, double _dMinute, double _dSeconde)
	{
		if (_dMonth < 3)
		{
			_dYear--;
			_dMonth += 12;
		}
		int l_c = (int) (_dYear / 100);
		int l_b = 2 - l_c + (l_c / 4);

		int l_t = (int) (fractionOfDay(_dHour, _dMinute, _dSeconde));

		return ((int) (kNumberOfDayInOneYear * (_dYear + kInitialYear))
				+ (int) (kNumberOfDayInOneMonth * (_dMonth + 1)) + _dDay + l_t + l_b - 1524.5);
	}

	/**
	 * Calculates the sideral hour angle
	 * 
	 * @param _dSideral_Time
	 *            The Sideral time
	 * @return A double that's the result
	 */
	static public double calculateSideralHourAngle(double _dSideral_Time)
	{
		return 2.0 * pi * _dSideral_Time / hms(23.0, 56.0, 4.0);
	}

	/**
	 * Calculates the hours dAngle
	 * 
	 * @param _dHour
	 *            The hour
	 * @param _dMin
	 *            The minute
	 * @param _dGMT
	 *            The GMT zone
	 * @return A double that's the result
	 */
	static public double calculateHourAngle(double _dHour, double _dMin, double _dGMT)
	{
		return (_dHour - 12.0 + _dMin / 60.0 - _dGMT) * 2.0 * pi / hms(23.0, 56.0, 4.0);
	}

	/**
	 * Calculate the sideral time
	 * 
	 * @param _dDay
	 *            The day
	 * @param _dMonth
	 *            The month
	 * @param _dYear
	 *            The year
	 * @param _dHour
	 *            The hour
	 * @param _dMinute
	 *            The _minute
	 * @param _dSecond
	 *            The second
	 * @return A double that's the result
	 */
	static public double calculateSideralTime(double _dDay, double _dMonth, double _dYear,
			double _dHour, double _dMinute, double _dSecond)
	{
		double l_JJ = calculate_JulianDate(_dDay, _dMonth, _dYear, _dHour, _dMinute, _dSecond);
		double l_T = (l_JJ - 2451545.0) / 36525.0;
		double l_H1 = 24110.54841 + 8640184.812866 * l_T + 0.093104 * l_T * l_T - 0.0000062 * l_T
				* l_T * l_T;
		double l_HSH = l_H1 / 3600.0;
		double l_HS = (l_HSH / 24.0 - (int) (l_HSH / 24.0)) * 24.0;

		return l_HS;
	}

	/**
	 * Smooth the sensor value by applying a low-pass filter to it.
	 * 
	 * From http://stackoverflow.com/a/6462517/1045559
	 * 
	 * @param _newValue
	 *            The newest measure of the sensor
	 * @param _oldValue
	 *            The previous value returned by this method
	 * @param _smoothFactor
	 *            A value defining how smooth the movement should be. 1 is no smoothing and 0 is
	 *            never updating.
	 * @param _smoothThreshold
	 *            The threshold in which the distance is big enough to turn immediately. 0 is jump
	 *            always, 360 (for a compass) is never jumping.
	 * @return The smoothed value. It has to be passed to the next call of this algorithm.
	 */
	public static double smooth(double _newValue, double _oldValue, double _smoothFactor,
			double _smoothThreshold)
	{
		if (Math.abs(_newValue - _oldValue) < 180)
		{
			if (Math.abs(_newValue - _oldValue) > _smoothFactor)
			{
				_oldValue = _newValue;
			}
			else
			{
				_oldValue = _oldValue + _smoothFactor * (_newValue - _oldValue);
			}
		}
		else
		{
			if (360.0 - Math.abs(_newValue - _oldValue) > _smoothFactor)
			{
				_oldValue = _newValue;
			}
			else
			{
				if (_oldValue > _newValue)
				{
					_oldValue = (_oldValue + _smoothFactor * ((360 + _newValue - _oldValue) % 360) + 360) % 360;
				}
				else
				{
					_oldValue = (_oldValue - _smoothFactor * ((360 - _newValue + _oldValue) % 360) + 360) % 360;
				}
			}
		}

		return _oldValue;
	}

	/**
	 * Calculate the 3 angles, which are azimuth, pitch and roll.
	 * 
	 * @param res
	 *            A 3 values array which will contains the results (heading, pitch, roll)
	 * @param acc
	 *            The 3 vectors of the accelerometer, in the PIC standard (0-65536).
	 * @param mag
	 *            The 3 vectors of the magnetometer, in the PIC standard (0-65536).
	 * @return true, if successful
	 */
	static public boolean calculateAngles(double[] res, int[] acc, int[] mag)
	{
		if (res.length != 3 || acc.length != 3 || mag.length != 3)
			return false;

		float accX = acc[0], accY = acc[1], accZ = acc[2];
		float magX = mag[0], magY = mag[1], magZ = mag[2];

		// On repasse en signé
		accX -= 32768.0;
		accY -= 32768.0;
		accZ -= 32768.0;
		magX -= 32768.0;
		magY -= 32768.0;
		magZ -= 32768.0;

		// On repasse à des valeurs non bornée
		accX /= 16384.0;
		accY /= 16384.0;
		accZ /= 16384.0;
		magX /= 32768.0;
		magY /= 32768.0;
		magZ /= 32768.0;

		// On passe en Newton et en uT
		accX *= 9.81;
		accY *= 9.81;
		accZ *= 9.81;
		magX *= 80.0;
		magY *= 80.0;
		magZ *= 80.0;

		// Conversion du système du PIC vers le système standard
		accZ *= -1.0;
		magX *= -1.0;
		magY *= -1.0;

		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(
				"x: " + accX + "\ny: " + accY + "\nz: " + accZ + "\nx: " + magX + "\ny: " + magY
						+ "\nz: " + magZ);
		float R[] = new float[9];
		float andacc[] = new float[] { accX, accY, accZ };
		float andmag[] = new float[] { magX, magY, magZ };

		if (mag != null && acc != null)
		{
			boolean success = getRotationMatrix(R, null, andacc, andmag);
			if (success)
			{
				float outR[] = new float[9];
				remapCoordinateSystem(R, 1, 3, outR);
				float orientation[] = new float[3];
				getOrientation(outR, orientation);
				for (int l_j = 0; l_j < orientation.length; l_j++)
				{
					res[l_j] = Math.toDegrees(orientation[l_j]);
				}

				return true;
			}
		}
		return false;
	}

	/**
	 * Copyright (C) 2008 The Android Open Source Project
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
	 * except in compliance with the License. You may obtain a copy of the License at
	 * 
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * Unless required by applicable law or agreed to in writing, software distributed under the
	 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
	 * either express or implied. See the License for the specific language governing permissions
	 * and limitations under the License.
	 * 
	 * <p>
	 * Computes the inclination matrix <b>I</b> as well as the rotation matrix <b>R</b> transforming
	 * a vector from the device coordinate system to the world's coordinate system which is defined
	 * as a direct orthonormal basis, where:
	 * </p>
	 * 
	 * <ul>
	 * <li>X is defined as the vector product <b>Y.Z</b> (It is tangential to the ground at the
	 * device's current location and roughly points East).</li>
	 * <li>Y is tangential to the ground at the device's current location and points towards the
	 * magnetic North Pole.</li>
	 * <li>Z points towards the sky and is perpendicular to the ground.</li>
	 * </ul>
	 * 
	 * <p>
	 * <center><img src="https://developer.android.com/images/axis_globe.png"
	 * alt="World coordinate-system diagram." border="0" /></center>
	 * </p>
	 * 
	 * <p>
	 * <hr>
	 * <p>
	 * By definition:
	 * <p>
	 * [0 0 g] = <b>R</b> * <b>gravity</b> (g = magnitude of gravity)
	 * <p>
	 * [0 m 0] = <b>I</b> * <b>R</b> * <b>geomagnetic</b> (m = magnitude of geomagnetic field)
	 * <p>
	 * <b>R</b> is the identity matrix when the device is aligned with the world's coordinate
	 * system, that is, when the device's X axis points toward East, the Y axis points to the North
	 * Pole and the device is facing the sky.
	 * 
	 * <p>
	 * <b>I</b> is a rotation matrix transforming the geomagnetic vector into the same coordinate
	 * space as gravity (the world's coordinate space). <b>I</b> is a simple rotation around the X
	 * axis. The inclination angle in radians can be computed with {@link #getInclination}.
	 * <hr>
	 * 
	 * <p>
	 * Each matrix is returned either as a 3x3 or 4x4 row-major matrix depending on the length of
	 * the passed array:
	 * <p>
	 * <u>If the array length is 16:</u>
	 * 
	 * <pre>
	 *   /  M[ 0]   M[ 1]   M[ 2]   M[ 3]  \
	 *   |  M[ 4]   M[ 5]   M[ 6]   M[ 7]  |
	 *   |  M[ 8]   M[ 9]   M[10]   M[11]  |
	 *   \  M[12]   M[13]   M[14]   M[15]  /
	 * </pre>
	 * 
	 * This matrix is ready to be used by OpenGL ES's
	 * {@link javax.microedition.khronos.opengles.GL10#glLoadMatrixf(float[], int)
	 * glLoadMatrixf(float[], int)}.
	 * <p>
	 * Note that because OpenGL matrices are column-major matrices you must transpose the matrix
	 * before using it. However, since the matrix is a rotation matrix, its transpose is also its
	 * inverse, conveniently, it is often the inverse of the rotation that is needed for rendering;
	 * it can therefore be used with OpenGL ES directly.
	 * <p>
	 * Also note that the returned matrices always have this form:
	 * 
	 * <pre>
	 *   /  M[ 0]   M[ 1]   M[ 2]   0  \
	 *   |  M[ 4]   M[ 5]   M[ 6]   0  |
	 *   |  M[ 8]   M[ 9]   M[10]   0  |
	 *   \      0       0       0   1  /
	 * </pre>
	 * 
	 * <p>
	 * <u>If the array length is 9:</u>
	 * 
	 * <pre>
	 *   /  M[ 0]   M[ 1]   M[ 2]  \
	 *   |  M[ 3]   M[ 4]   M[ 5]  |
	 *   \  M[ 6]   M[ 7]   M[ 8]  /
	 * </pre>
	 * 
	 * <hr>
	 * <p>
	 * The inverse of each matrix can be computed easily by taking its transpose.
	 * 
	 * <p>
	 * The matrices returned by this function are meaningful only when the device is not
	 * free-falling and it is not close to the magnetic north. If the device is accelerating, or
	 * placed into a strong magnetic field, the returned matrices may be inaccurate.
	 * 
	 * @param R
	 *            is an array of 9 floats holding the rotation matrix <b>R</b> when this function
	 *            returns. R can be null.
	 *            <p>
	 * 
	 * @param I
	 *            is an array of 9 floats holding the rotation matrix <b>I</b> when this function
	 *            returns. I can be null.
	 *            <p>
	 * 
	 * @param gravity
	 *            is an array of 3 floats containing the gravity vector expressed in the device's
	 *            coordinate. You can simply use the {@link android.hardware.SensorEvent#values
	 *            values} returned by a {@link android.hardware.SensorEvent SensorEvent} of a
	 *            {@link android.hardware.Sensor Sensor} of type
	 *            {@link android.hardware.Sensor#TYPE_ACCELEROMETER TYPE_ACCELEROMETER}.
	 *            <p>
	 * 
	 * @param geomagnetic
	 *            is an array of 3 floats containing the geomagnetic vector expressed in the 
	 *            device's coordinate. You can simply use the
	 *            {@link android.hardware.SensorEvent#values values} returned by a
	 *            {@link android.hardware.SensorEvent SensorEvent} of a
	 *            {@link android.hardware.Sensor Sensor} of type
	 *            {@link android.hardware.Sensor#TYPE_MAGNETIC_FIELD TYPE_MAGNETIC_FIELD}.
	 * 
	 * @return <code>true</code> on success, <code>false</code> on failure (for instance, if the
	 *         device is in free fall). On failure the output matrices are not modified.
	 * 
	 * @see #getOrientation(float[], float[])
	 * @see #remapCoordinateSystem(float[], int, int, float[])
	 */
	public static boolean getRotationMatrix(float[] R, float[] I, float[] gravity,
			float[] geomagnetic)
	{
		float Ax = gravity[0];
		float Ay = gravity[1];
		float Az = gravity[2];
		final float Ex = geomagnetic[0];
		final float Ey = geomagnetic[1];
		final float Ez = geomagnetic[2];
		float Hx = Ey * Az - Ez * Ay;
		float Hy = Ez * Ax - Ex * Az;
		float Hz = Ex * Ay - Ey * Ax;
		final float normH = (float) Math.sqrt(Hx * Hx + Hy * Hy + Hz * Hz);
		if (normH < 0.1f)
		{
			// device is close to free fall (or in space?), or close to
			// magnetic north pole. Typical values are > 100.
			return false;
		}
		final float invH = 1.0f / normH;
		Hx *= invH;
		Hy *= invH;
		Hz *= invH;
		final float invA = 1.0f / (float) Math.sqrt(Ax * Ax + Ay * Ay + Az * Az);
		Ax *= invA;
		Ay *= invA;
		Az *= invA;
		final float Mx = Ay * Hz - Az * Hy;
		final float My = Az * Hx - Ax * Hz;
		final float Mz = Ax * Hy - Ay * Hx;
		if (R != null)
		{
			if (R.length == 9)
			{
				R[0] = Hx;
				R[1] = Hy;
				R[2] = Hz;
				R[3] = Mx;
				R[4] = My;
				R[5] = Mz;
				R[6] = Ax;
				R[7] = Ay;
				R[8] = Az;
			}
			else if (R.length == 16)
			{
				R[0] = Hx;
				R[1] = Hy;
				R[2] = Hz;
				R[3] = 0;
				R[4] = Mx;
				R[5] = My;
				R[6] = Mz;
				R[7] = 0;
				R[8] = Ax;
				R[9] = Ay;
				R[10] = Az;
				R[11] = 0;
				R[12] = 0;
				R[13] = 0;
				R[14] = 0;
				R[15] = 1;
			}
		}
		if (I != null)
		{
			// compute the inclination matrix by projecting the geomagnetic
			// vector onto the Z (gravity) and X (horizontal component
			// of geomagnetic vector) axes.
			final float invE = 1.0f / (float) Math.sqrt(Ex * Ex + Ey * Ey + Ez * Ez);
			final float c = (Ex * Mx + Ey * My + Ez * Mz) * invE;
			final float s = (Ex * Ax + Ey * Ay + Ez * Az) * invE;
			if (I.length == 9)
			{
				I[0] = 1;
				I[1] = 0;
				I[2] = 0;
				I[3] = 0;
				I[4] = c;
				I[5] = s;
				I[6] = 0;
				I[7] = -s;
				I[8] = c;
			}
			else if (I.length == 16)
			{
				I[0] = 1;
				I[1] = 0;
				I[2] = 0;
				I[4] = 0;
				I[5] = c;
				I[6] = s;
				I[8] = 0;
				I[9] = -s;
				I[10] = c;
				I[3] = I[7] = I[11] = I[12] = I[13] = I[14] = 0;
				I[15] = 1;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Rotates the supplied rotation matrix so it is expressed in a different coordinate system.
	 * This is typically used when an application needs to compute the three orientation angles of
	 * the device (see {@link #getOrientation}) in a different coordinate system.
	 * </p>
	 * 
	 * <p>
	 * When the rotation matrix is used for drawing (for instance with OpenGL ES), it usually
	 * <b>doesn't need</b> to be transformed by this function, unless the screen is physically
	 * rotated, in which case you can use {@link android.view.Display#getRotation()
	 * Display.getRotation()} to retrieve the current rotation of the screen. Note that because the
	 * user is generally free to rotate their screen, you often should consider the rotation in
	 * deciding the parameters to use here.
	 * </p>
	 * 
	 * <p>
	 * <u>Examples:</u>
	 * <p>
	 * 
	 * <ul>
	 * <li>Using the camera (Y axis along the camera's axis) for an augmented reality application
	 * where the rotation angles are needed:</li>
	 * 
	 * <p>
	 * <ul>
	 * <code>remapCoordinateSystem(inR, AXIS_X, AXIS_Z, outR);</code>
	 * </ul>
	 * </p>
	 * 
	 * <li>Using the device as a mechanical compass when rotation is
	 * {@link android.view.Surface#ROTATION_90 Surface.ROTATION_90}:</li>
	 * 
	 * <p>
	 * <ul>
	 * <code>remapCoordinateSystem(inR, AXIS_Y, AXIS_MINUS_X, outR);</code>
	 * </ul>
	 * </p>
	 * 
	 * Beware of the above example. This call is needed only to account for a rotation from its
	 * natural orientation when calculating the rotation angles (see {@link #getOrientation}). If
	 * the rotation matrix is also used for rendering, it may not need to be transformed, for
	 * instance if your {@link android.app.Activity Activity} is running in landscape mode.
	 * </ul>
	 * 
	 * <p>
	 * Since the resulting coordinate system is orthonormal, only two axes need to be specified.
	 * 
	 * @param inR
	 *            the rotation matrix to be transformed. Usually it is the matrix returned by
	 *            {@link #getRotationMatrix}.
	 * 
	 * @param X
	 *            defines on which world axis and direction the X axis of the device is mapped.
	 * 
	 * @param Y
	 *            defines on which world axis and direction the Y axis of the device is mapped.
	 * 
	 * @param outR
	 *            the transformed rotation matrix. inR and outR can be the same array, but it is not
	 *            recommended for performance reason.
	 * 
	 * @return <code>true</code> on success. <code>false</code> if the input parameters are
	 *         incorrect, for instance if X and Y define the same axis. Or if inR and outR don't
	 *         have the same length.
	 * 
	 * @see #getRotationMatrix(float[], float[], float[], float[])
	 */
	private static boolean remapCoordinateSystem(float[] inR, int X, int Y, float[] outR)
	{
		/*
		 * X and Y define a rotation matrix 'r':
		 * 
		 * (X==1)?((X&0x80)?-1:1):0 (X==2)?((X&0x80)?-1:1):0 (X==3)?((X&0x80)?-1:1):0
		 * (Y==1)?((Y&0x80)?-1:1):0 (Y==2)?((Y&0x80)?-1:1):0 (Y==3)?((X&0x80)?-1:1):0 r[0] ^ r[1]
		 * 
		 * where the 3rd line is the vector product of the first 2 lines
		 */

		final int length = outR.length;
		if (inR.length != length)
			return false; // invalid parameter
		if ((X & 0x7C) != 0 || (Y & 0x7C) != 0)
			return false; // invalid parameter
		if (((X & 0x3) == 0) || ((Y & 0x3) == 0))
			return false; // no axis specified
		if ((X & 0x3) == (Y & 0x3))
			return false; // same axis specified

		// Z is "the other" axis, its sign is either +/- sign(X)*sign(Y)
		// this can be calculated by exclusive-or'ing X and Y; except for
		// the sign inversion (+/-) which is calculated below.
		int Z = X ^ Y;

		// extract the axis (remove the sign), offset in the range 0 to 2.
		final int x = (X & 0x3) - 1;
		final int y = (Y & 0x3) - 1;
		final int z = (Z & 0x3) - 1;

		// compute the sign of Z (whether it needs to be inverted)
		final int axis_y = (z + 1) % 3;
		final int axis_z = (z + 2) % 3;
		if (((x ^ axis_y) | (y ^ axis_z)) != 0)
			Z ^= 0x80;

		final boolean sx = (X >= 0x80);
		final boolean sy = (Y >= 0x80);
		final boolean sz = (Z >= 0x80);

		// Perform R * r, in avoiding actual muls and adds.
		final int rowLength = ((length == 16) ? 4 : 3);
		for (int j = 0; j < 3; j++)
		{
			final int offset = j * rowLength;
			for (int i = 0; i < 3; i++)
			{
				if (x == i)
					outR[offset + i] = sx ? -inR[offset + 0] : inR[offset + 0];
				if (y == i)
					outR[offset + i] = sy ? -inR[offset + 1] : inR[offset + 1];
				if (z == i)
					outR[offset + i] = sz ? -inR[offset + 2] : inR[offset + 2];
			}
		}
		if (length == 16)
		{
			outR[3] = outR[7] = outR[11] = outR[12] = outR[13] = outR[14] = 0;
			outR[15] = 1;
		}
		return true;
	}

	/**
	 * Computes the device's orientation based on the rotation matrix.
	 * <p>
	 * When it returns, the array values is filled with the result:
	 * <ul>
	 * <li>values[0]: <i>azimuth</i>, rotation around the Z axis.</li>
	 * <li>values[1]: <i>pitch</i>, rotation around the X axis.</li>
	 * <li>values[2]: <i>roll</i>, rotation around the Y axis.</li>
	 * </ul>
	 * <p>
	 * The reference coordinate-system used is different from the world coordinate-system defined
	 * for the rotation matrix:
	 * </p>
	 * <ul>
	 * <li>X is defined as the vector product <b>Y.Z</b> (It is tangential to the ground at the
	 * device's current location and roughly points West).</li>
	 * <li>Y is tangential to the ground at the device's current location and points towards the
	 * magnetic North Pole.</li>
	 * <li>Z points towards the center of the Earth and is perpendicular to the ground.</li>
	 * </ul>
	 * 
	 * <p>
	 * <center><img src="https://developer.android.com/images/axis_globe_inverted.png"
	 * alt="Inverted world coordinate-system diagram." border="0" /></center>
	 * </p>
	 * <p>
	 * All three angles above are in <b>radians</b> and <b>positive</b> in the
	 * <b>counter-clockwise</b> direction.
	 * 
	 * @param R
	 *            rotation matrix see {@link #getRotationMatrix}.
	 * 
	 * @param values
	 *            an array of 3 floats to hold the result.
	 * 
	 * @return The array values passed as argument.
	 * 
	 * @see #getRotationMatrix(float[], float[], float[], float[])
	 * @see GeomagneticField
	 */
	public static float[] getOrientation(float[] R, float values[])
	{
		/*
		 * 4x4 (length=16) case: / R[ 0] R[ 1] R[ 2] 0 \ | R[ 4] R[ 5] R[ 6] 0 | | R[ 8] R[ 9] R[10]
		 * 0 | \ 0 0 0 1 /
		 * 
		 * 3x3 (length=9) case: / R[ 0] R[ 1] R[ 2] \ | R[ 3] R[ 4] R[ 5] | \ R[ 6] R[ 7] R[ 8] /
		 */
		if (R.length == 9)
		{
			values[0] = (float) Math.atan2(R[1], R[4]);
			values[1] = (float) Math.asin(-R[7]);
			values[2] = (float) Math.atan2(-R[6], R[8]);
		}
		else
		{
			values[0] = (float) Math.atan2(R[1], R[5]);
			values[1] = (float) Math.asin(-R[9]);
			values[2] = (float) Math.atan2(-R[8], R[10]);
		}
		return values;
	}

	/**
	 * Convert the pic's longitude to real longitude
	 * 
	 * @param _duLon
	 *            Pic's longitude
	 * @param _cHemisphere
	 *            The actual hemisphere
	 * @return The real longitude
	 */
	static public double picLon2Lon(double _duLon, char _cHemisphere)
	{
		int dd = (int) (_duLon / 100);
		double mm = _duLon - (dd * 100.0);

		double ret = dd + mm / 60.0;

		if (_cHemisphere == 'W' || _cHemisphere == 'w')
			ret *= -1.0;

		return ret;
	}

	/**
	 * Convert the pic's latitude to real longitude
	 * 
	 * @param _duLat
	 *            Pic's latitude
	 * @param _cHemisphere
	 *            The actual hemisphere
	 * @return The real latitude
	 */
	static public double picLat2Lat(double _duLat, char _cHemisphere)
	{
		int dd = (int) (_duLat / 100);
		double mm = _duLat - (dd * 100.0);

		double ret = dd + mm / 60.0;

		if (_cHemisphere == 'S' || _cHemisphere == 's')
			ret *= -1.0;

		return ret;
	}

	/**
	 * Calculate the cosinus of a number
	 * 
	 * @param _dX
	 *            The value to transform
	 * @return The result of the operation
	 */
	static public double cos(double _dX)
	{
		return Math.cos(_dX);
	}

	/**
	 * Calculate the sinus of a number
	 * 
	 * @param _dX
	 *            The value to transform
	 * @return Return the result of the operation
	 */
	static public double sin(double _dX)
	{
		return Math.sin(_dX);
	}

	/**
	 * Calculate the ArcCosinus of a number
	 * 
	 * @param _dX
	 *            The value to transform
	 * @return Return the result of the operation
	 */
	static public double arccos(double _dX)
	{
		return Math.acos(_dX);
	}

	/**
	 * Calculate the ArcSinus of a number
	 * 
	 * @param _dX
	 *            The value to transform
	 * @return Return the result of the operation
	 */
	static public double arcsin(double _dX)
	{
		return Math.asin(_dX);
	}

	/**
	 * Calculate the tan of a number
	 * 
	 * @param _dX
	 *            The value to transform
	 * @return Return the result of the operation
	 */
	static public double tan(double _dX)
	{
		return Math.tan(_dX);
	}

	/**
	 * Calculate the ArcTan of a number
	 * 
	 * @param _dX
	 *            The value to transform
	 * @return Return the result of the operation
	 */
	static public double arctan(double _dX)
	{
		return Math.atan(_dX);
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getHour()
	{
		return dHour;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getMinute()
	{
		return dMinute;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getSecond()
	{
		return dSecond;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getGMT()
	{
		return dGMT;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getDay()
	{
		return dDay;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getMonth()
	{
		return dMonth;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getYear()
	{
		return dYear;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getDate_JulianCalendar()
	{
		return dDate_JulianCalendar;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getSideral_Time()
	{
		return dSideral_Time;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getLatitude()
	{
		return dLatitude;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getLongitude()
	{
		return dLongitude;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getDeclination()
	{
		return dDeclination;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAscension()
	{
		return dAscension;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAzimuth()
	{
		return dAzimuth;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getHeight()
	{
		return dHeight;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getHour_Angle_Star()
	{
		return dHour_Angle_Star;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAngle_Sideral_Time()
	{
		return dAngle_Sideral_Time;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAngle_Hour()
	{
		return dAngle_Hour;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getAngle()
	{
		return dAngle;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getX()
	{
		return dX;
	}

	/**
	 * getter of the private value
	 * 
	 * @return the private variable
	 */
	public double getY()
	{
		return dY;
	}
}