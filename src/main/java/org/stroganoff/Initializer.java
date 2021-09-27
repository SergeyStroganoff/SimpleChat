package org.stroganoff;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Initializer {
    public static final String APP_PROPERTIES = "/app.properties";

    public Properties getAppProp() throws IOException {
        InputStream inputStream = App.class.getResourceAsStream(APP_PROPERTIES);
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

}
