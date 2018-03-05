package database.transform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class Transform {

    public static void main(String[] args) {
        CombatSpells[] spells = CombatSpells.values();

        File f = new File("C:\\Users\\blackjem\\Desktop\\rs2beta\\untitled\\src\\database\\transform\\spells.json");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        for (CombatSpells s : spells) {
            stringBuilder.append("{");
            stringBuilder.append("\n");
            CombatNormalSpell cns = (CombatNormalSpell) s.getSpell();

            String spellName = s.name();

            String spellType = "";

            if (cns instanceof CombatAncientSpell) {
                spellType = "ANCIENT";
            } else if (cns instanceof CombatEffectSpell) {
                spellType = "EFFECT";
            } else if (cns instanceof CombatNormalSpell) {
                spellType = "NORMAL";
            }

            stringBuilder.append("\"").append("spellName\":\"").append(spellName).append("\",\n");
            stringBuilder.append("\"").append("spellType\":\"").append(spellType).append("\",\n");
            int spellId = cns.spellId();
            stringBuilder.append("\"").append("spellId\":").append(spellId).append(",\n");

            int maximumHit = cns.maximumHit();
            stringBuilder.append("\"").append("maximumHit\":").append(maximumHit).append(",\n");
            Optional<Animation> castAnimation = cns.castAnimation();
            int castAnimId = -1;
            if (castAnimation.isPresent())
                castAnimId = castAnimation.get().getAnimId();

            stringBuilder.append("\"").append("animation\":").append(castAnimId).append(",\n");

            double baseExperience = cns.baseExperience();
            stringBuilder.append("\"").append("baseExperience\":").append(baseExperience).append(",\n");

            Optional<Graphic> startGraphic = cns.startGraphic();
            Optional<Graphic> endGraphic = cns.endGraphic();

            int startGraphicId = -1;
            int endGraphicId = -1;
            int startGraphicHeight = 0;
            int endGraphicHeight = 0;

            if (startGraphic.isPresent()) {
                Graphic start = startGraphic.get();
                startGraphicId = start.getGraphicId();
                startGraphicHeight = start.getHeight();
            }

            if (endGraphic.isPresent()) {
                Graphic end = endGraphic.get();
                endGraphicId = end.getGraphicId();
                endGraphicHeight = end.getHeight();
            }

            Optional<Projectile> proj = s.getSpell().projectile(new CharacterNode(), new CharacterNode());
            int startHeight = 0;
            int endHeight = 0;
            int projId = -1;
            int curve = -1;
            int speed = -1;
            int delay = -1;

            if (proj.isPresent()) {
                startHeight = proj.get().getStartHeight();
                endHeight = proj.get().getEndHeight();
                projId = proj.get().getProjectileId();
                curve = proj.get().getCurve();
                speed = proj.get().getSpeed();
                delay = proj.get().getSpeed();
            }

            stringBuilder.append("\"projectile\":").append("{\n");
            stringBuilder.append("\"").append("id\":").append(projId).append(",\n");
            stringBuilder.append("\"").append("startHeight\":").append(startHeight).append(",\n");
            stringBuilder.append("\"").append("endHeight\":").append(endHeight).append(",\n");
            stringBuilder.append("\"").append("curve\":").append(curve).append(",\n");
            stringBuilder.append("\"").append("speed\":").append(speed).append(",\n");
            stringBuilder.append("\"").append("delay\":").append(delay).append(",\n");
            stringBuilder.append("},");

            stringBuilder.append("\"").append("startGraphic\":").append("{")
                    .append("\n").append("\"graphicId\":").append(startGraphicId).append(",\n")
                    .append("\"height\":").append(startGraphicHeight).append("\n").append("},\n");

            stringBuilder.append("\"").append("endGraphic\":").append("{")
                    .append("\n").append("\"graphicId\":").append(endGraphicId).append(",\n")
                    .append("\"height\":").append(endGraphicHeight).append("\n").append("},\n");


            Optional<Item[]> itemsRequired = cns.itemsRequired(null);
            Optional<Item[]> equipmentRequired = cns.equipmentRequired(null);

            Item[] requiredItems;
            Item[] requiredEquipment;


            if (itemsRequired.isPresent()) {
                requiredItems = itemsRequired.get();
                stringBuilder.append("\"").append("itemsRequired\":").append("[\n");
                for (Item i : requiredItems) {
                    stringBuilder.append("{\n")
                            .append("\"").append("itemId\":").append(i.getItemId()).append(",\n")
                            .append("\"").append("amount\":").append(i.getAmount()).append("\n},");
                }
                stringBuilder.append("],");

            } else {
                stringBuilder.append("\"").append("itemsRequired\":").append("[]").append(",\n");
            }

            if (equipmentRequired.isPresent()) {
                requiredEquipment = itemsRequired.get();
                stringBuilder.append("\"").append("equipmentRequired\":").append("[\n");
                for (Item i : requiredEquipment) {
                    stringBuilder.append("{\n")
                            .append("\"").append("itemId\":").append(i.getItemId()).append(",\n")
                            .append("\"").append("amount\":").append(i.getAmount()).append("\n},");
                }
                stringBuilder.append("]");
            } else {
                stringBuilder.append("\"").append("equipmentRequired\":").append("[]").append("\n");
            }


            stringBuilder.append("},");
        }
        stringBuilder.append("]");

        try {
            Files.write(Paths.get(f.getAbsolutePath()), stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
