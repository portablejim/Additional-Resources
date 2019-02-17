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

    @SuppressWarnings("NullableProblems")
    @Override
    public InputStream getInputStream(String resourceLocation) throws IOException {
        String targetLocation = resourceLocation;
        if(resourceLocation.startsWith("assets/"))
        {
            targetLocation = resourceLocation.substring("assets/".length());
        }
        else if("pack.mcmeta".equals(resourceLocation))
        {
            String pack_mcmeta = "{ \"pack\": { \"description\": \"Additional Resources resourcepack\", \"pack_format\": 4, \"_comment\": \"mods-resourcepacks\" } }";
            return new ByteArrayInputStream(pack_mcmeta.getBytes(StandardCharsets.UTF_8));
        }
        return super.getInputStream(targetLocation);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean resourceExists(String resourceLocation) {
        String targetLocation = resourceLocation;
        if(resourceLocation.startsWith("assets/"))
        {
            targetLocation = resourceLocation.substring("assets/".length());
        }
        else if("pack.mcmeta".equals(resourceLocation))
        {
            return true;
        }
        return super.resourceExists(targetLocation);
    }

    @SuppressWarnings("NullableProblems")
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
        return set;
    }

    private void resourceWalk(File p_199546_1_, int p_199546_2_, String p_199546_3_, List<ResourceLocation> p_199546_4_, String p_199546_5_, Predicate<String> p_199546_6_) {
        File[] afile = p_199546_1_.listFiles();
        if (afile != null) {
            for(File file1 : afile) {
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

    @SuppressWarnings("NullableProblems")
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String pathIn, int maxDepth, Predicate<String> filter) {
        List<ResourceLocation> output = Lists.newArrayList();
        if(type != null && "assets".equals(type.getDirectoryName()))
        {
            for(String s : this.getResourceNamespaces(type)) {
                this.resourceWalk(new File(new File(this.file, s), pathIn), maxDepth, s, output, pathIn + "/", filter);
            }
        }
        return output;
    }

    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
        return super.getMetadata(deserializer);
    }

    @SuppressWarnings("NullableProblems")
    @OnlyIn(Dist.CLIENT)
    public InputStream getRootResourceStream(String fileName) throws IOException {
        return super.getRootResourceStream(fileName);
    }
}
