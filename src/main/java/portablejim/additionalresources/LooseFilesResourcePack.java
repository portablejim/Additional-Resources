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

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 24/10/14
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class LooseFilesResourcePack implements IResourcePack {
    ModContainer container;
    public LooseFilesResourcePack(ModContainer container) {
        this.container = container;
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        BufferedInputStream stream = new BufferedInputStream(new NullInputStream(0));
        try {
            File modFolder = new File(Ar_Reference.DATA_FOLDER, resourceLocation.getResourceDomain());
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
            File modFolder = new File(Ar_Reference.DATA_FOLDER, resourceLocation.getResourceDomain());
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
        if(!Ar_Reference.DATA_FOLDER.exists()) {
            Ar_Reference.DATA_FOLDER.mkdirs();
        }

        Set<String> modNames = new HashSet<String>();
        File configFolder = Ar_Reference.DATA_FOLDER;
        for(File file : configFolder.listFiles()) {
            if(file.isDirectory()) {
                modNames.add(file.getName());
            }
        }

        return modNames;
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer metadataSerializer, String p_135058_2_) throws IOException {
        String fakePackMeta = "" +
                " 'pack': { \n" +
                "     'description': 'Additional resource files',\n" +
                "     'pack_format': 1\n" +
                "}";
        fakePackMeta.replaceAll("'", "\"");
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
