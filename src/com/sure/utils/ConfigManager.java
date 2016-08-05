
package com.sure.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;


/** 
 * This class is to manage the applicatin configruation.
 */
public class ConfigManager {


    private static Configuration config;

    private static ConfigManager instance;

    private ConfigManager() {
        loadConfig();
    }

    /**
     * Returns the singleton instance of ConfigManger.
     * 
     * @return the instance
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                instance = new ConfigManager();
            }
        }
        return instance;
    }

    /**
     * Loads the default configuration file.
     */
    public void loadConfig() {
        loadConfig("config.xml");
    }

    /**
     * Loads the specific configuration file.
     * 
     * @param configFileName the file name
     */
    public void loadConfig(String configFileName) {
        try {
            ConfigurationFactory factory = new ConfigurationFactory(
                    configFileName);
            config = factory.getConfiguration();
//            System.out.println("Configuration loaded: " + configFileName);
        } catch (Exception ex) {
            throw new RuntimeException("Configuration loading error: "
                    + configFileName, ex);
        }
    }

    /**
     * Returns the loaded configuration object.
     * 
     * @return the configuration
     */
    public Configuration getConfig() {
        return config;
    }

}
