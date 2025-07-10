package ec.edu.espe.turno.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoCajaCreacionDTO {
    
    private String codigoCaja;
    private String codigoCajero;
    private BigDecimal montoInicial;
    private List<DenominacionDTO> denominacionesIniciales;
} 