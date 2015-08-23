package com.awdean.poeima.data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Range;

public class ItemAttributes {

    // Core attributes
    public ItemRarity rarity = null;
    public String name = null;
    public String base = null;

    // Common inherent properties
    public ItemType type = null;
    public int quality = 0;
    // Armour inherent properties
    public int armour = 0;
    public int evasionRating = 0;
    public int energyShield = 0;
    public int block = 0;
    // Weapon inherent properties
    public Range<Integer> physical = null;
    public List<Range<Integer>> elemental = null;
    public Range<Integer> chaos = null;
    public BigDecimal critical = null;
    public BigDecimal attacks = null;

    // Item requirements
    public int levelRequired = 0;
    public int strengthRequired = 0;
    public int dexterityRequired = 0;
    public int intelligenceRequired = 0;

    // Item sockets
    public String sockets = null;

    // Item level
    public int level = 0;

    // Item properties
    public String implicit = null;
    public List<String> explicits = null;

    // Item tags
    public boolean corrupted = false;
    public boolean mirrored = false;
    public boolean unidentified = false;

    // All member variable names which have been (incorrectly) set multiple
    // times.
    public Set<String> dirty = null;

    public static Object join(Object lhs, Object rhs) {
        if (!(lhs instanceof ItemAttributes)) {
            return rhs;
        }
        ItemAttributes ialhs = (ItemAttributes) lhs;
        if (!(rhs instanceof ItemAttributes)) {
            return lhs;
        }
        ItemAttributes iarhs = (ItemAttributes) rhs;

        ItemAttributes combined = new ItemAttributes();
        // join the lhs
        combined.joinAll(ialhs);
        // join the rhs
        combined.joinAll(iarhs);

        return combined;
    }

    public void joinAll(ItemAttributes delta) {
        joinCore(delta);
        joinInherents(delta);
        joinRequirements(delta);
        joinSockets(delta);
        joinLevel(delta);
        joinProperties(delta);
        joinTags(delta);
    }

    public void joinCore(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join all core attributes.
        if (null != delta.rarity) {
            if (null != rarity) {
                markDirty("rarity");
            }
            rarity = delta.rarity;
        }
        if (null != delta.name) {
            if (null != name) {
                markDirty("name");
            }
            name = delta.name;
        }
        if (null != delta.base) {
            if (null != base) {
                markDirty("base");
            }
            base = delta.base;
        }
    }

    public void joinInherents(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join all common inherent properties.
        if (null != delta.type) {
            if (null != type) {
                markDirty("type");
            }
            type = delta.type;
        }
        if (0 != delta.quality) {
            if (0 != quality) {
                markDirty("quality");
            }
            quality = delta.quality;
        }
        // Join all armour inherent properties.
        if (0 != delta.armour) {
            if (0 != armour) {
                markDirty("armour");
            }
            armour = delta.armour;
        }
        if (0 != delta.evasionRating) {
            if (0 != evasionRating) {
                markDirty("evasionRating");
            }
            evasionRating = delta.evasionRating;
        }
        if (0 != delta.energyShield) {
            if (0 != energyShield) {
                markDirty("energyShield");
            }
            energyShield = delta.energyShield;
        }
        if (0 != delta.block) {
            if (0 != block) {
                markDirty("block");
            }
            block = delta.block;
        }
        // Join all weapon inherent properties.
        if (null != delta.physical) {
            if (null != physical) {
                markDirty("physical");
            }
            physical = delta.physical;
        }
        if (null != delta.elemental) {
            if (null != elemental) {
                markDirty("elemental");
            }
            elemental = delta.elemental;
        }
        if (null != delta.chaos) {
            if (null != chaos) {
                markDirty("chaos");
            }
            chaos = delta.chaos;
        }
        if (null != delta.critical) {
            if (null != critical) {
                markDirty("critical");
            }
            critical = delta.critical;
        }
        if (null != delta.attacks) {
            if (null != attacks) {
                markDirty("attacks");
            }
            attacks = delta.attacks;
        }
    }

    public void joinRequirements(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join all item requirements.
        if (0 != delta.levelRequired) {
            if (0 != levelRequired) {
                markDirty("levelRequired");
            }
            levelRequired = delta.levelRequired;
        }
        if (0 != delta.strengthRequired) {
            if (0 != strengthRequired) {
                markDirty("strengthRequired");
            }
            strengthRequired = delta.strengthRequired;
        }
        if (0 != delta.dexterityRequired) {
            if (0 != dexterityRequired) {
                markDirty("dexterityRequired");
            }
            dexterityRequired = delta.dexterityRequired;
        }
        if (0 != delta.intelligenceRequired) {
            if (0 != intelligenceRequired) {
                markDirty("intelligenceRequired");
            }
            intelligenceRequired = delta.intelligenceRequired;
        }
    }

    public void joinSockets(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join the item's sockets.
        if (null != delta.sockets) {
            if (null != sockets) {
                markDirty("sockets");
            }
            sockets = delta.sockets;
        }
    }

    public void joinLevel(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join the item's sockets.
        if (0 != delta.level) {
            if (0 != level) {
                markDirty("level");
            }
            level = delta.level;
        }
    }

    public void joinProperties(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join the item properties.
        if (null != delta.implicit) {
            if (null != implicit) {
                markDirty("implicit");
            }
            implicit = delta.implicit;
        }
        if (null != delta.explicits) {
            if (null != explicits) {
                markDirty("explicits");
            }
            explicits = delta.explicits;
        }
    }

    public void joinTags(ItemAttributes delta) {
        // Special case: Do nothing when given a null delta.
        if (null == delta) {
            return;
        }
        // Join the item tags.
        if (false != delta.corrupted) {
            if (false != corrupted) {
                markDirty("corrupted");
            }
            corrupted = delta.corrupted;
        }
        if (false != delta.mirrored) {
            if (false != mirrored) {
                markDirty("mirrored");
            }
            mirrored = delta.mirrored;
        }
        if (false != delta.unidentified) {
            if (false != unidentified) {
                markDirty("unidentified");
            }
            unidentified = delta.unidentified;
        }
        if (null != delta.dirty) {
            if (null == dirty) {
                dirty = new HashSet<>();
            }
            dirty.addAll(delta.dirty);
        }
    }

    public void markDirty(String string) {
        if (null == dirty) {
            dirty = new HashSet<>();
        }
        dirty.add(string);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + armour;
        result = prime * result + ((attacks == null) ? 0 : attacks.hashCode());
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + block;
        result = prime * result + ((chaos == null) ? 0 : chaos.hashCode());
        result = prime * result + (corrupted ? 1231 : 1237);
        result = prime * result + ((critical == null) ? 0 : critical.hashCode());
        result = prime * result + dexterityRequired;
        result = prime * result + ((dirty == null) ? 0 : dirty.hashCode());
        result = prime * result + ((elemental == null) ? 0 : elemental.hashCode());
        result = prime * result + energyShield;
        result = prime * result + evasionRating;
        result = prime * result + ((explicits == null) ? 0 : explicits.hashCode());
        result = prime * result + ((implicit == null) ? 0 : implicit.hashCode());
        result = prime * result + intelligenceRequired;
        result = prime * result + level;
        result = prime * result + levelRequired;
        result = prime * result + (mirrored ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((physical == null) ? 0 : physical.hashCode());
        result = prime * result + quality;
        result = prime * result + ((rarity == null) ? 0 : rarity.hashCode());
        result = prime * result + ((sockets == null) ? 0 : sockets.hashCode());
        result = prime * result + strengthRequired;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (unidentified ? 1231 : 1237);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ItemAttributes other = (ItemAttributes) obj;
        if (armour != other.armour) {
            return false;
        }
        if (attacks == null) {
            if (other.attacks != null) {
                return false;
            }
        } else if (!attacks.equals(other.attacks)) {
            return false;
        }
        if (base == null) {
            if (other.base != null) {
                return false;
            }
        } else if (!base.equals(other.base)) {
            return false;
        }
        if (block != other.block) {
            return false;
        }
        if (chaos == null) {
            if (other.chaos != null) {
                return false;
            }
        } else if (!chaos.equals(other.chaos)) {
            return false;
        }
        if (corrupted != other.corrupted) {
            return false;
        }
        if (critical == null) {
            if (other.critical != null) {
                return false;
            }
        } else if (!critical.equals(other.critical)) {
            return false;
        }
        if (dexterityRequired != other.dexterityRequired) {
            return false;
        }
        if (dirty == null) {
            if (other.dirty != null) {
                return false;
            }
        } else if (!dirty.equals(other.dirty)) {
            return false;
        }
        if (elemental == null) {
            if (other.elemental != null) {
                return false;
            }
        } else if (!elemental.equals(other.elemental)) {
            return false;
        }
        if (energyShield != other.energyShield) {
            return false;
        }
        if (evasionRating != other.evasionRating) {
            return false;
        }
        if (explicits == null) {
            if (other.explicits != null) {
                return false;
            }
        } else if (!explicits.equals(other.explicits)) {
            return false;
        }
        if (implicit == null) {
            if (other.implicit != null) {
                return false;
            }
        } else if (!implicit.equals(other.implicit)) {
            return false;
        }
        if (intelligenceRequired != other.intelligenceRequired) {
            return false;
        }
        if (level != other.level) {
            return false;
        }
        if (levelRequired != other.levelRequired) {
            return false;
        }
        if (mirrored != other.mirrored) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (physical == null) {
            if (other.physical != null) {
                return false;
            }
        } else if (!physical.equals(other.physical)) {
            return false;
        }
        if (quality != other.quality) {
            return false;
        }
        if (rarity != other.rarity) {
            return false;
        }
        if (sockets == null) {
            if (other.sockets != null) {
                return false;
            }
        } else if (!sockets.equals(other.sockets)) {
            return false;
        }
        if (strengthRequired != other.strengthRequired) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (unidentified != other.unidentified) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ItemAttributes [rarity=" + rarity + ", name=" + name + ", base=" + base + ", type=" + type
                + ", quality=" + quality + ", armour=" + armour + ", evasionRating=" + evasionRating + ", energyShield="
                + energyShield + ", block=" + block + ", physical=" + physical + ", elemental=" + elemental + ", chaos="
                + chaos + ", critical=" + critical + ", attacks=" + attacks + ", levelRequired=" + levelRequired
                + ", strengthRequired=" + strengthRequired + ", dexterityRequired=" + dexterityRequired
                + ", intelligenceRequired=" + intelligenceRequired + ", sockets=" + sockets + ", level=" + level
                + ", implicit=" + implicit + ", explicits=" + explicits + ", corrupted=" + corrupted + ", mirrored="
                + mirrored + ", unidentified=" + unidentified + ", dirty=" + dirty + "]";
    }

}
