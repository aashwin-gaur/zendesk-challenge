package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Mapped;
import com.zendesk.challenge.domain.commons.Scope;
import com.zendesk.challenge.io.UserInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchService {

    private static final String ID_KEY = "id";

    private List<Index<? extends Entity>> indices;
    private Map<Scope, Index<? extends Entity>> groupedByScope;

    public SearchService(List<Index<? extends Entity>> indices) {
        this.indices = indices;
        this.groupedByScope = indices.stream()
                .map(i -> Map.entry(i.getScope(), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * @param in                     input for searching within the indices
     * @param lazilyPopulateMappings needed to lazily load child objects
     * @return
     */
    public Set<? extends Entity> search(@NonNull UserInput in, boolean lazilyPopulateMappings) {
        return Optional.ofNullable(groupedByScope.get(in.getCategory())
                .getIndexedByFields()
                .get(in.getField())
                .get(in.getValue()))
                .orElseGet(Collections::emptySet)
                .stream()
                .map(i -> {
                    Index index = groupedByScope.get(in.getCategory());
                    int indexOfBaseDataList = (Integer) index.getIdToIndexMap().get(i);
                    Entity e = (Entity) index.getBaseData().get(indexOfBaseDataList);
                    if (in.getField().equals(ID_KEY) && lazilyPopulateMappings) {
                        lazilyPopulateMappings(e, groupedByScope.get(in.getCategory()).getTypeOfIndex());
                    }
                    return e;

                }).collect(Collectors.toSet());
    }

    /**
     * @param e           Takes an {@link Entity} e within a resolved entity and resolves all of the entities it is composed of
     * @param typeOfIndex Subtype of {@link Entity} e
     */
    private void lazilyPopulateMappings(Entity e, Class<? extends Entity> typeOfIndex) {
        Field[] fields = typeOfIndex.getDeclaredFields();
        Arrays.stream(fields)
                .filter(f -> Arrays.stream(f.getAnnotationsByType(Mapped.class))
                        .findFirst().isPresent())
                .forEach(f -> {
                    Annotation[] res = f.getAnnotationsByType(Mapped.class);
                    String referenceFieldName = ((Mapped) res[0]).value();
                    Type type = f.getGenericType();

                    if (type instanceof ParameterizedType) { //resolve to-many mappings
                        ParameterizedType pType = (ParameterizedType) type;
                        Type[] arr = pType.getActualTypeArguments();
                        Set<? extends Entity> result = search(new UserInput(Scope.byClass((Class<?>) arr[0]), referenceFieldName, e.getId()), false);
                        try {
                            f.setAccessible(true);
                            f.set(e, result);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    } else { //resolve to-1 mappings
                        Class<? extends Entity> entityType = (Class<? extends Entity>) type;
                        try {
                            Field referencedLocalField = typeOfIndex.getDeclaredField(referenceFieldName);
                            referencedLocalField.setAccessible(true);
                            String value = (String) referencedLocalField.get(e);
                            Set<? extends Entity> result = search(new UserInput(Scope.byClass(entityType), ID_KEY, value), false);
                            f.setAccessible(true);
                            f.set(e, result.stream().findAny().orElseGet(null));
                        } catch (IllegalAccessException | NoSuchFieldException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }

}
