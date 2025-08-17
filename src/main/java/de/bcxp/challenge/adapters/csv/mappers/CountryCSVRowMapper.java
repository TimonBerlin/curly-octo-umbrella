package de.bcxp.challenge.adapters.csv.mappers;

import de.bcxp.challenge.adapters.ConfigurableRowMapper;
import de.bcxp.challenge.countries.CountryEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CountryCSVRowMapper extends ConfigurableRowMapper<CountryEntity> {

    private static final Logger logger = LogManager.getLogger(CountryCSVRowMapper.class);

    public CountryCSVRowMapper(boolean skipInvalidRows) {
        super(skipInvalidRows);
    }

    /**
     * Validates if a number string is in the correct format (English format without commas).
     */
    private boolean validateNumberFormat(String numberStr) {
        if (numberStr == null || numberStr.trim().isEmpty()) {
            return false;
        }

        String trimmed = numberStr.trim();

        // Check for commas (not allowed in English format)
        if (trimmed.contains(",")) {
            logger.warn("Number format contains comma: " + trimmed);
            return false;
        }

        // Check for multiple dots (not allowed)
        long dotCount = trimmed.chars().filter(c -> c == '.').count();
        if (dotCount > 1) {
            logger.warn("Number format contains multiple dots: " + trimmed);
            return false;
        }

        return true;
    }

    @Override
    public CountryEntity mapRow(Map<String, String> row) {

        if (!isValidRow(row)) {

            if (this.skipInvalidRows) {
                logger.warn("Invalid row data: " + row);
                return null;
            } else {
                throw new IllegalArgumentException("Invalid row data: " + row);
            }

        }

        CountryEntity entity = new CountryEntity();

        entity.Name = row.get("Name").trim();
        entity.Capital = row.get("Capital").trim();
        entity.Accession = row.get("Accession").trim();

        // Parse Population using English format only
        try {
            entity.Population = Long.parseLong(row.get("Population").trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse population: " + row.get("Population"), e);
        }

        entity.Area = Double.parseDouble(row.get("Area (km²)").trim());
        entity.GDP = Long.parseLong(row.get("GDP (US$ M)").trim());
        entity.HDI = Double.parseDouble(row.get("HDI").trim());
        entity.MEPs = Integer.parseInt(row.get("MEPs").trim());

        return entity;
    }

    @Override
    public boolean isValidRow(Map<String, String> row) {
        if (row == null || row.isEmpty()) {
            logger.warn("Row is null or empty");
            return false;
        }

        // Check if all required columns are present
        String[] requiredColumns = {
                "Name", "Capital", "Accession", "Population", "Area (km²)", "GDP (US$ M)", "HDI", "MEPs"
        };

        for (String column : requiredColumns) {
            if (!row.containsKey(column) || row.get(column) == null || row.get(column).trim().isEmpty()) {
                logger.warn("Missing or empty required column: " + column);
                return false;
            }
        }

        try {
            // Validate Name and Capital (should not be empty)
            String name = row.get("Name").trim();
            String capital = row.get("Capital").trim();
            if (name.isEmpty()) {
                logger.warn("Country name is empty");
                return false;
            }
            if (capital.isEmpty()) {
                logger.warn("Capital city is empty for country: " + name);
                return false;
            }

            // Validate Population (should be positive and in English format only)
            String populationStr = row.get("Population").trim();

            // Use validateNumberFormat function to check format
            if (!validateNumberFormat(populationStr)) {
                logger.warn("Invalid population format (only English format accepted): " + populationStr + " for country: " + name);
                return false;
            }

            long population;
            try {
                population = Long.parseLong(populationStr);
            } catch (NumberFormatException e) {
                logger.warn("Cannot parse population as number: " + populationStr + " for country: " + name);
                return false;
            }

            if (population <= 0) {
                logger.warn("Invalid population value: " + population + " for country: " + name + " - Population must be greater than 0");
                return false;
            }

            // Validate Area (should be positive and not zero)
            String areaStr = row.get("Area (km²)").trim();
            double area = Double.parseDouble(areaStr);
            if (area <= 0) {
                logger.warn("Invalid area value: " + area + " for country: " + name + " - Area must be greater than 0");
                return false;
            }

            // Validate GDP (should be non-negative)
            long gdp = Long.parseLong(row.get("GDP (US$ M)").trim());
            if (gdp < 0) {
                logger.warn("Invalid GDP value: " + gdp + " for country: " + name + " - GDP cannot be negative");
                return false;
            }

            // Validate HDI (should be between 0 and 1)
            double hdi = Double.parseDouble(row.get("HDI").trim());
            if (hdi < 0 || hdi > 1) {
                logger.warn("Invalid HDI value: " + hdi + " for country: " + name + " - HDI must be between 0 and 1");
                return false;
            }

            // Validate MEPs (should be positive)
            int meps = Integer.parseInt(row.get("MEPs").trim());
            if (meps <= 0) {
                logger.warn("Invalid MEPs value: " + meps + " for country: " + name + " - MEPs must be greater than 0");
                return false;
            }

        } catch (NumberFormatException e) {
            // If any value cannot be parsed as expected type, row is invalid
            logger.warn("Number format error while parsing row data: " + e.getMessage() + " - Row: " + row);
            return false;
        }

        return true;
    }
}
