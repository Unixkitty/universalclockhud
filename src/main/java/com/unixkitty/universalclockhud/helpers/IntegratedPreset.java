package com.unixkitty.universalclockhud.helpers;

import com.unixkitty.universalclockhud.Config;
import com.unixkitty.universalclockhud.helpers.OverlayPositionHelper.AnchorPoint;
import com.unixkitty.universalclockhud.helpers.OverlayPositionHelper.OverlayPosition;
import net.minecraft.util.Tuple;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public enum IntegratedPreset
{
    HOTBAR_RIGHT(AnchorPoint.BOTTOM, 95, -31),
    HOTBAR_LEFT(AnchorPoint.BOTTOM, -161, -31),
    BOTTOM_LEFT(AnchorPoint.BOTTOM_LEFT, 2, -30),
    BOTTOM_RIGHT(AnchorPoint.BOTTOM_RIGHT, -68, -30),
    TOP_LEFT(AnchorPoint.TOP_LEFT, 1, 2),
    TOP_RIGHT(AnchorPoint.TOP_RIGHT, -67, 2),
    TOP(AnchorPoint.TOP, -33, 4),
    CUSTOM(Config.customPresetAnchorPoint, Config.customPresetXOffset, Config.customPresetYOffset);

    private final OverlayPosition position;

    IntegratedPreset(AnchorPoint point, int xOffset, int yOffset)
    {
        this(() -> point, () -> xOffset, () -> yOffset);
    }

    IntegratedPreset(Supplier<AnchorPoint> pointSupplier, Supplier<Integer> xOffsetSupplier, Supplier<Integer> yOffsetSupplier)
    {
        this.position = new OverlayPosition(pointSupplier, xOffsetSupplier, yOffsetSupplier);
    }

    public Tuple<Integer, Integer> calculatePosition(int scaledWidth, int scaledHeight)
    {
        return this.position.calculatePosition(scaledWidth, scaledHeight);
    }

}
