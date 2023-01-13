package mate.academy.rickandmortyapp.controller;

import lombok.extern.log4j.Log4j2;
import mate.academy.rickandmortyapp.dto.ApiResponseDto;
import mate.academy.rickandmortyapp.service.HttpClient;
import mate.academy.rickandmortyapp.service.MovieCharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/demo")
public class DemoController {
    private final MovieCharacterService characterService;
    private final HttpClient httpClient;

    public DemoController(MovieCharacterService characterService, HttpClient httpClient) {
        this.characterService = characterService;
        this.httpClient = httpClient;
    }

    @GetMapping
    public String runDemo() {
        ApiResponseDto apiResponseDto =
            httpClient.get("https://rickandmortyapi.com/api/character", ApiResponseDto.class);
        log.info("Api response {}", apiResponseDto);
        characterService.syncExternalCharacters();
        return "Done!";
    }
}
