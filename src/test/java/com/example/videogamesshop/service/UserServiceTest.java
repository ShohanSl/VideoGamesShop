package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserShortResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.exception.UserNotFoundException;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setUsername("alice");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserShortResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("alice", result.get(0).getUsername());
    }

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(2L);
        user.setUsername("bob");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        UserFullResponse result = userService.getUserById(2L);

        assertEquals(2L, result.getId());
        assertEquals("bob", result.getUsername());
    }

    @Test
    void shouldCreateUser() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("player_one");
        User saved = new User();
        saved.setId(1L);
        saved.setUsername("player_one");

        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserFullResponse result = userService.createUser(request);

        assertEquals(1L, result.getId());
        assertEquals("player_one", result.getUsername());
    }

    @Test
    void shouldUpdateUser() {
        User user = new User();
        user.setId(3L);
        user.setUsername("old_name");
        UserUpdateRequest request = new UserUpdateRequest();
        request.setUsername("new_name");
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        UserFullResponse result = userService.updateUser(3L, request);

        assertEquals("new_name", result.getUsername());
    }

    @Test
    void shouldAddGameToUser() {
        User user = new User();
        user.setId(1L);
        Game game = new Game();
        game.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));

        userService.addGameToUser(1L, 2L);

        assertEquals(1, user.getGames().size());
        assertSame(user, game.getLibraries().iterator().next());
    }

    @Test
    void shouldRemoveGameFromUser() {
        User user = new User();
        user.setId(4L);
        Game game = new Game();
        game.setId(5L);
        user.addGame(game);
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));
        when(gameRepository.findById(5L)).thenReturn(Optional.of(game));

        userService.removeGameFromUser(4L, 5L);

        assertEquals(0, user.getGames().size());
        assertEquals(0, game.getLibraries().size());
    }

    @Test
    void shouldDeleteUserAndCleanupRelations() {
        User user = new User();
        user.setId(1L);
        Game game = new Game();
        game.getLibraries().add(user);
        user.getGames().add(game);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertEquals(0, game.getLibraries().size());
        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowWhenUserMissing() {
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(50L));
    }
}
