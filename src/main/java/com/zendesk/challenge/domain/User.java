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
public class User implements Entity {

    @JsonAlias("_id")
    @NonNull
    private String id; //chose to make this a string as it doesn't need to be subject to arithmetic operations

    private String url;

    @JsonAlias("external_id")
    private String externalId;

    private String name;

    private String alias;

    @JsonAlias("created_at")
    private Date createdAt;

    private String verified;

    private String active;

    private String shared;

    private String suspended;

    private String role;

    private String locale;

    private String timezone;

    private String email;

    private String phone;

    private String signature;


    @JsonAlias("last_login_at")
    private String lastLoginAt;

    @JsonAlias("organization_id")
    private String organizationId;

    private Set<String> tags;

    @Mapped("organizationId")
    private Organization organization;

    @Mapped("submitterId")
    private Set<Ticket> assignedTickets;

    @Mapped("submitterId")
    private Set<Ticket> submittedTickets;

}
