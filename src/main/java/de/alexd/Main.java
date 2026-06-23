package de.alexd;

import java.io.IOException;
import java.time.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BodyCsv csv = new BodyCsv("body.csv");
        Scanner scanner = new Scanner(System.in);

        double height = enterDoubleData(scanner, "Enter your current height in cm");
        double weight = enterDoubleData(scanner, "Enter your current weight in kg");
        double neck = enterDoubleData(scanner, "Enter your neck circumference in cm");
        double waist = enterDoubleData(scanner, "Enter your waist circumference in cm");



        try {
            // Datensatz hinzufügen
            csv.appendEntry(
                    LocalDate.now(ZoneId.of("Europe/Berlin")),
                    height,
                    weight,
                    neck,
                    waist
            );

            // Alle Datensätze lesen
            for (BodyCsv.Entry e : csv.readEntries()) {
                System.out.println(e);
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Eingabefehler: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Dateifehler: " + ex.getMessage());
        }
    }

    private static double enterDoubleData(Scanner scanner, String msg) {
        while (true) {
            System.out.print(msg + ": ");
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);
                if (!Double.isFinite(value) || value <= 0) {
                    System.out.println("Please enter a number higher than 0.");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid entry. Please entry an Decimal (z. B. 181.5).");
            }
        }
    }
}