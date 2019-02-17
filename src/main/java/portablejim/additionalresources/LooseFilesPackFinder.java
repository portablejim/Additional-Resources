package portablejim.additionalresources;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;

import java.util.Map;

public class LooseFilesPackFinder implements IPackFinder {
    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        String s = "mod:additionalresources";
        T packInfo = ResourcePackInfo.func_195793_a(s, true, () -> new LooseFilesResourcePack(AdditionalResources.dataDir), packInfoFactory, ResourcePackInfo.Priority.BOTTOM);
        if (packInfo != null) {
            nameToPackMap.put(s, packInfo);
        }
    }
}
