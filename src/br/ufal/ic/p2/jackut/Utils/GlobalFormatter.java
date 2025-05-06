package br.ufal.ic.p2.jackut.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Formatador de lista de itens de qualquer tipo
 *
 * @author MarcosMelo
 * @version 1.0
 */

public class GlobalFormatter {

    
    // Formata uma lista de qualquer tipo no padrão {item1,item2,item3}

    /**
     * Obtém a lista formatada como string
     *
     * @param items Lista de itens
     * @return String com todos os itens no formato especificado nas User Stories
     */
    public static <T> String formatList(List<T> items) {
        if (items == null || items.isEmpty()) {
            return "{}";
        }
        return "{" + items.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")) + "}";
    }
}
