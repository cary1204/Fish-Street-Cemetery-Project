package group5.fishStreet;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.*;

public class Cemetery {
    
    // used a pattern to read from the pattern of the text file, and a numeric pattern to read from the numeric format of the text file
    private static final Pattern LINE = Pattern.compile(
            "^(.+?)\\s+(\\d{1,2})\\s+([A-Za-z]{3,9})\\.?\\s+(\\d{4})(?:\\s+(\\d+(?:\\.\\d+)?[wdmWDM]?)(?=\\s|$))?\\s*(.*)$");

    private static final String MONTHS = "janfebmaraprmayjunjulaugsepoctnovdec";

    private final TreeSet<Tombstone> tombstones = new TreeSet<>();
    private int linesRead = 0;
    private int linesSkipped = 0;
    private int duplicatesRemoved = 0;


    public void load(String fileName) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                linesRead++;
                Tombstone t = parseLine(line);
                if (t == null) {
                    linesSkipped++;
                } else if (!tombstones.add(t)) {
                    duplicatesRemoved++;
                }
            }
        }
    }

        // also a pettern to read from the numeric format of the text file, and a method to parse the line into a Tombstone object
    private static final Pattern NUMERIC_LINE = Pattern.compile(
            "^(.+?)\\s+(\\d{1,2})[/-](\\d{1,2})[/-](\\d{4})(?:\\s+(\\d+(?:\\.\\d+)?[wdmWDM]?)(?=\\s|$))?\\s*(.*)$");

    static Tombstone parseLine(String line) {
        String text = line.trim();
        try {
            Matcher n = NUMERIC_LINE.matcher(text);
            if (n.matches()) {
                LocalDate date = LocalDate.of(Integer.parseInt(n.group(4)),
                        Integer.parseInt(n.group(2)), Integer.parseInt(n.group(3)));
                return new Tombstone(n.group(1), date, n.group(5) == null ? "" : n.group(5), n.group(6));
            }
            Matcher m = LINE.matcher(text);
            if (!m.matches()) {
                return null;
            }
            int month = MONTHS.indexOf(m.group(3).substring(0, 3).toLowerCase());
            if (month < 0 || month % 3 != 0) {
                return null;
            }
            LocalDate date = LocalDate.of(Integer.parseInt(m.group(4)), month / 3 + 1,
                    Integer.parseInt(m.group(2)));
            return new Tombstone(m.group(1), date, m.group(5) == null ? "" : m.group(5), m.group(6));
        } catch (RuntimeException e) {
            return null;
        }
    }
    // methods to get the size of the tombstones, lines read, lines skipped, and duplicates removed
    public int size() { return tombstones.size(); }
    public int getLinesRead() { return linesRead; }
    public int getLinesSkipped() { return linesSkipped; }
    public int getDuplicatesRemoved() { return duplicatesRemoved; }

    public List<Tombstone> all() {
        return new ArrayList<>(tombstones);
    }

    public List<Tombstone> buriedOn(LocalDate date) {
        return range(date, date, null);
    }
    // method to find all burials in a date range, with optional location
    public List<Tombstone> range(LocalDate start, LocalDate end, String location) {
        List<Tombstone> result = new ArrayList<>();
        if (start.isAfter(end)) {
            return result;
        }
        String q = (location == null) ? "" : location.trim().toLowerCase();
        for (Tombstone t : tombstones) {
            LocalDate d = t.getBurialDate();
            if (d.isBefore(start)) continue;
            if (d.isAfter(end)) break;
            if (q.isEmpty() || t.getLocation().toLowerCase().contains(q)) {
                result.add(t);
            }
        }
        return result;
    }
    // method to calculate the average age in days of a list of Tombstone objects
    public static long averageAgeDays(List<Tombstone> people) {
        long total = 0;
        int counted = 0;
        for (Tombstone t : people) {
            if (t.hasAge()) {
                total += t.getAgeInDays();
                counted++;
            }
        }
        return counted == 0 ? -1 : Math.round((double) total / counted);
    }
}