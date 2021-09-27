package org.stroganoff;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Initializer {
    public static final String PROP_PATH = "src/org.stroganoff/resources/app.properties";   //"\\src\\test\\resources\\fileForTest";

    public Properties getAppProperties() throws IOException {
        String rootPath = new File("").getAbsolutePath();
        String filePath = rootPath + PROP_PATH;
        File file = new File(filePath);
        Properties properties = new Properties();
        FileReader fileReader = new FileReader(file);
        properties.load(fileReader);
        fileReader.close();
        return properties;
    }
}
