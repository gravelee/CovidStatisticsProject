package com.gravelee.covidstatistics.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gravelee.covidstatistics.domain.Record;

/**
 * 	This is the general utility class. Here we implemented the way 
 * 	to read the data set (locally or remote). We can also update our
 * 	local file. Also we transform the data to not accumulative per
 * 	date.  
 * 
 * 	@author Grproth
 */
public class GeneralUtility {

	private static final String URI= 
		"https://raw.githubusercontent.com/datasets/"
				+ "covid-19/master/data/countries-aggregated.csv";
	
	private static final String FILE_NAME = "countries-aggregated.csv";
	
	/**
	 * 	This is the method that reads the data online or locally 
	 * 	and then returns the appropriate list or our records.
	 * 	The country variable can have an exceptional case where 
	 * 	in some records two words with commas will be added to
	 *  the country name like "Korea, south,".
	 * 
	 * 	@param 	isRemote				( if the data will be red remotely or not)
	 * 	@param 	cumulativeData			( if the data passed are cumulative)
	 * 	@return	List<Record>			( the record list we need)
	 * 	@throws MalformedURLException	( when the url we gave does not exist or is corrupt)
	 * 	@throws IOException				( when something goes wrong with the io process)
	 */
	public static List<Record> readData( 
			boolean isRemote, boolean cumulativeData) 
			throws MalformedURLException, IOException{
		
		InputStream in;
		
		if(isRemote)
			in =  new URL(URI).openStream();
		else
			in =  new FileInputStream( new File(FILE_NAME));
		
		List<Record> list = new ArrayList<>();
		    
		    try ( InputStreamReader inr = new InputStreamReader(in);
		    		BufferedReader br = new BufferedReader(inr);) {
		        
		    	String line;
		    	String [] strArray;
		    	
		    	br.readLine();	// ignores the header
		
		while ( ( line = br.readLine()) != null) {
		    
				strArray = line.split(",");
		    	
		    	if( strArray.length < 6)	// specific way to read "Korea, South," as one.
		    		
		        	list.add( new Record( 
		        			LocalDate.parse( strArray[0]),
		        			strArray[1],
		        			Long.parseLong(strArray[2]),
		        			Long.parseLong(strArray[3]),
		        			Long.parseLong(strArray[4])));
		    	else
		    		
		    		list.add( new Record( 
	            			LocalDate.parse( strArray[0]),
	            			strArray[2] + strArray[1],
	            			Long.parseLong(strArray[3]),
	            			Long.parseLong(strArray[4]),
	            			Long.parseLong(strArray[5])));
		    }
		
			inr.close();
			br.close();
		}
		
		in.close();
		
		//System.out.println(list);
		
		if(!cumulativeData)
			list = morphDataToNoneCumulative(list);
		
		return list;
	}
	
	/**
	 * 	This method is called to downloads/updates the data set locally.
	 * 
	 * 	@throws MalformedURLException	( when the url we gave does not exist or is corrupt)
	 * 	@throws FileNotFoundException	( only when there is a same name file that is a director)
	 * 	@throws IOException				( when something goes wrong with the io process)
	 */
	public static void updateLocalData() 
			throws MalformedURLException, FileNotFoundException, IOException {
		
		FileOutputStream fos = new FileOutputStream(FILE_NAME);
		
		fos.getChannel().transferFrom(
				Channels.newChannel( new URL(URI).openStream())
				,0 ,Long.MAX_VALUE);
	
		fos.close();
	}
	
	/**
	 * 	This method change the cumulative data to none cumulative.
	 * 
	 * 	@param 	list			( the list to be converted)
	 * 	@return	List<Record>	( the converted list after the process)
	 * 	@throws IOException		( when something goes wrong with the io process)
	 */
	private static List<Record> morphDataToNoneCumulative(
			List<Record> list) throws IOException {
		
		List<Record> morphedList = new ArrayList<>();
		
		List<String> countryNames = list.stream()
			.map(Record::getCountryName)
			.collect( Collectors.toList());
		
		for( String country: countryNames) {
			
			List<Record> countryRecords = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.collect(Collectors.toList());
			
			for( int i=1; i<countryRecords.size(); i++) {
				
				countryRecords.get(i).setConfirmed(
						countryRecords.get(i).getConfirmed()-
						countryRecords.get(i-1).getConfirmed());
				
				countryRecords.get(i).setRecovered(
						countryRecords.get(i).getRecovered()-
						countryRecords.get(i-1).getRecovered());
				
				countryRecords.get(i).setDeaths(
						countryRecords.get(i).getDeaths()-
						countryRecords.get(i-1).getDeaths());
			}
			
			for( Record record: countryRecords)
				morphedList.add(record);
		}
		
		//System.out.println(morphedList.toString());
		
		return morphedList;
	}
}
