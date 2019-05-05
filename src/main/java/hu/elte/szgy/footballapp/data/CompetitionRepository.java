package hu.elte.szgy.footballapp.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    public List<Competition> findAll();
    public Competition findByName(String name);
}
