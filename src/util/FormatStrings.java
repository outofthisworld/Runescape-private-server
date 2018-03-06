package util;

import world.WorldConfig;
import world.entity.player.Player;

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


    public static StringBuilder welcomeMessage(Player player) {
        return new StringBuilder().append("Welcome to " )
                .append(WorldConfig.SERVER_NAME)
                .append(", currently ")
                .append(player.getWorld().getTotalPlayers())
                .append(" player is online.");
    }

    public static StringBuilder visitWebsite() {
        return new StringBuilder().append("Dont forget to visit our website at ")
                .append(WorldConfig.WEBSITE_URL);
    }
}
