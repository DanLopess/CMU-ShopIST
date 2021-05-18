package pt.ulisboa.tecnico.cmov.shopist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OutTimeRequestDTO {
    private String timestamp;
    private UUID uuid;
}
