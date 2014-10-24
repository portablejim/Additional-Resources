package portablejim.additionalresources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.input.NullInputStream;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class LooseFilesResourcePack implements IResourcePack {
    ModContainer container;
    public LooseFilesResourcePack(ModContainer container) {
        this.container = container;
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        BufferedInputStream stream = new BufferedInputStream(new NullInputStream(0));
        try {
            File modFolder = new File(Ar_Reference.getDataFolder(), resourceLocation.getResourceDomain());
            File targetFile = new File(modFolder, resourceLocation.getResourcePath());
            stream = new BufferedInputStream(new FileInputStream(targetFile));
        }
        catch (Exception e) {
            FMLLog.getLogger().error("Error reading resource " + resourceLocation.toString());
        }
        return stream;
    }

    @Override
    public boolean resourceExists(ResourceLocation resourceLocation) {
        try {
            File modFolder = new File(Ar_Reference.getDataFolder(), resourceLocation.getResourceDomain());
            File targetFile = new File(modFolder, resourceLocation.getResourcePath());
            return targetFile.exists();
        }
        catch (Exception e) {
            FMLLog.getLogger().error("Error reading resource " + resourceLocation.toString());
        }
        return false;
    }

    @Override
    public Set getResourceDomains() {
        if(!Ar_Reference.getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            Ar_Reference.getDataFolder().mkdirs();
        }

        Set<String> modNames = new HashSet<String>();

        try {
            File configFolder = Ar_Reference.getDataFolder();
            //noinspection ConstantConditions
            for(File file : configFolder.listFiles()) {
                if(file.isDirectory()) {
                    modNames.add(file.getName());
                }
            }
        }
        catch (Exception e) {
            FMLLog.getLogger().error("Additional Resources: Error listing config folder files");
        }

        return modNames;
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer metadataSerializer, String p_135058_2_) throws IOException {
        String fakePackMeta = "" +
                "{\n" +
                "  'pack': { \n" +
                "    'description': 'Additional resource files',\n" +
                "    'pack_format': 1\n" +
                "  }\n" +
                "}";
        fakePackMeta = fakePackMeta.replaceAll("'", "\"");
        JsonObject fakePackMetaJson = new JsonParser().parse(fakePackMeta).getAsJsonObject();

        return metadataSerializer.parseMetadataSection(p_135058_2_, fakePackMetaJson);
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }

    @Override
    public String getPackName() {
        return "Additional Resources bonus resource pack";
    }
}
