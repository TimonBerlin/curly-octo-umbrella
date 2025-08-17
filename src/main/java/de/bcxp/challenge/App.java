package de.bcxp.challenge;

import de.bcxp.challenge.adapters.csv.CSVRecordReader;
import de.bcxp.challenge.adapters.csv.mappers.WeatherCSVRowMapper;
import de.bcxp.challenge.weather.WeatherEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import static de.bcxp.challenge.application.FindDayWithSmallestTempSpread.runFindDayWithSmallestTempSpread;
import static de.bcxp.challenge.application.HighestNumberOfPeoplePerSquareKilometer.runHighestNumberOfPeoplePerSquareKilometer;
import static de.bcxp.challenge.weather.WeatherUtils.WEATHER_CSV_FORMAT;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 */
public final class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {
        logger.info("Starting BCXP Challenge Application - Timon Bomke - 2025");

        runFindDayWithSmallestTempSpread();
       logger.info(""); // Just to separate the outputs of the two applications
        runHighestNumberOfPeoplePerSquareKilometer();
    }
}
