package de.bcxp.challenge.weather;

public class WeatherEntity {
    /** Day number */
    public int Day;

    /** Maximum temperature */
    public int MxT;

    /** Minimum temperature */
    public int MnT;

    public int AvT;

    public double AvDP;

    public int oneHrP_TPcpn;

    public int PDir;

    public double AvSp;

    public int Dir;

    public int MxS;

    public double SkyC;

    public int MxR;

    public int Mn;

    public double R_AvSLP;

    public float getTemperatureSpread() {
        return MxT - MnT;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "Day=" + Day +
                ", MxT=" + MxT +
                ", MnT=" + MnT +
                ", AvT=" + AvT +
                ", AvDP=" + AvDP +
                ", oneHrP_TPcpn=" + oneHrP_TPcpn +
                ", PDir=" + PDir +
                ", AvSp=" + AvSp +
                ", Dir=" + Dir +
                ", MxS=" + MxS +
                ", SkyC=" + SkyC +
                ", MxR=" + MxR +
                ", Mn=" + Mn +
                ", R_AvSLP=" + R_AvSLP +
                '}';
    }
}
