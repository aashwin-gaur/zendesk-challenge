package com.zendesk.challenge.search;

import com.zendesk.challenge.domain.Organization;
import com.zendesk.challenge.domain.Ticket;
import com.zendesk.challenge.domain.User;
import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Scope;
import com.zendesk.challenge.io.IOUtils;

import java.util.Arrays;
import java.util.List;

public class DataInitializerService {

    private static final String RESOURCE_LOCATION = "src/main/resources/";
    private static final String ORGANIZATIONS_LOCATION = RESOURCE_LOCATION.concat("organizations.json");
    private static final String USERS_LOCATION = RESOURCE_LOCATION.concat("users.json");
    private static final String TICKETS_LOCATION = RESOURCE_LOCATION.concat("tickets.json");


    //for better extendability, could convert this to a reflective function that does not initialize
    // each index by name and instead iterates over all classes of type "Entity" in the classpath and generates indexes for those.
    // omitting in this instance due to time constraints
    public List<Index<? extends Entity>> initializeIndices() {
        //Original Ordering by id
        List<Organization> organizations = IOUtils.getDataJackson(ORGANIZATIONS_LOCATION, Organization.class);
        List<User> users = IOUtils.getDataJackson(USERS_LOCATION, User.class);
        List<Ticket> tickets = IOUtils.getDataJackson(TICKETS_LOCATION, Ticket.class);

        //Initialize indexes
        Index<Organization> orgIndex = new Index<>(Scope.ORGANIZATIONS, Organization.class, organizations);
        Index<User> userIndex = new Index<>(Scope.USERS, User.class, users);
        Index<Ticket> ticketIndex = new Index<>(Scope.TICKETS, Ticket.class, tickets);

        return Arrays.asList(orgIndex, userIndex, ticketIndex);
    }

}
