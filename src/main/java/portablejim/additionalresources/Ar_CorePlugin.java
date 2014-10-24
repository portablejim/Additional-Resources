package portablejim.additionalresources;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 24/10/14
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Ar_CorePlugin implements IFMLLoadingPlugin {

    public static File minecraftDirectory = new File("./");
    public static File corePluginLocation = null;

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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void injectData(Map<String, Object> data) {
        String interestingKey = "mcLocation";
        if(data.containsKey(interestingKey) && data.get(interestingKey) instanceof File) {
            minecraftDirectory = (File) data.get("mcLocation");
        }
        interestingKey = "coremodLocation";
        if(data.containsKey(interestingKey) && data.get(interestingKey) instanceof File) {
            corePluginLocation = (File) data.get(interestingKey);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
