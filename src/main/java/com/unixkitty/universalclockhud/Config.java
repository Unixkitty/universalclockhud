package com.unixkitty.universalclockhud;

import com.unixkitty.universalclockhud.helpers.IntegratedPreset;
import com.unixkitty.universalclockhud.helpers.OverlayPositionHelper.AnchorPoint;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Objects;

public class Config
{
    public static final ItemStack DEFAULT_CLOCK = new ItemStack(Items.CLOCK);
    private static final ForgeConfigSpec CLIENT_CONFIG;

    public static final ForgeConfigSpec.BooleanValue clockHUDEnabled;
    public static final ForgeConfigSpec.BooleanValue clockHUDOnlyFullscreen;
    public static final ForgeConfigSpec.BooleanValue clockHUDHideInChat;
    public static final ForgeConfigSpec.BooleanValue clockHUDBackgroundEnabled;

    public static final ForgeConfigSpec.BooleanValue clockHUDIngameTime;
    public static final ForgeConfigSpec.BooleanValue clockHUDRequireInventoryClock;
    public static final ForgeConfigSpec.BooleanValue clockHUDGoCrazy;
    public static final ForgeConfigSpec.BooleanValue clockHUD12HFormat;
    public static final ForgeConfigSpec.BooleanValue clockHUD12HAppendAMPM;

    public static final ForgeConfigSpec.EnumValue<IntegratedPreset> clockPositionOption;
    public static final ForgeConfigSpec.EnumValue<AnchorPoint> customPresetAnchorPoint;
    public static final ForgeConfigSpec.IntValue customPresetXOffset;
    public static final ForgeConfigSpec.IntValue customPresetYOffset;

    static
    {
        ForgeConfigSpec.Builder clientConfig = new ForgeConfigSpec.Builder();

        clockHUDEnabled = clientConfig
                .comment("Whether or not Universal Clock should be displayed in the HUD.")
                .define("clockHudEnabled", true);

        clientConfig.push("General");
        {
            clockHUDOnlyFullscreen = clientConfig
                    .comment("Whether or not Universal Clock should be displayed only when in fullscreen mode.")
                    .define("clockHudOnlyInFullscreen", false);
            clockHUDHideInChat = clientConfig
                    .comment("Whether or not Universal Clock should be hidden when chat screen is opened.")
                    .define("clockHudHideInChat", false);
            clockHUDBackgroundEnabled = clientConfig
                    .comment("Whether or not Universal Clock should have background.")
                    .define("clockHudBackgroundEnabled", true);
            clockPositionOption = clientConfig
                    .comment("Position preset for Universal Clock, default option puts it to the right of the hotbar.")
                    .defineEnum("clockPositionOption", IntegratedPreset.HOTBAR_RIGHT);
            clockHUDIngameTime = clientConfig
                    .comment("Show in-game time instead of real-world time.")
                    .define("clockHUDIngameTime", false);
            clockHUDRequireInventoryClock = clientConfig
                    .comment("If true, Universal Clock will only be shown if you have vanilla clock in the inventory.")
                    .define("clockHUDRequireInventoryClock", false);
            clockHUDGoCrazy = clientConfig
                    .comment("Whether or not displayed time should spin uncontrollably when in Nether or The End.")
                    .define("clockHUDGoCrazy", true);
            clockHUD12HFormat = clientConfig
                    .comment("If true, 12-hour format will be used instead of 24-hour.")
                    .define("clockHUD12HFormat", false);
            clockHUD12HAppendAMPM = clientConfig
                    .comment("If true, 12-hour format will have AM/PM appended.")
                    .define("clockHUD12HAppendAMPM", true);
        }
        clientConfig.pop();

        clientConfig.comment("Adjustments which will take effect if 'CUSTOM' is chosen as HUD position preset.").push("Custom Preset Options");
        {
            customPresetAnchorPoint = clientConfig
                    .comment("Anchor point for custom preset. It is important if you want you preset to be compatible with different GUI scaling.")
                    .defineEnum("customPresetAnchorPoint", AnchorPoint.BOTTOM);
            customPresetXOffset = clientConfig
                    .comment("The offset on X axis from chosen anchor point.")
                    .defineInRange("customPresetXOffset", -80, Integer.MIN_VALUE, Integer.MAX_VALUE);
            customPresetYOffset = clientConfig
                    .comment("The offset on Y axis from chosen anchor point.")
                    .defineInRange("customPresetYOffset", -92, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        clientConfig.pop();

        CLIENT_CONFIG = clientConfig.build();
    }

    private static void reload(ModConfig config, ModConfig.Type type)
    {
        if (Objects.requireNonNull(type) == ModConfig.Type.CLIENT)
        {
            CLIENT_CONFIG.setConfig(config.getConfigData());
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event)
    {
        if (!event.getConfig().getModId().equals(UniversalClockHUD.MODID)) return;

        reload(event.getConfig(), event.getConfig().getType());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.Reloading event)
    {
        if (!event.getConfig().getModId().equals(UniversalClockHUD.MODID)) return;

        reload(event.getConfig(), event.getConfig().getType());
    }

    public static void register(ModLoadingContext modLoadingContext)
    {
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }
}
