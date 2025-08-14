package de.bcxp.challenge.adapters;

import java.util.Map;



public interface RowMapper<T> {

    T mapRow(Map<String,String> row);

    boolean validateRow(Map<String,String> row);
}
