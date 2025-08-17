package de.bcxp.challenge.adapters.csv;

import de.bcxp.challenge.adapters.RecordReader;
import de.bcxp.challenge.adapters.RowMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class CSVRecordReader<T> implements RecordReader<T> {

    private final RowMapper<T> rowMapper;
    private final CSVFormat format;

    private static final Logger logger = LogManager.getLogger(CSVRecordReader.class);


    public CSVRecordReader(RowMapper<T> rowMapper, CSVFormat format) {
        this.rowMapper = rowMapper;
        this.format = format;
    }

    @Override
    public ArrayList<T> readAll(String filePath) {

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        if(!(new File(filePath).exists())) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }

        try (Reader readerToUse = new FileReader(filePath)) {

            ArrayList<T> results = new ArrayList<>();

            logger.info("Start reading CSV file {}", filePath);
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

}
