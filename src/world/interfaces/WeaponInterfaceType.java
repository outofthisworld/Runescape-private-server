package world.interfaces;

import java.util.Objects;

public enum WeaponInterfaceType {
    DART(),
    CLAWS(),
    UNARMED(),
    WHIP(),
    WARHAMMER(),
    STAFF(),
    SHORTBOW(),
    HALBERD(),
    DAGGER(),
    PICKAXE(),
    SWORD(),
    LONGSWORD(),
    TWO_HANDED_SWORD(),
    SCIMITAR(),
    BATTLEAXE(),
    MACE(),
    SPEAR(),
    CROSSBOW(),
    JAVELIN(),
    THROWNAXE(),
    KNIFE(),
    SCYTHE(),
    LONGBOW();

    WeaponInterfaceType() {

    }

    public int getId() {
        return Objects.hash(this.name());
    }
}
