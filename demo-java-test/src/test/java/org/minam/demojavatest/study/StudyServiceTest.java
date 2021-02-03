package org.minam.demojavatest.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minam.demojavatest.domain.Member;
import org.minam.demojavatest.domain.Study;
import org.minam.demojavatest.member.MemberService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class StudyServiceTest {

    @Mock MemberService memberService; // Mock만 있다고 되는게 아니다!!! - ExtendWith (Extension이 필요하다)
    @Mock StudyRepository studyRepository;

    @Test
    void createStudyService() {
        Member member = Member.builder()
                .id(1L)
                .email("member@member.com")
                .build();

        Optional<Member> optionalMember = memberService.findById(1L);
        assertNotNull(optionalMember.stream().map(Member::getId).toString());

        when(memberService.findById(1L)).thenReturn(Optional.of(member)); // Stubbing

        Study study = new Study(10, "minam");

        Optional<Member> findById = memberService.findById(1L);

        assertEquals("member@member.com", findById.get().getEmail());

        doThrow(new IllegalArgumentException()).when(memberService).validate(1L); // Stubbing

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L); // 호출되면 Throws 됨
        });

        StudyService studyService = new StudyService(memberService, studyRepository);

        Study newStudy = studyService.createNewStudy(1L, study);

        assertNotNull(study.getOwner());
        assertNotNull(studyService);

        assertEquals(study.getOwner().getName(), member.getName());

    }

    @Test
    @DisplayName("BDD 유형의 테스트")
    void testBddStyleCreateStudyService() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);

        assertNotNull(studyService);

        Member member = Member.builder()
                .id(1L)
                .email("givenMinam@minam.com")
                .build();

        Study study = new Study(20, "minam20");

        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(member, study.getOwner());
        then(memberService).should(Mockito.times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");

        // TODO studyRepository Mock 객체의 save 메소드를호출 시 study를 리턴하도록 만들기.
        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.openStudy(study);

        // Then
        // study의 status가 OPENED로 변경됐는지 확인
        assertEquals(study.getStatus(), StudyStatus.OPENED);

        // study의 openedDataTime이 null이 아닌지 확인
        assertNotNull(study.getOpenedDataTime());

        // memberService의 notify(study)가 호출 됐는지 확인.
        then(memberService).should(data -> memberService.notify(study) ).notify(study);
    }
}