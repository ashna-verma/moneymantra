package in.ashna.moneymantra.repository;

import in.ashna.moneymantra.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository <ProfileEntity, Long>{

    //select * from tbl_profiles where email = ?
    Optional<ProfileRepository> findByEmail(String email);

    //select * from tbl_profiles where activation_token = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);

}
