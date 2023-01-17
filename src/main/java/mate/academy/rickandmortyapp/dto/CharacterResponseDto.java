package mate.academy.rickandmortyapp.dto;

import lombok.Data;

@Data
public class CharacterResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private String status;
    private String gender;
}
