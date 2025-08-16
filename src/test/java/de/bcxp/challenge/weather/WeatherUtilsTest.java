package de.bcxp.challenge.weather;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherUtilsTest {

    @Test
    void testTemperatureSpreadComparator_sortsAscending() {
        // Arrange
        WeatherEntity day1 = new WeatherEntity();
        day1.Day = 1;
        day1.MxT = 20;
        day1.MnT = 10; // Spread: 10

        WeatherEntity day2 = new WeatherEntity();
        day2.Day = 2;
        day2.MxT = 15;
        day2.MnT = 10; // Spread: 5

        List<WeatherEntity> weatherList = Arrays.asList(day1, day2);

        // Act
        weatherList.sort(WeatherUtils.TEMPERATURE_SPREAD_COMPARATOR);

        // Assert
        assertEquals(2, weatherList.get(0).Day); // Smallest spread first
        assertEquals(1, weatherList.get(1).Day); // Larger spread second
    }
}
