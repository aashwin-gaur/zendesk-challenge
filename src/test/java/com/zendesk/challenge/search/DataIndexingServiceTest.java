package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.Organization;
import com.zendesk.challenge.domain.commons.Scope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class DataIndexingServiceTest {


    private DataIndexingService toTest;

    @BeforeEach
    public void setup() {
        toTest = new DataIndexingService();
    }

    @Test
    public void testValid() {
        Index i = new Index<>(Scope.ORGANIZATIONS, Organization.class, Collections.singletonList(new Organization("testId")));
        toTest.generateIndexes(i);

        Assertions.assertNotNull(i.getIndexedByFields());
        Assertions.assertEquals(Map.of("testId", Collections.singleton("testId")), i.getIndexedByFields().get("id"));
    }

}
