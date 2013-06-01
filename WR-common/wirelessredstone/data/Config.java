/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * Lesser General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>
 */
package wirelessredstone.data;

import net.minecraftforge.common.Configuration;
import wirelessredstone.core.WRCore;

public class Config {
    public static int rxID;
    public static int txID;

    public static void initConfig(Configuration config) {
        config.load();
        txID = config.get(Configuration.CATEGORY_BLOCK, "rxID", 179).getInt();
        rxID = config.get(Configuration.CATEGORY_BLOCK, "txID", 180).getInt();
        config.save();
    }
}
