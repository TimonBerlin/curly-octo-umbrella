package de.bcxp.challenge.countries;

import java.util.List;

import static de.bcxp.challenge.countries.CountryUtils.POPULATION_DENSITY_COMPARATOR;

public class CountryService {

    /**
     * Finds the country with the highest population density from the given list of countries.
     * 
     * @param countries List of CountryEntity objects to analyze
     * @return CountryEntity with the highest population density, or null if the list is empty or null
     */
    public static CountryEntity getCountryWithHighestPopulationDensity(List<CountryEntity> countries) {
        
        if (countries == null || countries.isEmpty()) {
            return null; // No data available
        } else if (countries.size() == 1) {
            return countries.get(0); // Only one country, return it
        }

        countries.sort(POPULATION_DENSITY_COMPARATOR);

        return countries.get(0);
    }
}
