package com.antoniocappiello.cloudapp.service.validator;

public class ItemPropertyValidator {
    public static boolean isNameValid(String itemName) {
        return !itemName.trim().isEmpty();
    }

    public static boolean isDescriptionValid(String itemDescription) {
        return !itemDescription.trim().isEmpty();
    }
}
