package com.redstoned.linker;

import net.fabricmc.api.ModInitializer;

public class Linker implements ModInitializer {
    
    @Override
    public void onInitialize() {
        MessageEvents.register();
    }
}

