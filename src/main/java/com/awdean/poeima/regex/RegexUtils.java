package com.awdean.poeima.regex;

public class RegexUtils {

    private RegexUtils() {
    }

    public static String toLiteralPattern(String string) {
        StringBuilder sb = new StringBuilder();

        for (int ii = 0; ii < string.length(); ii++) {
            char ch = string.charAt(ii);
            
            // unicode code points
            if (0x7f < ch) {
                sb.append(String.format("\\u%04x", ch));
            // ascii control characters and DEL
            } else if (0x20 > ch || 0x7f == ch) {
                String repr;
                switch (ch) {
                    case 0x07:
                        repr = "\\a"; break;
                    case 0x09:
                        repr = "\\t"; break;
                    case 0x0a:
                        repr = "\\n"; break;
                    case 0x0c:
                        repr = "\\f"; break;
                    case 0x0d:
                        repr = "\\r"; break;
                    case 0x1b:
                        repr = "\\e"; break;
                    default:
                        repr = String.format("\\x%02x", (int) ch);
                }
                sb.append(repr);
            // ascii printable characters
            } else {
                String repr;
                switch (ch) {
                    case '!':
                        repr = "\\!"; break;
                    case '$':
                        repr = "\\$"; break;
                    case '(':
                        repr = "\\("; break;
                    case ')':
                        repr = "\\)"; break;
                    case '*':
                        repr = "\\*"; break;
                    case '+':
                        repr = "\\+"; break;
                    case '-':
                        repr = "\\-"; break;
                    case '.':
                        repr = "\\."; break;
                    case ':':
                        repr = "\\:"; break;
                    case '<':
                        repr = "\\<"; break;
                    case '=':
                        repr = "\\="; break;
                    case '>':
                        repr = "\\>"; break;
                    case '?':
                        repr = "\\?"; break;
                    case '[':
                        repr = "\\["; break;
                    case '\\':
                        repr = "\\\\"; break;
                    case ']':
                        repr = "\\]"; break;
                    case '^':
                        repr = "\\^"; break;
                    case '{':
                        repr = "\\{"; break;
                    case '|':
                        repr = "\\|"; break;
                    case '}':
                        repr = "\\}"; break;
                    default:
                        repr = Character.toString(ch);
                }
                sb.append(repr);
            }
        }
        return sb.toString();
    }

}
