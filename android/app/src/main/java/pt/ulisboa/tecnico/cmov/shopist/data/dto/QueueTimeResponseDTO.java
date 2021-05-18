package pt.ulisboa.tecnico.cmov.shopist.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueTimeResponseDTO {
    private Integer meanTimeInQueueInLastHour;
    private Integer estimationTimeInQueue;
}
