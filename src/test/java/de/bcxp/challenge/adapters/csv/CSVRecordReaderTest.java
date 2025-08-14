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
    void testConstructor_NullFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CSVRecordReader<>(null, new TestRowMapper(), CSVFormat.DEFAULT);
        });
    }

    @Test
    void testConstructor_EmptyFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CSVRecordReader<>("", new TestRowMapper(), CSVFormat.DEFAULT);
        });
    }

    @Test
    void testConstructor_NonExistentFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CSVRecordReader<>("nonexistent.csv", new TestRowMapper(), CSVFormat.DEFAULT);
        });
    }

    @Test
    void testReadAll_Success() throws IOException {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader("name", "age")
                .setSkipHeaderRecord(true)
                .build();
        
        CSVRecordReader<TestData> reader = new CSVRecordReader<>(
            "src/test/resources/test.csv", 
            new TestRowMapper(), 
            format
        );
        
        ArrayList<TestData> result = reader.readAll();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).name);
        assertEquals("25", result.get(0).age);
        assertEquals("Jane", result.get(1).name);
        assertEquals("30", result.get(1).age);
        
        reader.close();
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
        public boolean validateRow(Map<String, String> row) {
            return true;
        }
    }
}
