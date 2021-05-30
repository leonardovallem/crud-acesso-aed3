package config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUtils {
    public static List<Integer> intersection(List<List<Integer>> listOfLists) {
        if(listOfLists.size() == 0) return new ArrayList<>();

        Set<Integer> intersecao = new HashSet<>(listOfLists.get(0));
        for(int i=1 ; i < listOfLists.size() ; i++) {    // obtém perguntas que possuem todas as palavras-chaves
            Set<Integer> temp = new HashSet<>(listOfLists.get(i));
            intersecao.retainAll(temp);
        }

        return new ArrayList<>(intersecao);
    }
}
