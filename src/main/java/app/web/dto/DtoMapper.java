package app.web.dto;

import app.fisherman.model.Fisherman;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

  public static EditProfileRequest fromFisherman(Fisherman fisherman) {

    return EditProfileRequest.builder()
        .username(fisherman.getUsername())
        .nickname(fisherman.getNickname())
        .imageUrl(fisherman.getImageUrl())
        .build();
  }
}
