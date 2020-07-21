package com.noosphere.mental_central;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.noosphere.mental_central");

        noClasses()
            .that()
                .resideInAnyPackage("com.noosphere.mental_central.service..")
            .or()
                .resideInAnyPackage("com.noosphere.mental_central.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.noosphere.mental_central.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
