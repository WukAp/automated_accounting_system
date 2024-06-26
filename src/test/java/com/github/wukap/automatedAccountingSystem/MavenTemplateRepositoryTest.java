package com.github.wukap.automatedAccountingSystem;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit-level testing for {@link MavenTemplateRepository} object.
 */
public class MavenTemplateRepositoryTest {

    @Test
    public void shouldCreateJavaRepositoryTemplateMain() {
        MavenTemplateRepository main = new MavenTemplateRepository();
        Assertions.assertNotNull(main);
    }

}
