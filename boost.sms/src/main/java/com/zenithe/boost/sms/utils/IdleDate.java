/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class IdleDate
extends Date {
    private static final long serialVersionUID = -1875981288045712454L;

    public static IdleDate parseString(String chaine, String format) {
        SimpleDateFormat typeFormat = new SimpleDateFormat(format);
        Date maDateFinale = new Date();
        try {
            maDateFinale = typeFormat.parse(chaine);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("date final" + maDateFinale);
        IdleDate date = new IdleDate();
        date.setTime(maDateFinale.getTime());
        System.out.println("date convertir" + date);
        return date;
    }

    public String toString(String format) {
        if ("".equalsIgnoreCase(format)) {
            format = "dd/MM/yyyy kk:mm:ss";
        }
        SimpleDateFormat formatDateJour = new SimpleDateFormat(format);
        String dateFormat = formatDateJour.format(this);
        return dateFormat;
    }

    public static String toString(Date date, String format) {
        if ("".equalsIgnoreCase(format)) {
            format = "dd/MM/yyyy kk:mm:ss";
        }
        SimpleDateFormat formatDateJour = new SimpleDateFormat(format);
        String dateFormat = formatDateJour.format(date);
        return dateFormat;
    }

    public HashMap<String, Long> difference(Date date) {
        HashMap<String, Long> result = new HashMap<String, Long>();
        long differenceTime = 0L;
        differenceTime = this.isOlder(date) ? date.getTime() - this.getTime() : this.getTime() - date.getTime();
        result.put("MILLISECOND", differenceTime);
        result.put("SECOND", differenceTime / 1000L);
        result.put("MINUTE", differenceTime / 1000L / 60L);
        result.put("HOUR", differenceTime / 1000L / 60L / 60L);
        result.put("DAY", differenceTime / 1000L / 60L / 60L / 24L);
        result.put("MONTH", differenceTime / 1000L / 60L / 60L / 24L / 30L);
        result.put("YEAR", differenceTime / 1000L / 60L / 60L / 24L / 30L / 12L);
        return result;
    }

    public boolean isOlder(Date date) {
        boolean result = false;
        if (date.getTime() > this.getTime()) {
            result = true;
        }
        return result;
    }

    public static boolean isOlder(Date date1, Date date2) {
        boolean result = false;
        if (date2.getTime() < date1.getTime()) {
            result = true;
        }
        return result;
    }

    public void addMillisecond(int millisecond) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(14, millisecond);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeMillisecond(int millisecond) {
        this.addMillisecond(-millisecond);
    }

    public void addSecond(int second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(13, second);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeSecond(int second) {
        this.addSecond(-second);
    }

    public void addMinute(int minute) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(12, minute);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeMinute(int minute) {
        this.addMinute(-minute);
    }

    public void addHour(int hour) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(10, hour);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeHour(int hour) {
        this.addHour(-hour);
    }

    public void addDay(int days) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(5, days);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeDay(int days) {
        this.addDay(-days);
    }

    public void addMonth(int month) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(2, month);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeMonth(int month) {
        this.addMonth(-month);
    }

    public void addYear(int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        calendar.add(1, year);
        this.setTime(calendar.getTime().getTime());
    }

    public void removeYear(int year) {
        this.addYear(-year);
    }

    public int getMillisecond() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(14);
    }

    public int getSecond() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(13);
    }

    public int getMinute() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(12);
    }

    public int getHour() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(10);
    }

    public int getDayMonth() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(5);
    }

    @Override
    public int getMonth() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(2) + 1;
    }

    @Override
    public int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this);
        return calendar.get(1);
    }

    public static int getMillisecond(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(14);
    }

    public static int getSecond(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static int getMinute(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getHour(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(10);
    }

    public static int getDayMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int getYear(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(1);
    }

    public long getTimestamp() {
        return this.getTime();
    }

    public static int getLastDayInMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int maxDay = calendar.getActualMaximum(5);
        return maxDay;
    }
}
