package de.bcxp.challenge.weather;

import java.util.List;

import static de.bcxp.challenge.weather.WeatherUtils.TEMPERATURE_SPREAD_COMPARATOR;

public class WeatherService {

    public static WeatherEntity getDayWithSmallestTemperatureSpread(List<WeatherEntity> weatherData) {

        if (weatherData == null || weatherData.isEmpty()) {
            return null; // No data available
        } else if (weatherData.size() == 1) {
            return weatherData.get(0); // Only one day of data, return it
        }

        weatherData.sort(TEMPERATURE_SPREAD_COMPARATOR);


        return weatherData.get(0);
    }
}
