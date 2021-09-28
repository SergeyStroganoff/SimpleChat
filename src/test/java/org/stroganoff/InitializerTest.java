package org.stroganoff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stroganoff.exception.PropertiesException;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitializerTest {

    Initializer initializer = new Initializer();
    Properties properties;

    @BeforeEach
    void init() throws PropertiesException, IOException {
        properties = initializer.getAppProp();
    }

    @Test
    void getAppProp_Return_Expected_Port() {
        //GIVEN
        int expectedPort = 3128;
        //WHEN
        int actualPort = Integer.parseInt(properties.getProperty("port"));
        //THEN
        assertEquals(expectedPort, actualPort);
    }

    @Test
    void propertyVerify_Must_Return_True() {
        //WHEN
        boolean actual = initializer.propertyVerify(properties);
        //THEN
        assertTrue(actual);
    }
}