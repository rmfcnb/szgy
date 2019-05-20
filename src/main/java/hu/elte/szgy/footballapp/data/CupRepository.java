package hu.elte.szgy.footballapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CupRepository extends JpaRepository<Cup, Integer> {
    public Cup findCupByName(String name);
}