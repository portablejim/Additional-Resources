package portablejim.additionalresources;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.FolderPackFinder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("additionalresources")
public class AdditionalResources {
    public static final Logger LOGGER = LogManager.getLogger();

    public static File dataDir;

    public AdditionalResources()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientStuff);
    }

    private void clientStuff(final FMLClientSetupEvent event)
    {
        LOGGER.warn("ClientStuff");
        Minecraft mc = event.getMinecraftSupplier().get();
        File targetFolder = new File(mc.gameDir, "mods-resourcepacks");
        dataDir = targetFolder;
        targetFolder.mkdir();
        mc.getResourcePackList().addPackFinder(new LooseFilesPackFinder());
    }
}
