package ec.edu.espe.turno.modelo;

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
@Document(collection = "turnos_caja")
public class TurnoCaja {

    @Id
    private String id;
    
    @Indexed(unique = true)
    private String codigoTurno;
    
    private String codigoCaja;
    private String codigoCajero;
    private LocalDateTime inicioTurno;
    private BigDecimal montoInicial;
    private LocalDateTime finTurno;
    private BigDecimal montoFinal;
    private String estado; // ABIERTO, CERRADO
    private List<Denominacion> denominacionesIniciales;
    private Long version;
} 