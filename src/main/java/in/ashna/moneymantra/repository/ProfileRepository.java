package in.ashna.moneymantra.repository;

import in.ashna.moneymantra.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository <ProfileEntity, Long>{

    Optional<ProfileRepository> findByEmail(String email);

}
