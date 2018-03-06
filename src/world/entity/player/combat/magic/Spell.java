package world.entity.player.combat.magic;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Spell {
    WIND_STRIKE(1152),
    CONFUSE(1153),
    WATER_STRIKE(1154),
    EARTH_STRIKE(1156),
    WEAKEN(1157),
    FIRE_STRIKE(1158),
    WIND_BOLT(1160),
    CURSE(1161),
    BIND(1572),
    WATER_BOLT(1163),
    EARTH_BOLT(1166),
    FIRE_BOLT(1169),
    CRUMBLE_UNDEAD(1171),
    WIND_BLAST(1172),
    WATER_BLAST(1175),
    IBAN_BLAST(1539),
    SNARE(1582),
    MAGIC_DART(12037),
    EARTH_BLAST(1177),
    FIRE_BLAST(1181),
    SARADOMIN_STRIKE(1190),
    CLAWS_OF_GUTHIX(1191),
    FLAMES_OF_ZAMORAK(1192),
    WIND_WAVE(1183),
    WATER_WAVE(1185),
    VULNERABILITY(1542),
    EARTH_WAVE(1188),
    ENFEEBLE(1543),
    FIRE_WAVE(1189),
    ENTANGLE(1592),
    STUN(1562),
    TELEBLOCK(12445),
    SMOKE_RUSH(12939),
    SHADOW_RUSH(12987),
    BLOOD_RUSH(12901),
    ICE_RUSH(12861),
    SMOKE_BURST(12963),
    SHADOW_BURST(13011),
    BLOOD_BURST(12919),
    ICE_BURST(12881),
    SMOKE_BLITZ(12951),
    SHADOW_BLITZ(12999),
    BLOOD_BLITZ(12911),
    ICE_BLITZ(12871),
    SMOKE_BARRAGE(12975),
    SHADOW_BARRAGE(13023),
    BLOOD_BARRAGE(12929),
    ICE_BARRAGE(12891);
    /*Add lunar spells*/

    private static final Map<Integer, Spell> map;

    static {
        map = new HashMap<>(Spell.values().length);
        Arrays.stream(Spell.values()).forEach(s -> map.put(s.getId(), s));
    }

    private int spellId;

    Spell(int spellId) {
        this.spellId = spellId;
    }

    public static Spell fromIndex(int i) {
        return map.get(i);
    }

    public int getId() {
        return spellId;
    }
}
