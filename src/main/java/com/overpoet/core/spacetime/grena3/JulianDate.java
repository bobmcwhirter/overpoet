package com.overpoet.core.spacetime.grena3;


import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Calculate Julian date for a given point in time. This follows the algorithm described in Reda, I.; Andreas, A.
 * (2003): Solar Position Algorithm for Solar Radiation Applications. NREL Report No. TP-560-34302, Revised January
 * 2008.
 *
 * @author Klaus Brunner
 */
public final class JulianDate {
    private final double julianDate;
    private final double deltaT;

    /**
     * Construct a Julian date, assuming deltaT to be 0.
     *
     * @param date date and time
     */
    public JulianDate(final ZonedDateTime date) {
        ZonedDateTime utc = createUtcCalendar(date);
        this.julianDate = calcJulianDate(utc);
        this.deltaT = 0.0;
    }

    /**
     * Construct a Julian date from another.
     */
    public JulianDate(final double fromJulianDate, final double deltaT) {
        this.julianDate = fromJulianDate;
        this.deltaT = deltaT;
    }


    /**
     * Construct a Julian date, observing deltaT.
     *
     * @param date date and time
     * @param deltaT Difference between earth rotation time and terrestrial time (or Universal Time and Terrestrial Time),
     *               in seconds. See
     *               <a href ="http://asa.usno.navy.mil/SecK/DeltaT.html">http://asa.usno.navy.mil/SecK/DeltaT.html</a>.
     *               For the year 2015, a reasonably accurate default would be 68.
     */
    public JulianDate(final ZonedDateTime date, final double deltaT) {
        ZonedDateTime calendar = createUtcCalendar(date);
        this.julianDate = calcJulianDate(calendar);
        this.deltaT = deltaT;
    }

    static ZonedDateTime createUtcCalendar(final ZonedDateTime fromCalendar) {
        return fromCalendar.withZoneSameInstant(ZoneOffset.UTC);
    }

    private double calcJulianDate(ZonedDateTime calendar) {
        int y = (calendar.get(ChronoField.ERA) == 1) ? calendar.get(ChronoField.YEAR) : -calendar
                .get(ChronoField.YEAR);
        int m = calendar.get(ChronoField.MONTH_OF_YEAR);

        if (m < 3) {
            y = y - 1;
            m = m + 12;
        }

        final double d = calendar.get(ChronoField.DAY_OF_MONTH)
                + (calendar.get(ChronoField.HOUR_OF_DAY) + (calendar.get(ChronoField.MINUTE_OF_HOUR) + calendar.get(ChronoField.SECOND_OF_MINUTE) / 60.0) / 60.0)
                / 24.0;
        final double jd = Math.floor(365.25 * (y + 4716.0)) + Math.floor(30.6001 * (m + 1)) + d - 1524.5;
        final double a = Math.floor(y / 100.0);
        final double b = jd > 2299160.0 ? (2.0 - a + Math.floor(a / 4.0)) : 0.0;

        return jd + b;
    }

    public double getJulianDate() {
        return julianDate;
    }

    public double getJulianEphemerisDay() {
        return julianDate + deltaT / 86400.0;
    }

    public double getJulianCentury() {
        return (julianDate - 2451545.0) / 36525.0;
    }

    public double getJulianEphemerisCentury() {
        return (getJulianEphemerisDay() - 2451545.0) / 36525.0;
    }

    public double getJulianEphemerisMillennium() {
        return getJulianEphemerisCentury() / 10.0;
    }

    @Override
    public String toString() {
        return String.format("%.5f", julianDate);
    }
}
