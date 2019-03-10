package io.github.binout.soccer.infrastructure.persistence.mongo

import io.github.binout.soccer.domain.date.MatchDate
import io.github.binout.soccer.domain.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mongolink.MongoSession
import java.time.Month

@ExtendWith(MongolinkExtension::class)
class MongoLeagueMatchDateRepositoryTest {

    private lateinit var repository: MongoLeagueMatchDateRepository
    private lateinit var playerRepository: MongoPlayerRepository

    @BeforeEach
    fun initRepository(currentSession: MongoSession) {
        playerRepository = MongoPlayerRepository { currentSession }
        repository = MongoLeagueMatchDateRepository { currentSession }
    }

    @Test
    fun should_persist_date_without_player() {
        repository.add(MatchDate.newDateForLeague(2016, Month.APRIL, 1))
        repository.session().flush()

        val matchDate = repository.byDate(2016, Month.APRIL, 1)
        assertThat(matchDate).isPresent
        assertThat(matchDate.get().id()).isNotNull()
        assertThat(matchDate.get().presents().count()).isZero()
    }

    @Test
    fun should_persist_date_with_player() {
        val benoit = Player("benoit")
        playerRepository.add(benoit)
        repository.session().flush()

        val date = MatchDate.newDateForLeague(2016, Month.APRIL, 1)
        date.present(benoit)
        repository.add(date)
        repository.session().flush()

        val matchDate = repository.byDate(2016, Month.APRIL, 1)
        assertThat(matchDate).isPresent
        assertThat(matchDate.get().id()).isNotNull()
        assertThat(matchDate.get().presents().count()).isEqualTo(1)
        assertThat(matchDate.get().presents().findFirst().get()).isEqualTo("benoit")
    }
}