package br.ufal.ic.p2.jackut.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class GlobalFormatter {

    // Formata uma lista de qualquer tipo no padrão {item1,item2,item3}
    public static <T> String formatList(List<T> items) {
        if (items == null || items.isEmpty()) {
            return "{}";
        }
        return "{" + items.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")) + "}";
    }
}
