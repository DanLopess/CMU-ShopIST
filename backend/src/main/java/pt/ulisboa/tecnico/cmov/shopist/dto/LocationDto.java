package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Double latitude;
    private Double longitude;
}
