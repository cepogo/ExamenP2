package ec.edu.espe.turno.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoCajaCierreDTO {
    
    private String codigoTurno;
    private BigDecimal montoFinal;
    private List<DenominacionDTO> denominacionesFinales;
} 