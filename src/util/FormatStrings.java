package util;

public final class FormatStrings {


    private FormatStrings() {

    }


    public static StringBuilder itemRequirement(String skillName, int requiredLevel, String itemName) {
        return new StringBuilder().append("A")
                .append(skillName)
                .append(" level of ")
                .append(requiredLevel)
                .append(" is required to wear ")
                .append(itemName);
    }


}
