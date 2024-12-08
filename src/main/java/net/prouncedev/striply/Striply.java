package net.prouncedev.striply;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Striply implements ModInitializer {
	public static final String MOD_ID = "striply";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static KeyBinding toggleKey;
	public static boolean strippingEnabled = false;

	@Override
	public void onInitialize() {
		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.striply.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F8,
				"category.striply"
		));

		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
			if (toggleKey.wasPressed()) {
				strippingEnabled = !strippingEnabled;
				String messageKey = strippingEnabled ? "message.striply.enabled" : "message.striply.disabled";
				Formatting color = strippingEnabled ? Formatting.GREEN : Formatting.RED;
                assert minecraftClient.player != null;
                minecraftClient.player.sendMessage(
						Text.translatable(messageKey).formatted(color),
						true
				);
			}
		});
	}
}