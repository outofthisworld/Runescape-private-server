package world.definitions.item;

import world.definitions.IDefinition;
import world.definitions.types.WeaponInterfaceType;

public class WeaponInterfaceDefinition implements IDefinition {
    private int id;
    private String interfaceType;

    public int getId() {
        return id;
    }

    public String getInterfaceName() {
        return interfaceType;
    }

    public WeaponInterfaceType getWeaponInterfaceType(){
        WeaponInterfaceType type = WeaponInterfaceType.valueOf(interfaceType);
        if(type == null){
            throw new RuntimeException("Invalid weapon interface type");
        }
        return type;
    }
}
