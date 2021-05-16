package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueTimeResponseDTO {
    private Integer meanTimeInLastHourInSeconds;
    private Integer queueCount;
}
