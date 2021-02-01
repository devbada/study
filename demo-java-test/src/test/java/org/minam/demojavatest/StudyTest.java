package org.minam.demojavatest;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StudyTest {


    @Test
    @DisplayName("Study 생성")
    void create_new_study() {
        Study study = new Study(0);

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

        assertThrows(IllegalArgumentException.class, () -> new Study(-1));

        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(500);
            Thread.sleep(50);
        });

    }

    @Test
    @DisplayName("create Test 입니다. 🥱") // emoji도 표현이 된다.
    void create() {
        System.out.println("create");
        Study study = new Study();
        assertNotNull(study);
    }

    @BeforeAll // 테스트 클래스 내의 여러 테스트가 모두 실행하기 전에 한번 호출됨, static 이어야 하고, void여야 하며 private 는 안됨.
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
}