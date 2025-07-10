package ec.edu.espe.turno.servicio;

import ec.edu.espe.turno.dto.TurnoCajaCreacionDTO;
import ec.edu.espe.turno.dto.TurnoCajaCierreDTO;
import ec.edu.espe.turno.excepcion.CrearTurnoException;
import ec.edu.espe.turno.excepcion.TurnoNoEncontradoException;
import ec.edu.espe.turno.excepcion.TurnoYaAbiertoException;
import ec.edu.espe.turno.mapper.TurnoCajaMapper;
import ec.edu.espe.turno.modelo.TurnoCaja;
import ec.edu.espe.turno.repositorio.TurnoCajaRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TurnoCajaService {
    
    private final TurnoCajaRepositorio turnoCajaRepositorio;
    private final TurnoCajaMapper turnoCajaMapper;
    
    private String generarCodigoTurno(String codigoCaja, String codigoCajero) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return codigoCaja + "-" + codigoCajero + "-" + fecha;
    }
    
    public TurnoCaja abrirTurno(TurnoCajaCreacionDTO dto) {
        log.info("Abriendo turno para caja: {}, cajero: {}", dto.getCodigoCaja(), dto.getCodigoCajero());
        
        String codigoTurno = generarCodigoTurno(dto.getCodigoCaja(), dto.getCodigoCajero());
        
        // Verificar si ya existe un turno abierto
        turnoCajaRepositorio.findByCodigoCajaAndCodigoCajeroAndEstadoAndInicioTurnoBetween(
            dto.getCodigoCaja(), dto.getCodigoCajero(), "ABIERTO", 
            LocalDateTime.now().withHour(0).withMinute(0), 
            LocalDateTime.now().withHour(23).withMinute(59))
            .ifPresent(turno -> {
                throw new TurnoYaAbiertoException("TurnoCaja", "Ya existe un turno abierto para este cajero en esta caja");
            });
        
        TurnoCaja turno = turnoCajaMapper.toEntity(dto);
        turno.setCodigoTurno(codigoTurno);
        turno.setInicioTurno(LocalDateTime.now());
        turno.setEstado("ABIERTO");
        turno.setVersion(1L);
        
        try {
            TurnoCaja turnoGuardado = turnoCajaRepositorio.save(turno);
            log.info("Turno abierto exitosamente: {}", codigoTurno);
            return turnoGuardado;
        } catch (Exception e) {
            log.error("Error al abrir turno: {}", e.getMessage());
            throw new CrearTurnoException("TurnoCaja", "Error al abrir turno: " + e.getMessage());
        }
    }
    
    public TurnoCaja cerrarTurno(TurnoCajaCierreDTO dto) {
        log.info("Cerrando turno: {}", dto.getCodigoTurno());
        
        TurnoCaja turno = turnoCajaRepositorio.findByCodigoTurno(dto.getCodigoTurno())
            .orElseThrow(() -> new TurnoNoEncontradoException("Turno no encontrado: " + dto.getCodigoTurno(), 2, "TurnoCaja"));
        
        if (!"ABIERTO".equals(turno.getEstado())) {
            throw new TurnoNoEncontradoException("El turno no está abierto: " + dto.getCodigoTurno(), 2, "TurnoCaja");
        }
        
        turno.setFinTurno(LocalDateTime.now());
        turno.setMontoFinal(dto.getMontoFinal());
        turno.setEstado("CERRADO");
        turno.setVersion(turno.getVersion() + 1L);
        
        try {
            TurnoCaja turnoCerrado = turnoCajaRepositorio.save(turno);
            log.info("Turno cerrado exitosamente: {}", dto.getCodigoTurno());
            return turnoCerrado;
        } catch (Exception e) {
            log.error("Error al cerrar turno: {}", e.getMessage());
            throw new CrearTurnoException("TurnoCaja", "Error al cerrar turno: " + e.getMessage());
        }
    }
    
    public TurnoCaja obtenerTurno(String codigoTurno) {
        log.info("Obteniendo turno: {}", codigoTurno);
        return turnoCajaRepositorio.findByCodigoTurno(codigoTurno)
            .orElseThrow(() -> new TurnoNoEncontradoException("Turno no encontrado: " + codigoTurno, 2, "TurnoCaja"));
    }
    
    public List<TurnoCaja> listarTurnosPorCajero(String codigoCaja, String codigoCajero) {
        log.info("Listando turnos para caja: {}, cajero: {}", codigoCaja, codigoCajero);
        return turnoCajaRepositorio.findByCodigoCajaAndCodigoCajero(codigoCaja, codigoCajero);
    }
} 