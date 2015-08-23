package com.awdean.poeima.regex;

import static com.awdean.poeima.regex.RegexUtils.toLiteralPattern;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.awdean.poeima.data.ItemAttributes;
import com.awdean.poeima.data.ItemRarity;
import com.google.common.collect.Maps;

public class CopiedItemInfo {

    public static final String PROPERTIES = "CopiedItemInfo.properties";

    private static final CopiedItemInfo INSTANCE = new CopiedItemInfo();

    private String blockSeparator;

    private String prefixRarity;
    private String prefixItemLevel;

    private String tokenRarityMagic;
    private String tokenRarityRare;

    private String tagCorrupted;
    private String tagMirrored;
    private String tagUnidentified;

    private String substringFlaskHelp;
    private String substringJewelHelp;
    private String substringMapHelp;

    public CopiedItemInfo() {
        Properties props = new Properties();
        try {
            props.load(this.getClass().getResourceAsStream(PROPERTIES));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load default properties file: " + PROPERTIES, ex);
        }
        init(props);
    }

    public CopiedItemInfo(Properties props) {
        init(props);
    }

    public static CopiedItemInfo getInstance() {
        return INSTANCE;
    }

    public boolean accepts(String string) {
        return string.startsWith(prefixRarity);
    }

    private Pattern getPropertiesPattern() {
        String ltrim = "\\s*", rtrim = "\\s*?";
        String line = rtrim + "[\\n\\r]+" + ltrim;
        String block = line + toLiteralPattern(getBlockSeparator()) + line;
        String text = "(?!" + toLiteralPattern(getBlockSeparator()) + ")" + ".+";

        StringBuilder sb = new StringBuilder("^");
        // An item has between zero and one implicit modifiers.
        sb.append("(?:").
               append(block).append("(").append(text).append(")").
           append(")??");
        // An item has between zero and six explicit modifiers.
        sb.append("(?:").
               append(block).append("(").
                   append(text).append("(?:").append(line).append(text).append(")*?").
               append(")").
           append(")?");
        // This should consume all remaining text.
        sb.append(ltrim).append("$");
        
        return Pattern.compile(sb.toString());
    }
    
    public ItemAttributes parse(String string) {
        ItemAttributes item = new ItemAttributes();
        
        String remainder = parseHeader(item, string);
        if (null == remainder) {
            return null;
        }
        
        remainder = parseFooter(item, remainder);
        if (null == remainder) {
            return null;
        }
        
        Matcher matcher = getPropertiesPattern().matcher(remainder);
        if (! matcher.matches()) {
            return null;
        }
        
        ItemAttributes parsed = new ItemAttributes();
        parsed.implicit = matcher.group(1);
        parsed.explicits = (null == matcher.group(2) ? null : Arrays.asList(matcher.group(2).split("[\n\r]+")));

        item.joinAll(parsed);
        
        return item;
    }
    
    private Pattern getHeaderPattern() {
        String ltrim = "\\s*", rtrim = "\\s*?";
        String line = rtrim + "[\\n\\r]+" + ltrim;
        String block = line + toLiteralPattern(getBlockSeparator()) + line;
        String text = "(?!" + toLiteralPattern(getBlockSeparator()) + ")" + ".+";
        String any = "(?:" + text + "|" + line + ")+";

        StringBuilder sb = new StringBuilder("^");
        // The first few lines are assumed to be in a fixed format.
        sb.append(ltrim).append(toLiteralPattern(getPrefixRarity())).append(ltrim).
               append("(").
                   append(toLiteralPattern(getTokenRarityMagic())).
                   append("|").append(toLiteralPattern(getTokenRarityRare())).
               append(")");
        sb.append(line).
               append("(").append(text).append(")");
        sb.append("(?:").
               append(line).append("(").append(text).append(")").
           append(")?");
        // We ignore any noise, like item attributes, requirements, or sockets
        sb.append("(?:").
               append(block).append(any).
           append(")*?");
        // The item's level is the last block printed before the modifiers.
        sb.append(block).append(toLiteralPattern(getPrefixItemLevel())).
           append(ltrim).append("(\\d+)");
        // Finally, accept any remaining text for further processing.
        sb.append("((?:.|[\\n\\r])*)");
        
        return Pattern.compile(sb.toString());
    }
    
    public String parseHeader(ItemAttributes item, String string) {
        Matcher matcher = getHeaderPattern().matcher(string);
        if (! matcher.find()) {
            return null;
        }
        
        ItemAttributes parsed = new ItemAttributes();
        parsed.rarity = ItemRarity.parseItemRarity(matcher.group(1));
        parsed.name = matcher.group(2);
        parsed.base = matcher.group(3);
        parsed.level = Integer.parseInt(matcher.group(4));
        
        item.joinAll(parsed);
        
        return matcher.group(5);
    }

    private static String reversed(String string) {
        return new StringBuilder(string).reverse().toString();
    }
    
    private Pattern getFooterPattern() {
        String ltrim = "\\s*", rtrim = "\\s*?";
        String line = rtrim + "[\\n\\r]+" + ltrim;
        String rblock = line + toLiteralPattern(reversed(getBlockSeparator())) + line;
        String rtext = "(?!" + toLiteralPattern(reversed(getBlockSeparator())) + ")" + ".+";
        // This regex matches any blocks containing help substrings.
        StringBuilder sb = new StringBuilder();
        sb.append("(?:").append(rtext).append(line).append(")*").
           append(".*").append("(?:").
               append("(?:").append(toLiteralPattern(reversed(getSubstringFlaskHelp()))).append(")").
               append("|(?:").append(toLiteralPattern(reversed(getSubstringJewelHelp()))).append(")").
               append("|(?:").append(toLiteralPattern(reversed(getSubstringMapHelp()))).append(")").
           append(")").append(".*?").
           append("(?:").append(line).append(rtext).append(")*");
        String help = sb.toString();
        
        sb = new StringBuilder("^");
        // Next, we'll discard all descriptive text below the modifiers.
        sb.append(ltrim).append("(?:").
               append("(?:").
                   append("(").append(toLiteralPattern(reversed(getTagCorrupted()))).append(")").
                   append("|(").append(toLiteralPattern(reversed(getTagMirrored()))).append(")").
                   append("|(").append(toLiteralPattern(reversed(getTagUnidentified()))).append(")").
                   append("|").append(help).
               append(")").append(rblock).
           append(")*");

        // Finally, accept any remaining text for further processing.
        sb.append("((?:.|[\\n\\r])*)");

        return Pattern.compile(sb.toString());
    }
    
    public String parseFooter(ItemAttributes item, String string) {
        Matcher matcher = getFooterPattern().matcher(reversed(string));
        if (!matcher.find()) {
            return null;
        }

        ItemAttributes parsed = new ItemAttributes();
        parsed.corrupted = (null == matcher.group(1) ? false : true);
        parsed.mirrored = (null == matcher.group(2) ? false : true);
        parsed.unidentified = (null == matcher.group(3) ? false : true);
        
        item.joinAll(parsed);
        
        return reversed(matcher.group(4));
    }

    private void init(Properties props) {
        // Check that the properties file only contains String values
        Set<Object> nonstringPropertyNames = new TreeSet<>(props.keySet());
        nonstringPropertyNames.removeAll(props.stringPropertyNames());
        if (!nonstringPropertyNames.isEmpty()) {
            String reason = new StringBuilder("In ").append(PROPERTIES).append(" keys have non-string values: ")
                    .append(nonstringPropertyNames).toString();
            Throwable cause = new InvalidPropertiesFormatException(reason);
            throw new RuntimeException(cause);
        }
        // Use reflection to get all private instance variables.
        List<Field> fields = Arrays.stream(CopiedItemInfo.class.getDeclaredFields())
                .filter((Field field) -> field.getType().equals(String.class))
                .filter((Field field) -> Modifier.isPrivate(field.getModifiers()))
                .filter((Field field) -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
        Map<String, Field> variables = Maps.uniqueIndex(fields, (Field field) -> field.getName());
        // Check the set of key strings from the properties object.
        Set<String> names = variables.keySet();
        Set<String> propNames = props.stringPropertyNames();
        if (!propNames.equals(names)) {
            StringBuilder sb = new StringBuilder();
            sb.append("In ").append(PROPERTIES).append(" key mismatch:\n");

            Set<String> excess = new TreeSet<String>(names);
            excess.removeAll(propNames);
            if (0 < excess.size()) {
                sb.append("Excess keys - ").append(excess).append('\n');
            }

            Set<String> missing = new TreeSet<String>(propNames);
            missing.removeAll(names);
            if (0 < missing.size()) {
                sb.append("Missing keys - ").append(missing).append('\n');
            }

            // Don't attempt recovery when given an invalid configuration.
            sb.setLength(sb.length() - 1);
            Throwable cause = new InvalidPropertiesFormatException(sb.toString());
            throw new RuntimeException(cause);
        }
        // Use the Properties values to populate the instance variables
        for (Map.Entry<String, Field> binding : variables.entrySet()) {
            Field field = binding.getValue();
            assert(!field.isAccessible());
            field.setAccessible(true);
            try {
                binding.getValue().set(this, props.get(binding.getKey()));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                field.setAccessible(false);
            }
        }
    }

    /**
     * @return the blockSeparator
     */
    public String getBlockSeparator() {
        return blockSeparator;
    }

    /**
     * @return the prefixRarity
     */
    public String getPrefixRarity() {
        return prefixRarity;
    }

    /**
     * @return the prefixItemLevel
     */
    public String getPrefixItemLevel() {
        return prefixItemLevel;
    }

    /**
     * @return the tokenRarityMagic
     */
    public String getTokenRarityMagic() {
        return tokenRarityMagic;
    }

    /**
     * @return the tokenRarityRare
     */
    public String getTokenRarityRare() {
        return tokenRarityRare;
    }

    /**
     * @return the tagCorrupted
     */
    public String getTagCorrupted() {
        return tagCorrupted;
    }

    /**
     * @return the tagMirrored
     */
    public String getTagMirrored() {
        return tagMirrored;
    }

    /**
     * @return the tagUnidentified
     */
    public String getTagUnidentified() {
        return tagUnidentified;
    }

    /**
     * @return the substringFlaskHelp
     */
    public String getSubstringFlaskHelp() {
        return substringFlaskHelp;
    }

    /**
     * @return the substringJewelHelp
     */
    public String getSubstringJewelHelp() {
        return substringJewelHelp;
    }

    /**
     * @return the substringMapHelp
     */
    public String getSubstringMapHelp() {
        return substringMapHelp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CopiedItemInfo [blockSeparator=" + blockSeparator + ", prefixRarity=" + prefixRarity
                + ", prefixItemLevel=" + prefixItemLevel + ", tokenRarityMagic=" + tokenRarityMagic
                + ", tokenRarityRare=" + tokenRarityRare + ", tagCorrupted=" + tagCorrupted + ", tagMirrored="
                + tagMirrored + ", tagUnidentified=" + tagUnidentified + ", substringFlaskHelp=" + substringFlaskHelp
                + ", substringJewelHelp=" + substringJewelHelp + ", substringMapHelp=" + substringMapHelp + "]";
    }

}
