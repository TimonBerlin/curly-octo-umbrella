package de.bcxp.challenge.countries;

public class CountryEntity {
    public String Name;

    public String Capital;

    public String Accession;

    /** Population count */
    public long Population;

    /** Area in square kilometers */
    public double Area;

    public long GDP;

    public double HDI;

    public int MEPs;

    public double getPopulationDensity() {
        if (Area == 0) {
            return 0;
        }
        return Population / Area;
    }

    @Override
    public String toString() {
        return "CountryEntity{" +
                "Name='" + Name + '\'' +
                ", Capital='" + Capital + '\'' +
                ", Accession='" + Accession + '\'' +
                ", Population=" + Population +
                ", Area=" + Area +
                ", GDP=" + GDP +
                ", HDI=" + HDI +
                ", MEPs=" + MEPs +
                '}';
    }
}
