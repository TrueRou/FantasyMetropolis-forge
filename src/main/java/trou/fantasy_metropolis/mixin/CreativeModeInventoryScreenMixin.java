package trou.fantasy_metropolis.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import trou.fantasy_metropolis.capability.container.FakeContainer;
import trou.fantasy_metropolis.capability.container.WhiterSlot;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @Unique
    Slot fantasyMetropolis$cachedSlot = null;
    @Unique
    int fantasyMetropolis$cachedSlotIndex = 0;

    @Inject(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void selectTab(CreativeModeTab tab, CallbackInfo ci, CreativeModeTab creativeModeTab, AbstractContainerMenu abstractContainerMenu, int i, int n, int j) {
        var target = abstractContainerMenu.slots.get(i);
        if (target instanceof WhiterSlot) {
            fantasyMetropolis$cachedSlot = target;
            fantasyMetropolis$cachedSlotIndex = i;
            abstractContainerMenu.slots.set(i, new Slot(new FakeContainer(), 0, 0, 0));
        }
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void selectTab(CreativeModeTab tab, CallbackInfo ci) {
        assert Minecraft.getInstance().player != null;
        if (fantasyMetropolis$cachedSlot != null && fantasyMetropolis$cachedSlotIndex != 0) Minecraft.getInstance().player.inventoryMenu.slots.set(fantasyMetropolis$cachedSlotIndex, fantasyMetropolis$cachedSlot);
        fantasyMetropolis$cachedSlot = null; fantasyMetropolis$cachedSlotIndex = 0;
    }
}
