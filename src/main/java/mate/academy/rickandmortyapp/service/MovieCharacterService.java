package mate.academy.rickandmortyapp.service;

import java.util.List;
import mate.academy.rickandmortyapp.model.MovieCharacter;

public interface MovieCharacterService {
    MovieCharacter getRandomCharacter();

    List<MovieCharacter> findAllByNameContains(String namePart);
}
