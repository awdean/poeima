package com.awdean.data;

public enum ItemRarity {
    NORMAL("Normal"),
    MAGIC("Magic"),
    RARE("Rare"),
    UNIQUE("Unique");
    
    private String _description;
    
    private ItemRarity(String description) {
        _description = description;
    }

    public String toString() {
        return _description;
    }
    
}
