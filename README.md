# BettercallPaul Programming Challenge - Solution

## Overview

This repository contains the solution for the BettercallPaul programming challenge.
The solution is implemented in Java and uses Apache Commons CSV for CSV file handling and Log4j2 for logging.

I tried to keep the code clean and robust. It's also possible to easily expand the program to read JSON files or any other format.

**I hope you like it! :)**

## Dependencies

### Apache Commons CSV

Apache Commons CSV is used for reading and writing CSV files in a simple and efficient manner.
Apache is a well-known trusted library for handling CSV files in Java applications, providing a straightforward API for parsing and generating CSV data.

### Log4j2

Log4j2 is used for logging throughout the application. It provides a flexible and powerful logging framework.
Log4j2 is a widely used logging library in the Java ecosystem, known for its performance and configurability.

## Running the Solution

### Run with Maven
```bash
./mvnw compile exec:java -Dexec.mainClass="de.bcxp.challenge.App"
```

### Run tests
```bash
./mvnw test
```
