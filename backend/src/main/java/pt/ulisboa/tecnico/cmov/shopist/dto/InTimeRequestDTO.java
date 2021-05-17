package pt.ulisboa.tecnico.cmov.shopist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class InTimeRequestDTO {
    private String timestamp;
    private Coordinates coordinates;
}
