package org.minam.demojavatest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Member {
    private Long id;
    private String name;
    private String email;
}
