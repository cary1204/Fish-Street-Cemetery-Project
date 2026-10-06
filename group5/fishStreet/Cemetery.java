package group5.fishStreet;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.*;

public class Cemetery {

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