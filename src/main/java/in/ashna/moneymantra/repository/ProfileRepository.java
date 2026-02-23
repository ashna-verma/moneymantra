package in.ashna.moneymantra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository {

    Optional<ProfileRepository> findByEmail(String email);

}
