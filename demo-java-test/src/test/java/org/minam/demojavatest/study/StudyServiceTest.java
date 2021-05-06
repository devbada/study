package org.minam.demojavatest.study;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minam.demojavatest.domain.Member;
import org.minam.demojavatest.domain.Study;
import org.minam.demojavatest.member.MemberService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Testcontainers
@Slf4j
//@ContextConfiguration(initializers = StudyServiceTest.ContainerPropertyInitialize.class)
class StudyServiceTest {

    @Mock MemberService memberService; // Mock만 있다고 되는게 아니다!!! - ExtendWith (Extension이 필요하다)
    @Mock StudyRepository studyRepository;

    @Autowired Environment environment;

    @LocalServerPort
    private int PORT;

    @Container
    static GenericContainer<?> postgreSQLContainer = new GenericContainer<>("postgres")
            .withReuse(true)
            .withEnv("POSTGRES_PASSWORD", "password")
            .withEnv("POSTGRES_DB", "studytest")
            .waitingFor(Wait.forListeningPort());

    @BeforeEach
    void beforeEach() {

        String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS"));
        log.info(formatDate);

        log.info("=======================");
        log.info("BeforeEach: 포트 : PORT = " + PORT);
        log.info("=======================");

        //log.info(environment.getProperty("container.port"));
        studyRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll() {
        // Container 내의 로그도 같이 출력 STDOUT 으로 출력됨.
        Slf4jLogConsumer slf4jLogConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(slf4jLogConsumer);

    }

    @Test
    void simpleTest() {
        log.info("simpleTest");
    }

    @Test
    void create_study_test() {
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

    @RepeatedTest(1000)
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
        then(memberService).should(data -> memberService.notify(study)).notify(study);
        //then(memberService).shouldHaveNoMoreInteractions(); // 에러가 나는게 정상 (바로 위의 구문 때문)
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");

        // TODO studyRepository Mock 객체의 save 메소드를 호출 시 study를 리턴하도록 만들기.
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

    @Test
    void collectionListTest() {

        Map<String, Object> paramObj = new HashMap<>();

        List<Map<String, Object>> list1 = new ArrayList<>();

        for (int i=0; i < 10; i++) {
            addObjectToListViaParam(list1, paramObj, i);
        }

        log.info(String.valueOf(list1.size()));

        List<LomBokTestClass> list2 = new ArrayList<>();
        for (int i=0; i < 10; i++) {
            LomBokTestClass lombokParam = LomBokTestClass.builder()
                                            .id(String.valueOf(i))
                                            .email("minam" + i +": mail")
                                            .name( i + "stat name").build();

            addLombokObjectToListViaParam(list2, lombokParam, i);

        }

        log.info(String.valueOf(list2.size()));

        List<LombokTest> list3 = new ArrayList<>();

        for (int i=0; i < 10; i++) {
            LombokTest lombokParam = LombokTest.builder()
                    .id(String.valueOf(i))
                    .email("minam" + i +": mail")
                    .name( i + "stat name").build();

            addLombokObjectToListViaParam2(list3, lombokParam, i);

        }

        log.info(String.valueOf(list3.size()));

    }

    private void addLombokObjectToListViaParam2(List<LombokTest> list, LombokTest lombokParam, int i) {
        list.add(lombokParam);
    }

    private void addObjectToListViaParam(List<Map<String, Object>> list, Map<String, Object> paramObj, int i) {
        paramObj.put("name", "minam.name is so good" + i);
        paramObj.put("id", "minam.id" + i);
        paramObj.put("address", "minam.address" + i);
        paramObj.put("email", "minam@mail.com" + i);

        list.add(paramObj);

    }

    private void addLombokObjectToListViaParam(List<LomBokTestClass> list, LomBokTestClass lomBokTestClassParam, int i) {
        list.add(lomBokTestClassParam);
    }

    @Getter
    @Setter
    @Builder
    static class LomBokTestClass {
        String name;
        String id;
        String value;
        String email;
    }

//    // Properties 가져오기
//    static class ContainerPropertyInitialize implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//        @Override
//        public void initialize(ConfigurableApplicationContext applicationContext) {
//            TestPropertyValues.of("container.port=" + postgreSQLContainer.getMappedPort(5432))
//            .applyTo(applicationContext.getEnvironment());
//        }
//    }
}