package de.bcxp.challenge.weather;

import org.apache.commons.csv.CSVFormat;
import java.util.Comparator;

public class WeatherUtils {

    /**
     * Comparator to sort WeatherEntity objects by temperature spread in ascending order.
     * The entity with the smallest temperature spread will be at index 0.
     */
    public static final Comparator<WeatherEntity> TEMPERATURE_SPREAD_COMPARATOR = 
            Comparator.comparing(WeatherEntity::getTemperatureSpread);

    public static CSVFormat WEATHER_CSV_FORMAT = CSVFormat.Builder.create()
            .setHeader("Day", "MxT", "MnT", "AvT", "AvDP", "1HrP TPcpn", "PDir", "AvSp", "Dir", "MxS", "SkyC", "MxR", "Mn", "R AvSLP")
            .setSkipHeaderRecord(true)
            .setDelimiter(',')
            .get();
}
