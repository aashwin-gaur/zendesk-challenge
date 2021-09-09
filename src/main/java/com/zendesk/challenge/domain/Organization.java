package com.zendesk.challenge.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Mapped;
import lombok.*;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(of = "id") //order by id field by default
public class Organization implements Entity {

    @NonNull
    @JsonAlias("_id")
    private String id; //chose to make this a string as the only reason something should be a numerical value is if it needs to be subject to arithmetic operations

    private String url;

    @JsonAlias("external_id")
    private String externalId;

    private String name;

    private String details;

    @JsonAlias("domain_names")
    private Set<String> domainNames;

    @JsonAlias("created_at")
    private Date createdAt;

    @JsonAlias("shared_tickets")
    private String sharedTickets;

    private Set<String> tags;

    @Mapped("organizationId")
    private Set<Ticket> tickets;

    @Mapped("organizationId")
    private Set<User> users;
}
