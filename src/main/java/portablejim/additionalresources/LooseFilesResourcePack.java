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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

public class LooseFilesResourcePack extends FolderPack {
    public LooseFilesResourcePack(File folder) {
        super(folder);
    }

    // Minecraft likes to request the wrong file, try to correct it.
    private ResourceLocation stripMcMeta(ResourceLocation input) {
        final String mcm = ".mcmeta";
        if(input != null && input.getPath() != null && input.getPath().length() > mcm.length() && input.getPath().endsWith(mcm)) {
            return new ResourceLocation(input.getNamespace(), input.getPath().substring(0, input.getPath().length()-mcm.length()));
        }
        return input;
    }

    private String stripMcMeta(String input) {
        final String mcm = ".mcmeta";
        if(input != null && input != null && input.length() > mcm.length() && input.endsWith(mcm)) {
            return input.substring(0, input.length()-mcm.length());
        }
        return input;
    }

    @Override
    public InputStream getInputStream(String resourceLocation) throws IOException {
        String targetLocation = resourceLocation;
        AdditionalResources.LOGGER.warn("Asked for file: " + resourceLocation);
        if(resourceLocation.startsWith("assets/"))
        {
            targetLocation = resourceLocation.substring("assets/".length());
        }
        else if("pack.mcmeta".equals(resourceLocation))
        {
            String pack_mcmeta = "{ \"pack\": { \"description\": \"Additional Resources resourcepack\", \"pack_format\": 4, \"_comment\": \"mods-resourcepacks\" } }";
            return new ByteArrayInputStream(pack_mcmeta.getBytes(StandardCharsets.UTF_8));
        }
        InputStream stream = super.getInputStream((targetLocation));
        //BufferedInputStream stream = new BufferedInputStream(new NullInputStream(0));
        /*try {
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
        }*/
        AdditionalResources.LOGGER.warn(stream);
        return stream;
    }

    @Override
    public boolean resourceExists(String resourceLocation) {
        String targetLocation = resourceLocation;
        AdditionalResources.LOGGER.warn("Asked if file exists: " + resourceLocation);
        if(resourceLocation.startsWith("assets/"))
        {
            targetLocation = resourceLocation.substring("assets/".length());
        }
        else if("pack.mcmeta".equals(resourceLocation))
        {
            return true;
        }
        boolean output = super.resourceExists((targetLocation));
        /*try {
            File modFolder = new File(Ar_Reference.getDataFolder(), resourceLocation.getResourceDomain());
            File targetFile = new File(modFolder, resourceLocation.getResourcePath());
            return targetFile.exists();
        }
        catch (Exception e) {
            FMLLog.getLogger().error("Error reading resource " + resourceLocation.toString());
        }*/
        AdditionalResources.LOGGER.warn(output);
        return output;
    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type) {
        Set<String> set = Sets.newHashSet();
        if("assets".equals(type.getDirectoryName()))
        {
            File[] afile = this.file.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
            if (afile != null) {
                for(File file2 : afile) {
                    String s = getRelativeString(this.file, file2);
                    if (s.equals(s.toLowerCase(Locale.ROOT))) {
                        set.add(s.substring(0, s.length() - 1));
                    } else {
                        this.onIgnoreNonLowercaseNamespace(s);
                    }
                }
            }
        }
        AdditionalResources.LOGGER.warn("Namespaces: " + type.getDirectoryName() + " => " + set.size());

        return set;
    }

    private void resourceWalk(File p_199546_1_, int p_199546_2_, String p_199546_3_, List<ResourceLocation> p_199546_4_, String p_199546_5_, Predicate<String> p_199546_6_) {
        File[] afile = p_199546_1_.listFiles();
        AdditionalResources.LOGGER.warn("Walk1Pre1: " + p_199546_1_);
        AdditionalResources.LOGGER.warn("Walk1Pre2: " + afile);
        if (afile != null) {
            for(File file1 : afile) {
                AdditionalResources.LOGGER.warn("Walk1: " + "assets/" + p_199546_5_ + file1.getName());
                AdditionalResources.LOGGER.warn("Walk2: " + p_199546_5_ + file1.getName());
                if (file1.isDirectory()) {
                    if (p_199546_2_ > 0) {
                        this.resourceWalk(file1, p_199546_2_ - 1, p_199546_3_, p_199546_4_, p_199546_5_ + file1.getName() + "/", p_199546_6_);
                    }
                } else if (!file1.getName().endsWith(".mcmeta") && p_199546_6_.test(file1.getName())) {
                    try {
                        p_199546_4_.add(new ResourceLocation(p_199546_3_, p_199546_5_ + file1.getName()));
                    } catch (ResourceLocationException resourcelocationexception) {
                        AdditionalResources.LOGGER.error(resourcelocationexception.getMessage());
                    }
                }
            }
        }

    }

    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String pathIn, int maxDepth, Predicate<String> filter) {
        AdditionalResources.LOGGER.warn("WalkStart: " + type.getDirectoryName() + " - " + pathIn);
        List<ResourceLocation> output = Lists.newArrayList();
        if(type != null && "assets".equals(type.getDirectoryName()))
        {
            for(String s : this.getResourceNamespaces(type)) {
                this.resourceWalk(new File(new File(this.file, s), pathIn), maxDepth, s, output, pathIn + "/", filter);
            }
        }
        AdditionalResources.LOGGER.warn("WalkFinish: " + output.size());
        return output;
    }

    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
        T output = super.getMetadata(deserializer);
        AdditionalResources.LOGGER.warn(deserializer.getSectionName());
        return output;
    }

    @OnlyIn(Dist.CLIENT)
    public InputStream getRootResourceStream(String fileName) throws IOException {
        InputStream output = super.getRootResourceStream(fileName);
        return output;
    }






    /*
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
    */
}
