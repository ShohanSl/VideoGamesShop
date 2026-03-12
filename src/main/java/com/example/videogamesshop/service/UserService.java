package com.example.videogamesshop.service;

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

    public List<UserShortResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toShortResponse)
                .toList();
    }

    public UserFullResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toFullResponse(user);
    }

    public UserFullResponse createUser(UserCreateRequest request) {
        User user = UserMapper.toEntity(request);
        User saved = userRepository.save(user);
        return UserMapper.toFullResponse(saved);
    }

    public UserFullResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        UserMapper.updateEntity(user, request);
        return UserMapper.toFullResponse(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        for (Game game : user.getGames()) {
            game.getLibraries().remove(user);
        }
        userRepository.delete(user);
    }

    public void addGameToUser(Long userId, Long gameId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        user.addGame(game);
    }

    public void removeGameFromUser(Long userId, Long gameId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        user.removeGame(game);
    }
}