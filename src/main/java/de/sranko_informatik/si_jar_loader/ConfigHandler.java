package de.sranko_informatik.si_jar_loader;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigHandler {
    public static final Path configPath = Paths.get("./target/classes/application.yml");

    private static ConfigHandler configHandler;

    Configuration config;

    /**
     * Get instance of ConfigHandler
     * @return
     * @throws FileNotFoundException
     */
    public static ConfigHandler getInstance() throws FileNotFoundException {
        return getInstance(configPath);
    }

    /**
     * Get instance of ConfigHandler
     * @param configPath
     * @return
     * @throws FileNotFoundException
     */
    public static ConfigHandler getInstance(Path configPath) throws FileNotFoundException {
        if(configHandler == null) {
            configHandler = new ConfigHandler(configPath);
        }
        return configHandler;
    }

    /**
     * Constructor
     * @param configPath
     * @throws FileNotFoundException
     */
    private ConfigHandler(Path configPath) throws FileNotFoundException {
        this.config = loadConfig(configPath);
    }

    /**
     * Load config.yml
     * @param configPath
     * @throws FileNotFoundException
     */
    public Configuration loadConfig(Path configPath) throws FileNotFoundException {
        Constructor constructor = new Constructor(Configuration.class);
        Yaml yaml = new Yaml(constructor);
        return yaml.load(new FileInputStream(configPath.toFile()));
    }

    /**
     * Dump config to config.yml
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void dumpConfig() throws IllegalArgumentException, IllegalAccessException, IOException {
        dumpConfig(this.config, this.configPath);
    }

    /**
     * Dump config to config.yml
     * @param configPath
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void dumpConfig(Configuration config, Path configPath) throws IllegalArgumentException, IllegalAccessException, IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yml = new Yaml(options);
        yml.dump(config, new FileWriter(configPath.toFile()));
    }

    public Configuration getConfig() {
        return this.config;
    }
}
