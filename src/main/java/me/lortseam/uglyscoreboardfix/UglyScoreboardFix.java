package me.lortseam.uglyscoreboardfix;

import lombok.Getter;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.uglyscoreboardfix.config.Settings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer {

    public static final String MOD_ID = "uglyscoreboardfix";
    @Getter
    private static Config config;

    @Override
    public void onInitializeClient() {
        config = Config.builder(UglyScoreboardFix.MOD_ID)
                .add(new Settings())
                .build();
    }

}
