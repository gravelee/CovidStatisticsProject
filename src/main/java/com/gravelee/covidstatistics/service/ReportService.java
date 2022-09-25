package com.gravelee.covidstatistics.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.gravelee.covidstatistics.domain.Record;

/**
 * 	This interface defines all the appropriate
 * 	manipulation methods that has been asked
 *  from the the specification document. 
 * 
 * 	@author Grproth
 */
public interface ReportService {

	String countryWithMostConfirmedCases(
		final boolean cumulativeData);
	
	long numberOfRecordsForCountry( 
		final String country);
	
	Map<String, Long> maxNumberOfDeathsPerCountry(
		final boolean cumulativeData);
	
	BigDecimal avgNumberOfDeathsWorldWide( 
		final Map<String, Long> theMap);
	
	//	this is an add to the specification document
	List<Record> maxNumberOfAllStatsPerCountry(
		final boolean cumulativeData);
}
