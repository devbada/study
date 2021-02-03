package org.minam.demojavatest;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.minam.demojavatest.domain.Study;
import org.minam.demojavatest.study.StudyStatus;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayNameGeneration(ReplaceUnderscores.class)
@TestInstance(Lifecycle.PER_CLASS)
class StudyTest {

    int value = 1;

    @Order(1)
    @Test
    @DisplayName("Study 생성")
    void create_new_study() {
        Study study = new Study(value++, "minam");

       /* assertNotNull(study);

        // 기대하는 값을 앞, 실제 나오는 값을 오른쪽에 두면 되지만 변경되어도 테스트는 정상동작 함.
        // 테스트 실패 시 표시되는 메세지는 일반 문자열 연산을 사용하는 것보다 람다식을 사용하면 성능에 이점이 있을 수 있다.
        assertEquals(study.getStatus(), StudyStatus.DRAFT, () -> " 스터디의 처음 상태는 DRAFT여야 합니다.");

        assertTrue( study.getLimit() > 0, () -> "최대 참석인원은 0명보다 많아야 합니다.");
        */
        // Functional Interface () 여러개의 조건문을 한번에 실행할 수 있다.

        study.setLimit(100);
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(study.getStatus(), StudyStatus.DRAFT, () -> " 스터디의 처음 상태는 DRAFT여야 합니다."),
                () -> assertTrue( study.getLimit() > 0, () -> "최대 참석인원은 0명보다 많아야 합니다.")
        );

        assertThrows(IllegalArgumentException.class, () -> new Study(-1, "minus"));

        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(500, "duration");
            Thread.sleep(50);
        });
    }

    @Order(0)
    @FastTest
    @DisplayName("조건에 따라 테스트하기")
    void conditional_test() {
        System.out.println("Environment is " + System.getenv("LOCAL"));
        System.out.println(value++);
        String env = System.getenv("TEST_ENV");

        assumingThat("LOCAL".equalsIgnoreCase(env), () -> {
            System.out.println("local test");
            Study study = new Study(50, "local test");
            assertThat(study.getLimit()).isGreaterThan(10);
        });

        assumingThat(null == env, () -> {
            System.out.println("env is null but test is okay");
            Study study = new Study(100, "env is null");
            assertThat(study.getStatus()).isEqualTo(StudyStatus.DRAFT);
        });
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.WINDOWS})
    void onMacOthersTest() {
        System.out.println("Except MAC OS");
    }

    @FastTest
    @EnabledOnOs(OS.MAC)
    void onMacTest() {
        System.out.println("MAC Test");
    }

    @Test
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10})
    void run_on_jre_8 () {
        System.out.println("JRE 8 TEST");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "local")
    void environmentVariableTest() {
        System.out.println("You are running on 'local' variable environment");
    }

    @Test
    @DisplayName("create Test 입니다. 🥱") // emoji도 표현이 된다.
    void create() {
        System.out.println("create");
        Study study = new Study();
        assertNotNull(study);
    }

    @BeforeAll // 테스트 클래스 내의 여러 테스트가 모두 실행하기 전에 한번 호출됨, static 이어야 하고, void여야 하며 private 는 안됨. - 그러나, 테스트 순서 정하기에 따른 TestInstance를 클래스에 PER_CLASS로 지정하면 static이 없어도 된다)
    static void beforeAll() {
        System.out.println("BeforeAll");
    }

    @Test
    @Disabled // 테스트를 실행하지 않음
    void not_run_test() {
        System.out.println("NotRunning");
    }

    @AfterAll // 모든 테스트가 끝나고 한번 실행
    static void afterAll() {
        System.out.println("AfterAll");

    }

    @BeforeEach // 각각의 테스트가 실행될 때마다 실행. static이 아니어도 된다.
    void beforeEach() {
        System.out.println("BeforeEach");
    }

    @AfterEach // 각각의 테스트가 끝날 때마마다 실행,
    void afterEach() {
        System.out.println("AfterEach");

    }

    @Test
    void underscore_test() {
        System.out.println("underscore_test");
    }

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name="{index} {displayName} message={0}")
    @ValueSource(strings = {"겨울은", "가고", "봄이", "오고", "있어요"})
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name="{index} {displayName} message={0}")
    @EmptySource
    @NullSource
    @CsvSource({"하늘이", "맑아요"})
    void parameterized_sources_test(String message) {
        System.out.println(message);
    }

    @DisplayName("Limit Study 박살내기")
    @ParameterizedTest(name="{index} {displayName} limit={0}")
    @ValueSource(ints = {0, 1, 100, -1})
    void limitStudyPrameterizeTest(Integer limit) {
        Study study = new Study(limit, "limitStudyParamerized");
        System.out.println(study.getLimit());
    }
}