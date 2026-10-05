package group1.fishStreet;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.*;

public class Cemetery {

    private static final Pattern LINE = Pattern.compile(
            "^(.+?)\\s+(\\d{1,2})\\s+([A-Za-z]{3,9})\\.?\\s+(\\d{4})\\s+(\\d+(?:\\.\\d+)?[wdmWDM]?)\\s*(.*)$");

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

    static Tombstone parseLine(String line) {
        Matcher m = LINE.matcher(line.trim());
        if (!m.matches()) {
            return null;
        }
        int month = MONTHS.indexOf(m.group(3).substring(0, 3).toLowerCase());
        if (month < 0 || month % 3 != 0) {
            return null;
        }
        try {
            LocalDate date = LocalDate.of(Integer.parseInt(m.group(4)), month / 3 + 1,
                    Integer.parseInt(m.group(2)));
            return new Tombstone(m.group(1), date, m.group(5), m.group(6));
        } catch (RuntimeException e) {
            return null;
        }
    }

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

    public static long averageAgeDays(List<Tombstone> people) {
        if (people.isEmpty()) {
            return 0;
        }
        long total = 0;
        for (Tombstone t : people) {
            total += t.getAgeInDays();
        }
        return Math.round((double) total / people.size());
    }
}