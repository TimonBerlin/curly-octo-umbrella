package de.bcxp.challenge.weather;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    @Nested
    public class GetDayWithSmallestTemperatureSpread {

        /**
         * Helper method to create a WeatherEntity with specified day, max temperature, and min temperature.
         */
        private WeatherEntity createWeatherEntity(int day, int maxTemp, int minTemp) {
            WeatherEntity entity = new WeatherEntity();
            entity.Day = day;
            entity.MxT = maxTemp;
            entity.MnT = minTemp;
            return entity;
        }


        @Test
        void testGetDayWithSmallestTemperatureSpread_NullInput() {
            // Test with null input
            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(null);
            assertNull(result, "Should return null for null input");
        }

        @Test
        void testGetDayWithSmallestTemperatureSpread_EmptyList() {
            // Test with empty list
            List<WeatherEntity> emptyList = new ArrayList<>();
            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(emptyList);
            assertNull(result, "Should return null for empty list");
        }

        @Test
        void testGetDayWithSmallestTemperatureSpread_SingleElement() {
            // Test with single element
            WeatherEntity singleDay = createWeatherEntity(1, 25, 15); // spread = 10
            List<WeatherEntity> singleElementList = Arrays.asList(singleDay);

            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(singleElementList);
            assertNotNull(result, "Should return the single element");
            assertEquals(singleDay, result, "Should return the same single element");
            assertEquals(1, result.Day, "Day should be 1");
        }

        @Test
        void testGetDayWithSmallestTemperatureSpread_MultipleElements() {
            // Test with multiple elements - should return the one with smallest temperature spread
            WeatherEntity day1 = createWeatherEntity(1, 30, 20); // spread = 10
            WeatherEntity day2 = createWeatherEntity(2, 25, 20); // spread = 5 (smallest)
            WeatherEntity day3 = createWeatherEntity(3, 35, 15); // spread = 20
            WeatherEntity day4 = createWeatherEntity(4, 28, 18); // spread = 10

            List<WeatherEntity> weatherData = Arrays.asList(day1, day2, day3, day4);

            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(weatherData);
            assertNotNull(result, "Should return a result");
            assertEquals(day2, result, "Should return day 2 with smallest spread");
            assertEquals(2, result.Day, "Day should be 2");
            assertEquals(5.0f, result.getTemperatureSpread(), "Temperature spread should be 5");
        }

        @Test
        void testGetDayWithSmallestTemperatureSpread_EqualSpreads() {
            // Test with equal temperature spreads - should return the first one after sorting
            WeatherEntity day1 = createWeatherEntity(1, 25, 20); // spread = 5
            WeatherEntity day2 = createWeatherEntity(2, 30, 25); // spread = 5
            WeatherEntity day3 = createWeatherEntity(3, 22, 17); // spread = 5

            List<WeatherEntity> weatherData = Arrays.asList(day1, day2, day3);

            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(weatherData);
            assertNotNull(result, "Should return a result");
            assertEquals(5.0f, result.getTemperatureSpread(), "Temperature spread should be 5");
            // Since all have equal spreads, any of them could be returned (depends on sorting stability)
            assertTrue(Arrays.asList(day1, day2, day3).contains(result),
                    "Should return one of the days with equal smallest spread");
        }

        @Test
        void testGetDayWithSmallestTemperatureSpread_NegativeTemperatures() {
            // Test with negative temperatures
            WeatherEntity day1 = createWeatherEntity(1, 5, -10); // spread = 15
            WeatherEntity day2 = createWeatherEntity(2, -5, -8); // spread = 3 (smallest)
            WeatherEntity day3 = createWeatherEntity(3, 0, -12); // spread = 12

            List<WeatherEntity> weatherData = Arrays.asList(day1, day2, day3);

            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(weatherData);
            assertNotNull(result, "Should return a result");
            assertEquals(day2, result, "Should return day 2 with smallest spread");
            assertEquals(2, result.Day, "Day should be 2");
            assertEquals(3.0f, result.getTemperatureSpread(), "Temperature spread should be 3");
        }

        @Test
        void testGetDayWithSmallestTemperatureSpread_ZeroSpread() {
            // Test with zero temperature spread
            WeatherEntity day1 = createWeatherEntity(1, 20, 10); // spread = 10
            WeatherEntity day2 = createWeatherEntity(2, 15, 15); // spread = 0 (smallest)
            WeatherEntity day3 = createWeatherEntity(3, 25, 20); // spread = 5

            List<WeatherEntity> weatherData = Arrays.asList(day1, day2, day3);

            WeatherEntity result = WeatherService.getDayWithSmallestTemperatureSpread(weatherData);
            assertNotNull(result, "Should return a result");
            assertEquals(day2, result, "Should return day 2 with zero spread");
            assertEquals(2, result.Day, "Day should be 2");
            assertEquals(0.0f, result.getTemperatureSpread(), "Temperature spread should be 0");
        }


    }
}
