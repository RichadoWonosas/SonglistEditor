package ap.yadora.richadowonosas.fmdslst.util;

import java.util.Calendar;

public class TimestampTransformer {
    public static final int TIME_YEAR = 0;
    public static final int TIME_MONTH = 1;
    public static final int TIME_DATE = 2;
    public static final int TIME_HOUR = 3;
    public static final int TIME_MINUTE = 4;
    public static final int TIME_SECOND = 5;

    private final Calendar calendar;

    public TimestampTransformer(long timestamp) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000);
    }

    public void setTimestamp(long timestamp) {
        calendar.setTimeInMillis(timestamp * 1000);
    }

    public void setDateTime(int[] dateTime) {
        calendar.set(dateTime[TIME_YEAR],
                (dateTime[TIME_MONTH] - 1) % 12,
                dateTime[TIME_DATE] % getDayAmountOfMonth(dateTime[TIME_YEAR], dateTime[TIME_MONTH]),
                dateTime[TIME_HOUR] % 24,
                dateTime[TIME_MINUTE] % 60,
                dateTime[TIME_SECOND] % 60);
    }

    public long getTimestamp() {
        return calendar.getTimeInMillis() / 1000;
    }

    public int[] getTimes() {
        return new int[]{
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        };
    }

    private int getDayAmountOfMonth(int year, int month) {
        switch ((month - 1) % 12 + 1) {
            default:
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return ((year & 3) != 0 || (((year % 100) == 0) && ((year % 400) != 0))) ? 28 : 29;
        }
    }

}
