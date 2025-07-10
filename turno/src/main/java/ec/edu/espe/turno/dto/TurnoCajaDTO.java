package ec.edu.espe.turno.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoCajaDTO {
    
    private String id;
    private String codigoTurno;
    private String codigoCaja;
    private String codigoCajero;
    private LocalDateTime inicioTurno;
    private BigDecimal montoInicial;
    private LocalDateTime finTurno;
    private BigDecimal montoFinal;
    private String estado;
    private List<DenominacionDTO> denominacionesIniciales;
    private Long version;
} 