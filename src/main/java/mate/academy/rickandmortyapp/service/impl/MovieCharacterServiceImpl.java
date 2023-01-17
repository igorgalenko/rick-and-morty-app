package mate.academy.rickandmortyapp.service.impl;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import mate.academy.rickandmortyapp.dto.external.ApiResponseDto;
import mate.academy.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import mate.academy.rickandmortyapp.model.MovieCharacter;
import mate.academy.rickandmortyapp.repository.MovieCharacterRepository;
import mate.academy.rickandmortyapp.service.HttpClient;
import mate.academy.rickandmortyapp.service.MovieCharacterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private static final String API_CHARACTER_URL = "https://rickandmortyapi.com/api/character";
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper characterMapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
                                     MovieCharacterRepository movieCharacterRepository,
                                     MovieCharacterMapper characterMapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.characterMapper = characterMapper;
    }

    @PostConstruct
    @Scheduled(cron = "* 0 8 * * *")
    public void syncExternalCharacters() {
        log.info("Starting of sync data with 3rd party API");
        ApiResponseDto responseDto = httpClient.get(API_CHARACTER_URL, ApiResponseDto.class);

        saveDtoToDb(responseDto);

        while ((responseDto.getInfo().getNext()) != null) {
            responseDto = httpClient.get(responseDto.getInfo().getNext(), ApiResponseDto.class);
            saveDtoToDb(responseDto);
        }
        log.info("Ending of sync data with 3rd party API");
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);
        log.info("Getting movie character by random id " + randomId);
        return movieCharacterRepository.getReferenceById(randomId);
    }

    @Override
    public List<MovieCharacter> findAllByNameContains(String namePart) {
        log.info("Finding movie characters by name part: " + namePart);
        return movieCharacterRepository.findAllByNameContains(namePart);
    }

    protected void saveDtoToDb(ApiResponseDto apiResponseDto) {
        Map<Long, MovieCharacter> charactersWithIds = Arrays.stream(apiResponseDto.getResults())
                .map(characterMapper::toModel)
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));

        Map<Long, Long> idsFromDb =
                movieCharacterRepository.findAllByExternalIdIn(charactersWithIds.keySet()).stream()
                        .collect(Collectors.toMap(MovieCharacter::getExternalId,
                                MovieCharacter::getId));

        List<MovieCharacter> charactersToDb = new ArrayList<>(charactersWithIds.values());
        charactersToDb.forEach(c -> c.setId(idsFromDb.get(c.getExternalId())));

        movieCharacterRepository.saveAll(charactersToDb);
    }
}
