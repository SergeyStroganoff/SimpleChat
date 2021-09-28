package org.stroganoff;

import org.stroganoff.exception.PropertiesException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Initializer {
    public static final String APP_PROPERTIES = "/app.properties";
    private final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public Properties getAppProp() throws IOException, PropertiesException {
        Properties properties = new Properties();
        InputStream inputStream = App.class.getResourceAsStream(APP_PROPERTIES);
        if (inputStream == null) {
            throw new PropertiesException("Файл настроек не найден");
        }
        properties.load(inputStream);
        if (!propertyVerify(properties)) {
            throw new PropertiesException("Файл настроек имеет неверный формат");
        }
        return properties;
    }

    public boolean propertyVerify(Properties properties) {
        String port = properties.getProperty("port");
        String serverIP = properties.getProperty("serverIP");

        if (!port.matches("^\\d{1,6}")) {
            return false;
        }
        return serverIP.matches(IPADDRESS_PATTERN);
    }
}
