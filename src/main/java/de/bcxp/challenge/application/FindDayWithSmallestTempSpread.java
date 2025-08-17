package de.bcxp.challenge.application;

import de.bcxp.challenge.adapters.csv.CSVRecordReader;
import de.bcxp.challenge.adapters.csv.mappers.WeatherCSVRowMapper;
import de.bcxp.challenge.weather.WeatherEntity;
import de.bcxp.challenge.weather.WeatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static de.bcxp.challenge.weather.WeatherUtils.WEATHER_CSV_FORMAT;

/**
 * This class is the entry point for the application that finds the day with the smallest temperature spread.
 * It will read temperature records and determine which day has the smallest difference between maximum and minimum temperatures.
 */
public class FindDayWithSmallestTempSpread {

    private static final Logger logger = LogManager.getLogger(FindDayWithSmallestTempSpread.class);

    public static void main(String[] args) {
        runFindDayWithSmallestTempSpread();
    }

    public static void runFindDayWithSmallestTempSpread() {

        logger.info("Starting findDayWithSmallestTempSpread...");

        try {

            String weatherFilePath = "src/main/resources/de/bcxp/challenge/weather.csv";


            CSVRecordReader<WeatherEntity> csvRecordReader = new CSVRecordReader<WeatherEntity>(
                    new WeatherCSVRowMapper(true),
                    WEATHER_CSV_FORMAT
            );

            ArrayList<WeatherEntity> weatherData = csvRecordReader.readAll(weatherFilePath);

            WeatherEntity dayWithSmallestSpread = WeatherService.getDayWithSmallestTemperatureSpread(weatherData);

            logger.info("Day ({}) is the day with the smallest temperature spread: {}", dayWithSmallestSpread.Day, dayWithSmallestSpread.getTemperatureSpread());

        } catch (Exception e) {
            logger.error("An error occurred ", e);
        }
    }
}
