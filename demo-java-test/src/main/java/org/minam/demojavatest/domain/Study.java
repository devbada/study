package org.minam.demojavatest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.minam.demojavatest.study.StudyStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Study {

    public Study (int limit, String name) {
        if ( limit < 0 ) {
            throw new IllegalArgumentException("limit 은 0보다 커야 합니다.");
        }
        this.limit = limit;
        this.name = name;
    }

    private StudyStatus status = StudyStatus.DRAFT;

    private String name;

    private int limit;

    private Member owner;

    private LocalDateTime openedDataTime;

}
