package de.bcxp.challenge.adapters.csv.mappers;

import de.bcxp.challenge.countries.CountryEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountryCSVRowMapperTest {

    /**
     * Helper method to create a valid country data row.
     */
    private Map<String, String> createValidRow() {
        Map<String, String> row = new HashMap<>();
        row.put("Name", "Germany");
        row.put("Capital", "Berlin");
        row.put("Accession", "1957-03-25");
        row.put("Population", "83240525");
        row.put("Area (km²)", "357114.0");
        row.put("GDP (US$ M)", "4259935");
        row.put("HDI", "0.947");
        row.put("MEPs", "96");
        return row;
    }

    @Nested
    public class MapRowTests {

        @Test
        void testMapRow_ValidData() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            CountryEntity result = mapper.mapRow(createValidRow());

            assertNotNull(result);
            assertEquals("Germany", result.Name);
            assertEquals("Berlin", result.Capital);
            assertEquals("1957-03-25", result.Accession);
            assertEquals(83240525L, result.Population);
            assertEquals(357114.0, result.Area, 0.01);
            assertEquals(4259935L, result.GDP);
            assertEquals(0.947, result.HDI, 0.001);
            assertEquals(96, result.MEPs);
        }

        @Test
        void testMapRow_SkipInvalidRows_False_ThrowsException() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            
            // Test null row - should throw exception
            assertThrows(IllegalArgumentException.class, () -> mapper.mapRow(null));
            
            // Test missing column - should throw exception
            Map<String, String> incompleteRow = createValidRow();
            incompleteRow.remove("Name");
            assertThrows(IllegalArgumentException.class, () -> mapper.mapRow(incompleteRow));
            
            // Test invalid population format with comma - should throw exception
            Map<String, String> invalidRow = createValidRow();
            invalidRow.put("Population", "83,240,525");
            assertThrows(IllegalArgumentException.class, () -> mapper.mapRow(invalidRow));
        }

        @Test
        void testMapRow_SkipInvalidRows_True_ReturnsNull() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(true);
            
            // Test null row - should return null instead of throwing
            assertNull(mapper.mapRow(null));
            
            // Test empty row - should return null instead of throwing
            assertNull(mapper.mapRow(new HashMap<>()));
            
            // Test missing column - should return null instead of throwing
            Map<String, String> incompleteRow = createValidRow();
            incompleteRow.remove("Capital");
            assertNull(mapper.mapRow(incompleteRow));
            
            // Test invalid population format - should return null instead of throwing
            Map<String, String> invalidRow = createValidRow();
            invalidRow.put("Population", "83,240,525");
            assertNull(mapper.mapRow(invalidRow));
        }

        @Test
        void testMapRow_WithWhitespace() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            Map<String, String> rowWithWhitespace = createValidRow();
            rowWithWhitespace.put("Name", "  France  ");
            rowWithWhitespace.put("Population", " 67391582 ");

            CountryEntity result = mapper.mapRow(rowWithWhitespace);
            assertNotNull(result);
            assertEquals("France", result.Name);
            assertEquals(67391582L, result.Population);
        }
    }

    @Nested
    public class ValidateNumberFormatTests {

        @Test
        void testValidateNumberFormat_ValidNumbers() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            
            // Test valid population numbers (English format)
            Map<String, String> validRow1 = createValidRow();
            validRow1.put("Population", "83240525");
            assertTrue(mapper.validateRow(validRow1));

            Map<String, String> validRow2 = createValidRow();
            validRow2.put("Population", "1000000");
            assertTrue(mapper.validateRow(validRow2));

            Map<String, String> validRow3 = createValidRow();
            validRow3.put("Population", "123");
            assertTrue(mapper.validateRow(validRow3));
        }

        @Test
        void testValidateNumberFormat_InvalidNumbers() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            
            // Test population with comma (German format) - should be invalid
            Map<String, String> invalidRow1 = createValidRow();
            invalidRow1.put("Population", "83,240,525");
            assertFalse(mapper.validateRow(invalidRow1));

            // Test population with multiple dots - should be invalid
            Map<String, String> invalidRow2 = createValidRow();
            invalidRow2.put("Population", "83.240.525");
            assertFalse(mapper.validateRow(invalidRow2));

            // Test population with letters - should be invalid
            Map<String, String> invalidRow3 = createValidRow();
            invalidRow3.put("Population", "83240525abc");
            assertFalse(mapper.validateRow(invalidRow3));

            // Test empty population - should be invalid
            Map<String, String> invalidRow4 = createValidRow();
            invalidRow4.put("Population", "");
            assertFalse(mapper.validateRow(invalidRow4));

            // Test population with spaces in between - should be invalid
            Map<String, String> invalidRow5 = createValidRow();
            invalidRow5.put("Population", "83 240 525");
            assertFalse(mapper.validateRow(invalidRow5));
        }

        @Test
        void testValidateNumberFormat_EdgeCases() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            
            // Test with leading/trailing whitespace - should be valid after trimming
            Map<String, String> whitespaceRow = createValidRow();
            whitespaceRow.put("Population", "  83240525  ");
            assertTrue(mapper.validateRow(whitespaceRow));
            
            // Test very large number - should be valid
            Map<String, String> largeNumberRow = createValidRow();
            largeNumberRow.put("Population", "9223372036854775807"); // Long.MAX_VALUE
            assertTrue(mapper.validateRow(largeNumberRow));
            
            // Test single digit - should be valid
            Map<String, String> singleDigitRow = createValidRow();
            singleDigitRow.put("Population", "1");
            assertTrue(mapper.validateRow(singleDigitRow));
        }
    }

    @Nested
    public class ValidateRowTests {

        @Test
        void testValidateRow_PopulationValidation() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            
            // Valid population
            assertTrue(mapper.validateRow(createValidRow()));
            
            // Zero population - should be invalid
            Map<String, String> zeroPopulation = createValidRow();
            zeroPopulation.put("Population", "0");
            assertFalse(mapper.validateRow(zeroPopulation));
            
            // Negative population - should be invalid
            Map<String, String> negativePopulation = createValidRow();
            negativePopulation.put("Population", "-1000");
            assertFalse(mapper.validateRow(negativePopulation));
        }

        @Test
        void testValidateRow_OtherFieldValidation() {
            CountryCSVRowMapper mapper = new CountryCSVRowMapper(false);
            
            // Empty name - should be invalid
            Map<String, String> emptyName = createValidRow();
            emptyName.put("Name", "");
            assertFalse(mapper.validateRow(emptyName));
            
            // Empty capital - should be invalid
            Map<String, String> emptyCapital = createValidRow();
            emptyCapital.put("Capital", "");
            assertFalse(mapper.validateRow(emptyCapital));
            
            // Invalid HDI (> 1) - should be invalid
            Map<String, String> invalidHDI = createValidRow();
            invalidHDI.put("HDI", "1.5");
            assertFalse(mapper.validateRow(invalidHDI));
            
            // Negative GDP - should be invalid
            Map<String, String> negativeGDP = createValidRow();
            negativeGDP.put("GDP (US$ M)", "-1000");
            assertFalse(mapper.validateRow(negativeGDP));
        }
    }
}
