package com.awdean.poeima.data;

public enum ItemType {
    // Armour Types
    BODY_ARMOUR("Body Armour"),
    BOOTS("Boots"),
    GLOVES("Gloves"),
    HELMET("Helmet"),
    SHIELD("Shield"),
    // Jewellery Types
    AMULET("Amulet"),
    BELT("Belt"),
    QUIVER("Quiver"),
    RING("Ring"),
    // Weapon Types
    BOW("Bow"),
    CLAW("Claw"),
    DAGGER("Dagger"),
    ONE_HANDED_AXE("One Handed Axe"),
    ONE_HANDED_MACE("One Handed Mace"),
    ONE_HANDED_SWORD("One Handed Sword"),
    STAFF("Staff"),
    TWO_HANDED_AXE("Two Handed Axe"),
    TWO_HANDED_MACE("Two Handed Mace"),
    TWO_HANDED_SWORD("Two Handed Sword"),
    WAND("Wand");
    
    private String _description;
    
    private ItemType(String description) {
        _description = description;
    }
    
    @Override
    public String toString() {
        return _description;
    }

    public static ItemType parseItemType(String string) {
        if (null == string) {
            return null;
        }
        for (ItemType itemType : ItemType.values()) {
            if (string.equals(itemType.toString())) {
                return itemType;
            }
        }
        return null;
    }


}
