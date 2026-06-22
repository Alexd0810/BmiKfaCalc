package de.alexd;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        BodyCsv csv = new BodyCsv("body.csv");

        double height;
        double weight;
        double neck;
        double waist;



        // Datensatz hinzufügen
        csv.appendEntry(
                LocalDate.now(ZoneId.of("Europe/Berlin")),
                180,
                99,
                40,
                86
        );

        // Alle Datensätze lesen
        for (BodyCsv.Entry e : csv.readEntries()) {
            System.out.println(e);
        }
    }
}