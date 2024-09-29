package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class ExampleMod implements ModInitializer {

	private final Map<PlayerEntity, Boolean> hitboxState = new HashMap<>();
	private static final double DEFAULT_HITBOX_HEIGHT = 2.0; // Стандартная высота хитбокса
	private static final double MODIFIED_HITBOX_HEIGHT = 2.0; // Высота измененного хитбокса

	@Override
	public void onInitialize() {
		// Обработчик события нажатия клавиши
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				// Проверяем, была ли нажата клавиша H
				if (isKeyPressed(GLFW.GLFW_KEY_H)) {
					toggleHitbox(client.world);
				}
			}
		});
	}

	// Метод для проверки, нажата ли клавиша
	private boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
	}

	private void toggleHitbox(World world) {
		// Проходим по всем игрокам в мире
		for (PlayerEntity player : world.getPlayers()) {
			// Проверяем текущее состояние хитбокса
			boolean isModified = hitboxState.getOrDefault(player, false);

			// Переключаем состояние хитбокса
			if (isModified) {
				resetHitbox(player);
			} else {
				changeHitbox(player);
			}

			// Обновляем состояние
			hitboxState.put(player, !isModified);
		}
	}

	private void changeHitbox(PlayerEntity player) {
		// Новые размеры хитбокса
		Box newHitbox = new Box(
				player.getX() - 0.5,
				player.getY(),
				player.getZ() - 0.5,
				player.getX() + 0.5,
				player.getY() + MODIFIED_HITBOX_HEIGHT,
				player.getZ() + 0.5
		);
		player.setBoundingBox(newHitbox);
	}

	private void resetHitbox(PlayerEntity player) {
		// Возвращаем хитбокс к стандартному размеру
		Box defaultHitbox = new Box(
				player.getX() - 0.5,
				player.getY(),
				player.getZ() - 0.5,
				player.getX() + 0.5,
				player.getY() + DEFAULT_HITBOX_HEIGHT,
				player.getZ() + 0.5
		);
		player.setBoundingBox(defaultHitbox);
	}
}