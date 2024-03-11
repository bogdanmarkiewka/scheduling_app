package bogdanmarkiewka.schedulingapp.user


import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

class UserServiceSpec extends Specification {

    UserService userService

    UserRepository userRepository
    UserMapper userMapper

    void setup() {
        userRepository = Stub(UserRepository.class)
        userMapper = Stub(UserMapper.class)

        userService = new UserService(
                userRepository,
                userMapper
        )
    }

    def "should create user successfully"() {
        given:
        UUID userId = UUID.randomUUID()
        String email = "test@test.pl"

        CreateUserDto createUserDto = new CreateUserDto("John Doe", email)
        UserEntity userEntity = createTestUser(userId)

        when:
        userRepository.existsByEmail(email) >> false
        userMapper.toEntity(createUserDto) >> userEntity
        userRepository.save(userEntity) >> userEntity

        then:
        UUID createdUserId = userService.create(createUserDto)
        createdUserId == userId
    }

    def "should throw UserAlreadyExistException if user with given email already exist"() {
        given:
        String email = "test@test.pl"

        CreateUserDto createUserDto = new CreateUserDto("John Doe", email)

        when:
        userRepository.existsByEmail(email) >> true

        and:
        userService.create(createUserDto)

        then:
        def exception = thrown(UserAlreadyExistException)
        exception.message == "User with given email already exist [email: ${email}]"
    }

    private static UserEntity createTestUser(UUID id) {
        UserEntity userEntity = new UserEntity()
        ReflectionTestUtils.setField(userEntity, "id", id)
        return userEntity
    }

}
