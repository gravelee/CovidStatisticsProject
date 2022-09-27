package com.gravelee.covidstatistics;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.gravelee.covidstatistics.domain.Record;
import com.gravelee.covidstatistics.service.ReportService;
import com.gravelee.covidstatistics.service.impl.ReportServiceImpl;
import com.gravelee.covidstatistics.utility.GeneralUtility;

/**
 * 	The driver class of our application and here we also 
 *  test our data set. The project in general reads
 * 	specific covid cumulative data from a url or a file
 *  locally interprets those data based on the services
 *  implemented and here we print those query answers to
 *  the terminal.j This project can be adapted for any
 *  data set but you need to implement the model class
 *  of your data (like Record.java). The application can
 *  interpret cumulative and none cumulative data sets.
 *  
 *  If the application becomes bigger we will move the
 *  code of main into a new class like TestQeuries and
 *  from here we will call the method that will 
 *  implement that.
 * 
 * 	@author Grproth
 */
public class CovidStatisticsApp {
    
	public static void main( String[] args ) 
			throws IOException, ClassNotFoundException{
        
		
    	List<Record> list = GeneralUtility.readData(false,true);	
    	// first for remote or not, second for cumulative data or not
    	
    	ReportService service = new ReportServiceImpl(list);
    	
    	//System.out.println(list.toString());	// prints the whole data set
    	
    	
    	//*
    	
    	String country = 
    		service.countryWithMostConfirmedCases(true);
    	
    	System.out.println(
            	"The country with the most confirmed cases of Covid 19\n\n" 
            		+ country + "\n\n");
            	
    	
    	long mostRecordNumber = 
    		service.numberOfRecordsForCountry("Greece");
    	
    	System.out.println(
        	"Number of records for country \"" + "Greece" + "\"\n\n" 
        		+ mostRecordNumber + "\n\n");
    	
    	//*/
    	
    	
    	//*
    	
    	Map<String,Long> deathsPerCountry = 
    		service.maxNumberOfDeathsPerCountry(true);
    	
    	System.out.println("Max number of deaths per country\n");
    	
    	deathsPerCountry.entrySet().stream()
    		.sorted(Map.Entry.comparingByValue())
    		.forEach(System.out::println);
    	System.out.println("\n");
    	
    	
    	BigDecimal avgNumberOfDeaths = 
    		service.avgNumberOfDeathsWorldWide(deathsPerCountry);
    	
    	System.out.println(
        	"Average number of deaths world wide\n\n" 
        			+ avgNumberOfDeaths + "\n\n");
    	
    	//*/
    	
    	
    	//*
    	
    	List<Record> maxesList =
    		service.maxNumberOfAllStatsPerCountry(true);
    	
    	System.out.println("Max number of all three stuts per country\n");
    	
    	maxesList.stream()
			.sorted(Comparator.comparing(
					Record::getCountryName))
			.forEach(System.out::println);
		System.out.println("\n");
		
		//*/
    }
}