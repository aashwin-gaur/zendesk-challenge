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
public class Ticket implements Entity {

    @NonNull
    @JsonAlias("_id")
    private String id; //chose to make this a string as the only reason something should be a numerical value is if it needs to be subject to arithmetic operations

    private String url;

    @JsonAlias("external_id")
    private String externalId;

    private String type;

    private String subject;

    private String description;

    private String priority;

    @JsonAlias("created_at")
    private Date createdAt;

    private String status;

    @JsonAlias("submitter_id")
    private String submitterId;

    @JsonAlias("assignee_id")
    private String assigneeId;

    @JsonAlias("organization_id")
    private String organizationId;

    @JsonAlias("domain_names")
    private Set<String> domainNames;

    @JsonAlias("has_incidents")
    private String hasIncidents;

    private Set<String> tags;

    @JsonAlias("due_at")
    private Date dueAt;

    private String via;

    @Mapped("organizationId")
    private Organization organization;

    @Mapped("submitterId")
    private User submitter;

    @Mapped("assigneeId")
    private User assignee;

}
