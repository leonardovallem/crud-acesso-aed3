package data.config;

import java.text.Normalizer;

public class KeywordHandler {
    private static final String[] IGNORED_KEYWORDS = new String[]
            {"de", "da", "do", "para", "por", "pela", "pelo", "que", "qual", "onde", "quando", "é", "era", "foi", "vai", "como", "o", "a"};

    public static boolean isRelevant(String keyword) {
        for (String ignoredKeyword : IGNORED_KEYWORDS) {
            if(ignoredKeyword.equalsIgnoreCase(keyword)) return false;
        }
        return true;
    }

    public static String normalize(String keyword) {
        return Normalizer.normalize(keyword.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("?", "")
                .replace("_", "")
                .replace("!", "")
                .replace("-", "")
                .toLowerCase();
    }
}
