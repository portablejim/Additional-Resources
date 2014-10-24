package portablejim.additionalresources;

import java.io.File;

public class Ar_Reference {
    public static final String DATA_FOLDERNAME = "Additional_Resources";
    public static File minecraftDirectory = new File("./");
    public static File corePluginLocation = null;

    public static File getDataFolder() {
        return new File(minecraftDirectory, DATA_FOLDERNAME);
    }
}
