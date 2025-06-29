package com.workout.tracker.mappers;

import com.workout.tracker.dto.UserCreateRequestDto;
import com.workout.tracker.dto.UserDto;
import com.workout.tracker.dto.UserRegisterDto;
import com.workout.tracker.dto.UserUpdateRequestDto;
import com.workout.tracker.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    User fromCreateRequest(UserCreateRequestDto dto);

    User fromRegisterRequest(UserRegisterDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateRequestDto dto, @MappingTarget User user);
}


