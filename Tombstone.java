package group1.fishStreet;

import java.time.LocalDate;

public class Tombstone implements Comparable<Tombstone> {

    public static final int DAYS_PER_MONTH = 30;
    public static final int DAYS_PER_YEAR = 360;

    private static final String[] MONTH_NAMES = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final String name;
    private final LocalDate burialDate;
    private final String ageText;
    private final long ageInDays;
    private final String location;

    public Tombstone(String name, LocalDate burialDate, String ageText, String location) {
        this.name = name.trim();
        this.burialDate = burialDate;
        this.ageText = ageText.trim();
        this.ageInDays = parseAgeToDays(this.ageText);
        this.location = location.trim();
    }

    public static long parseAgeToDays(String raw) {
        String s = raw.trim().toLowerCase();
        if (s.isEmpty()) {
            return 0;
        }
        try {
            char last = s.charAt(s.length() - 1);
            if (last == 'd') {
                return Long.parseLong(s.substring(0, s.length() - 1).trim());
            }
            if (last == 'w') {
                return 7L * Long.parseLong(s.substring(0, s.length() - 1).trim());
            }
            if (last == 'm') {
                return (long) DAYS_PER_MONTH * Long.parseLong(s.substring(0, s.length() - 1).trim());
            }
            int dot = s.indexOf('.');
            if (dot < 0) {
                return (long) DAYS_PER_YEAR * Long.parseLong(s);
            }
            String yearPart = s.substring(0, dot).trim();
            String monthPart = s.substring(dot + 1).trim();
            long years = yearPart.isEmpty() ? 0 : Long.parseLong(yearPart);
            long months = monthPart.isEmpty() ? 0 : Long.parseLong(monthPart);
            return years * DAYS_PER_YEAR + months * DAYS_PER_MONTH;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unrecognised age: " + raw);
        }
    }

    public static String formatAge(long totalDays) {
        long years = totalDays / DAYS_PER_YEAR;
        long months = (totalDays % DAYS_PER_YEAR) / DAYS_PER_MONTH;
        long days = totalDays % DAYS_PER_MONTH;
        return years + " years, " + months + " months and " + days + " days";
    }

    public String getName() { return name; }
    public LocalDate getBurialDate() { return burialDate; }
    public String getAgeText() { return ageText; }
    public long getAgeInDays() { return ageInDays; }
    public String getLocation() { return location; }

    @Override
    public int compareTo(Tombstone o) {
        int c = burialDate.compareTo(o.burialDate);
        if (c != 0) return c;
        c = name.compareToIgnoreCase(o.name);
        if (c != 0) return c;
        c = Long.compare(ageInDays, o.ageInDays);
        if (c != 0) return c;
        return location.compareToIgnoreCase(o.location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tombstone)) return false;
        return compareTo((Tombstone) obj) == 0;
    }

    @Override
    public int hashCode() {
        int h = burialDate.hashCode();
        h = 31 * h + name.toLowerCase().hashCode();
        h = 31 * h + Long.hashCode(ageInDays);
        h = 31 * h + location.toLowerCase().hashCode();
        return h;
    }

    @Override
    public String toString() {
        String date = String.format("%02d %s %d", burialDate.getDayOfMonth(),
                MONTH_NAMES[burialDate.getMonthValue() - 1], burialDate.getYear());
        return String.format("%-28s %-12s %-8s %s", name, date, ageText, location);
    }
}