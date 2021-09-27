package org.stroganoff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {

    Initializer initializer = new Initializer();

    @Test
    void getAppProperties() {
    }

    @Test
    void getPropPath() {
        //GIVEN
        String expected = "M:\\PROGRAMMING\\EPAM\\EDUCATION\\Multithreating\\SimpleChat\\src\\main\\resources\\app.properties";
        //WHEN
        String actual = initializer.getPropPath();
        // THEN
        assertEquals(expected, actual);
    }
}