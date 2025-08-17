package de.bcxp.challenge.application;

import de.bcxp.challenge.adapters.csv.CSVRecordReader;
import de.bcxp.challenge.adapters.csv.mappers.CountryCSVRowMapper;
import de.bcxp.challenge.countries.CountryEntity;
import de.bcxp.challenge.countries.CountryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static de.bcxp.challenge.countries.CountryUtils.COUNTRY_CSV_FORMAT;

public class HighestNumberOfPeoplePerSquareKilometer {

    private static final Logger logger = LogManager.getLogger(HighestNumberOfPeoplePerSquareKilometer.class);

    public static void main(String[] args) {
        runHighestNumberOfPeoplePerSquareKilometer();
    }

    public static void runHighestNumberOfPeoplePerSquareKilometer() {

        logger.info("Starting HighestNumberOfPeoplePerSquareKilometer");

        try {

            String counterFilePath = "src/main/resources/de/bcxp/challenge/countries.csv";


            CSVRecordReader<CountryEntity> csvRecordReader = new CSVRecordReader<>(
                    new CountryCSVRowMapper(true),
                    COUNTRY_CSV_FORMAT
            );

            ArrayList<CountryEntity> countryData = csvRecordReader.readAll(counterFilePath);

            CountryEntity countryWithHighestDensity = CountryService.getCountryWithHighestPopulationDensity(countryData);

            logger.info("Country ({}) has the highest population density: {} people/km²",
                    countryWithHighestDensity.Name,
                    countryWithHighestDensity.getPopulationDensity());


        } catch (Exception e) {
            logger.error("An error occurred ", e);
        }
    }
}
