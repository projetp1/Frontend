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

    private double Frac(double x)
    {
        return x - Math.floor(x);
    }
	
	/**
	 * calculatePositionSun
	 * Calculates the sun's declination and ascension and uses calculateAll()
	 */
	public void calculatePositionSun()
	{
         //Thank to Patrick Ellenberger
		
         double T = (this.dDate_JulianCalendar - 2451545.0) / 36525.0;
         double eps = 23.43929111 * Math.PI / 180.0;
         double L,M;
         double pi2 = 2.0 * Math.PI;
         M  = pi2 * Frac ( 0.993133 + 99.997361*T);
         L  = pi2 * Frac ( 0.7859453 + M/pi2 +
                 (6893.0*sin(M)+72.0*sin(2.0*M)+6191.2*T) / 1296.0e3);
         
         double S = sin(-eps);
         double C = cos(-eps);

         double mat[][] = new double [3][3];
         
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
         
         double vec[] = new double [3];
         vec[0] = L;
         vec[1] = 0.0;
         vec[2] = 1.0;
         
         double cosEl = cos(theta);            
         vec[0] = 1 * cos(phi) * cosEl;
         vec[1] = 1 * sin(phi) * cosEl;
         vec[2] = 1 * sin(theta);         
         
         double e_sun[] = new double [3];
         
         for (int i = 0; i < 3; i ++)
         {
             double Scalp = 0.0;
             
             for (int j = 0; j < 3; j ++)
                 Scalp += mat[i][j] * vec[j];                    
             
             e_sun[i] = Scalp;
         }
         
         double rhoSqr = e_sun[0] * e_sun[0] + e_sun[1] * e_sun[1];
         double m_r = Math.sqrt(rhoSqr + e_sun[2] * e_sun[2]);
         
         if ((e_sun[0] == 0.0) && (e_sun[1] == 0.0))
             phi = 0.0;
         else
             phi = Math.atan2(e_sun[1], e_sun[0]);
         
         if (phi < 0.0)
             phi += 2.0 * Math.PI;
         
         double rho = Math.sqrt(rhoSqr);
         if ((e_sun[2] == 0.0) && (rho == 0.0))
             theta = 0.0;
         else
             theta = Math.atan2(e_sun[2], rho);
         
        this.dAscension = phi*180/Math.PI;
        this.dDeclination = theta*180/Math.PI; 
        
        this.calculateAll(this.dDeclination, this.dAscension);
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
	
	static public boolean isLeapYear(int _year)
	{
		if(_year%4 == 0)
			if(_year%100 == 0)
				if(_year%400 == 0)
					return true;
				else
					return false;
			else
				return true;
		else
			return false;
	}
	
	/**
	 * calculateHourGMT
	 * Gives the hour from HMT
	 * @param _cal : Use a calendar for calculate the GTM hour
	 * @return : Return an int that's the hour GMT
	 */
	static public int calculateHourGMT(Calendar _cal)
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
	static public double calculateX(double _dHeight,double _dAzimuth)
	{
		double l_x = 1*((-2.0/pi)*_dHeight+1);
		
		return l_x*sin(_dAzimuth);
		//return (cos(_dHeight)*sin(_dAzimuth)/(sin(_dHeight)+1));
	}
	
	/**
	 * calculateY
	 * Calculates the Y value of a coordinate system of 2D. It's a projection.
	 * @param _dHeight : The Height of the star, calculated with "calculate_Height()"
	 * @param _dAzimuth : The Azimuth of the star, calculated with "calculate_Azimuth()";
	 * @return : Return a double that contains the Y coordinate
	 */
	static public double calculateY(double _dHeight,double _dAzimuth)
	{
		double l_y = 1*((-2.0/pi)*_dHeight+1);
		
		return l_y*cos(_dAzimuth);
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
	static public double hms(double _dHour, double _dMinute, double _dSecond)
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
	static public double fractionOfDay(double _dHour, double _dMinute, double _dSecond)
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
	static public double calculate_height(double _dec, double _dLat, double _star_angle)
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
	static public double calculate_azimuth(double _dec, double _dLat, double _dHeight, double _star_angle)
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
	static public double calculate_JulianDate(double _dDay, double _dMonth, double _dYear, double _dHour,double _dMinute, double _dSecond)
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

	static public double calculateAngleCompass(float _accX, float _accY, float _accZ, float _magX, float _magY, float _magZ)
	{		
		// On repasse en signé
		_accX -= 32768.0;
		_accY -= 32768.0;
		_accZ -= 32768.0;
		_magX -= 32768.0;
		_magY -= 32768.0;
		_magZ -= 32768.0;
		
		// On repasse à des valeurs non bornée
		_accX /= 16384.0;
		_accY /= 16384.0;
		_accZ /= 16384.0;
		_magX /= 32768.0;
		_magY /= 32768.0;
		_magZ /= 32768.0;
		
		// On passe en Newton et en uT
		_accX *= 9.81;
		_accY *= 9.81;
		_accZ *= 9.81;
		_magX *= 80.0;
		_magY *= 80.0;
		_magZ *= 80.0;
		
		// Conversion du système du PIC vers le système standard
		_accZ *= -1.0;
		_magX *= -1.0;
		_magY *= -1.0;
		
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("x: " + _accX + "\ny: " + _accY + "\nz: " + _accZ + "\nx: " + _magX + "\ny: " + _magY + "\nz: " + _magZ);
		float R[] = new float[9];
		float I[] = new float[9];
		float acc[] = new float[] {_accX, _accY, _accZ};
		float mag[] = new float[] {_magX, _magY, _magZ};

		if(mag != null && acc != null) {
			boolean success = getRotationMatrix(R, I, acc, mag);
			if (success) {
				float orientation[] = new float[3];
				getOrientation(R, orientation);
				return Math.toDegrees(orientation[0]);
			}
		}
		return 0.0;
	}

	static public double calculateAngleInclinometer(double _dX,double _dY,double _dZ)
	{
		return 0;
	}
	
	/**
     * <p>
     * Computes the inclination matrix <b>I</b> as well as the rotation matrix
     * <b>R</b> transforming a vector from the device coordinate system to the
     * world's coordinate system which is defined as a direct orthonormal basis,
     * where:
     * </p>
     *
     * <ul>
     * <li>X is defined as the vector product <b>Y.Z</b> (It is tangential to
     * the ground at the device's current location and roughly points East).</li>
     * <li>Y is tangential to the ground at the device's current location and
     * points towards the magnetic North Pole.</li>
     * <li>Z points towards the sky and is perpendicular to the ground.</li>
     * </ul>
     *
     * <p>
     * <center><img src="../../../images/axis_globe.png"
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
     * [0 m 0] = <b>I</b> * <b>R</b> * <b>geomagnetic</b> (m = magnitude of
     * geomagnetic field)
     * <p>
     * <b>R</b> is the identity matrix when the device is aligned with the
     * world's coordinate system, that is, when the device's X axis points
     * toward East, the Y axis points to the North Pole and the device is facing
     * the sky.
     *
     * <p>
     * <b>I</b> is a rotation matrix transforming the geomagnetic vector into
     * the same coordinate space as gravity (the world's coordinate space).
     * <b>I</b> is a simple rotation around the X axis. The inclination angle in
     * radians can be computed with {@link #getInclination}.
     * <hr>
     *
     * <p>
     * Each matrix is returned either as a 3x3 or 4x4 row-major matrix depending
     * on the length of the passed array:
     * <p>
     * <u>If the array length is 16:</u>
     *
     * <pre>
     *   /  M[ 0]   M[ 1]   M[ 2]   M[ 3]  \
     *   |  M[ 4]   M[ 5]   M[ 6]   M[ 7]  |
     *   |  M[ 8]   M[ 9]   M[10]   M[11]  |
     *   \  M[12]   M[13]   M[14]   M[15]  /
     *</pre>
     *
     * This matrix is ready to be used by OpenGL ES's
     * {@link javax.microedition.khronos.opengles.GL10#glLoadMatrixf(float[], int)
     * glLoadMatrixf(float[], int)}.
     * <p>
     * Note that because OpenGL matrices are column-major matrices you must
     * transpose the matrix before using it. However, since the matrix is a
     * rotation matrix, its transpose is also its inverse, conveniently, it is
     * often the inverse of the rotation that is needed for rendering; it can
     * therefore be used with OpenGL ES directly.
     * <p>
     * Also note that the returned matrices always have this form:
     *
     * <pre>
     *   /  M[ 0]   M[ 1]   M[ 2]   0  \
     *   |  M[ 4]   M[ 5]   M[ 6]   0  |
     *   |  M[ 8]   M[ 9]   M[10]   0  |
     *   \      0       0       0   1  /
     *</pre>
     *
     * <p>
     * <u>If the array length is 9:</u>
     *
     * <pre>
     *   /  M[ 0]   M[ 1]   M[ 2]  \
     *   |  M[ 3]   M[ 4]   M[ 5]  |
     *   \  M[ 6]   M[ 7]   M[ 8]  /
     *</pre>
     *
     * <hr>
     * <p>
     * The inverse of each matrix can be computed easily by taking its
     * transpose.
     *
     * <p>
     * The matrices returned by this function are meaningful only when the
     * device is not free-falling and it is not close to the magnetic north. If
     * the device is accelerating, or placed into a strong magnetic field, the
     * returned matrices may be inaccurate.
     *
     * @param R
     *        is an array of 9 floats holding the rotation matrix <b>R</b> when
     *        this function returns. R can be null.
     *        <p>
     *
     * @param I
     *        is an array of 9 floats holding the rotation matrix <b>I</b> when
     *        this function returns. I can be null.
     *        <p>
     *
     * @param gravity
     *        is an array of 3 floats containing the gravity vector expressed in
     *        the device's coordinate. You can simply use the
     *        {@link android.hardware.SensorEvent#values values} returned by a
     *        {@link android.hardware.SensorEvent SensorEvent} of a
     *        {@link android.hardware.Sensor Sensor} of type
     *        {@link android.hardware.Sensor#TYPE_ACCELEROMETER
     *        TYPE_ACCELEROMETER}.
     *        <p>
     *
     * @param geomagnetic
     *        is an array of 3 floats containing the geomagnetic vector
     *        expressed in the device's coordinate. You can simply use the
     *        {@link android.hardware.SensorEvent#values values} returned by a
     *        {@link android.hardware.SensorEvent SensorEvent} of a
     *        {@link android.hardware.Sensor Sensor} of type
     *        {@link android.hardware.Sensor#TYPE_MAGNETIC_FIELD
     *        TYPE_MAGNETIC_FIELD}.
     *
     * @return <code>true</code> on success, <code>false</code> on failure (for
     *         instance, if the device is in free fall). On failure the output
     *         matrices are not modified.
     *
     * @see #getInclination(float[])
     * @see #getOrientation(float[], float[])
     * @see #remapCoordinateSystem(float[], int, int, float[])
     */
	public static boolean getRotationMatrix(float[] R, float[] I,
            float[] gravity, float[] geomagnetic) {
        // TODO: move this to native code for efficiency
        float Ax = gravity[0];
        float Ay = gravity[1];
        float Az = gravity[2];
        final float Ex = geomagnetic[0];
        final float Ey = geomagnetic[1];
        final float Ez = geomagnetic[2];
        float Hx = Ey*Az - Ez*Ay;
        float Hy = Ez*Ax - Ex*Az;
        float Hz = Ex*Ay - Ey*Ax;
        final float normH = (float)Math.sqrt(Hx*Hx + Hy*Hy + Hz*Hz);
        if (normH < 0.1f) {
            // device is close to free fall (or in space?), or close to
            // magnetic north pole. Typical values are  > 100.
            return false;
        }
        final float invH = 1.0f / normH;
        Hx *= invH;
        Hy *= invH;
        Hz *= invH;
        final float invA = 1.0f / (float)Math.sqrt(Ax*Ax + Ay*Ay + Az*Az);
        Ax *= invA;
        Ay *= invA;
        Az *= invA;
        final float Mx = Ay*Hz - Az*Hy;
        final float My = Az*Hx - Ax*Hz;
        final float Mz = Ax*Hy - Ay*Hx;
        if (R != null) {
            if (R.length == 9) {
                R[0] = Hx;     R[1] = Hy;     R[2] = Hz;
                R[3] = Mx;     R[4] = My;     R[5] = Mz;
                R[6] = Ax;     R[7] = Ay;     R[8] = Az;
            } else if (R.length == 16) {
                R[0]  = Hx;    R[1]  = Hy;    R[2]  = Hz;   R[3]  = 0;
                R[4]  = Mx;    R[5]  = My;    R[6]  = Mz;   R[7]  = 0;
                R[8]  = Ax;    R[9]  = Ay;    R[10] = Az;   R[11] = 0;
                R[12] = 0;     R[13] = 0;     R[14] = 0;    R[15] = 1;
            }
        }
        if (I != null) {
            // compute the inclination matrix by projecting the geomagnetic
            // vector onto the Z (gravity) and X (horizontal component
            // of geomagnetic vector) axes.
            final float invE = 1.0f / (float)Math.sqrt(Ex*Ex + Ey*Ey + Ez*Ez);
            final float c = (Ex*Mx + Ey*My + Ez*Mz) * invE;
            final float s = (Ex*Ax + Ey*Ay + Ez*Az) * invE;
            if (I.length == 9) {
                I[0] = 1;     I[1] = 0;     I[2] = 0;
                I[3] = 0;     I[4] = c;     I[5] = s;
                I[6] = 0;     I[7] =-s;     I[8] = c;
            } else if (I.length == 16) {
                I[0] = 1;     I[1] = 0;     I[2] = 0;
                I[4] = 0;     I[5] = c;     I[6] = s;
                I[8] = 0;     I[9] =-s;     I[10]= c;
                I[3] = I[7] = I[11] = I[12] = I[13] = I[14] = 0;
                I[15] = 1;
            }
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
     * <p>The reference coordinate-system used is different from the world
     * coordinate-system defined for the rotation matrix:</p>
     * <ul>
     * <li>X is defined as the vector product <b>Y.Z</b> (It is tangential to
     * the ground at the device's current location and roughly points West).</li>
     * <li>Y is tangential to the ground at the device's current location and
     * points towards the magnetic North Pole.</li>
     * <li>Z points towards the center of the Earth and is perpendicular to the ground.</li>
     * </ul>
     *
     * <p>
     * <center><img src="../../../images/axis_globe_inverted.png"
     * alt="Inverted world coordinate-system diagram." border="0" /></center>
     * </p>
     * <p>
     * All three angles above are in <b>radians</b> and <b>positive</b> in the
     * <b>counter-clockwise</b> direction.
     * 
     * @param R
     *        rotation matrix see {@link #getRotationMatrix}.
     * 
     * @param values
     *        an array of 3 floats to hold the result.
     * 
     * @return The array values passed as argument.
     * 
     * @see #getRotationMatrix(float[], float[], float[], float[])
     * @see GeomagneticField
     */
    public static float[] getOrientation(float[] R, float values[]) {
        /*
         * 4x4 (length=16) case:
         *   /  R[ 0]   R[ 1]   R[ 2]   0  \
         *   |  R[ 4]   R[ 5]   R[ 6]   0  |
         *   |  R[ 8]   R[ 9]   R[10]   0  |
         *   \      0       0       0   1  /
         *
         * 3x3 (length=9) case:
         *   /  R[ 0]   R[ 1]   R[ 2]  \
         *   |  R[ 3]   R[ 4]   R[ 5]  |
         *   \  R[ 6]   R[ 7]   R[ 8]  /
         *
         */
        if (R.length == 9) {
            values[0] = (float)Math.atan2(R[1], R[4]);
            values[1] = (float)Math.asin(-R[7]);
            values[2] = (float)Math.atan2(-R[6], R[8]);
        } else {
            values[0] = (float)Math.atan2(R[1], R[5]);
            values[1] = (float)Math.asin(-R[9]);
            values[2] = (float)Math.atan2(-R[8], R[10]);
        }
        return values;
    }
	
	static public double picLon2Lon(double _duLon,char _cHemisphere)
	{
		int dd = (int) (_duLon / 100);
		double mm = _duLon - (dd * 100.0);
		
		double ret = (double)dd + mm / 60.0;
		
		if(_cHemisphere == 'W' || _cHemisphere == 'w')
			ret *= -1.0;
		
		return ret;
	}
	
	static public double picLat2Lat(double _duLat,char _cHemisphere)
	{
		int dd = (int) (_duLat / 100);
		double mm = _duLat - (dd * 100.0);
		
		double ret = (double)dd + mm / 60.0;
		
		if(_cHemisphere == 'S' || _cHemisphere == 's')
			ret *= -1.0;
		
		return ret;
	}
	
	/**
	 * calculateSideralHourAngle Calculates the sideral hour angle
	 * 
	 * @param _dSideral_Time : The Sideral time
	 * @return : Return a double that's the result
	 */
	static public double calculateSideralHourAngle(double _dSideral_Time)
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
	static public double calculateHourAngle(double _dHour, double _dMin, double _dGMT)
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
	static public double calculateSideralTime(double _dDay, double _dMonth, double _dYear, double _dHour,double _dMinute, double _dSecond)
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
	static public double cos(double _dX)
	{
		return Math.cos(_dX);
	}
	
	/**
	 * sin
	 * Calculate the sinus of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static public double sin(double _dX)
	{
		return Math.sin(_dX);
	}
	
	/**
	 * arccos
	 * Calculate the ArcCosinus of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static public double arccos(double _dX)
	{
		return Math.acos(_dX);
	}
	
	/**
	 * arcsin
	 * Calculate the ArcSinus of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static public double arcsin(double _dX)
	{
		return Math.asin(_dX);
	}
	
	/**
	 * tan
	 * Calculate the tan of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static public double tan(double _dX)
	{
		return Math.tan(_dX);
	}

	/**
	 * arctan
	 * Calculate the ArcTan of a number
	 * @param _dX : The value to transform
	 * @return : Return the result of the operation
	 */
	static public double arctan(double _dX)
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