package ec.edu.espe.transaccion.repositorio;

import ec.edu.espe.transaccion.modelo.TransaccionTurno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionTurnoRepositorio extends MongoRepository<TransaccionTurno, String> {

    Optional<TransaccionTurno> findByCodigoTransaccion(String codigoTransaccion);
    
    List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
    
    List<TransaccionTurno> findByCodigoCajaAndCodigoCajero(String codigoCaja, String codigoCajero);
    
    List<TransaccionTurno> findByTipoTransaccion(String tipoTransaccion);
    
    List<TransaccionTurno> findByCodigoTurnoAndTipoTransaccion(String codigoTurno, String tipoTransaccion);
    
    List<TransaccionTurno> findByFechaTransaccionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<TransaccionTurno> findByCodigoTurnoAndFechaTransaccionBetween(String codigoTurno, LocalDateTime fechaInicio, LocalDateTime fechaFin);
} 