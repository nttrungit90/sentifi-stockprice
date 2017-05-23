##  Sentifi Stock Price Service ##

Stock Price Service provide REST APIs about stock price of ticker symbols.
Those APIs will be consumed by a REST API Client which may be a Web UI, Mobile App, another Microservice or even a command line utility such as Curl.
Sentifi Stock Price Service is built on the most popular open source framework and technologies such as:
* `Spring Boot `
* `Spring MVC`
* `EhCache (to cache frequently used data)`
* `Springfox Swagger 2 (for REST API documentation)`


### Prerequisites
Your system must have Java 8 and Maven installed.

### Run the Application
Download the project and open it and then go to the folder that contains the pom.xml file.
Open CMD and run the application with the command:
```
mvn spring-boot:run
```
### REST APIs
Request a Close Price for a ticker symbol for a range of dates (start date and end date).
* `localhost:8080/api/v2/GE/closePrice?startDate=2017-04-03&endDate2017-04-06 `
Request the 200 day moving average price for a ticker symbol beginning with a start date.
* `localhost:8080/api/v2/GE/200dma?startDate=2016-05-21 `
Single request for the 200 day moving average price for a up to 1000 ticker symbols beginning with a start date.
* `localhost:8080/api/v2/200dma?startDate=2017-06-01&tickerSysmbols=GE,FE,InvalidTicker `



