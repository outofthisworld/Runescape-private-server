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

package world.interfaces;

public enum SidebarInterface {
    /**
     * Values
     * The below are the different values for this packet.
     * <p>
     * Value	Icon	Norm. ID
     * 0	Attack type	2433
     * 1	Stats	3917
     * 2	Quests	638
     * 3	Inventory	3213
     * 4	Wearing	1644
     * 5	Prayer	5608
     * 6	Magic	1151
     * 7	EMPTY
     * 8	Friends list	5065
     * 9	Ignore list	5715
     * 10	Log out	2449
     * 11	Settings	4445
     * 12	Emotes	147
     * 13	Music
     */
    ATTACK_TYPE(2433),
    STATS(3917),
    QUESTS(638),
    /*
        3214
        6980
        11130
    */
    INVENTORY(3213),
    WEARING(1644),
    PRAYER(5608),
    MAGIC(1151),
    EMPTY(0),
    FRIENDS_LIST(5065),
    IGNORE_LIST(5715),
    LOG_OUT(2449),
    SETTINGS(4445),
    EMOTES(147),
    MUSIC(6299);

    private final int id;

    SidebarInterface(int id) {
        this.id = id;
    }

    public int getInterfaceId() {
        return id;
    }
}
