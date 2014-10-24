package portablejim.additionalresources;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.util.StringTranslate;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 24/10/14
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Ar_ModContainer extends DummyModContainer {
    public Ar_ModContainer() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = "additionalresources";
        metadata.name = "Additional Resources";
        metadata.version = "0.0.1";
        metadata.authorList = Arrays.asList("Portablejim");
        metadata.description = "Load loose files as additional resources";
        metadata.url= "";
        metadata.updateUrl = "";
        metadata.screenshots = new String[0];
        metadata.logoFile = "";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Override
    public File getSource() {
        return Ar_CorePlugin.corePluginLocation;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return LooseFilesResourcePack.class;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        if(event.getSide() == Side.SERVER) {
            File baseDir = new File(Ar_CorePlugin.minecraftDirectory, Ar_Reference.DATA_FOLDERNAME);
            try {
                for(File modDir : baseDir.listFiles()) {
                    try {
                        File testFile = new File(modDir, "lang/en_US.lang");
                        if(testFile.exists() && testFile.canRead()) {
                            StringTranslate.inject(new FileInputStream(testFile));
                        }
                    }
                    catch (Exception e) {
                        FMLLog.getLogger().error("AdditionalResources: Error reading lang/en_us.lang for " + modDir.getName());
                    }
                }
            }
            catch (NullPointerException e) {
                FMLLog.getLogger().error("Additional Resources: Error reading from configuration directory " + baseDir.getName());
            }
        }
    }
}
