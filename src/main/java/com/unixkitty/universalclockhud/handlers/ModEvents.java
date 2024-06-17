package com.unixkitty.universalclockhud.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.unixkitty.universalclockhud.Config;
import com.unixkitty.universalclockhud.UniversalClockHUD;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Calendar;
import java.util.Random;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = UniversalClockHUD.MODID, value = Dist.CLIENT)
public class ModEvents
{
    private static final Random theySeeMeRollin = new Random();
    private static final ResourceLocation CLOCK_BACKGROUNDS_LOCATION = new ResourceLocation(UniversalClockHUD.MODID, "textures/gui/clock_hud_backgrounds.png");

    @SubscribeEvent
    public static void onOverlayRender(final RenderGuiEvent.Post event)
    {
        if (!Config.clockHUDEnabled.get())
        {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        ItemStack clock = Config.DEFAULT_CLOCK;
        GuiGraphics guiGraphics = event.getGuiGraphics();

        if (Config.clockHUDHideInChat.get())
        {
            if (Minecraft.getInstance().screen instanceof ChatScreen)
            {
                return;
            }
        }

        if (Config.clockHUDOnlyFullscreen.get())
        {
            if (!Minecraft.getInstance().getWindow().isFullscreen())
            {
                return;
            }
        }

        if (minecraft.player == null || minecraft.level == null)
        {
            return;
        }

        if (Config.clockHUDRequireInventoryClock.get())
        {
            clock = SuperpositionHandler.getVanillaClock(minecraft.player);

            if (clock.isEmpty())
            {
                return;
            }
        }

        minecraft.getProfiler().push(UniversalClockHUD.MODID);

        RenderSystem.enableBlend();

        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        boolean longBackground = Config.clockHUD12HFormat.get() && Config.clockHUD12HAppendAMPM.get();

        PoseStack poseStack = event.getGuiGraphics().pose();
        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);

        Tuple<Integer, Integer> truePos = Config.clockPositionOption.get().calculatePosition(width, height);

        if (longBackground)
        {
            switch (Config.clockPositionOption.get())
            {
                case TOP -> truePos.setA(truePos.getA() - 6);
                case HOTBAR_LEFT, TOP_RIGHT, BOTTOM_RIGHT -> truePos.setA(truePos.getA() - 12);
            }
        }

        if (Config.clockHUDBackgroundEnabled.get())
        {
            int VHeight = 28;

            event.getGuiGraphics().blit(
                    CLOCK_BACKGROUNDS_LOCATION,
                    truePos.getA(),
                    truePos.getB(),
                    0,
                    isNighttime(minecraft.level) ? longBackground ? VHeight * 2 : 0 : longBackground ? VHeight * 3 : VHeight,
                    longBackground ? 78 : 66,
                    VHeight
            );
        }
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);

        String text;

        if (Config.clockHUDIngameTime.get())
        {
            text = Config.clockHUD12HFormat.get() ? SuperpositionHandler.get12hFromTicks(minecraft.level)
                    : SuperpositionHandler.get24hFromIngame(minecraft.level);
        }
        else
        {
            int hour = Calendar.getInstance().get(Config.clockHUD12HFormat.get() ? Calendar.HOUR : Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);

            if (Config.clockHUD12HFormat.get() && hour == 0)
            {
                hour = 12;
            }

            text = (hour <= 9 ? ("0" + hour) : ("" + hour)) + ":" + (minute <= 9 ? ("0" + minute) : ("" + minute));
        }

        if (Config.clockHUDGoCrazy.get())
        {
            if (minecraft.level.dimension() == Level.NETHER || minecraft.level.dimension() == Level.END)
            {
                String alt_text = "";
                for (int i = 0; i < text.length(); i++)
                {
                    alt_text = alt_text.concat(Character.isDigit(text.charAt(i)) ? "" + theySeeMeRollin.nextInt(10) : "" + text.charAt(i));
                }

                text = alt_text;
            }
        }

        Font textRenderer = minecraft.font;

        guiGraphics.renderItem(clock, truePos.getA() + 6, truePos.getB() + 6);

        //noinspection DataFlowIssue
        guiGraphics.drawString(textRenderer, text, truePos.getA() + 29, truePos.getB() + 10, ChatFormatting.GOLD.getColor());

        poseStack.popPose();

        RenderSystem.disableBlend();

        minecraft.getProfiler().pop();
    }

    private static boolean isNighttime(ClientLevel level)
    {
        final long l = level.getDayTime() % 24000L;

        return l >= 12542 && l <= 23460;
    }
}
