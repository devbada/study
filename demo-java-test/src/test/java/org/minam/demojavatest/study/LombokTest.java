package org.minam.demojavatest.study;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LombokTest {
    String name;
    String id;
    String value;
    String email;
}
