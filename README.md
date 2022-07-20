
# Coding Task

This project is for coding task from SCB. It service two APIs with spring boot at localhost at GET localhost/ and POST localhost/api/v1/enrich respectively.

## Getting Start

### Quick Start
cd to the root directory of the project

`cd CodingTask`

and then issue command:

`mvn spring-boot:run`

The command will run the service on localhost at port 80 and it is ready to accept request

### How to build the project

cd to the root directory of the project 

`cd CodingTask`

and issue

`mvn package`

This command will build the project into Jar packages

### How to execute the Jar packages built with Java -jar command

cd to root directory of the project 

`cd CodingTask`

and issue

`java -jar target/codingtask-0.0.1-SNAPSHOT.jar`

### How to run test on the project

cd to root directory of the project

`cd CodingTask`

and issue

`mvn test`

### How to use enrich API

cd to root directory of the project

`cd CodingTask`

And use the following bash command

`curl --location --request POST 'http://localhost/api/v1/enrich' --header 'Content-Type: text/csv' --header 'Accept: text/csv' --data-binary '@files/trade.csv'`

Note: `--data '@files/trade.csv'` would not work because it would omits the line break in the file such that it is not possible to tell the price data from the date string at the next line. Therefore we need to use `--data-binary '@file/trade.csv'` to retain the line break for the data.
