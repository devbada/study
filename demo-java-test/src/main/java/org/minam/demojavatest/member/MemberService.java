package org.minam.demojavatest.member;

import org.minam.demojavatest.domain.Member;
import org.minam.demojavatest.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberService extends JpaRepository<Member, Long> {
    void validate(Long memberId);

    void notify(Study study);
}
