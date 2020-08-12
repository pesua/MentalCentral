package com.noosphere.mental_central.service.mapper;

import com.noosphere.mental_central.domain.Authority;
import com.noosphere.mental_central.domain.User;
import com.noosphere.mental_central.domain.UserExtra;
import com.noosphere.mental_central.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public List<UserDTO> usersToUserDTOs(List<User> users, List<UserExtra> userExtras) {
        List<UserDTO> userDTOList = new ArrayList<>();

        users.stream()
            .filter(Objects::nonNull)
            .forEach(u -> {
                Optional<UserDTO> userDTO = userExtras.stream()
                    .filter(ue -> Objects.equals(ue.getId(), u.getId()))
                    .map(ue -> userToUserDTO(u, ue))
                    .findFirst();

                userDTO.ifPresent(userDTOList::add);
            });

        return userDTOList;
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public UserDTO userToUserDTO(User user, UserExtra userExtra) {
        return new UserDTO(user, userExtra);
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }


    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
