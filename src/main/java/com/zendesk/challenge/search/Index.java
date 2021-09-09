package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Scope;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public class Index<T extends Entity> {

    @NonNull
    private Scope scope;
    @NonNull
    private Class<T> typeOfIndex;
    @NonNull
    private List<T> baseData;

    private Map<String, Integer> idToIndexMap;

    @Setter
    private Map<String, Map<String, Set<String>>> indexedByFields;

    private Set<Field> fieldsIndexed;

    public void initialize() {
        this.idToIndexMap = IntStream
                .range(0, this.baseData.size())
                .mapToObj(i -> Map.entry(this.baseData.get(i).getId(), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.fieldsIndexed = DataIndexingService.indexedFields(typeOfIndex)
                .collect(Collectors.toSet());
    }

}
