package io.github.binout.soccer.domain.season;

import io.github.binout.soccer.domain.player.Player;

import java.util.Map;

public class SeasonStatistics {

    private final Map<String, Long> gamesPlayed;

    SeasonStatistics(Map<String, Long> gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int nbPlayers() {
        return this.gamesPlayed.size();
    }

    public int gamesPlayed(Player player) {
        return this.gamesPlayed.getOrDefault(player.name(), 0L).intValue();
    }
}
