package dev.skillter.blockgravity.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.skillter.blockgravity.BlockGravity;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import dev.skillter.blockgravity.config.ConfigEntriesEnum;



// TODO: Optimize the config read/write
//  Currently: I/O on every get() and set()
//  Optimal: Load to memory on startup. No I/O on get(). Add reload() method and config reload command.




public class Config {
    private static final String FILE_NAME = "config.json";
    private static final File FILE = new File(BlockGravity.INSTANCE.getDataFolder().getAbsolutePath(), FILE_NAME);

    //public static FileConfiguration config = BlockGravity.INSTANCE.getConfig();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger getLogger = BlockGravity.INSTANCE.getLogger();

    public static void initConfig() {

        try {
            if (!FILE.exists()) {
                getLogger.log(Level.INFO, "The config file doesn't exist. Initializing now... ");
                JsonObject generalCategory = new JsonObject(); // Make a category

                // Enumerate through all Enum entries and add every config option with a default value
                for (ConfigEntriesEnum entry : ConfigEntriesEnum.values()) {
                    generalCategory.addProperty(entry.toString(), entry.getDefaultValue());
                }

                try {
                    FILE.getParentFile().mkdirs();
                    FILE.createNewFile();
                    setContent(generalCategory);
                } catch (IOException e) {
                    String msg = "The config failed initializing: " + e.getCause().toString();
                    getLogger.log(Level.WARNING, msg, e.getCause());
                }
            }
        } catch (Exception e) {
            getLogger.log(Level.WARNING, "Failed to check if the config file already exists.", e.getCause());
        }
    }

    public static boolean get(ConfigEntriesEnum entry) throws IOException {
        return getContent().getAsJsonPrimitive(entry.toString()).getAsBoolean();
    }

    public static void set(ConfigEntriesEnum entry, boolean value) throws IOException {
        JsonObject generalCategory = getContent(); // Get the whole json config
        generalCategory.addProperty(entry.toString(), value); // Change the value of the specified entry
        setContent(generalCategory); // Set the whole json config file to our modified copy
    }


    private static JsonObject getContent() throws IOException {
        try (Reader reader = new FileReader(FILE)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private static void setContent(JsonObject generalCategory) throws IOException {
        try (Writer writer = new FileWriter(FILE, false)) {
            gson.toJson(generalCategory, writer);
        }
    }


}
