package ec.edu.espe.transaccion.servicio;

import ec.edu.espe.transaccion.dto.TransaccionTurnoCreacionDTO;
import ec.edu.espe.transaccion.excepcion.CrearTransaccionException;
import ec.edu.espe.transaccion.excepcion.TransaccionNoEncontradaException;
import ec.edu.espe.transaccion.mapper.TransaccionTurnoMapper;
import ec.edu.espe.transaccion.modelo.TransaccionTurno;
import ec.edu.espe.transaccion.repositorio.TransaccionTurnoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionTurnoService {
    
    private final TransaccionTurnoRepositorio transaccionTurnoRepositorio;
    private final TransaccionTurnoMapper transaccionTurnoMapper;
    private final ValidacionTurnoService validacionTurnoService;
    
    private String generarCodigoTransaccion() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public TransaccionTurno procesarTransaccion(TransaccionTurnoCreacionDTO dto) {
        log.info("Procesando transacción tipo: {} para turno: {}", dto.getTipoTransaccion(), dto.getCodigoTurno());
        
        // Validaciones con el microservicio de turnos
        validacionTurnoService.validarCaja(dto.getCodigoCaja());
        validacionTurnoService.validarCajero(dto.getCodigoCajero());
        validacionTurnoService.validarCajeroEnCaja(dto.getCodigoCaja(), dto.getCodigoCajero());
        validacionTurnoService.validarTurnoAbierto(dto.getCodigoTurno());
        
        TransaccionTurno transaccion = transaccionTurnoMapper.toEntity(dto);
        transaccion.setCodigoTransaccion(generarCodigoTransaccion());
        transaccion.setFechaTransaccion(LocalDateTime.now());
        transaccion.setVersion(1L);
        
        try {
            TransaccionTurno transaccionGuardada = transaccionTurnoRepositorio.save(transaccion);
            log.info("Transacción procesada exitosamente: {}", transaccionGuardada.getCodigoTransaccion());
            return transaccionGuardada;
        } catch (Exception e) {
            log.error("Error al procesar transacción: {}", e.getMessage());
            throw new CrearTransaccionException("TransaccionTurno", "Error al procesar transacción: " + e.getMessage());
        }
    }
    
    public TransaccionTurno obtenerTransaccion(String codigoTransaccion) {
        log.info("Obteniendo transacción: {}", codigoTransaccion);
        return transaccionTurnoRepositorio.findByCodigoTransaccion(codigoTransaccion)
            .orElseThrow(() -> new TransaccionNoEncontradaException("Transacción no encontrada: " + codigoTransaccion, 2, "TransaccionTurno"));
    }
    
    public List<TransaccionTurno> listarTransaccionesPorTurno(String codigoTurno) {
        log.info("Listando transacciones para turno: {}", codigoTurno);
        return transaccionTurnoRepositorio.findByCodigoTurno(codigoTurno);
    }
    
    public List<TransaccionTurno> listarTransaccionesPorTipo(String codigoTurno, String tipoTransaccion) {
        log.info("Listando transacciones tipo: {} para turno: {}", tipoTransaccion, codigoTurno);
        return transaccionTurnoRepositorio.findByCodigoTurnoAndTipoTransaccion(codigoTurno, tipoTransaccion);
    }
} 