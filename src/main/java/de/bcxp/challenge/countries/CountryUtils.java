package de.bcxp.challenge.countries;

import org.apache.commons.csv.CSVFormat;
import java.util.Comparator;

public class CountryUtils {

    /**
     * Comparator to sort CountryEntity objects by population density in descending order.
     * The entity with the highest population density will be at index 0.
     */
    public static final Comparator<CountryEntity> POPULATION_DENSITY_COMPARATOR = 
            Comparator.comparing(CountryEntity::getPopulationDensity).reversed();

    public static CSVFormat COUNTRY_CSV_FORMAT = CSVFormat.Builder.create()
            .setHeader("Name", "Capital", "Accession", "Population", "Area (km²)", "GDP (US$ M)", "HDI", "MEPs")
            .setSkipHeaderRecord(true)
            .setDelimiter(';')
            .get();
}
