package org.minam.demojavatest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Study {

    public Study (int limit) {
        if ( limit < 0 ) {
            throw new IllegalArgumentException("limit 은 0보다 커야 합니다.");
        }
        this.limit = limit;
    }

    private StudyStatus status = StudyStatus.DRAFT;

    private int limit;
}
