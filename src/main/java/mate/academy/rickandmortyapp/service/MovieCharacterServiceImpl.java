package mate.academy.rickandmortyapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import mate.academy.rickandmortyapp.dto.external.ApiResponseDto;
import mate.academy.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import mate.academy.rickandmortyapp.model.MovieCharacter;
import mate.academy.rickandmortyapp.repository.MovieCharacterRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    @Scheduled(cron = "* 0 8 * * *")
    @Override
    public void syncExternalCharacters() {
        ApiResponseDto apiResponseDto = httpClient.get(API_CHARACTER_URL, ApiResponseDto.class);

        saveDtoToDb(apiResponseDto);

        while ((apiResponseDto.getInfo().getNext()) != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiResponseDto.class);
            saveDtoToDb(apiResponseDto);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);
        return movieCharacterRepository.getReferenceById(randomId);
    }

    @Override
    public List<MovieCharacter> findAllByNameContains(String namePart) {
        return movieCharacterRepository.findAllByNameContains(namePart);
    }

    private void saveDtoToDb(ApiResponseDto apiResponseDto) {
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
