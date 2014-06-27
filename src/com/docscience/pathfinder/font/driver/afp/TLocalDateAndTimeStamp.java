package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class TLocalDateAndTimeStamp {
	
	public static final int TYPE_CREATION = 0x00;
	public static final int TYPE_RETIRED  = 0x01;
	public static final int TYPE_REVISION = 0x03;

	private static final int YEAR_PART1_19XX = 0x40;
	private static final int YEAR_PART1_20XX = 0xF0;
	private static final int YEAR_PART1_29XX = 0xF9;
		
	private int type;
	private int year;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int hundredthsSecond;

	private static final int NUMBER_ZERO = 0xF0;
	private static final int NUMBER_NINE = 0xF9;
	
	private static final int loadNumber(MODCAByteBuffer buffer, int offset, int length) {
		int r = 0;
		for (int i=0; i<length; ++i) {
			int b = buffer.getByte(i + offset) & 0xff;
			if (b >= NUMBER_ZERO && b <= NUMBER_NINE) {
				r = (r * 10) + (b - NUMBER_ZERO);
			}
			else {
				throw new IllegalArgumentException("invalid number format");
			}
		}
		return r;
	}
	
	private static final void saveNumber(MODCAByteBuffer buffer, int number, int nBytes) {
		for (int i=0; i<nBytes; ++i) {
			int factor = (int) Math.pow(10, nBytes - i - 1);
			int b = ((number / factor) % 10) + NUMBER_ZERO;
			buffer.putByte((byte) b);
		}
	}
	
	public void setTriplet(MODCATriplet triplet) {
		if (triplet.getTID() != MODCATriplet.ID_LOCAL_DATA_AND_TIME_STAMP) {
			throw new IllegalArgumentException("invalid triplet id for local date and time stamp");
		}
		MODCAByteBuffer contents = triplet.getContents();
		type = contents.getUBIN1(0);
		if (type != TYPE_CREATION 
				&& type != TYPE_RETIRED 
				&& type != TYPE_REVISION) {
			throw new IllegalArgumentException("unknown local date and time stamp type");
		}
		int yearPart1 = contents.getUBIN1(1);
		if (yearPart1 == YEAR_PART1_19XX) {
			year = 1900;
		}
		else if (yearPart1 >= YEAR_PART1_20XX && yearPart1 <= YEAR_PART1_29XX) {
			year = 2000 + (yearPart1 - YEAR_PART1_20XX) * 100;
		}
		else {
			throw new IllegalArgumentException("year part 1 out of range");
		}
		
		int yearPart2 = loadNumber(contents, 2, 2);
		if (yearPart2 < 0 || yearPart2 > 99) {
			throw new IllegalArgumentException("year part 2 out of range");
		}
		year += yearPart2;
		
		day = loadNumber(contents, 4, 3);
		if (day < 1 || day > 366) {
			throw new IllegalArgumentException("value of day out of range");
		}
		
		hour = loadNumber(contents, 7, 2);
		if (hour < 0 || hour > 23) {
			throw new IllegalArgumentException("value of hour out of range");
		}
		
		minute = loadNumber(contents, 9, 2);
		if (minute < 0 || minute > 59) {
			throw new IllegalArgumentException("value of minute out of range");
		}
		
		second = loadNumber(contents, 11, 2);
		if (second < 0 || second > 59) {
			throw new IllegalArgumentException("value of second out of range");
		}
		
		hundredthsSecond = loadNumber(contents, 13, 2);
		if (hundredthsSecond < 0 || hundredthsSecond > 99) {
			throw new IllegalArgumentException("value of hundredths second out of range");
		}
	}
	
	public MODCATriplet getTriplet() {
		MODCATriplet triplet = new MODCATriplet();
		triplet.setID(MODCATriplet.ID_LOCAL_DATA_AND_TIME_STAMP);
		MODCAByteBuffer contents = triplet.getContents();
		contents.putUBIN1(type);
		if (year < 2000) {
			contents.putUBIN1(YEAR_PART1_19XX);
		}
		else {
			contents.putUBIN1(YEAR_PART1_20XX + ((year - 2000) / 100));
		}
		saveNumber(contents, year % 100, 2);
		saveNumber(contents, day, 3);
		saveNumber(contents, hour, 2);
		saveNumber(contents, minute, 2);
		saveNumber(contents, second, 2);
		saveNumber(contents, hundredthsSecond, 2);
		return triplet;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type != TYPE_CREATION 
				&& type != TYPE_RETIRED 
				&& type != TYPE_REVISION) {
			throw new IllegalArgumentException("unknown local date and time stamp type");
		}
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		if (year < 1900 || year > 2999) {
			throw new IllegalArgumentException("value of year out of range");
		}
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		if (day < 1 || day > 366) {
			throw new IllegalArgumentException("value of day out of range");
		}
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		if (hour < 0 || hour > 23) {
			throw new IllegalArgumentException("value of hour out of range");
		}
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		if (minute < 0 || minute > 59) {
			throw new IllegalArgumentException("value of minute out of range");
		}
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		if (second < 0 || second > 59) {
			throw new IllegalArgumentException("value of second out of range");
		}
		this.second = second;
	}

	public int getHundredthsSecond() {
		return hundredthsSecond;
	}

	public void setHundredthsSecond(int hundredthsSecond) {
		if (hundredthsSecond < 0 || hundredthsSecond > 99) {
			throw new IllegalArgumentException("value of hundredths second out of range");
		}
		this.hundredthsSecond = hundredthsSecond;
	}
	
}
