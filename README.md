# CovidStatisticsProject

This is the Covid Statistics project. 

This application implements some query type services about 
an online source of covid world wide data. The application 
can download the source locally and use it that way. It 
calculates and prints the country with the most confirmed 
cases, it can count the number of records a country has 
within the source file, it can print a list of countries 
with their respective max number of confirmed, recovered 
or death cases and it also calculates the average deaths 
per country.


Now let's talk about the architecture of the project. It 
has five packages the main is com.gravelee.covidstatistics. 
Here we have the driver class CovidStatisticsApp.java. In 
that class we start the program and do all of our test cases. 
Then we have the .domain package. There we model our problem 
with classes. One class is needed there the Record.java class. 
This is a container class for our data when we read them from 
the url or local file. Then we have the .service package where 
we have the interface to be implemented. We define all the 
methods with our program functionaliry to be implemented into 
our ReportService.java file. Within the .service.imple we have 
ReportServiceImpl.java class. This is implementing the interface. 
We implement in many ways the results cause I wanted to test the 
old regular ways or writing java code with the new way streams 
offer. Lastly but not least we have the .utility package there 
we have the GeneralUtility.java class. In that class we implement 
all the download/update or just read our source file.
