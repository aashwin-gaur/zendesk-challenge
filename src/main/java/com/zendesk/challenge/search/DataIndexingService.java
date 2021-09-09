package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.commons.Entity;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class DataIndexingService {

    private static final String NULL_VALUE = "__NULL_VALUE__";
    private static final String ID_KEY = "id";


    public void generateIndexes(@NonNull Index<? extends Entity> index) {
        index.initialize();
        index.setIndexedByFields(generateIndexesForFields(index.getBaseData(), index.getTypeOfIndex()));
    }

    static Stream<Field> indexedFields(Class<? extends Entity> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getType().equals(String.class));//ignore Date and Sets
    }

    private Map<String, Map<String, Set<String>>> generateIndexesForFields(List<? extends Entity> list, Class<? extends Entity> clazz) {
        return indexedFields(clazz)
                .collect(Collectors.toMap(Field::getName, k -> list.stream()
                        .map(toValueAndIdMapEntries(k))
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toSet())))));
    }


    private Function<Entity, Map.Entry<String, String>> toValueAndIdMapEntries(Field k) {
        return each -> {
            k.setAccessible(true);
            String key = null;
            String value = null;
            try {
                key = (String) k.get(each);
                key = key == null ? NULL_VALUE : key;
                Field id = each.getClass().getDeclaredField(ID_KEY);
                id.setAccessible(true);
                value = (String) id.get(each);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return Map.entry(key, value);
        };
    }

}
