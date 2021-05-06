package org.minam.demojavatest.study;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@Disabled
public class ArchTests {

    @Test
    void packageDependencyTests() {
        JavaClasses classes = new ClassFileImporter().importPackages("org.minam.demojavatest");

        ArchRule domainPackageRule = classes().that().resideInAPackage("..domain..").should().onlyBeAccessed().byAnyPackage("..study..", "..member..");

        domainPackageRule.check(classes);

    }
}
