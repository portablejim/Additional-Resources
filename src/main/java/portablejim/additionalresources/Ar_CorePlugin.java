package portablejim.additionalresources;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

public class Ar_CorePlugin implements IFMLLoadingPlugin {


    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return "portablejim.additionalresources.Ar_ModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        String interestingKey = "mcLocation";
        if(data.containsKey(interestingKey) && data.get(interestingKey) instanceof File) {
            Ar_Reference.minecraftDirectory = (File) data.get("mcLocation");
        }
        interestingKey = "coremodLocation";
        if(data.containsKey(interestingKey) && data.get(interestingKey) instanceof File) {
            Ar_Reference.corePluginLocation = (File) data.get(interestingKey);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
