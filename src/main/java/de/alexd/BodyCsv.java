package de.alexd;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.log10;

public class BodyCsv {

    private static final String HEADER =
            "Datum,Körpergröße,Gewicht,Hals,Bauchumfang,KFA-NAVY,BMI";

    private final Path datei;

    public BodyCsv(String dateiname) {
        this.datei = Path.of(dateiname);
    }

    /**
     * Erstellt die CSV-Datei inklusive Kopfzeile, falls sie nicht existiert.
     */
    public void createIfNotExists() throws IOException {
        if (!Files.exists(datei)) {
            Files.writeString(datei, HEADER + System.lineSeparator());
        }
    }

    /**
     * Fügt einen neuen Datensatz an.
     */
    public void appendEntry(LocalDate datum, double height, double weight, double neck, double waist) throws IOException {

        createIfNotExists();

        double kfaNavy = calcKfa(height, neck, waist);
        double bmi = calcBmi(height, weight);

        String zeile = String.format(Locale.US, "%s,%.1f,%.1f,%.1f,%.1f,%.2f,%.2f%n",datum,height,weight,neck,waist,kfaNavy,bmi);

        Files.writeString(
                datei,
                zeile,
                StandardOpenOption.APPEND
        );
    }

    /**
     * Liest alle Einträge (ohne Kopfzeile).
     */
    public List<Entry> readEntries() throws IOException {

        createIfNotExists();

        List<Entry> list = new ArrayList<>();

        List<String> zeilen = Files.readAllLines(datei);

        for (int i = 1; i < zeilen.size(); i++) {

            String[] s = zeilen.get(i).split(",");

            if (s.length != 7) {
                continue;
            }

            list.add(new Entry(
                    LocalDate.parse(s[0]),
                    Double.parseDouble(s[1]),
                    Double.parseDouble(s[2]),
                    Double.parseDouble(s[3]),
                    Double.parseDouble(s[4]),
                    Double.parseDouble(s[5]),
                    Double.parseDouble(s[6])
            ));
        }

        return list;
    }

    private double calcBmi(double heightCm, double weightKg) {

        if (heightCm <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException("Größe und Gewicht müssen größer als 0 sein.");
        }

        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    private double calcKfa(double heightCm, double neckCm, double waistCm) {

        if (waistCm <= neckCm) {
            throw new IllegalArgumentException(
                    "Bauchumfang muss größer als Halsumfang sein."
            );
        }

        return 86.010 * log10(waistCm - neckCm)
                - 70.041 * log10(heightCm)
                + 30.30;
    }

    /**
         * Ein Datensatz.
         */
    public record Entry(LocalDate date, double height, double weight, double neck, double waist,
                        double kfaNavy, double bmi) {

    @Override
        public String toString() {
            return "Entry{" +
                    "datum='" + date + '\'' +
                    ", height=" + height +
                    ", gewicht=" + weight +
                    ", hals=" + neck +
                    ", bauchumfang=" + waist +
                    ", kfaNavy=" + kfaNavy +
                    ", bmi=" + bmi +
                    '}';
        }
    }
}