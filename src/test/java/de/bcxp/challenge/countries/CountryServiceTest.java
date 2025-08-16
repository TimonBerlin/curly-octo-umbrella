package de.bcxp.challenge.countries;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryServiceTest {

    @Nested
    public class GetCountryWithHighestPopulationDensity {

        /**
         * Helper method to create a CountryEntity with specified name, population, and area.
         */
        private CountryEntity createCountryEntity(String name, long population, double area) {
            CountryEntity entity = new CountryEntity();
            entity.Name = name;
            entity.Population = population;
            entity.Area = area;
            return entity;
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_NullInput() {
            // Test with null input
            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(null);
            assertNull(result, "Should return null for null input");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_EmptyList() {
            // Test with empty list
            List<CountryEntity> emptyList = new ArrayList<>();
            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(emptyList);
            assertNull(result, "Should return null for empty list");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_SingleElement() {
            // Test with single element
            CountryEntity singleCountry = createCountryEntity("Germany", 83000000, 357000); // density ≈ 232.5
            List<CountryEntity> singleElementList = Arrays.asList(singleCountry);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(singleElementList);
            assertNotNull(result, "Should return the single element");
            assertEquals(singleCountry, result, "Should return the same single element");
            assertEquals("Germany", result.Name, "Country name should be Germany");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_MultipleElements() {
            // Test with multiple elements - should return the one with highest population density
            CountryEntity country1 = createCountryEntity("Germany", 83000000, 357000); // density ≈ 232.5
            CountryEntity country2 = createCountryEntity("Netherlands", 17500000, 41500); // density ≈ 421.7 (highest)
            CountryEntity country3 = createCountryEntity("France", 67000000, 643800); // density ≈ 104.1
            CountryEntity country4 = createCountryEntity("Poland", 38000000, 312700); // density ≈ 121.5

            List<CountryEntity> countryData = Arrays.asList(country1, country2, country3, country4);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(countryData);
            assertNotNull(result, "Should return a result");
            assertEquals(country2, result, "Should return Netherlands with highest density");
            assertEquals("Netherlands", result.Name, "Country name should be Netherlands");
            assertEquals(421.68674698795183, result.getPopulationDensity(), 0.01, "Population density should be approximately 421.7");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_EqualDensities() {
            // Test with equal population densities - should return the first one after sorting
            CountryEntity country1 = createCountryEntity("Country A", 1000000, 10000); // density = 100
            CountryEntity country2 = createCountryEntity("Country B", 2000000, 20000); // density = 100
            CountryEntity country3 = createCountryEntity("Country C", 500000, 5000); // density = 100

            List<CountryEntity> countryData = Arrays.asList(country1, country2, country3);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(countryData);
            assertNotNull(result, "Should return a result");
            assertEquals(100.0, result.getPopulationDensity(), 0.01, "Population density should be 100");
            // Since all have equal densities, any of them could be returned (depends on sorting stability)
            assertTrue(Arrays.asList(country1, country2, country3).contains(result),
                    "Should return one of the countries with equal highest density");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_ZeroArea() {
            // Test with zero area (edge case)
            CountryEntity country1 = createCountryEntity("Normal Country", 1000000, 10000); // density = 100
            CountryEntity country2 = createCountryEntity("Zero Area Country", 1000000, 0); // density = 0
            CountryEntity country3 = createCountryEntity("Small Country", 500000, 1000); // density = 500 (highest)

            List<CountryEntity> countryData = Arrays.asList(country1, country2, country3);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(countryData);
            assertNotNull(result, "Should return a result");
            assertEquals(country3, result, "Should return Small Country with highest density");
            assertEquals("Small Country", result.Name, "Country name should be Small Country");
            assertEquals(500.0, result.getPopulationDensity(), 0.01, "Population density should be 500");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_ZeroPopulation() {
            // Test with zero population
            CountryEntity country1 = createCountryEntity("Normal Country", 1000000, 10000); // density = 100 (highest)
            CountryEntity country2 = createCountryEntity("Empty Country", 0, 10000); // density = 0
            CountryEntity country3 = createCountryEntity("Small Population", 100000, 5000); // density = 20

            List<CountryEntity> countryData = Arrays.asList(country1, country2, country3);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(countryData);
            assertNotNull(result, "Should return a result");
            assertEquals(country1, result, "Should return Normal Country with highest density");
            assertEquals("Normal Country", result.Name, "Country name should be Normal Country");
            assertEquals(100.0, result.getPopulationDensity(), 0.01, "Population density should be 100");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_VeryHighDensity() {
            // Test with very high population density (like city-states)
            CountryEntity country1 = createCountryEntity("Germany", 83000000, 357000); // density ≈ 232.5
            CountryEntity country2 = createCountryEntity("Monaco", 39000, 2); // density = 19500 (highest)
            CountryEntity country3 = createCountryEntity("Singapore", 5900000, 720); // density ≈ 8194.4

            List<CountryEntity> countryData = Arrays.asList(country1, country2, country3);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(countryData);
            assertNotNull(result, "Should return a result");
            assertEquals(country2, result, "Should return Monaco with highest density");
            assertEquals("Monaco", result.Name, "Country name should be Monaco");
            assertEquals(19500.0, result.getPopulationDensity(), 0.01, "Population density should be 19500");
        }

        @Test
        void testGetCountryWithHighestPopulationDensity_LowDensity() {
            // Test with very low population densities
            CountryEntity country1 = createCountryEntity("Mongolia", 3300000, 1564000); // density ≈ 2.1
            CountryEntity country2 = createCountryEntity("Australia", 25700000, 7692000); // density ≈ 3.3 (highest)
            CountryEntity country3 = createCountryEntity("Canada", 38000000, 9985000); // density ≈ 3.8 (actually highest)

            List<CountryEntity> countryData = Arrays.asList(country1, country2, country3);

            CountryEntity result = CountryService.getCountryWithHighestPopulationDensity(countryData);
            assertNotNull(result, "Should return a result");
            assertEquals(country3, result, "Should return Canada with highest density");
            assertEquals("Canada", result.Name, "Country name should be Canada");
            assertEquals(3.8055381138247895, result.getPopulationDensity(), 0.01, "Population density should be approximately 3.8");
        }
    }
}
