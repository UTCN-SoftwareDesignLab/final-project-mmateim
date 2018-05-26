package demo.service;

import demo.dto.UserDto;
import demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User findByUsername(String username);
    User findById(Integer id);
    User create(UserDto user);
    void delete(int userId);
    User update(UserDto user, Integer id);
}
