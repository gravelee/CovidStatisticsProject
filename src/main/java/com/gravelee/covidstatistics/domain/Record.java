package com.gravelee.covidstatistics.domain;

import java.time.LocalDate;

/**
 * 	This is the class that will hold our data.
 * 	We have this specific format of date, country,
 *  confirmed, recovered, deaths.
 * 
 * 	@author Grproth
 */
public class Record {

	private final LocalDate date;
	private final String countryName;
	private long confirmed;
	private long recovered;
	private long deaths;
	
	public Record( final LocalDate date, 
		final String countryName, final long confirmed, 
		long recovered, long deaths) {
		
		this.date = date;
		this.countryName = countryName;
		this.confirmed = confirmed;
		this.recovered = recovered;
		this.deaths = deaths;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getCountryName() {
		return countryName;
	}

	public long getConfirmed() {
		return confirmed;
	}

	public long getRecovered() {
		return recovered;
	}

	public long getDeaths() {
		return deaths;
	}
	
	public void setConfirmed( long confirmed) {
		
		this.confirmed = confirmed;
	}
	
	public void setRecovered( long recovered) {
		
		this.recovered = recovered;
	}

	public void setDeaths( long deaths) {
	
		this.deaths = deaths;
	}
	
	public String header() {
		
		return "Date, Country, Confirmed, Recovered, Deaths";
	}
	
	@Override
	public String toString() {
		
		return date + ", " + countryName + ", " 
				+ confirmed + ", " + recovered + ", " + deaths;
	}
}
