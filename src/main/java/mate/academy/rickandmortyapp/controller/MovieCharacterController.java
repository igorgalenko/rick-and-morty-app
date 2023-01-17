package mate.academy.rickandmortyapp.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import mate.academy.rickandmortyapp.dto.CharacterResponseDto;
import mate.academy.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import mate.academy.rickandmortyapp.model.MovieCharacter;
import mate.academy.rickandmortyapp.service.MovieCharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movie-characters")
public class MovieCharacterController {
    private final MovieCharacterService characterService;
    private final MovieCharacterMapper mapper;

    public MovieCharacterController(MovieCharacterService characterService,
                                    MovieCharacterMapper mapper) {
        this.characterService = characterService;
        this.mapper = mapper;
    }

    @GetMapping("/random")
    @ApiOperation(value = "return random movie character from DB")
    CharacterResponseDto getRandomCharacter() {
        MovieCharacter randomCharacter = characterService.getRandomCharacter();
        return mapper.toDto(randomCharacter);
    }

    @GetMapping("/by-name")
    @ApiOperation(value = "find all movie characters matched with part of a name")
    List<CharacterResponseDto> findAllByName(@RequestParam("name") String namePart) {
        return characterService.findAllByNameContains(namePart).stream()
                .map(mapper::toDto)
                .toList();
    }
}
