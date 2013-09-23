package com.imaginea.serverlocator.retrialclass;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;


public class HitServerUsingUrl {

	
	 public static void doSearch(String queryString) {
		 try {
		 // open a url connection
		 URL url = new URL(queryString);
		 URLConnection connection = url.openConnection();
		 connection.setDoOutput(true);
		 // write the query string to the search engine
		 PrintStream ps = new PrintStream(connection.getOutputStream());
		 ps.println(queryString);
		 ps.close();
		 // read the result
		 DataInputStream input = 
		 new DataInputStream(connection.getInputStream());
		 String inputLine = null;
		 while((inputLine = input.readLine())!= null) {
		 System.out.println(inputLine);
		 }
		 } 
		 catch(Exception e) {
		 e.printStackTrace();
		 }
		 }
	 
	 public static void main(String[] args) {
		// doSearch("jdbc:mysql://localhost:3306");
		 //doSearch("http://localhost:8080");
		 System.out.println(isValidVersion("3."));
	}
	 
	 private static boolean isValidVersion(String version){
			char[] versionCharArr = version.toCharArray();
			for(int i=0; i< versionCharArr.length; i++){
				byte versionByte = (byte) versionCharArr[i];
				if((!(versionByte > 47 && versionByte < 58) && versionByte != 46))
					return false;
			}
			return true;
		}
}
