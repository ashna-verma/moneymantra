package in.ashna.moneymantra.service;

import in.ashna.moneymantra.dto.ProfileDTO;
import in.ashna.moneymantra.entity.ProfileEntity;
import in.ashna.moneymantra.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        ProfileEntity newProfile= toEntity(profileDTO);
        newProfile.setActivationToken();

        return profileDTO;
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        return ProfileEntity.builder()
                .id(ProfileDTO.getId())
                .fullName(ProfileDTO.getFullName())
                .email(ProfileDTO.getEmail())
                .password(ProfileDTO.getPassword())
                .profileImageUrl(ProfileDTO.getProfileImageUrl())
                .createdAt(ProfileDTO.getCreatedAt())
                .updatedAt(ProfileDTO.getUpdatedAt())
                .build();
    }

}
