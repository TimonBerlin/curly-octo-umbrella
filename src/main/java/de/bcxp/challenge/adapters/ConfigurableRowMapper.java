package de.bcxp.challenge.adapters;

public abstract class ConfigurableRowMapper<T> implements RowMapper<T>{

    public boolean skipInvalidRows = false;

    public ConfigurableRowMapper(boolean skipInvalidRows) {
        // Constructor to allow configuration of the mapper
        this.skipInvalidRows = skipInvalidRows;
    }
}
