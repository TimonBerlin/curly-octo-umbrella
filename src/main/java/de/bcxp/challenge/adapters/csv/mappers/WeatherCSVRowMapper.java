package de.bcxp.challenge.adapters.csv.mappers;

import de.bcxp.challenge.App;
import de.bcxp.challenge.adapters.ConfigurableRowMapper;
import de.bcxp.challenge.adapters.RowMapper;
import de.bcxp.challenge.weather.WeatherEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class WeatherCSVRowMapper extends ConfigurableRowMapper<WeatherEntity> {

    private static final Logger logger = LogManager.getLogger(WeatherCSVRowMapper.class);

    public WeatherCSVRowMapper(boolean skipInvalidRows) {
        super(skipInvalidRows);
    }

    @Override
    public WeatherEntity mapRow(Map<String, String> row) {

        if (!validateRow(row)) {

            if (this.skipInvalidRows) {
                logger.warn("Invalid row data: " + row);
                return null;
            } else {
                throw new IllegalArgumentException("Invalid row data: " + row);
            }

        }

        WeatherEntity entity = new WeatherEntity();

        entity.Day = Integer.parseInt(row.get("Day"));
        entity.MxT = Integer.parseInt(row.get("MxT"));
        entity.MnT = Integer.parseInt(row.get("MnT"));
        entity.AvT = Integer.parseInt(row.get("AvT"));
        entity.AvDP = Double.parseDouble(row.get("AvDP"));
        entity.oneHrP_TPcpn = Integer.parseInt(row.get("1HrP TPcpn"));
        entity.PDir = Integer.parseInt(row.get("PDir"));
        entity.AvSp = Double.parseDouble(row.get("AvSp"));
        entity.Dir = Integer.parseInt(row.get("Dir"));
        entity.MxS = Integer.parseInt(row.get("MxS"));
        entity.SkyC = Double.parseDouble(row.get("SkyC"));
        entity.MxR = Integer.parseInt(row.get("MxR"));
        entity.Mn = Integer.parseInt(row.get("Mn"));
        entity.R_AvSLP = Double.parseDouble(row.get("R AvSLP"));

        return entity;
    }

    @Override
    public boolean validateRow(Map<String, String> row) {
        if (row == null || row.isEmpty()) {
            return false;
        }

        // Check if all required columns are present
        String[] requiredColumns = {
                "Day", "MxT", "MnT", "AvT", "AvDP", "1HrP TPcpn",
                "PDir", "AvSp", "Dir", "MxS", "SkyC", "MxR", "Mn", "R AvSLP"
        };

        for (String column : requiredColumns) {
            if (!row.containsKey(column) || row.get(column) == null || row.get(column).trim().isEmpty()) {
                return false;
            }
        }

        try {
            // Validate Day (should be between 1 and 31)
            int day = Integer.parseInt(row.get("Day").trim());
            if (day < 1 || day > 31) {
                return false;
            }

            // Validate temperatures (reasonable range: -50 to 150 Fahrenheit)
            int maxTemp = Integer.parseInt(row.get("MxT").trim());
            int minTemp = Integer.parseInt(row.get("MnT").trim());
            int avgTemp = Integer.parseInt(row.get("AvT").trim());

            if (maxTemp < -50 || maxTemp > 150 || minTemp < -50 || minTemp > 150 || avgTemp < -50 || avgTemp > 150) {
                return false;
            }

            // Maximum temperature should be >= minimum temperature
            if (maxTemp < minTemp) {
                return false;
            }

            // Validate double values
            double avDP = Double.parseDouble(row.get("AvDP").trim());
            double avSp = Double.parseDouble(row.get("AvSp").trim());
            double skyC = Double.parseDouble(row.get("SkyC").trim());
            double rAvSLP = Double.parseDouble(row.get("R AvSLP").trim());


            /*

            Not sure if these checks are needed, but keeping them commented out for now

            // Wind speed should be non-negative
            if (avSp < 0) {
                return false;
            }

            // Sky coverage should be between 0 and 100 (percentage)
            if (skyC < 0 || skyC > 200) { // allowing some tolerance for unusual values
                return false;
            }

            // Atmospheric pressure should be reasonable (900-1100 hPa)
            if (rAvSLP < 900 || rAvSLP > 1100) {
                return false;
            }


             */

            // Validate integer values
            int oneHrP = Integer.parseInt(row.get("1HrP TPcpn").trim());
            int pDir = Integer.parseInt(row.get("PDir").trim());
            int dir = Integer.parseInt(row.get("Dir").trim());
            int mxS = Integer.parseInt(row.get("MxS").trim());
            int mxR = Integer.parseInt(row.get("MxR").trim());
            int mn = Integer.parseInt(row.get("Mn").trim());


            /*
            Not sure if these checks are needed, but keeping them commented out for now

            // Wind directions should be between 0 and 360 degrees
            if (pDir < 0 || pDir > 360 || dir < 0 || dir > 360) {
                return false;
            }

            // Wind speeds should be non-negative
            if (mxS < 0) {
                return false;
            }

            // Precipitation should be non-negative
            if (oneHrP < 0) {
                return false;
            }
            */


        } catch (NumberFormatException e) {
            // If any value cannot be parsed as expected type, row is invalid
            return false;
        }

        return true;
    }
}
