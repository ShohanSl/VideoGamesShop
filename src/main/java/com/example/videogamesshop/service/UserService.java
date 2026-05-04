package com.example.videogamesshop.service;

import com.example.videogamesshop.cache.GameCacheService;
import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserShortResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.entity.UserRole;
import com.example.videogamesshop.exception.GameNotFoundException;
import com.example.videogamesshop.exception.UserNotFoundException;
import com.example.videogamesshop.mapper.UserMapper;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameCacheService cacheService;
    private final PasswordEncoder passwordEncoder;

    public List<UserShortResponse> getAllUsers() {
        return userRepository.findAllByRoleOrderByUsernameAsc(UserRole.USER).stream()
                .map(UserMapper::toShortResponse)
                .toList();
    }

    public UserFullResponse getUserById(Long id) {
        return UserMapper.toFullResponse(findRegularUserById(id));
    }

    public UserFullResponse createUser(UserCreateRequest request) {
        User user = UserMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        User saved = userRepository.save(user);
        return UserMapper.toFullResponse(saved);
    }

    public UserFullResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findRegularUserById(id);
        UserMapper.updateEntity(user, request);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        return UserMapper.toFullResponse(user);
    }

    public void deleteUser(Long id) {
        User user = findRegularUserById(id);
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
        User user = findRegularUserById(userId);
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

    private User findRegularUserById(Long id) {
        return userRepository.findByIdAndRole(id, UserRole.USER)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private Game findGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }
}
