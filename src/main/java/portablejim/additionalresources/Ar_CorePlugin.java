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


import java.io.File;
import java.util.Map;

public class Ar_CorePlugin {


    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return "portablejim.additionalresources.Ar_ModContainer";
    }

    public String getSetupClass() {
        return null;
    }

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

    public String getAccessTransformerClass() {
        return null;
    }
}
