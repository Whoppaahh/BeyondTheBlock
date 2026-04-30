package net.ryan.beyond_the_block.utils.battle;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class  BattleCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(" battle")
                .then(literal("start")
                        .then(argument("opponent", EntityArgumentType.player())
                                .then(argument("your ", EntityArgumentType.entity())
                                        .then(argument("opponent ", EntityArgumentType.entity())
                                                .executes(context -> {
                                                    ServerPlayerEntity playerA = context.getSource().getPlayer();
                                                    ServerPlayerEntity playerB = EntityArgumentType.getPlayer(context, "opponent");

                                                    if (!(EntityArgumentType.getEntity(context, "your ") instanceof LivingEntity fighterA)) {
                                                        context.getSource().sendError(Text.literal("Your fighter must be a living entity."));
                                                        return 0;
                                                    }

                                                    if (!(EntityArgumentType.getEntity(context, "opponent ") instanceof LivingEntity fighterB)) {
                                                        context.getSource().sendError(Text.literal("Opponent fighter must be a living entity."));
                                                        return 0;
                                                    }

                                                    if (! BattleUtil.isValidBattle (fighterA) || ! BattleUtil.isValidBattle (fighterB)) {
                                                        context.getSource().sendError(Text.literal("Both fighters must be valid battle  s."));
                                                        return 0;
                                                    }

                                                    if (! BattleUtil.isOwnedBy(fighterA, playerA.getUuid())) {
                                                        context.getSource().sendError(Text.literal("You do not own your selected  ."));
                                                        return 0;
                                                    }

                                                    if (! BattleUtil.isOwnedBy(fighterB, playerB.getUuid())) {
                                                        context.getSource().sendError(Text.literal("Opponent does not own their selected  ."));
                                                        return 0;
                                                    }

                                                     BattleRules rules = new  BattleRules();

                                                     BattleSession session = new  BattleSession(
                                                            playerA.getWorld(),
                                                            playerA.getBlockPos(),
                                                            playerA,
                                                            fighterA,
                                                            playerB,
                                                            fighterB,
                                                            rules
                                                    );

                                                     BattleManager.addSession(session);
                                                    session.start(playerA.getServer());

                                                    return 1;
                                                }))))));
    }
}