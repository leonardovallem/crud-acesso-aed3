package config;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class KeywordHandler {
    private static final String[] IGNORED_KEYWORDS = new String[]
            {"de", "da", "das", "do", "dos", "para", "por", "pela", "pelas", "pelo", "pelos", "que", "qual", "quais", "onde",
                    "quando", "é", "era", "foi", "vai", "como", "o", "a", "quanto", "quantos", "quantas", "seu", "sua",
                    "dele", "dela", "delas", "deles", };

    /**
     * Retorna verdadeiro caso a palavra-chave recebida não
     * seja igual a nenhuma das palavras a serem ignoras
     *
     * @param keyword Palavra-chave a ser testada
     */
    public static boolean isRelevant(String keyword) {
        for (String ignoredKeyword : IGNORED_KEYWORDS) {
            if(ignoredKeyword.equalsIgnoreCase(keyword)) return false;
        }
        return true;
    }

    /**
     * @param keyword Palavra-chave a ser normalizada
     * @return Palavra-chave com capitalização minúscula, sem acentuação e caracteres especiais */
    public static String normalize(String keyword) {
        return Normalizer.normalize(keyword.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("?", "")
                .replace("_", "")
                .replace("!", "")
                .replace("-", "")
                .toLowerCase();
    }

    /**
     * @param phrase String contendo palavras chaves separadas por espaço
     * @return List de Strings contendo somente as palavras-chaves relevantes encontradas
     */
    public static List<String> relevantKeywords(String phrase) {
        String[] words = phrase.trim().split(" ");
        List<String> keywords = new ArrayList<>();

        for(var word : words) {
            if (isRelevant(word)) keywords.add(normalize(word));
        }

        return keywords;
    }
}
