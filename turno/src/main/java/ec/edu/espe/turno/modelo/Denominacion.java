package ec.edu.espe.turno.modelo;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Clase para manejar denominaciones dentro de las entidades
public class Denominacion {
    
    private Integer billete;
    private Integer cantidad;
    private BigDecimal monto;
} 