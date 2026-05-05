package com.nexusretail.common.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for calculating service years and formatting duration strings
 */
public class ServiceYearsCalculator {

    /**
     * Calculate service years from hire date to current date
     * @param hireDate the date when the employee was hired
     * @return formatted string in format "X years, Y months, Z days"
     */
    public static String calculateServiceYears(LocalDate hireDate) {
        if (hireDate == null) {
            return null;
        }

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(hireDate, currentDate);

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return formatDuration(years, months, days);
    }

    /**
     * Calculate service years from hire date to a specific date
     * @param hireDate the date when the employee was hired
     * @param referenceDate the date to calculate service years up to
     * @return formatted string in format "X years, Y months, Z days"
     */
    public static String calculateServiceYears(LocalDate hireDate, LocalDate referenceDate) {
        if (hireDate == null || referenceDate == null) {
            return null;
        }

        Period period = Period.between(hireDate, referenceDate);

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return formatDuration(years, months, days);
    }

    /**
     * Calculate total days of service
     * @param hireDate the date when the employee was hired
     * @return total number of days since hire date
     */
    public static long calculateTotalServiceDays(LocalDate hireDate) {
        if (hireDate == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(hireDate, LocalDate.now());
    }

    /**
     * Calculate total months of service
     * @param hireDate the date when the employee was hired
     * @return total number of months since hire date
     */
    public static long calculateTotalServiceMonths(LocalDate hireDate) {
        if (hireDate == null) {
            return 0;
        }

        return ChronoUnit.MONTHS.between(hireDate, LocalDate.now());
    }

    /**
     * Calculate total years of service
     * @param hireDate the date when the employee was hired
     * @return total number of years since hire date
     */
    public static long calculateTotalServiceYears(LocalDate hireDate) {
        if (hireDate == null) {
            return 0;
        }

        return ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }

    /**
     * Format duration components into a readable string
     * @param years number of years
     * @param months number of months
     * @param days number of days
     * @return formatted string like "2 years, 3 months, 15 days"
     */
    private static String formatDuration(int years, int months, int days) {
        StringBuilder sb = new StringBuilder();

        if (years > 0) {
            sb.append(years).append(" year").append(years == 1 ? "" : "s");
        }

        if (months > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(months).append(" month").append(months == 1 ? "" : "s");
        }

        if (days > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(days).append(" day").append(days == 1 ? "" : "s");
        }

        // If all are 0, return "0 days"
        if (sb.length() == 0) {
            return "0 days";
        }

        return sb.toString();
    }
}
