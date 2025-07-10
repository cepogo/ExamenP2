package ec.edu.espe.turno.repositorio;

import ec.edu.espe.turno.modelo.TurnoCaja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoCajaRepositorio extends MongoRepository<TurnoCaja, String> {

    Optional<TurnoCaja> findByCodigoTurno(String codigoTurno);
    
    List<TurnoCaja> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TurnoCaja> findByEstado(String estado);
    
    List<TurnoCaja> findByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, String estado);
    
    List<TurnoCaja> findByInicioTurnoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    Optional<TurnoCaja> findByCodigoCajaAndCodigoCajeroAndEstadoAndInicioTurnoBetween(
        String codigoCaja, String codigoCajero, String estado, LocalDateTime fechaInicio, LocalDateTime fechaFin);
} 