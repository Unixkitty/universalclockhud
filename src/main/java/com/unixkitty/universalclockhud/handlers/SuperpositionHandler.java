package com.unixkitty.universalclockhud.handlers;

import com.unixkitty.universalclockhud.Config;
import com.unixkitty.universalclockhud.compat.CuriosCompat;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class SuperpositionHandler
{
    @SuppressWarnings("unchecked")
    public static ItemStack getVanillaClock(Player player)
    {
        Inventory inventory = player.getInventory();

        if (CuriosCompat.isLoaded())
        {
            ItemStack curio = CuriosCompat.findCurio(player);

            if (curio != ItemStack.EMPTY)
            {
                return curio;
            }
        }

        for (List<ItemStack> list : new List[]{inventory.armor, inventory.items, inventory.offhand})
        {
            for (ItemStack itemStack : list)
            {
                if (itemStack.getItem() == Items.CLOCK)
                {
                    return itemStack;
                }
            }
        }

        return ItemStack.EMPTY;
    }


    public static String get24hFromIngame(Level level)
    {
        return getTimeFromTicks(level, false);
    }

    public static String get12hFromTicks(Level level)
    {
        return getTimeFromTicks(level, true);
    }

    private static String getTimeFromTicks(Level level, boolean h12)
    {
        double ratio = 1000.0 / 60.0;

        int fullDaytime = (int) ((level.getDayTime() + 6000L) % 24000L);

        int dayTime = h12 ? (fullDaytime % 12000) : fullDaytime;
        int hours = dayTime / 1000;
        int minutes = (int) ((dayTime % 1000) / ratio);

        if (h12 && hours == 0)
        {
            hours = 12;
        }

        String text = (hours < 10 ? "0" : "") +
                hours + ":" +
                (minutes < 10 ? "0" : "") +
                minutes;

        if (h12 && Config.clockHUD12HAppendAMPM.get())
        {
            text += fullDaytime >= 12000 ? " PM" : " AM";
        }

        return text;
    }
}
