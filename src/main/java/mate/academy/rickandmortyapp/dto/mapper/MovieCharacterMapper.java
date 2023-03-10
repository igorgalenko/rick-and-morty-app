package mate.academy.rickandmortyapp.dto.mapper;

import mate.academy.rickandmortyapp.dto.CharacterResponseDto;
import mate.academy.rickandmortyapp.dto.external.ApiCharacterDto;
import mate.academy.rickandmortyapp.model.Gender;
import mate.academy.rickandmortyapp.model.MovieCharacter;
import mate.academy.rickandmortyapp.model.Status;
import org.springframework.stereotype.Component;

@Component
public class MovieCharacterMapper {
    public MovieCharacter toModel(ApiCharacterDto dto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setExternalId(dto.getId());
        movieCharacter.setName(dto.getName());
        movieCharacter.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        movieCharacter.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        return movieCharacter;
    }

    public CharacterResponseDto toDto(MovieCharacter movieCharacter) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        responseDto.setId(movieCharacter.getId());
        responseDto.setName(movieCharacter.getName());
        responseDto.setStatus(movieCharacter.getStatus().name());
        responseDto.setGender(movieCharacter.getGender().name());
        responseDto.setExternalId(movieCharacter.getExternalId());
        return responseDto;
    }
}
