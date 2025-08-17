package de.bcxp.challenge.adapters.csv;

import de.bcxp.challenge.adapters.RowMapper;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CSVRecordReaderTest {

    @Test
    void testReadAll_NullFilePath() {
        CSVRecordReader<TestData> reader = new CSVRecordReader<>(new TestRowMapper(), CSVFormat.DEFAULT);
        assertThrows(IllegalArgumentException.class, () -> {
            reader.readAll(null);
        });
    }

    @Test
    void testReadAll_EmptyFilePath() {
        CSVRecordReader<TestData> reader = new CSVRecordReader<>(new TestRowMapper(), CSVFormat.DEFAULT);
        assertThrows(IllegalArgumentException.class, () -> {
            reader.readAll("");
        });
    }

    @Test
    void testReadAll_NonExistentFile() {
        CSVRecordReader<TestData> reader = new CSVRecordReader<>(new TestRowMapper(), CSVFormat.DEFAULT);
        assertThrows(IllegalArgumentException.class, () -> {
            reader.readAll("nonexistent.csv");
        });
    }

    @Test
    void testReadAll_Success() throws IOException {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader("name", "age")
                .setSkipHeaderRecord(true)
                .build();

        CSVRecordReader<TestData> reader = new CSVRecordReader<>(
            new TestRowMapper(),
            format
        );

        ArrayList<TestData> result = reader.readAll("src/test/resources/test.csv");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).name);
        assertEquals("25", result.get(0).age);
        assertEquals("Jane", result.get(1).name);
        assertEquals("30", result.get(1).age);
    }

    // Simple test data class
    static class TestData {
        String name;
        String age;

        TestData(String name, String age) {
            this.name = name;
            this.age = age;
        }
    }

    // Simple test row mapper
    static class TestRowMapper implements RowMapper<TestData> {
        @Override
        public TestData mapRow(Map<String, String> row) {
            return new TestData(row.get("name"), row.get("age"));
        }

        @Override
        public boolean isValidRow(Map<String, String> row) {
            return true;
        }
    }
}
