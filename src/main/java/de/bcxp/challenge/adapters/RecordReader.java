package de.bcxp.challenge.adapters;

import java.util.List;

public interface RecordReader<T> {

    List<T> readAll(String filePath);

}
