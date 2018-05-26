package demo.service;

import demo.dto.UserDto;
import demo.entity.User;
import demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(UserDto userDto) {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        String pass = encoder.encodePassword(userDto.getPassword(), "");
        userDto.setPassword(pass);
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getRole(), userDto.getName());
        return userRepository.save(user);
    }

    @Override
    public void delete(int userId) {
        userRepository.delete(userId);
    }

    @Override
    public User update(UserDto userDto, Integer id) {
        if(id == null)
            return null;
        User user = userRepository.findById(id);
        user.setRole(userDto.getRole());
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        return userRepository.save(user);
    }
}
