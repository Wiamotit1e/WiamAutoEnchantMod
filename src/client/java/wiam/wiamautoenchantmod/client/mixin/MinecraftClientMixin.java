package wiam.wiamautoenchantmod.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wiam.wiamautoenchantmod.client.AutoEnchantSign;
import wiam.wiamautoenchantmod.client.AutoEnchanterController;
import wiam.wiamautoenchantmod.client.WiamUtil;
import wiam.wiamautoenchantmod.client.config.ConfigService;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (currentScreen == null) {
            WiamUtil.INSTANCE.setAutoEnchantSign(AutoEnchantSign.FINISHING);
            WiamUtil.INSTANCE.getExcludedItems().clear();
            WiamUtil.INSTANCE.setCounterOfWaitingForResult(0);
            return;
        }
        if(this.player == null || this.interactionManager == null) return;
        if (!(currentScreen instanceof EnchantmentScreen screen)) return;
        AutoEnchanterController.INSTANCE.autoEnchant(screen, interactionManager, player, ConfigService.INSTANCE.getConfig().getRules());
    }

}
