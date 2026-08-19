package com.savebite.util;

public final class ValidationUtil {

    private ValidationUtil() {
        // Utility class - prevent object creation
    }

    public static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        return value.trim();
    }

    public static double parsePositiveDouble(
            String value,
            String fieldName) {

        try {

            double number =
                    Double.parseDouble(value.trim());

            if (number <= 0) {
                throw new IllegalArgumentException(
                        fieldName
                                + " must be greater than zero."
                );
            }

            return number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    fieldName + " must be a valid number."
            );
        }
    }

    public static int parsePositiveInt(
            String value,
            String fieldName) {

        try {

            int number =
                    Integer.parseInt(value.trim());

            if (number <= 0) {
                throw new IllegalArgumentException(
                        fieldName
                                + " must be greater than zero."
                );
            }

            return number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be a whole number."
            );
        }
    }

    public static double parseDiscount(
            String value) {

        try {

            double discount =
                    Double.parseDouble(value.trim());

            if (discount < 0 || discount > 100) {

                throw new IllegalArgumentException(
                        "Discount must be between 0 and 100."
                );
            }

            return discount;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Discount must be a valid number."
            );
        }
    }
}