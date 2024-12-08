package net.prouncedev.striply.mixin;

import net.minecraft.block.PillarBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.AxeItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.prouncedev.striply.Striply;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
	@Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
	public void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		if (player.getStackInHand(hand).getItem() instanceof AxeItem && player.getWorld().getBlockState(hitResult.getBlockPos()).getBlock() instanceof PillarBlock pillarBlock ) {
			String blockName = pillarBlock.getTranslationKey();

			if (blockName.endsWith("_log") && !blockName.contains("stripped")) {
				if (!Striply.strippingEnabled) {
					String messageKey = "message.striply.attempted_strip";
					Formatting color = Formatting.RED;
					player.sendMessage(
							Text.translatable(messageKey).formatted(color),
							true
					);
					cir.setReturnValue(ActionResult.FAIL);
					cir.cancel();
				}
			}
		}
	}
}