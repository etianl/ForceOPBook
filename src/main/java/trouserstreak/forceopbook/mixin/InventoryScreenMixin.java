//Credits to CrushedPixel for their first implementation of a forceOP sign module https://www.youtube.com/watch?v=KofDNaPZWfg

package trouserstreak.forceopbook.mixin;

import modes.CustomBookMenuScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<CreativeInventoryScreen.CreativeScreenHandler> {

	@Unique
	private final MinecraftClient mc = MinecraftClient.getInstance();

	public InventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory inventory, Text text) {
		super(screenHandler, inventory, text);
	}

	@Inject(method = "init", at = @At("TAIL"))
	protected void init(CallbackInfo ci) {
		addDrawableChild(new ButtonWidget.Builder(Text.literal("ForceOPBook"), this::openCustomMenu)
				.position(x + 50, y + 185)
				.size(100, 20)
				.build()
		);
	}

	@Unique
	private void openCustomMenu(ButtonWidget button) {
		mc.setScreen(new CustomBookMenuScreen(this));
	}
}