package ec.edu.espe.transaccion.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionTurnoCreacionDTO {
    
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private String tipoTransaccion;
    private BigDecimal montoTotal;
    private List<DenominacionDTO> denominaciones;
} 