package com.locarie.backend.services.favorite;

import com.locarie.backend.domain.dto.user.UserDto;
import java.util.List;

public interface FavoriteBusinessService {
  void favoriteBusiness(Long userId, Long businessId);

  void unfavoriteBusiness(Long userId, Long businessId);

  List<UserDto> listFavoriteBusinesses(Long userId);

  List<UserDto> listFavoredBy(Long businessId);
}
