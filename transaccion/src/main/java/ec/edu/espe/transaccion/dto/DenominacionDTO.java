package ec.edu.espe.transaccion.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DenominacionDTO {
    
    private Integer billete;
    private Integer cantidad;
    private BigDecimal monto;
} 