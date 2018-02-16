/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package database.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;

public class DataScraper {


    private static final ItemDefinition[] defs;
    private static final Gson builder = new GsonBuilder().setPrettyPrinting().create();

    static {

        String fileString = null;
        try {
            fileString = new String(Files.readAllBytes(new File("./data_dumped").toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        defs = DataScraper.builder.fromJson(fileString, ItemDefinition[].class);

    }

    public static void dump_rs_wiki() {

        for (int i = 0; i < DataScraper.defs.length; i++) {

            if (DataScraper.defs[i].name.equals("null")) {
                continue;
            }


            BufferedReader reader = null;
            try {
                URL url = new URL("http://oldschoolrunescape.wikia.com/wiki/"
                        + DataScraper.defs[i].name.replaceAll(" ", "_").replaceAll("'", "%27"));
                reader = new BufferedReader(new InputStreamReader(url.openStream()));


                String line;
                int bonusCount = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Members\" title=\"Members\">Members only</a>?")) {
                        String next = reader.readLine();
                        if (next != null) {
                            if (next.toLowerCase().contains("yes")) {
                                DataScraper.defs[i].membersOnly = true;
                            } else {
                                DataScraper.defs[i].membersOnly = false;
                            }
                        }
                    }

                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Quest_items\" title=\"Quest items\">Quest item</a>?")) {
                        String next = reader.readLine();
                        if (next != null) {
                            if (next.toLowerCase().contains("yes")) {
                                DataScraper.defs[i].questItem = true;
                            } else {
                                DataScraper.defs[i].questItem = false;
                            }
                        }
                    }

                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Tradeable\" title=\"Tradeable\" class=\"mw-redirect\">Tradeable</a>?")) {
                        String next = reader.readLine();
                        if (next != null) {
                            if (next.toLowerCase().contains("yes")) {
                                DataScraper.defs[i].tradeable = true;
                            } else {
                                DataScraper.defs[i].tradeable = false;
                            }
                        }
                    }

                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Equipment\" title=\"Equipment\">Equipable</a>?")) {
                        String next = reader.readLine();
                        if (next != null) {
                            if (next.toLowerCase().contains("yes")) {
                                DataScraper.defs[i].equipable = true;
                            } else {
                                DataScraper.defs[i].equipable = false;
                            }
                        }
                    }

                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Stackable_items\" title=\"Stackable items\">Stackable</a>?")) {
                        String next = reader.readLine();
                        if (next != null) {
                            if (next.toLowerCase().contains("yes")) {
                                DataScraper.defs[i].stackable = true;
                            } else {
                                DataScraper.defs[i].stackable = false;
                            }
                        }
                    }

                    if (!DataScraper.defs[i].name.contains("potion") && line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/High_Level_Alchemy\" title=\"High Level Alchemy\">High Alch</a>")) {
                        String next = reader.readLine();
                        if (next != null) {

                            String val = next.replaceFirst("</th><td>\\s?\\d+&?#", "").replaceFirst("</th><td>\\s?\\d+,\\d+&?#?", "").replace("</th><td> 0&#", "")
                                    .replace(";", "").replace("</th><td> 3&#", "").replace("coins", "").replace("coin", "").trim();

                            try {
                                DataScraper.defs[i].highAlchValue = Integer.valueOf(val);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (!DataScraper.defs[i].name.contains("potion") && line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Low_Level_Alchemy\" title=\"Low Level Alchemy\">Low Alch</a>")) {
                        String next = reader.readLine();
                        if (next != null) {

                            String val = next.replaceFirst("</th><td>\\s?\\d+&?#", "").replaceFirst("</th><td>\\s?\\d+,\\d+&?#?", "").replace("</th><td> 0&#", "")
                                    .replace(";", "").replace("</th><td> 3&#", "").replace("coins", "").replace("coin", "").trim();

                            try {
                                DataScraper.defs[i].lowAlchValue = Integer.valueOf(val);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Prices#Store_Price\" title=\"Prices\">Store price</a>")) {
                        String next = reader.readLine();
                        if (next != null) {

                            String val = next.replaceFirst("</th><td>\\s?\\d+&?#", "").replaceFirst("</th><td>\\s?\\d+,\\d+&?#?", "").replace("</th><td> 0&#", "")
                                    .replace("</th><td> 3&#", "").replace("coins", "").replace("coin", "").replaceAll(";.*", "").replace("</th><td>", "").trim();


                            if (val.toLowerCase().trim().equals("not sold")) {
                                DataScraper.defs[i].sellable = false;
                            } else {

                                try {
                                    DataScraper.defs[i].buyPrice = Integer.valueOf(val);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if (line.contains("<th style=\"white-space: nowrap;\"><a href=\"/wiki/Weight\" title=\"Weight\">Weight</a>")) {
                        String next = reader.readLine();
                        if (next != null) {

                            String val = next.replaceFirst("</th><td>\\s?\\d+\\s?(\\.)?(\\d+)?&?#", "").replaceFirst("</th><td>\\s?\\d+,\\d+&?#?", "").replace("</th><td> 0&#", "")
                                    .replace(";", "").replace("</th><td> 3&#", "").replace("coins", "").replace("coin", "").replace("kg", "").trim();

                            try {
                                DataScraper.defs[i].weight = Integer.valueOf(val);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    if (line.contains("<td colspan=\"2\" style=\"padding:3px 7px 3px 7px; line-height:140%; text-align:center;\">")) {
                        String examine = line.replace("<td colspan=\"2\" style=\"padding:3px 7px 3px 7px; line-height:140%; text-align:center;\">", "");

                        DataScraper.defs[i].examine = examine;
                    }


                   /*   HEAD(0),
                    CAPE(1),
                    AMULET(2),
                    WEAPON(3),
                    CHEST(4),
                    SHIELD(5),
                    LEGS(7),
                    HANDS(9),
                    FEET(10),
                    RING(12),
                    ARROWS(13);*/


                    if (line.contains("<a href=\"/wiki/Head_slot\"")) {
                        DataScraper.defs[i].slotId = 0;

                    }

                    if (line.contains("<a href=\"/wiki/Cape_slot\"")) {
                        DataScraper.defs[i].slotId = 1;
                        System.out.println("cape");
                    }

                    if (line.contains("<a href=\"/wiki/Neck_slot\"")) {
                        DataScraper.defs[i].slotId = 2;
                        System.out.println("neck");
                    }

                    if (line.contains("<a href=\"/wiki/Two-handed_slot\"")) {
                        DataScraper.defs[i].doubleHanded = true;
                        DataScraper.defs[i].slotId = 3;
                        System.out.println("two handed!");
                    }

                    if (line.contains("<a href=\"/wiki/Body_slot\"")) {
                        DataScraper.defs[i].slotId = 4;
                        System.out.println("chest");
                    }

                    if (line.contains("<a href=\"/wiki/Shield_slot\"")) {
                        DataScraper.defs[i].slotId = 5;
                        System.out.println("shield");
                    }

                    if (line.contains("<a href=\"/wiki/Legwear_slot\"")) {
                        DataScraper.defs[i].slotId = 7;
                        System.out.println("legs");
                    }

                    if (line.contains("<a href=\"/wiki/Hand_slot\"")) {
                        DataScraper.defs[i].slotId = 9;
                        System.out.println("hands");
                    }

                    if (line.contains("<a href=\"/wiki/Feet_slot\"")) {
                        DataScraper.defs[i].slotId = 10;
                        System.out.println("feet");
                    }

                    if (line.contains("<a href=\"/wiki/Ring_slot\"")) {
                        DataScraper.defs[i].slotId = 12;
                        System.out.println("ring");
                    }

                    if (line.contains("<a href=\"/wiki/Ammunition_slot")) {
                        DataScraper.defs[i].slotId = 13;
                        System.out.println("ammunition");
                    }

                    //attack bonuses
                    //stab 0
                    //slash 1
                    //crush 2
                    //magic 3
                    //ranged 4

                    //defence bonuses
                    //stab 5
                    //slash 6
                    //crash 7
                    //magic 8
                    //ranged 9

                    //other
                    //strength: 10
                    //ranged: 11
                    //magic: 12
                    //prayer: 13

                    if (line.contains("<td style=\"text-align: center; width: 35px;\">") || line.contains("</td><td style=\"text-align: center; width: 35px;\">")) {
                        String val = line.replace("<td style=\"text-align: center; width: 35px;\">", "")
                                .replace("</td><td style=\"text-align: center; width: 35px;\">", "").replace("</td>", "").replace("+", "");

                        try {
                            if (bonusCount < DataScraper.defs[i].bonuses.length) {
                                DataScraper.defs[i].bonuses[bonusCount++] = Integer.valueOf(val);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void dump_rs_help() throws IOException {
        for (int i = 0; i < DataScraper.defs.length; i++) {

            if (DataScraper.defs[i].name.equals("null")) {
                continue;
            }


            URL url = new URL("http://2007rshelp.com/items.php?id=" + DataScraper.defs[i].id);
            System.out.println(DataScraper.defs[i].id);
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

            con.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            con.setRequestProperty("SimpleCache-Control", "max-age=0");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Cookie", "__cfduid=d834ad0aa8f8e5d7ba1b3968dee9135881517955301; zybezskin=bluelight; __utmc=229830303; __utmz=229830303.1517955306.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __utma=229830303.1219473518.1517955304.1517955304.1517955304.1; __qca=P0-1710673473-1517955305622; __utmt=1; __utmt_b=1; __utmb=229830303.36.10.1517955306");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            con.setRequestProperty("Accept-Charset", "utf-8");
            con.setRequestProperty("SimpleCache-Control", "no-cache");
            con.setRequestProperty("Content-Length", "0");
            con.setRequestProperty("Host", "2007rshelp.com");
            con.setRequestProperty("DNT", "1");

            BufferedReader r = null;
            InputStream err = null;
            try {

                r = new BufferedReader(new InputStreamReader(new BufferedInputStream(con.getInputStream())));
            } catch (IOException e) {

                e.printStackTrace();
            }

            StringBuilder s = new StringBuilder();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    s.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            Document d = Jsoup.parse(s.toString());
            Elements e = d.getElementsByAttributeValue("style", "width:76%;margin:0 12%;border:1px solid #000;border-top:none");
            Element ele = e.first();
            if (ele != null) {
                Elements tdEles = ele.getElementsByTag("td");
                Iterator<Element> tds = tdEles.iterator();
                for (; tds.hasNext(); ) {
                    String tdText = tds.next().text().trim().toLowerCase();
                    if (tdText.equals("members:")) {
                        if (tds.hasNext()) {
                            String innerText = tds.next().text().trim().toLowerCase();
                            if (innerText.equals("yes")) {
                                DataScraper.defs[i].membersOnly = true;
                            } else if (innerText.equals("no")) {
                                DataScraper.defs[i].membersOnly = false;
                            }
                        }
                    }

                    if (tdText.equals("tradable:")) {
                        if (tds.hasNext()) {
                            String innerText = tds.next().text().trim().toLowerCase();
                            if (innerText.equals("yes")) {
                                DataScraper.defs[i].tradeable = true;
                            } else if (innerText.equals("no")) {
                                DataScraper.defs[i].tradeable = false;
                            }
                        }
                    }

                    if (tdText.equals("stackable:")) {
                        if (tds.hasNext()) {
                            String innerText = tds.next().text().trim().toLowerCase();
                            if (innerText.equals("yes")) {
                                DataScraper.defs[i].stackable = true;
                            } else if (innerText.equals("no")) {
                                DataScraper.defs[i].stackable = false;
                            }
                        }
                    }

                    if (tdText.equals("equipable:")) {
                        if (tds.hasNext()) {
                            String innerText = tds.next().text().trim().toLowerCase();
                            if (innerText.equals("yes")) {
                                DataScraper.defs[i].equipable = true;
                            } else if (innerText.equals("no")) {
                                DataScraper.defs[i].equipable = false;
                            }
                        }
                    }

                }
            }

        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("./data_dumped2");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            DataScraper.dump_rs_help();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter w = null;
        try {
            w = new BufferedWriter(new FileWriter(file));
            DataScraper.builder.toJson(DataScraper.defs, ItemDefinition[].class, w);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            w.flush();
            w.close();
        }
    }

    public static class NpcDefinition {
        private final boolean attackable = false;
        private final boolean retreats = false;
        private final boolean aggressive = false;
        private final boolean poisinous = false;
        private final int respawn = 20;
        private final int maxHit = 0;
        private final int attackSpeed = 6;
        private final int[] stats = new int[18];
        private final String[] combatTypes = new String[6];
        private int id;
        private String name;
        private String examine;
        private int size;
        private int walkRadius;
        private int hitpoints;
        private int attackAnim;
        private int defenceAnim;
        private int deathAnim;
        private int combatLevel;
        private int slayerLevel;
        private int combatFollowDistance;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getExamine() {
            return examine;
        }

        public int getSize() {
            return size;
        }

        public int getWalkRadius() {
            return walkRadius;
        }

        public boolean isAttackable() {
            return attackable;
        }

        public boolean isRetreats() {
            return retreats;
        }

        public boolean isAggressive() {
            return aggressive;
        }

        public boolean isPoisinous() {
            return poisinous;
        }

        public int getRespawn() {
            return respawn;
        }

        public int getHitpoints() {
            return hitpoints;
        }

        public int getMaxHit() {
            return maxHit;
        }

        public int getAttackSpeed() {
            return attackSpeed;
        }

        public int getAttackAnim() {
            return attackAnim;
        }

        public int getDefenceAnim() {
            return defenceAnim;
        }

        public int getDeathAnim() {
            return deathAnim;
        }

        public int getCombatLevel() {
            return combatLevel;
        }

        public int[] getStats() {
            return stats;
        }

        public int getSlayerLevel() {
            return slayerLevel;
        }

        public int getCombatFollowDistance() {
            return combatFollowDistance;
        }

        public String[] getCombatTypes() {
            return combatTypes;
        }
    }

    public static class ItemDefinition {

        private final int[] bonuses = new int[14];
        private int id;
        private String name;
        private String examine;
        private int buyPrice;
        private int sellPrice;
        private boolean stackable;
        private boolean noted;
        private int noteId;
        private boolean doubleHanded;
        private String equipmentType;
        private boolean tradeable;
        private boolean sellable;
        private boolean dropable;
        private boolean membersOnly;
        private boolean questItem;
        private int highAlchValue;
        private int lowAlchValue;
        private int weight;
        private boolean equipable;
        private int slotId;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getExamine() {
            return examine;
        }

        public int getBuyPrice() {
            return buyPrice;
        }

        public boolean isStackable() {
            return stackable;
        }

        public boolean isNoted() {
            return noted;
        }

        public int getNoteId() {
            return noteId;
        }

        public boolean isDoubleHanded() {
            return doubleHanded;
        }

        public String getEquipmentType() {
            return equipmentType;
        }

        public boolean isTradeable() {
            return tradeable;
        }

        public boolean isSellable() {
            return sellable;
        }

        public boolean isDropable() {
            return dropable;
        }

        public int[] getBonuses() {
            return bonuses;
        }
    }
}

