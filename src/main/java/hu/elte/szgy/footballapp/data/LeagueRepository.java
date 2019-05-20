package hu.elte.szgy.footballapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface LeagueRepository extends JpaRepository<League, Integer> {
    public League findLeagueByName(String name);
}
