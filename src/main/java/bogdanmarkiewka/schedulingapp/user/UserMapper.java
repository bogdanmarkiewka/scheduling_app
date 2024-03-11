package bogdanmarkiewka.schedulingapp.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(CreateUserDto createUserDto);

}

