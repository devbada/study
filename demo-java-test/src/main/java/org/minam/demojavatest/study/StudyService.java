package org.minam.demojavatest.study;

import org.minam.demojavatest.domain.Member;
import org.minam.demojavatest.domain.Study;
import org.minam.demojavatest.member.MemberService;

import java.time.LocalDateTime;
import java.util.Optional;

public class StudyService {
    private final MemberService memberService;

    private final StudyRepository studyRepository;

     public StudyService(MemberService memberService, StudyRepository studyRepository) {
         assert memberService != null;
         assert studyRepository != null;

         this.memberService = memberService;
         this.studyRepository = studyRepository;
     }

     public Study createNewStudy(Long memberId, Study study) {
         Optional<Member> member = memberService.findById(memberId);
         study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member doesn't exist for id: '{"+ memberId +"}'")));
         return studyRepository.save(study);
     }

    public Study openStudy(Study study) {
         study.setStatus(StudyStatus.OPENED);
         study.setOpenedDataTime(LocalDateTime.now());
         return studyRepository.save(study);
    }
}
