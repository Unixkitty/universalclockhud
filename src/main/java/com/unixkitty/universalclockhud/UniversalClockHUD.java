package com.unixkitty.universalclockhud;

import com.unixkitty.universalclockhud.compat.CuriosCompat;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(UniversalClockHUD.MODID)
public class UniversalClockHUD
{
    public static final String MODID = "universalclockhud";

    public UniversalClockHUD()
    {
        CuriosCompat.init();

        Config.register(ModLoadingContext.get());
    }
}
