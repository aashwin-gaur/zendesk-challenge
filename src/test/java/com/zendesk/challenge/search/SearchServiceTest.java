package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.Organization;
import com.zendesk.challenge.domain.Ticket;
import com.zendesk.challenge.domain.User;
import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Scope;
import com.zendesk.challenge.io.UserInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SearchServiceTest {

    private SearchService toTest;

    private DataIndexingService dataIndexingService = new DataIndexingService();

    @BeforeEach
    public void setup() {

        Organization org = new Organization("testId");
        Index<Organization> i = new Index<>(Scope.ORGANIZATIONS, Organization.class, Collections.singletonList(org));
        dataIndexingService.generateIndexes(i);

        User user = new User("testUserId");
        user.setOrganizationId("testId");
        Index<User> i2 = new Index<>(Scope.USERS, User.class, Collections.singletonList(user));
        dataIndexingService.generateIndexes(i2);

        Ticket ticket = new Ticket("testTicketId");
        ticket.setOrganizationId("testId");
        Index<Ticket> i3 = new Index<>(Scope.TICKETS, Ticket.class, Collections.singletonList(ticket));
        dataIndexingService.generateIndexes(i3);

        toTest = new SearchService(List.of(i, i2, i3));
    }

    @Test
    public void test() {
        Set<? extends Entity> organizationSet = toTest.search(new UserInput(Scope.ORGANIZATIONS, "id", "testId"), false);
        Organization organization = (Organization) organizationSet.stream().findFirst().get();
        Assertions.assertEquals("testId", organization.getId());
        Assertions.assertNull(organization.getUsers());
    }

    @Test
    public void testLazyLoading() {
        Set<? extends Entity> organizationSet = toTest.search(new UserInput(Scope.ORGANIZATIONS, "id", "testId"), true);
        Organization organization = (Organization) organizationSet.stream().findFirst().get();
        Assertions.assertEquals("testId", organization.getId());
        Assertions.assertEquals("testId", organization.getTickets().stream().findFirst().get().getOrganizationId());
        Assertions.assertEquals("testId", organization.getUsers().stream().findFirst().get().getOrganizationId());
    }

}
