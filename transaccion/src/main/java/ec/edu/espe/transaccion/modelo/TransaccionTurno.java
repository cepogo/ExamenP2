package ec.edu.espe.transaccion.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transacciones_turno")
public class TransaccionTurno {

    @Id
    private String id;
    
    @Indexed(unique = true)
    private String codigoTransaccion;
    
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private String tipoTransaccion; // INICIO, AHORRO, DEPOSITO, CIERRE
    private BigDecimal montoTotal;
    private List<Denominacion> denominaciones;
    private LocalDateTime fechaTransaccion;
    private Long version;
} 