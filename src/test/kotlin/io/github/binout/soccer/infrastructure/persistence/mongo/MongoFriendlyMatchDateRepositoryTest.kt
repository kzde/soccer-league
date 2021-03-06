package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.date.MatchDate
import io.github.binout.soccer.domain.player.Player
import io.github.binout.soccer.domain.player.PlayerName
import io.github.binout.soccer.domain.player.values
import io.github.binout.soccer.infrastructure.persistence.MongoConfiguration
import io.github.binout.soccer.infrastructure.persistence.MongoFriendlyMatchDateRepository
import io.github.binout.soccer.infrastructure.persistence.MongoPlayerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class MongoFriendlyMatchDateRepositoryTest {

    private lateinit var repository: MongoFriendlyMatchDateRepository
    private lateinit var playerRepository: MongoPlayerRepository

    @BeforeEach
    fun initRepository() {
        playerRepository = MongoPlayerRepository(MongoConfiguration("").database())
        repository = MongoFriendlyMatchDateRepository(MongoConfiguration("").database())
    }

    @Test
    fun should_persist_date_without_player() {
        repository.replace(MatchDate.newDateForFriendly(2016, Month.APRIL, 1))

        val matchDate = repository.byDate(2016, Month.APRIL, 1)
        assertThat(matchDate).isNotNull
        assertThat(matchDate!!.date).isEqualTo(LocalDate.of(2016, Month.APRIL, 1))
        assertThat(matchDate.presents().count()).isZero()
    }

    @Test
    fun should_persist_date_with_player() {
        val benoit = Player(PlayerName("benoit"))
        playerRepository.add(benoit)

        val date = MatchDate.newDateForFriendly(2016, Month.APRIL, 1)
        date.present(benoit)
        repository.replace(date)

        val matchDate = repository.byDate(2016, Month.APRIL, 1)
        assertThat(matchDate).isNotNull
        assertThat(matchDate!!.date).isEqualTo(LocalDate.of(2016, Month.APRIL, 1))
        assertThat(matchDate.presents().values()).containsOnly("benoit")
    }
}