package com.tkisor.llrcore.mixin.gtm;

import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVein;
import com.gregtechceu.gtceu.api.data.worldgen.ores.OreGenerator;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = OreGenerator.class, remap = false)
public class OreGeneratorMixin {

    @Inject(
            method = "generateOres(Lcom/gregtechceu/gtceu/api/data/worldgen/ores/OreGenerator$VeinConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/ChunkPos;)Ljava/util/Optional;",
            at = @At("RETURN"),
            cancellable = true)
    public void generateOres(
                             OreGenerator.VeinConfiguration config, WorldGenLevel level, ChunkPos chunkPos, CallbackInfoReturnable<Optional<GeneratedVein>> cir) {
        cir.setReturnValue(Optional.empty());
    }
}
