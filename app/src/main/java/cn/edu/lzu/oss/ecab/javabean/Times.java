package cn.edu.lzu.oss.ecab.javabean;

import java.util.Locale;

public class Times {
    private int timeYear;
    private int timeMonth;
    private int timeDay;
    private int timeHour;
    private int timeMinute;
    private int timeSecond;

    public Times(int timeYear, int timeMonth, int timeDay, int timeHour, int timeMinute, int timeSecond) {
        this.timeYear = timeYear;
        this.timeMonth = timeMonth;
        this.timeDay = timeDay;
        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
        this.timeSecond = timeSecond;
    }

    public int getTimeYear() {
        return timeYear;
    }

    public void setTimeYear(int timeYear) {
        this.timeYear = timeYear;
    }

    public int getTimeMonth() {
        return timeMonth;
    }

    public void setTimeMonth(int timeMonth) {
        this.timeMonth = timeMonth;
    }

    public int getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(int timeDay) {
        this.timeDay = timeDay;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public int getTimeSecond() {
        return timeSecond;
    }

    public void setTimeSecond(int timeSecond) {
        this.timeSecond = timeSecond;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA,"%d-%d-%d %d:%d:%d",timeYear,timeMonth,timeDay,timeHour,timeMinute,timeSecond);
    }
}
