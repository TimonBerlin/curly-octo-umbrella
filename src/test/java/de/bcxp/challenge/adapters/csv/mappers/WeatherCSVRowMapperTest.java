package de.bcxp.challenge.adapters.csv.mappers;

import de.bcxp.challenge.weather.WeatherEntity;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WeatherCSVRowMapperTest {

    /**
     * Helper method to create a valid weather data row.
     */
    private Map<String, String> createValidRow() {
        Map<String, String> row = new HashMap<>();
        row.put("Day", "1");
        row.put("MxT", "88");
        row.put("MnT", "59");
        row.put("AvT", "74");
        row.put("AvDP", "53.8");
        row.put("1HrP TPcpn", "0");
        row.put("PDir", "280");
        row.put("AvSp", "9.6");
        row.put("Dir", "270");
        row.put("MxS", "17");
        row.put("SkyC", "1.6");
        row.put("MxR", "93");
        row.put("Mn", "23");
        row.put("R AvSLP", "1016.3");
        return row;
    }

    @Test
    void testMapRow_ValidData() {
        WeatherCSVRowMapper mapper = new WeatherCSVRowMapper(false);
        WeatherEntity result = mapper.mapRow(createValidRow());

        assertNotNull(result);
        assertEquals(1, result.Day);
        assertEquals(88, result.MxT);
        assertEquals(59, result.MnT);
        assertEquals(53.8, result.AvDP, 0.01);
    }

    @Test
    void testMapRow_SkipInvalidRows_False_ThrowsException() {
        WeatherCSVRowMapper mapper = new WeatherCSVRowMapper(false);

        // Test null row - should throw exception
        assertThrows(IllegalArgumentException.class, () -> mapper.mapRow(null));

        // Test missing column - should throw exception
        Map<String, String> incompleteRow = createValidRow();
        incompleteRow.remove("Day");
        assertThrows(IllegalArgumentException.class, () -> mapper.mapRow(incompleteRow));

        // Test invalid number format - should throw exception
        Map<String, String> invalidRow = createValidRow();
        invalidRow.put("Day", "invalid_number");
        assertThrows(IllegalArgumentException.class, () -> mapper.mapRow(invalidRow));
    }

    @Test
    void testMapRow_SkipInvalidRows_True_ReturnsNull() {
        WeatherCSVRowMapper mapper = new WeatherCSVRowMapper(true);

        // Test null row - should return null instead of throwing
        assertNull(mapper.mapRow(null));

        // Test empty row - should return null instead of throwing
        assertNull(mapper.mapRow(new HashMap<>()));

        // Test missing column - should return null instead of throwing
        Map<String, String> incompleteRow = createValidRow();
        incompleteRow.remove("MxT");
        assertNull(mapper.mapRow(incompleteRow));

        // Test invalid number format - should return null instead of throwing
        Map<String, String> invalidRow = createValidRow();
        invalidRow.put("AvDP", "not_a_number");
        assertNull(mapper.mapRow(invalidRow));

        // Test invalid day range - should return null instead of throwing
        Map<String, String> invalidDay = createValidRow();
        invalidDay.put("Day", "0");
        assertNull(mapper.mapRow(invalidDay));

        // Test max temp < min temp - should return null instead of throwing
        Map<String, String> tempOrder = createValidRow();
        tempOrder.put("MxT", "50");
        tempOrder.put("MnT", "60");
        assertNull(mapper.mapRow(tempOrder));
    }

    @Test
    void testMapRow_SkipInvalidRows_Configuration() {
        // Test that the same invalid data behaves differently based on configuration
        Map<String, String> invalidRow = createValidRow();
        invalidRow.put("Day", "invalid");

        // With skipInvalidRows = false, should throw exception
        WeatherCSVRowMapper strictMapper = new WeatherCSVRowMapper(false);
        assertThrows(IllegalArgumentException.class, () -> strictMapper.mapRow(invalidRow));

        // With skipInvalidRows = true, should return null
        WeatherCSVRowMapper lenientMapper = new WeatherCSVRowMapper(true);
        assertNull(lenientMapper.mapRow(invalidRow));
    }

    @Test
    void testIsValidRow() {
        WeatherCSVRowMapper mapper = new WeatherCSVRowMapper(false);

        // Valid data
        assertTrue(mapper.isValidRow(createValidRow()));

        // Invalid cases
        assertFalse(mapper.isValidRow(null));
        assertFalse(mapper.isValidRow(new HashMap<>()));

        Map<String, String> invalidDay = createValidRow();
        invalidDay.put("Day", "0");
        assertFalse(mapper.isValidRow(invalidDay));

        Map<String, String> invalidTemp = createValidRow();
        invalidTemp.put("MxT", "200");
        assertFalse(mapper.isValidRow(invalidTemp));

        Map<String, String> tempOrder = createValidRow();
        tempOrder.put("MxT", "50");
        tempOrder.put("MnT", "60");
        assertFalse(mapper.isValidRow(tempOrder));
    }

    @Test
    void testMapRow_WithWhitespace() {
        WeatherCSVRowMapper mapper = new WeatherCSVRowMapper(false);
        Map<String, String> rowWithWhitespace = createValidRow();
        rowWithWhitespace.put("Day", "  2  ");
        rowWithWhitespace.put("MxT", " 90 ");

        WeatherEntity result = mapper.mapRow(rowWithWhitespace);
        assertNotNull(result);
        assertEquals(2, result.Day);
        assertEquals(90, result.MxT);
    }
}
