package com.crewcloud.apps.crewboard.util;


import com.crewcloud.apps.crewboard.activity.logintest.Statics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by david on 12/23/15.
 */
public class TimeUtils {
    public static final String TAG = "TimeUtils";
    public static final Calendar FIRST_DAY_OF_TIME;
    public static final Calendar LAST_DAY_OF_TIME;
    public static final int DAYS_OF_TIME;
    public static final int MONTHS_OF_TIME;
    public static final int YEARS_OF_TIME;
    public static final int WEEKS_OF_TIME;

    static {
        FIRST_DAY_OF_TIME = Calendar.getInstance();
        FIRST_DAY_OF_TIME.set(Calendar.getInstance().get(Calendar.YEAR) - 100, Calendar.JANUARY, 1);
        LAST_DAY_OF_TIME = Calendar.getInstance();
        LAST_DAY_OF_TIME.set(Calendar.getInstance().get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31);
        DAYS_OF_TIME = (int) ((LAST_DAY_OF_TIME.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis()) / (24 * 60 * 60 * 1000));
        MONTHS_OF_TIME = (LAST_DAY_OF_TIME.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR)) * 12 + LAST_DAY_OF_TIME.get(Calendar.MONTH) - FIRST_DAY_OF_TIME.get(Calendar.MONTH);
        YEARS_OF_TIME = (LAST_DAY_OF_TIME.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR));
        WEEKS_OF_TIME = (int) ((LAST_DAY_OF_TIME.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis()) / (7 * 24 * 60 * 60 * 1000));
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 000);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Calendar getYearForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.YEAR, position);
        return cal;
    }

    public static boolean isBetweenTime(String strFromTime, String strToTime) {
        Calendar calendar = Calendar.getInstance();
        Calendar calFromTime = getTimeFromStr(strFromTime);
        Calendar calToTime = getTimeFromStr(strToTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int hourFromTime = calFromTime.get(Calendar.HOUR_OF_DAY);
        int minuteFromTime = calFromTime.get(Calendar.MINUTE);
        int hourToTime = calToTime.get(Calendar.HOUR_OF_DAY);
        int minuteToTime = calToTime.get(Calendar.MINUTE);
        if (hourFromTime == hour || hour == hourToTime) {
            if (hourFromTime == hour && hour == hourToTime) {
                if (minuteFromTime <= minute && minute <= minuteToTime) {
                    return true;
                } else {
                    return false;
                }
            } else if (hourFromTime == hour && hour != hourToTime) {
                if (minuteFromTime <= minute) {
                    return true;
                } else {
                    return false;
                }
            } else if (hourFromTime != hour && hour == hourToTime) {
                if (minute <= minuteToTime) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (hourFromTime < hour && hour < hourToTime) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static Calendar getTimeFromStr(String strTime) {
        String hours = strTime.substring(3, 5);
        String minutes = strTime.substring(6, 8);
        int intHours = Integer.parseInt(hours);
        int intMinutes = Integer.parseInt(minutes);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, intHours);
        calendar.set(Calendar.MINUTE, intMinutes);
        return calendar;
    }

    public static int getPositionForWeek(Calendar week) {
        if (week != null) {
            return (int) ((week.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / 86400000 / 7  //(24 * 60 * 60 * 1000)
            );
        }
        return 0;
    }

    public static int getPositionForDay(Calendar day) {
        if (day != null) {
            return (int) ((day.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / 86400000  //(24 * 60 * 60 * 1000)
            );
        }
        return 0;
    }

    public static int getPositionForMonth(Calendar month) {
        if (month != null) {
            int diffYear = month.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR);
            return diffYear * 12 + month.get(Calendar.MONTH) - FIRST_DAY_OF_TIME.get(Calendar.MONTH);
        }
        return 0;
    }

    public static int getPositionForYear(Calendar year) throws IllegalArgumentException {
        if (year != null) {
            return (year.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR));
        }
        return 0;
    }

    public static Calendar getWeekForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.WEEK_OF_YEAR, position);
        return cal;
    }

    public static Calendar getMonthForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.YEAR, position / 12);
        cal.add(Calendar.MONTH, position % 12);
        return cal;
    }

    public static Calendar getMonthForPosition2(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.YEAR, position / 12);
        cal.add(Calendar.MONTH, position % 12);
        return cal;
    }

    public static Calendar getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, position);
        return cal;
    }


    public static String showTime(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static String showTimeWithoutTimeZone(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date(date));
    }

    public static Calendar getCurrentWeekOfYear(Calendar cal) {
        cal.get(Calendar.WEEK_OF_YEAR);
        return cal;
    }

    public static Calendar getCurrentDayOfMonth(Calendar cal) {
        cal.get(Calendar.DAY_OF_MONTH);
        return cal;
    }

    public static int getCurrentWeek(long timeInMilliSecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMilliSecond);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    //    public static Calendar getFirstDayOfWeek(Calendar cal) {
//        cal.set(Calendar.WEEK_OF_YEAR, 1);
//        return cal;
//    }
    public static Calendar getFirstDayOfWeek(Calendar cal) {
        int number_week = cal.get(Calendar.WEEK_OF_YEAR);
        cal.set(Calendar.WEEK_OF_YEAR, number_week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));

        Date date = cal.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        String ngay = sf.format(date);
//        Log.e(TAG,""+ngay);
        cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(ngay));
        return cal;
    }

    public static Calendar getEndDayOfWeek(Calendar cal) {
        cal.set(Calendar.WEEK_OF_YEAR, cal.getActualMaximum(Calendar.WEEK_OF_YEAR));
        return cal;
    }

    public static Calendar getFirstDayOfMonth(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal;
    }

    public static Calendar getEndDayOfMonth(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal;
    }

    public static Calendar getFirstMonOfYear(Calendar cal) {
        cal.set(Calendar.MONTH, 0);
        return cal;
    }

    public static int getDayOffset(Calendar cal) {
        int i = 0;
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return 7;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
        }
        return i;
    }


    public static int getTimezoneOffsetInMinutes() {
        TimeZone tz = TimeZone.getDefault();
        int offsetMinutes = tz.getRawOffset() / 60000;
        return offsetMinutes;
    }

    public static boolean isSameDate(long day1, long day2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(day1);
        cal2.setTimeInMillis(day2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isMonday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return true;
        }
        return false;
    }

    public static boolean isTuesday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            return true;
        }
        return false;
    }

    public static boolean isWednesday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            return true;
        }
        return false;
    }

    public static boolean isThursday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return true;
        }
        return false;
    }

    public static boolean isFriday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            return true;
        }
        return false;
    }

    public static boolean isSaturday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    public static boolean isSunday(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }


    public static String displayTimeWithoutOffset(String timeString, boolean isToday) {
        SimpleDateFormat formatter;
        try {
            long time;

            if (timeString.contains("(")) {
                timeString = timeString.replace("/Date(", "");
                int plusIndex = timeString.indexOf("+");

                if (plusIndex != -1) {
                    time = Long.valueOf(timeString.substring(0, plusIndex));
                } else {
                    time = Long.valueOf(timeString.substring(0, timeString.indexOf(")")));
                }
            } else {
                time = Long.valueOf(timeString);
            }

            if (isToday) {
                formatter = new SimpleDateFormat(Statics.DATE_FORMAT_HH_MM_AA, Locale.getDefault());
//                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            } else {
                formatter = new SimpleDateFormat(Statics.DATE_FORMAT_YYYY_MM_DD, Locale.getDefault());
//                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            }


            return formatter.format(new Date(time));
        } catch (Exception e) {
            return "";
        }
    }

    public static String displayTimeWithoutOffset(String birthDate) {
        String result = "";
        try {
            String timeString;
            timeString = birthDate.substring(birthDate.indexOf('(') + 1, birthDate.indexOf('+'));
            Date date = new Date(Long.parseLong(timeString));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            result = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String displayTimeWithoutOffsetV2(String birthDate) {
        String result = "";
        try {
            String timeString;
            timeString = birthDate.substring(birthDate.indexOf('(') + 1, birthDate.indexOf('+'));
            Date date = new Date(Long.parseLong(timeString));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String formatYear(String birthDate) {
        String result = "";
        try {
            String timeString;
            timeString = birthDate.substring(birthDate.indexOf('(') + 1, birthDate.indexOf('+'));
            Date date = new Date(Long.parseLong(timeString));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            result = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getTime(String timeString) {
        SimpleDateFormat formatter;
        try {
            long time;

            if (timeString.contains("(")) {
                timeString = timeString.replace("/Date(", "");
                int plusIndex = timeString.indexOf("+");

                if (plusIndex != -1) {
                    time = Long.valueOf(timeString.substring(0, plusIndex));
                } else {
                    time = Long.valueOf(timeString.substring(0, timeString.indexOf(")")));
                }
            } else {
                time = Long.valueOf(timeString);
            }

                formatter = new SimpleDateFormat(Statics.DATE_FORMAT_YYYY_MM_DD, Locale.getDefault());
//                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            return formatter.format(new Date(time));
        } catch (Exception e) {
            return "";
        }
    }


    public static long getTimeFromString(String timeString) {
        long time;
        try {
            String tempTimeString = timeString;
            tempTimeString = tempTimeString.replace("/Date(", "");
            tempTimeString = tempTimeString.replace(")/", "");
            time = Long.valueOf(tempTimeString);
        } catch (Exception e) {
            return 0;
        }
        return time;
    }

    public static int getNumberWeekOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);

        int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        return (ordinalDay - weekDay + 10) / 7;
    }

    //-2: today
    //-3: Yesterday
    //-4: this month
    //-5: last Month
    //-1: default
    public static long getTimeForMail(long time) {
        int date = -1;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        if (cal.get(Calendar.YEAR) == getYearNote(time)) {
            if (cal.get(Calendar.MONTH) == getMonthNote(time)) {
                int temp = cal.get(Calendar.DAY_OF_MONTH) - getDateNote(time);
                if (cal.get(Calendar.DAY_OF_MONTH) == getDateNote(time)) {
                    date = -2;
                } else if (temp == 1) {
                    date = -3;
                } else {
                    date = -4;
                }
            } else if (cal.get(Calendar.MONTH) - 1 == getMonthNote(time)) {
                date = -5;
            }
        } else if (cal.get(Calendar.YEAR) == getYearNote(time) + 1) {
            if (cal.get(Calendar.MONTH) == 0 && getMonthNote(time) == 11) {
                date = -5;
            }
        }
        return date;
    }


    public static int getYearNote(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonthNote(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDateNote(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
