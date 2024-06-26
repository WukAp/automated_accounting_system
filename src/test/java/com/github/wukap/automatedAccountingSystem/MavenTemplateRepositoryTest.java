package com.github.wukap.automatedAccountingSystem;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit-level testing for {@link Application} object.
 */
public class MavenTemplateRepositoryTest {

    @Test
    public void shouldCreateJavaRepositoryTemplateMain() {
        Application main = new Application();
        Assertions.assertNotNull(main);
    }

}
