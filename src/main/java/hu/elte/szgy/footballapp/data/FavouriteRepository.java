package hu.elte.szgy.footballapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {
    public Optional<Favourite> findByUser(User user);
}
