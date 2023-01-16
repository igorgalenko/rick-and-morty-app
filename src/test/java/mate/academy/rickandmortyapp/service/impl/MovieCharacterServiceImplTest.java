package mate.academy.rickandmortyapp.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import mate.academy.rickandmortyapp.dto.external.ApiCharacterDto;
import mate.academy.rickandmortyapp.dto.external.ApiInfoDto;
import mate.academy.rickandmortyapp.dto.external.ApiResponseDto;
import mate.academy.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import mate.academy.rickandmortyapp.model.Gender;
import mate.academy.rickandmortyapp.model.MovieCharacter;
import mate.academy.rickandmortyapp.model.Status;
import mate.academy.rickandmortyapp.repository.MovieCharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieCharacterServiceImplTest {
    @Mock
    private MovieCharacterRepository characterRepository;
    @Mock
    private MovieCharacterMapper mapper;
    @InjectMocks
    private MovieCharacterServiceImpl characterService;

    @Test
    void shouldSaveWithoutExceptions() {
        ApiResponseDto responseDto = new ApiResponseDto();
        ApiCharacterDto rickDto = new ApiCharacterDto();
        rickDto.setId(1L);
        rickDto.setName("Rick");
        rickDto.setGender(Gender.MALE.name());
        rickDto.setStatus(Status.ALIVE.name());

        ApiCharacterDto mortyDto = new ApiCharacterDto();
        mortyDto.setId(2L);
        mortyDto.setName("Morty");
        mortyDto.setGender(Gender.MALE.name());
        mortyDto.setStatus(Status.ALIVE.name());

        MovieCharacterMapper characterMapper = new MovieCharacterMapper();
        MovieCharacter rick = characterMapper.toModel(rickDto);
        MovieCharacter morty = characterMapper.toModel(mortyDto);

        responseDto.setInfo(new ApiInfoDto());
        responseDto.setResults(new ApiCharacterDto[]{rickDto, mortyDto});

        Mockito.when(characterRepository.saveAll(any(List.class))).thenReturn(List.of(rick, morty));
        Mockito.when(mapper.toModel(rickDto)).thenReturn(rick);
        Mockito.when(mapper.toModel(mortyDto)).thenReturn(morty);

        characterService.saveDtoToDb(responseDto);

        Mockito.verify(characterRepository, Mockito.times(1)).saveAll(List.of(rick, morty));
    }
}
