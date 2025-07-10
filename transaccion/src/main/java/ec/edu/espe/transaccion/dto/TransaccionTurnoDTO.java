package ec.edu.espe.transaccion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionTurnoDTO {
    
    private String id;
    private String codigoTransaccion;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private String tipoTransaccion;
    private BigDecimal montoTotal;
    private List<DenominacionDTO> denominaciones;
    private LocalDateTime fechaTransaccion;
    private Long version;
} 