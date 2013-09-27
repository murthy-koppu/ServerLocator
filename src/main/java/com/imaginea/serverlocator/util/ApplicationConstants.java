package com.imaginea.serverlocator.util;

public interface ApplicationConstants {
	 public static final int MILLIS_PER_SECOND = 1000;
	 public static final String WEB_SERVER_HEADER_NAME = "Server";
	 public static final int MYSQL_SERVER_MIN_TIME_OUT_PERIOD = 10 * MILLIS_PER_SECOND;
	 public static final int MYSQL_SERVER_MAX_TIME_OUT_PERIOD = 60 * MILLIS_PER_SECOND;
	 public static final int ORACLE_DB_MIN_TIME_OUT_PERIOD = 20 * MILLIS_PER_SECOND;
	 public static final int ORACLE_DB_MAX_TIME_OUT_PERIOD = 240 * MILLIS_PER_SECOND;
	 public static final int APP_SERVER_MIN_TIME_OUT_PERIOD = 5 * MILLIS_PER_SECOND;
	 public static final int APP_SERVER_MAX_TIME_OUT_PERIOD = 30 * MILLIS_PER_SECOND;
}
