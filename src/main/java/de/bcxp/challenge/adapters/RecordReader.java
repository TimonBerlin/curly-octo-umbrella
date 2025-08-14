package de.bcxp.challenge.adapters;

import java.util.ArrayList;
import java.util.List;

public interface RecordReader<T> {

    List<T> readAll();

    /**
     * Closes the reader and releases any resources associated with it.
     */
    void close();

}
