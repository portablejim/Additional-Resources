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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.util.StringTranslate;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class Ar_ModContainer extends DummyModContainer {
    public Ar_ModContainer() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = "additionalresources";
        metadata.name = "Additional Resources";
        metadata.version = "0.1.0";
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
        return Ar_Reference.corePluginLocation;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return LooseFilesResourcePack.class;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        if(event.getSide() == Side.SERVER) {
            File baseDir = new File(Ar_Reference.minecraftDirectory, Ar_Reference.DATA_FOLDERNAME);
            try {
                //noinspection ConstantConditions
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
