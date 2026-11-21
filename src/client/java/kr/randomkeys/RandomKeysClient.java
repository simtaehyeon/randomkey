package kr.randomkeys;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class RandomKeysClient implements ClientModInitializer {
    private static final Map<KeyMapping, InputConstants.Key> ORIGINAL = new IdentityHashMap<>();

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("randomkeys")
                .then(ClientCommandManager.literal("shuffle").executes(context -> {
                    shuffle(context.getSource());
                    return 1;
                }))
                .then(ClientCommandManager.literal("reset").executes(context -> {
                    reset(context.getSource());
                    return 1;
                }))
            )
        );
    }

    private static void shuffle(net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source) {
        KeyMapping[] mappings = Minecraft.getInstance().options.keyMappings;
        if (ORIGINAL.isEmpty()) {
            for (KeyMapping mapping : mappings) ORIGINAL.put(mapping, mapping.getKey());
        }

        List<InputConstants.Key> keys = new ArrayList<>();
        for (KeyMapping mapping : mappings) keys.add(mapping.getKey());
        Collections.shuffle(keys);

        for (int i = 0; i < mappings.length; i++) mappings[i].setKey(keys.get(i));
        KeyMapping.resetMapping();
        source.sendFeedback(Component.literal("[Random Keys] 모든 조작키를 무작위로 섞었습니다!"));
    }

    private static void reset(net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source) {
        if (ORIGINAL.isEmpty()) {
            source.sendFeedback(Component.literal("[Random Keys] 아직 섞은 기록이 없습니다."));
            return;
        }
        for (Map.Entry<KeyMapping, InputConstants.Key> entry : ORIGINAL.entrySet()) {
            entry.getKey().setKey(entry.getValue());
        }
        KeyMapping.resetMapping();
        source.sendFeedback(Component.literal("[Random Keys] 원래 키 설정으로 복구했습니다!"));
    }
}
