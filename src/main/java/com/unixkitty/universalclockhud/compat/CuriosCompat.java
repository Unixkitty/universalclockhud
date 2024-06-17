package com.unixkitty.universalclockhud.compat;

import com.unixkitty.universalclockhud.Config;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.concurrent.atomic.AtomicReference;

public class CuriosCompat
{
    public static final String MODID = "curios";
    private static boolean loaded = false;

    public static void init()
    {
        loaded = ModList.get().isLoaded(MODID);

        if (!loaded) return;

        InterModComms.sendTo(MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CURIO.getMessageBuilder().cosmetic().build());
    }

    public static boolean isLoaded()
    {
        return loaded;
    }

    public static ItemStack findCurio(Player player)
    {
        AtomicReference<ItemStack> result = new AtomicReference<>(ItemStack.EMPTY);

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> curiosInventory.findFirstCurio(Config.DEFAULT_CLOCK.getItem()).ifPresent(slotResult ->
                result.set(slotResult.stack())));

        return result.get();
    }
}
