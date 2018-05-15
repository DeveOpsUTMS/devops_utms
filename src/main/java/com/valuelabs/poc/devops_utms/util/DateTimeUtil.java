package com.valuelabs.poc.devops_utms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

public class DateTimeUtil {

	public static Map<String, String> getDateDatesOldBetween(int year, String month) {

		Map<String, String> dateList = new TreeMap<>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		// int year = 2017;
		int monthNumber = getMonthNumber(month);

		Calendar cal = new GregorianCalendar(year, monthNumber, 1);
		System.out.println("Number : " + month);
		try {
			do {
				cal.add(Calendar.DATE, 0);
				String StringFromDate = simpleDateFormat.format(cal.getTime());
				// Date dateFrom = simpleDateFormat.parse(StringFromDate);

				cal.add(Calendar.DATE, 6);
				String stringToDate = simpleDateFormat.format(cal.getTime());
				// Date dateToDate = simpleDateFormat.parse(stringToDate);

				dateList.put(StringFromDate, stringToDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
			} while (cal.get(Calendar.MONTH) == monthNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	public static Map<String, String> getDateDatesBetween(int year, String month) {

		Map<String, String> dateList = new TreeMap<>();
		int lastDay;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		// int year = 2017;
		int monthNumber = getMonthNumber(month);

		Calendar cal = new GregorianCalendar(year, monthNumber, 1);
		System.out.println("Number : " + month);
		lastDay = getLastDayOfMonth(year, monthNumber);
		System.out.println("lastDay : " + lastDay);
		try {
			do {
				cal.add(Calendar.DATE, 0);
				String StringFromDate = simpleDateFormat.format(cal.getTime());
				// Date dateFrom = simpleDateFormat.parse(StringFromDate);

				cal.add(Calendar.DATE, 6);
				String stringToDate = simpleDateFormat.format(cal.getTime());
				// Date dateToDate = simpleDateFormat.parse(stringToDate);

				dateList.put(StringFromDate, stringToDate);

				cal.add(Calendar.DAY_OF_YEAR, 1);
			} while (cal.get(Calendar.DAY_OF_MONTH) == lastDay);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	public static Map<String, String> getDateDates2Weeks(int year, String month) {

		Map<String, String> dateList = new TreeMap<>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		// int year = 2017;
		int monthNumber = getMonthNumber(month);
		int lastDay;
		Calendar cal = new GregorianCalendar(year, monthNumber, 1);
		System.out.println("Number : " + month);
		try {
			do {
				cal.add(Calendar.DATE, 0);
				String StringFromDate = simpleDateFormat.format(cal.getTime());
				// Date dateFrom = simpleDateFormat.parse(StringFromDate);

				cal.add(Calendar.DATE, 6);
				String stringToDate = simpleDateFormat.format(cal.getTime());
				// Date dateToDate = simpleDateFormat.parse(stringToDate);

				dateList.put(StringFromDate, stringToDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);

				lastDay = getLastDayOfMonth(year, monthNumber);
				System.out.println("lastDay : " + lastDay);

			} while (cal.get(Calendar.DAY_OF_MONTH) == monthNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/*
	 * public static void main(String[] args) { Calendar calendar =
	 * Calendar.getInstance(); int lastDate =
	 * calendar.getActualMaximum(Calendar.DATE);
	 * 
	 * calendar.set(Calendar.DATE, lastDate); int lastDay =
	 * calendar.get(Calendar.DAY_OF_WEEK);
	 * 
	 * System.out.println("Last Date: " + calendar.getTime());
	 * 
	 * System.out.println("Last Day : " + lastDay); }
	 */

	public static int getLastDayOfMonth(int year, int month) {
		Calendar calendar = new GregorianCalendar(year, month, Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDay = calendar.get(Calendar.DAY_OF_WEEK);
		return lastDay;
	}

	/*
	 * public static Date getLastDateOfMonth(int year, int month) { Calendar
	 * calendar = new GregorianCalendar(year, month, Calendar.DAY_OF_MONTH);
	 * calendar.set(Calendar.DAY_OF_MONTH,
	 * calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); return
	 * calendar.getTime();
	 * 
	 * Calendar calendar = Calendar.getInstance(); int lastDate =
	 * calendar.getActualMaximum(Calendar.DATE);
	 * 
	 * calendar.set(Calendar.DATE, lastDate); int lastDay =
	 * calendar.get(Calendar.DAY_OF_WEEK);
	 * 
	 * 
	 * }
	 */

	/*
	 * public static Map<String, String> getDateDatesBetween(int year, String
	 * month) {
	 * 
	 * Map<String, String> dateList = new TreeMap<>();
	 * 
	 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	 * 
	 * // int year = 2017; int monthNumber = getMonthNumber(month);
	 * 
	 * Calendar cal = new GregorianCalendar(year, monthNumber, 1);
	 * System.out.println("Number : " + month); try { do {
	 * cal.add(Calendar.DATE, 0); String StringFromDate =
	 * simpleDateFormat.format(cal.getTime()); // Date dateFrom =
	 * simpleDateFormat.parse(StringFromDate);
	 * 
	 * cal.add(Calendar.DATE, 6); String stringToDate =
	 * simpleDateFormat.format(cal.getTime()); // Date dateToDate =
	 * simpleDateFormat.parse(stringToDate);
	 * 
	 * dateList.put(StringFromDate, stringToDate);
	 * 
	 * cal.add(Calendar.DAY_OF_YEAR, 1); } while (cal.get(Calendar.MONTH) ==
	 * monthNumber); } catch (Exception e) { e.printStackTrace(); } return
	 * dateList; }
	 */

	public static int getMonthNumber(String month) {
		String monthValue = month;
		int result = 0;
		if (!monthValue.isEmpty()) {
			switch (monthValue) {
			case "January":
				result = 0;
				break;

			case "February":
				result = 1;
				break;

			case "March":
				result = 2;
				break;

			case "April":
				result = 3;
				break;

			case "May":
				result = 4;
				break;

			case "June":
				result = 5;
				break;

			case "July":
				result = 6;
				break;

			case "August":
				result = 7;
				break;

			case "September":
				result = 8;
				break;

			case "October":
				result = 9;
				break;

			case "November":
				result = 10;
				break;

			case "December":
				result = 11;
				break;
			}
		}
		return result;
	}

	public static String getMonthName(int value) {
		int monthValue = value;
		String result = "";
		if (monthValue != 0) {
			switch (monthValue) {
			case 0:
				result = "January";
				break;

			case 1:
				result = "February";
				break;

			case 2:
				result = "March";
				break;

			case 3:
				result = "April";
				break;

			case 4:
				result = "May";
				break;

			case 5:
				result = "June";
				break;

			case 6:
				result = "July";
				break;

			case 7:
				result = "August";
				break;

			case 8:
				result = "September";
				break;

			case 9:
				result = "October";
				break;

			case 10:
				result = "November";
				break;

			case 11:

				result = "December";
				break;
			}
		}
		return result;
	}

	public static String dateFormate(String dateStr){
		String newString = dateStr.replace("T", " ");
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = null;
		try {
			date = (Date)formatter.parse(newString);
			System.out.println("date "+date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);        

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR) +" "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
		return formatedDate;
	}
	
	public static Date stringToDate(String stringDate){
		//String dateStr = "Thu Jan 25 16:02:28 IST 2018";
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = null;
		try {
			date = (Date)formatter.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToString(Date date){
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String stringDate = df.format(date);
		return stringDate;
	}

}
