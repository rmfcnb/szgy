package hu.elte.szgy.footballapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    public List<Team> findAll();
    public Optional<Team> findByName (String name);
}
