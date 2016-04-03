package com.netease.channel.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class HtmlCharacterEntityReferences {
    static final char REFERENCE_START = 38;
    static final String DECIMAL_REFERENCE_START = "&#";
    static final String HEX_REFERENCE_START = "&#x";
    static final char REFERENCE_END = 59;
    static final char CHAR_NULL = 65535;
    private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";
    private final String[] characterToEntityReferenceMap = new String[3000];

    private final Map<String, Character> entityReferenceToCharacterMap = new HashMap<String, Character>(252);

    public HtmlCharacterEntityReferences() {
        Properties entityReferences = new Properties();

        InputStream is = HtmlCharacterEntityReferences.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (is == null) {
            throw new IllegalStateException(
                    "Cannot find [HtmlCharacterEntityReferences.properties] as class path resource");
        }
        try {
            try {
                entityReferences.load(is);
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: "
                            + ex.getMessage());
        }

        Enumeration<?> keys = entityReferences.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            int referredChar = Integer.parseInt(key);

            int index = (referredChar < 1000) ? referredChar : referredChar - 7000;
            String reference = entityReferences.getProperty(key);
            this.characterToEntityReferenceMap[index] = '&' + reference + ';';
            // System.out.println(count++ + "--" + index + "->" + characterToEntityReferenceMap[index] + "<br/>");
            this.entityReferenceToCharacterMap.put(reference, new Character((char) referredChar));
        }
    }

    public int getSupportedReferenceCount() {
        return this.entityReferenceToCharacterMap.size();
    }

    public boolean isMappedToReference(char character) {
        return (convertToReference(character) != null);
    }

    public String convertToReference(char character) {
        if ((character < 1000) || ((character >= 8000) && (character < 10000))) {
            int index = (character < 1000) ? character : character - 7000;
            String entityReference = this.characterToEntityReferenceMap[index];
            if (entityReference != null) {
                return entityReference;
            }
        }
        return null;
    }

    public char convertToCharacter(String entityReference) {
        Character referredCharacter = (Character) this.entityReferenceToCharacterMap.get(entityReference);
        if (referredCharacter != null) {
            return referredCharacter.charValue();
        }
        return 65535;
    }
}
