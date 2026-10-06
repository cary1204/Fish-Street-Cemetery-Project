package group5.fishStreet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class FishStreet {
    // displat the month with first 3 letter
    private static final String[] MONTHS = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};

    public static void main(String[] args) {
        String file = args.length > 0 ? args[0] : "cemetery.txt"; //scanner
        Cemetery cemetery = new Cemetery();
        try {
            cemetery.load(file);
        } catch (IOException e) {
            System.out.println("Could not read " + file + ": " + e.getMessage());
            System.out.println("Run from the folder containing cemetery.txt, or pass its path as an argument.");
            return;
        } // some error handling for file reading from stackoverflow
        System.out.println("Loaded " + cemetery.size() + " burials ("
                + cemetery.getDuplicatesRemoved() + " duplicates removed, "
                + cemetery.getLinesSkipped() + " unreadable lines skipped).");

        Scanner in = new Scanner(System.in);
        boolean running = true;
        while (running) { // while loop to keep the program running until user chooses to quit
            System.out.println();
            System.out.println("[1] Specific date");
            System.out.println("[2] Burials in a date range at a location");
            System.out.println("[3] Burials in a date range (any location)");
            System.out.println("[4] Total number of burials");
            System.out.println("[5] All burials in date order");
            System.out.println("[6] Quit");
            System.out.print("Choose an option: ");
            if (!in.hasNextLine()) break;
            switch (in.nextLine().trim()) {
                case "1": byDate(cemetery, in); break;
                case "2": byRange(cemetery, in, true); break;
                case "3": byRange(cemetery, in, false); break;
                case "4": System.out.println("Number of people in the Fishstreet cemetery:  " + cemetery.size()); break;
                case "5":
                    System.out.println("Printout of burials in date order:");
                    print(cemetery.all());
                    break;
                case "6": running = false; break;
                default: System.out.println("Please enter 1-6.");
            }
        }
    }

    private static void byDate(Cemetery c, Scanner in) { //find all burials on a specific date
        System.out.println();
        LocalDate d = askDate(in, "Enter a Date: ");
        if (d == null) return;
        List<Tombstone> found = c.buriedOn(d);
        if (found.isEmpty()) {
            System.out.println("None found");
        } else {
            for (Tombstone t : found) {
                System.out.println(t.getName());
            }
        }
    }

    private static void byRange(Cemetery c, Scanner in, boolean withLocation) {
         //find all burials in a date range, with optional location
         //used boolean to avoid 1 more method
        System.out.println();
        LocalDate start = askDate(in, "Enter start date: ");
        if (start == null) return;
        LocalDate end = askDate(in, "Enter end date: ");
        if (end == null) return;
        if (start.isAfter(end)) {
            System.out.println("The start date must not be after the end date.");
            return;
        }
        String location = null;
        if (withLocation) {
            System.out.print("Enter location: ");
            location = in.hasNextLine() ? in.nextLine().trim() : "";
        }

        List<Tombstone> found = c.range(start, end, location);
        String header = "From (" + Tombstone.longDate(start) + ") to (" + Tombstone.longDate(end) + ")";
        if (withLocation && location != null && !location.isEmpty()) {
            header += " on (" + location + ")";
        }
        System.out.println(header + ":");
        System.out.println("Number of people: " + found.size());
        long avg = Cemetery.averageAgeDays(found);
        if (avg >= 0) {
            System.out.println("Average age: " + Tombstone.formatAge(avg));
        }
    }

    private static LocalDate askDate(Scanner in, String date) {
        // using the treeset to quick find o(1)
        System.out.print(date);
        if (!in.hasNextLine()) return null;
        String text = in.nextLine().trim();
        try {
            if (text.contains("/")) {
                String[] p = text.split("/");
                if (p.length != 3) throw new IllegalArgumentException();
                return LocalDate.of(Integer.parseInt(p[2].trim()),
                        Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim()));
            }
            String[] p = text.split("\\s+");
            if (p.length != 3) throw new IllegalArgumentException();
            int month = Arrays.asList(MONTHS).indexOf(p[1].substring(0, 3).toLowerCase()) + 1;
            return LocalDate.of(Integer.parseInt(p[2]), month, Integer.parseInt(p[0]));
        } catch (RuntimeException e) {
            System.out.println("Invalid date. Use mm/dd/yyyy, e.g. 01/05/1813.");
            return null;
        }
    }

    private static void print(List<Tombstone> list) {
        for (Tombstone t : list) {
            System.out.println(t);
        }
    }
}