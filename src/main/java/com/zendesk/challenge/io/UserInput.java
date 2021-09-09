package com.zendesk.challenge.io;

import com.zendesk.challenge.domain.commons.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {
    private Scope category;
    private String field;
    private String value;
}
