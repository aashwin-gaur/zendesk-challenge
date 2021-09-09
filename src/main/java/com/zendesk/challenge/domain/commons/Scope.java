package com.zendesk.challenge.domain.commons;

import com.zendesk.challenge.domain.Organization;
import com.zendesk.challenge.domain.Ticket;
import com.zendesk.challenge.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Scope {

    ORGANIZATIONS("organizations", Organization.class),
    TICKETS("tickets", Ticket.class),
    USERS("users", User.class);

    @Getter
    @NonNull
    private String value;

    @Getter
    private Class scopeClass;

    public static Scope byClass(Class clazz) {
        return Arrays.stream(Scope.values())
                .filter(s -> s.getScopeClass().equals(clazz))
                .findFirst().orElseThrow(RuntimeException::new);
    }

}
