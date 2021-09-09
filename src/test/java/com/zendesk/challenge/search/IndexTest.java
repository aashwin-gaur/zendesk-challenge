package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.Organization;
import com.zendesk.challenge.domain.commons.Scope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class IndexTest {

    @Test
    public void testIndexInitialization() {

        Index i = new Index<>(Scope.ORGANIZATIONS, Organization.class, Collections.singletonList(new Organization("testId")));
        i.initialize();

        Assertions.assertNotNull(i.getFieldsIndexed());
        Assertions.assertEquals(6, i.getFieldsIndexed().size());
    }

    @Test
    public void testInitialization2() {

        Index i = new Index<>(Scope.ORGANIZATIONS, Organization.class, Collections.singletonList(new Organization("testId")));
        i.initialize();

        Assertions.assertNotNull(i.getIdToIndexMap());
        Assertions.assertEquals(1, i.getIdToIndexMap().size());
        Assertions.assertEquals(Map.of("testId", 0), i.getIdToIndexMap());
    }

}
