package de.bcxp.challenge.adapters.csv;

import de.bcxp.challenge.adapters.RecordReader;
import de.bcxp.challenge.adapters.RowMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class CSVRecordReader<T> implements RecordReader<T> {

    private final Reader reader;
    private final RowMapper<T> rowMapper;
    private final CSVFormat format;
    private CSVParser parser;

    private static final Logger logger = LogManager.getLogger(CSVRecordReader.class);


    public CSVRecordReader(String filePath, RowMapper<T> rowMapper, CSVFormat format) throws IOException {

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        if(!(new File(filePath).exists())) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }

        this.reader = new FileReader(filePath);
        this.rowMapper = rowMapper;
        this.format = format;
    }

    @Override
    public ArrayList<T> readAll() {

        try (Reader readerToUse = this.reader) {

            ArrayList<T> results = new ArrayList<>();

            logger.info("Start reading CSV file");
            Iterable<CSVRecord> records = format.parse(readerToUse);

            logger.info("Mapping rows");

            for(CSVRecord record : records) {
                T mappedRow = this.rowMapper.mapRow(record.toMap());
                if (mappedRow != null) {
                    results.add(mappedRow);
                }else{
                    logger.warn("Skipping row " + record.toString());
                }
            }

            logger.info("End reading CSV file");

            return results;

        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }

    }

    @Override
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (parser != null) {
                parser.close();
            }
        } catch (IOException e) {
            logger.error("Error closing CSV reader resources", e);
        }
    }
}
