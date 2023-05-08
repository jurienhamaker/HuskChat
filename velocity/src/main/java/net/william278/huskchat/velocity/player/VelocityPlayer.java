/*
 * This file is part of HuskChat, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.huskchat.velocity.player;

import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.audience.Audience;
import net.william278.huskchat.player.Player;
import net.william278.huskchat.velocity.HuskChatVelocity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Velocity implementation of a cross-platform {@link Player}
 */
public class VelocityPlayer implements Player {

    private static final HuskChatVelocity plugin = HuskChatVelocity.getInstance();

    private VelocityPlayer() {
    }

    private com.velocitypowered.api.proxy.Player player;

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public UUID getUuid() {
        return player.getUniqueId();
    }

    @Override
    public int getPing() {
        return (int) player.getPing();
    }

    @Override
    public String getServerName() {
        AtomicReference<ServerConnection> connection = new AtomicReference<>();
        player.getCurrentServer().ifPresent(connection::set);
        if (connection.get() != null) {
            return connection.get().getServerInfo().getName();
        }
        return null;
    }

    @Override
    public int getPlayersOnServer() {
        AtomicReference<ServerConnection> connection = new AtomicReference<>();
        player.getCurrentServer().ifPresent(connection::set);
        if (connection.get() != null) {
            return connection.get().getServer().getPlayersConnected().size();
        }
        return 0;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @NotNull
    @Override
    public Audience getAudience() {
        return player;
    }

    /**
     * Adapts a cross-platform {@link Player} to a Velocity {@link com.velocitypowered.api.proxy.Player} object
     *
     * @param player {@link Player} to adapt
     * @return The {@link com.velocitypowered.api.proxy.Player} object, {@code null} if they are offline
     */
    public static Optional<com.velocitypowered.api.proxy.Player> adaptVelocity(@NotNull Player player) {
        return plugin.getProxyServer().getPlayer(player.getUuid());
    }

    /**
     * Adapts a Velocity {@link com.velocitypowered.api.proxy.Player} to a cross-platform {@link Player} object
     *
     * @param player {@link com.velocitypowered.api.proxy.Player} to adapt
     * @return The {@link Player} object
     */
    @NotNull
    public static VelocityPlayer adaptCrossPlatform(com.velocitypowered.api.proxy.Player player) {
        VelocityPlayer velocityPlayer = new VelocityPlayer();
        velocityPlayer.player = player;
        return velocityPlayer;
    }
}
