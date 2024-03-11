package bogdanmarkiewka.schedulingapp.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UUID create(CreateUserDto createUserDto) {
        validateUser(createUserDto.email());

        UserEntity userEntity = userMapper.toEntity(createUserDto);
        UserEntity savedEntity = userRepository.save(userEntity);
        return savedEntity.getId();
    }

    private void validateUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException(email);
        }
    }

}
