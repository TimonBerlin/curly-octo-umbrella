package de.bcxp.challenge.adapters;

import java.util.Map;



public interface RowMapper<T> {

    T mapRow(Map<String,String> row);

    boolean isValidRow(Map<String,String> row);
}
