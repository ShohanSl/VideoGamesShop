package com.example.videogamesshop.service;

import com.example.videogamesshop.cache.GameCacheService;
import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserShortResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.exception.GameNotFoundException;
import com.example.videogamesshop.exception.UserNotFoundException;
import com.example.videogamesshop.mapper.UserMapper;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameCacheService cacheService;

    public List<UserShortResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toShortResponse)
                .toList();
    }

    public UserFullResponse getUserById(Long id) {
        return UserMapper.toFullResponse(findUserById(id));
    }

    public UserFullResponse createUser(UserCreateRequest request) {
        User user = UserMapper.toEntity(request);
        User saved = userRepository.save(user);
        return UserMapper.toFullResponse(saved);
    }

    public UserFullResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findUserById(id);
        UserMapper.updateEntity(user, request);
        return UserMapper.toFullResponse(user);
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);
        for (Game game : user.getGames()) {
            game.getLibraries().remove(user);
        }
        userRepository.delete(user);
        cacheService.clear();
    }

    public void addGameToUser(Long userId, Long gameId) {
        updateUserGameRelation(userId, gameId, true);
    }

    public void removeGameFromUser(Long userId, Long gameId) {
        updateUserGameRelation(userId, gameId, false);
    }

    private void updateUserGameRelation(Long userId, Long gameId, boolean attachGame) {
        User user = findUserById(userId);
        Game game = findGameById(gameId);
        if (attachGame) {
            user.addGame(game);
            cacheService.clear();
            return;
        }
        user.removeGame(game);
        cacheService.clear();
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private Game findGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }
}
