/*
 * This file is part of AdditionalResources.
 *
 * VeinMiner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * VeinMiner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with VeinMiner.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package portablejim.additionalresources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
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

    // Minecraft likes to request the wrong file, try to correct it.
    private ResourceLocation stripMcMeta(ResourceLocation input) {
        final String mcm = ".mcmeta";
        if(input != null && input.getResourcePath() != null && input.getResourcePath().length() > mcm.length() && input.getResourcePath().endsWith(mcm)) {
            return new ResourceLocation(input.getResourceDomain(), input.getResourcePath().substring(0, input.getResourcePath().length()-mcm.length()));
        }
        return input;
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        BufferedInputStream stream = new BufferedInputStream(new NullInputStream(0));
        try {
            ResourceLocation strippedLocation = stripMcMeta(resourceLocation);
            if(!resourceExists(resourceLocation) && resourceExists(strippedLocation)) {
                resourceLocation = strippedLocation;
            }
            File modFolder = new File(Ar_Reference.getDataFolder(), resourceLocation.getResourceDomain());
            File targetFile = new File(modFolder, resourceLocation.getResourcePath());
            stream = new BufferedInputStream(new FileInputStream(targetFile));
        }
        catch (Exception e) {
            FMLLog.getLogger().error("Error reading resource " + resourceLocation.toString());
        }
        ResourceLocation strippedLocation = stripMcMeta(resourceLocation);
        if(strippedLocation != resourceLocation) {
            return getInputStream(strippedLocation);
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
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        String fakePackMeta = "" +
                "{\n" +
                "  'pack': { \n" +
                "    'description': 'Additional resource files',\n" +
                "    'pack_format': 2\n" +
                "  }\n" +
                "}";
        fakePackMeta = fakePackMeta.replaceAll("'", "\"");
        JsonObject fakePackMetaJson = new JsonParser().parse(fakePackMeta).getAsJsonObject();

        return metadataSerializer.parseMetadataSection(metadataSectionName, fakePackMetaJson);
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
