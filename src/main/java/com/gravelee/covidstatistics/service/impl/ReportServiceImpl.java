package com.gravelee.covidstatistics.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.gravelee.covidstatistics.domain.Record;
import com.gravelee.covidstatistics.service.ReportService;

/**
 * 	This is the implementation of ReportService interface.
 * 
 * 	@author Grproth
 */
public class ReportServiceImpl implements ReportService{

	private List<Record> list;
	
	public ReportServiceImpl( List<Record> list){
		
		this.list = list;
	}
	
	/**
	 * 	This returns the country name with the most confirmed
	 * 	cases. If our data are cumulative it is easier to spot
	 * 	the record in question. If not we need to do an extra
	 * 	calculation creating a map. We use the data structure 
	 *  to stream out the record at last.
	 */
	public String countryWithMostConfirmedCases( 
			final boolean cumulativeData) {
		
		// This works only for cumulative data.
		
		if(cumulativeData) {
			
			return list.stream()
				.max( Comparator.comparing(
						Record::getConfirmed))
				.orElseThrow(NoSuchElementException::new)
				.getCountryName();
				
		}
		
		// This works only for none cumulative data (it takes time).
		//*
		
		Map<String,Long> maxConfirmedPerCountry = new HashMap<>();
		
		List<String> countryNames = list.stream()
			.map(Record::getCountryName)
			.distinct()
			.collect( Collectors.toList());
		
		for( String country : countryNames) {
		
			long maxConfirmedNumber = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.map(Record::getConfirmed)
				.reduce((n1,n2)->n1+n2)
				.orElseThrow(NoSuchElementException::new);
			
//				after filter if we put this code it works only for cumulative data
//				
//				.max( Comparator.comparing(
//						Record::getConfirmed))
//				.orElseThrow(NoSuchElementException::new)
//				.getConfirmed();
				
			maxConfirmedPerCountry.put( country, maxConfirmedNumber);
		}
		
		return maxConfirmedPerCountry.entrySet().stream()
			.max(Map.Entry.comparingByValue())
			.orElseThrow(NoSuchElementException::new)
			.getKey();
		
		//*/
		
		// This works only for none cumulative data (old way).
		/*
		
		Map<String,Long> maxConfirmedPerCountry = new HashMap<>();
		
		for( Record record : list) {
			
			String countryName = record.getCountryName();
			
			if( maxConfirmedPerCountry.containsKey(countryName)) {
				
				maxConfirmedPerCountry.replace(
					countryName,
					maxConfirmedPerCountry.get(countryName),
					maxConfirmedPerCountry.get(
						countryName)+record.getConfirmed());
			}
			else {
				
				maxConfirmedPerCountry.put( countryName,
						record.getConfirmed());
			}
		}
		
		//System.out.println( confirmedSumPerCountry.toString());	// prints the map
		
		return maxConfirmedPerCountry.entrySet().stream()
			.max(Map.Entry.comparingByValue())
			.get().getKey();
		
		//*/
	}
	
	/**
	 * 	This method counts the number of records we have 
	 * 	in our data set from a specific country. In comments
	 *  I have the way to implement that with the old regular
	 *  way, without streams.
	 */
	public long numberOfRecordsForCountry( final String country) {
		
		// This works for both cases
		//*
			
		return list.stream()
			.filter(r->r.getCountryName().equals(country))
			.count();
		
		//*/
		
		// This works only for none cumulative data (old way).
		/*
		
		Map<String,Long> recordsPerCountry = new HashMap<>();
		
		for( CovidStatistics record : list) {
			
			String countryName = record.getCountryName();
			
			if( recordsPerCountry.containsKey(countryName)) {
				
//				System.out.println("Map entry: (" 
//						+ countryName + ", " 
//						+ recordsPerCountry.get(
//								countryName) + ")\n");

				recordsPerCountry.replace(
					countryName,
					recordsPerCountry.get(countryName),
					recordsPerCountry.get(countryName)+1);
			}
			else {
				
				recordsPerCountry.put( countryName,1L);
			}
		}
		
		//System.out.println( recordsPerCountry.toString());	// prints the map
		
		return recordsPerCountry.get(country);
		
		//*/
	}
	
	/**
	 * 	This method returns a map of country names with
	 * 	the max number of deaths per country. Again
	 *  we need to know if our data are cumulative or
	 *  not cause we need other interpretation for
	 *  those cases.
	 */
	public Map<String, Long> maxNumberOfDeathsPerCountry(
			final boolean cumulativeData){
		
		Map<String,Long> maxDeathsPerCountry = new HashMap<>();
		
		List<String> countryNames = list.stream()
				.map(Record::getCountryName)
				.distinct()
				.collect( Collectors.toList());
		
		// This works only for cumulative data.
		
		if(cumulativeData) {
		
			for( String country : countryNames) {
			
				long maxDeathNumber = list.stream()
					.filter(r->r.getCountryName().equals(country))
					.max( Comparator.comparing(
							Record::getDeaths))
					.orElseThrow(NoSuchElementException::new)
					.getDeaths();
				
				maxDeathsPerCountry.put( country, maxDeathNumber);
			}
			
			return maxDeathsPerCountry;
		}
		
		// This works only for none cumulative data (it takes time).
		//*
		
		for( String country : countryNames) {
		
			long maxDeathNumber = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.map(Record::getDeaths)
				.reduce((n1,n2)->n1+n2)
				.orElseThrow(NoSuchElementException::new);
			
			maxDeathsPerCountry.put( country, maxDeathNumber);
		}
		
		return maxDeathsPerCountry;
		
		//*/
		
		// This works only for none cumulative data (old way).
		/*
		
		Map<String,Long> maxDeathsPerCountry = new HashMap<>();
		
		for( Record record : list) {
			
			String countryName = record.getCountryName();
			
			if( maxDeathsPerCountry.containsKey(countryName)) {
				
				maxDeathsPerCountry.replace(
					countryName,
					maxDeathsPerCountry.get(countryName),
					maxDeathsPerCountry.get(countryName)
						+record.getDeaths());
			}
			else {
				
				maxDeathsPerCountry.put( countryName,
						record.getDeaths());
			}
		}
		
		return maxDeathsPerCountry;
		
		//*/
	}
	
	/**
	 * 	This method calculates the average deaths world wide.
	 */
	public BigDecimal avgNumberOfDeathsWorldWide( 
			final Map<String, Long> theMap) {
		
		long sum = 
			theMap.values().stream()
				.reduce((l1,l2)->l1+l2)
				.orElseThrow(NoSuchElementException::new);
		
		return BigDecimal.valueOf(sum/theMap.size());
	}
	
	/**
	 * 	This method calculates the max of all three statistics.
	 */
	public List<Record> maxNumberOfAllStatsPerCountry(
			final boolean cumulativeData){
		
		List<String> countryNames = list.stream()
			.map(Record::getCountryName)
			.distinct()
			.collect( Collectors.toList());
			
		List<Record> maxesPerCountry = new ArrayList<>();
		
		// This works only for cumulative data.
		
		if(cumulativeData) {
			
			for( String country: countryNames) {
				
				LocalDate date = list.stream()
					.filter(r->r.getCountryName().equals(country))
					.max(Comparator.comparing(Record::getDate))
					.orElseThrow(NoSuchElementException::new)
					.getDate();
				
				Long confirmed = list.stream()
					.filter(r->r.getCountryName().equals(country))
					.max( Comparator.comparing(
						Record::getConfirmed))
					.orElseThrow(NoSuchElementException::new)
					.getConfirmed();
				
				Long recovered = list.stream()
					.filter(r->r.getCountryName().equals(country))
					.max( Comparator.comparing(
						Record::getRecovered))
					.orElseThrow(NoSuchElementException::new)
					.getRecovered();
				
				Long dead = list.stream()
					.filter(r->r.getCountryName().equals(country))
					.max( Comparator.comparing(
						Record::getDeaths))
					.orElseThrow(NoSuchElementException::new)
					.getDeaths();
				
				maxesPerCountry.add( 
					new Record(date,country,confirmed,recovered,dead));
			}
			
			return maxesPerCountry;
		}
		
		// This works only for none cumulative data (it takes time).
		
		for( String country: countryNames) {
			
			LocalDate date = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.max(Comparator.comparing(Record::getDate))
				.orElseThrow(NoSuchElementException::new)
				.getDate();
			
			Long confirmed = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.map(Record::getConfirmed)
				.reduce((n1,n2)->n1+n2)
				.orElseThrow(NoSuchElementException::new);
			
			Long recovered = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.map(Record::getRecovered)
				.reduce((n1,n2)->n1+n2)
				.orElseThrow(NoSuchElementException::new);
			
			Long dead = list.stream()
				.filter(r->r.getCountryName().equals(country))
				.map(Record::getDeaths)
				.reduce((n1,n2)->n1+n2)
				.orElseThrow(NoSuchElementException::new);
				
			maxesPerCountry.add( 
				new Record(date,country,confirmed,recovered,dead));
		}
		
		return maxesPerCountry;
	}
}
