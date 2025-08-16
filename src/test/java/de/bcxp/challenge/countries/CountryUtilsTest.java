package de.bcxp.challenge.countries;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CountryUtilsTest {

    @Test
    void testPopulationDensityComparator_sortsDescending() {
        // Arrange
        CountryEntity country1 = new CountryEntity();
        country1.Name = "Country1";
        country1.Population = 1000;
        country1.Area = 100; // Density: 10

        CountryEntity country2 = new CountryEntity();
        country2.Name = "Country2";
        country2.Population = 2000;
        country2.Area = 100; // Density: 20

        List<CountryEntity> countryList = Arrays.asList(country1, country2);

        // Act
        countryList.sort(CountryUtils.POPULATION_DENSITY_COMPARATOR);

        // Assert
        assertEquals("Country2", countryList.get(0).Name); // Highest density first
        assertEquals("Country1", countryList.get(1).Name); // Lower density second
    }
}
