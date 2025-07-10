package ec.edu.espe.transaccion.controlador;

import ec.edu.espe.transaccion.dto.TransaccionTurnoCreacionDTO;
import ec.edu.espe.transaccion.dto.TransaccionTurnoDTO;
import ec.edu.espe.transaccion.mapper.TransaccionTurnoMapper;
import ec.edu.espe.transaccion.modelo.TransaccionTurno;
import ec.edu.espe.transaccion.servicio.TransaccionTurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacciones")
@Tag(name = "Transacciones de Turno", description = "Gestiona las transacciones de los turnos de cajeros")
@RequiredArgsConstructor
@Slf4j
public class TransaccionTurnoControlador {

    private final TransaccionTurnoService transaccionTurnoService;
    private final TransaccionTurnoMapper transaccionTurnoMapper;
    
    private static final Set<Integer> DENOMINACIONES_VALIDAS = Set.of(1, 5, 10, 20, 50, 100);
    private static final Set<String> TIPOS_TRANSACCION_VALIDOS = Set.of("INICIO", "AHORRO", "DEPOSITO", "CIERRE");

    @PostMapping("/procesar")
    @Operation(summary = "Procesar transacción", description = "Procesa una nueva transacción para un turno específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transacción procesada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El turno no está abierto")
    })
    public ResponseEntity<TransaccionTurnoDTO> procesarTransaccion(@Valid @RequestBody TransaccionTurnoCreacionDTO dto) {
        log.info("Solicitud para procesar transacción: tipo={}, turno={}", dto.getTipoTransaccion(), dto.getCodigoTurno());
        
        // Validaciones
        validarCodigoCaja(dto.getCodigoCaja());
        validarCodigoCajero(dto.getCodigoCajero());
        validarCodigoTurno(dto.getCodigoTurno());
        validarTipoTransaccion(dto.getTipoTransaccion());
        validarMontoTotal(dto.getMontoTotal());
        validarDenominaciones(dto.getDenominaciones(), dto.getMontoTotal());
        
        TransaccionTurno transaccion = transaccionTurnoService.procesarTransaccion(dto);
        return ResponseEntity.ok(transaccionTurnoMapper.toDTO(transaccion));
    }

    @GetMapping("/turno/{codigoTurno}")
    @Operation(summary = "Listar transacciones por turno", description = "Lista todas las transacciones de un turno específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<TransaccionTurnoDTO>> listarTransaccionesPorTurno(@PathVariable String codigoTurno) {
        log.info("Solicitud para listar transacciones del turno: {}", codigoTurno);
        
        validarCodigoTurno(codigoTurno);
        
        List<TransaccionTurno> transacciones = transaccionTurnoService.listarTransaccionesPorTurno(codigoTurno);
        List<TransaccionTurnoDTO> transaccionesDTO = transacciones.stream()
                .map(transaccionTurnoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(transaccionesDTO);
    }

    @GetMapping("/turno/{codigoTurno}/tipo/{tipoTransaccion}")
    @Operation(summary = "Listar transacciones por tipo", description = "Lista las transacciones de un turno por tipo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<TransaccionTurnoDTO>> listarTransaccionesPorTipo(
            @PathVariable String codigoTurno,
            @PathVariable String tipoTransaccion) {
        
        log.info("Solicitud para listar transacciones: turno={}, tipo={}", codigoTurno, tipoTransaccion);
        
        validarCodigoTurno(codigoTurno);
        validarTipoTransaccion(tipoTransaccion);
        
        List<TransaccionTurno> transacciones = transaccionTurnoService.listarTransaccionesPorTipo(codigoTurno, tipoTransaccion);
        List<TransaccionTurnoDTO> transaccionesDTO = transacciones.stream()
                .map(transaccionTurnoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(transaccionesDTO);
    }

    @GetMapping("/{codigoTransaccion}")
    @Operation(summary = "Obtener transacción por código", description = "Consulta una transacción específica por su código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transacción encontrada"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public ResponseEntity<TransaccionTurnoDTO> obtenerTransaccion(@PathVariable String codigoTransaccion) {
        log.info("Solicitud para obtener transacción: {}", codigoTransaccion);
        
        validarCodigoTransaccion(codigoTransaccion);
        
        TransaccionTurno transaccion = transaccionTurnoService.obtenerTransaccion(codigoTransaccion);
        return ResponseEntity.ok(transaccionTurnoMapper.toDTO(transaccion));
    }

    // ================= VALIDACIONES =================

    private void validarCodigoCaja(String codigoCaja) {
        if (codigoCaja == null || codigoCaja.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de caja es requerido");
        }
        if (!codigoCaja.matches("^CAJ\\d{2}$")) {
            throw new IllegalArgumentException("Formato de código caja inválido. Use: CAJ01, CAJ02, etc.");
        }
    }

    private void validarCodigoCajero(String codigoCajero) {
        if (codigoCajero == null || codigoCajero.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de cajero es requerido");
        }
        if (!codigoCajero.matches("^USU\\d{2}$")) {
            throw new IllegalArgumentException("Formato de código cajero inválido. Use: USU01, USU02, etc.");
        }
    }

    private void validarCodigoTurno(String codigoTurno) {
        if (codigoTurno == null || codigoTurno.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de turno es requerido");
        }
        if (!codigoTurno.matches("^CAJ\\d{2}-USU\\d{2}-\\d{8}$")) {
            throw new IllegalArgumentException("Formato de código turno inválido. Use: CAJ01-USU01-20250109");
        }
    }

    private void validarCodigoTransaccion(String codigoTransaccion) {
        if (codigoTransaccion == null || codigoTransaccion.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de transacción es requerido");
        }
        if (!codigoTransaccion.matches("^TXN[A-Z0-9]{8}$")) {
            throw new IllegalArgumentException("Formato de código transacción inválido. Use: TXN12345678");
        }
    }

    private void validarTipoTransaccion(String tipoTransaccion) {
        if (tipoTransaccion == null || tipoTransaccion.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de transacción es requerido");
        }
        if (!TIPOS_TRANSACCION_VALIDOS.contains(tipoTransaccion.toUpperCase())) {
            throw new IllegalArgumentException("Tipo de transacción inválido. Use: INICIO, AHORRO, DEPOSITO, CIERRE");
        }
    }

    private void validarMontoTotal(BigDecimal montoTotal) {
        if (montoTotal == null || montoTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto total debe ser mayor a 0");
        }
    }

    private void validarDenominaciones(List<ec.edu.espe.transaccion.dto.DenominacionDTO> denominaciones, BigDecimal montoTotal) {
        if (denominaciones == null || denominaciones.isEmpty()) {
            throw new IllegalArgumentException("Las denominaciones son requeridas");
        }

        BigDecimal suma = BigDecimal.ZERO;
        for (ec.edu.espe.transaccion.dto.DenominacionDTO den : denominaciones) {
            if (!DENOMINACIONES_VALIDAS.contains(den.getBillete())) {
                throw new IllegalArgumentException("Denominación no válida: " + den.getBillete() + ". Use: 1, 5, 10, 20, 50, 100");
            }
            if (den.getCantidad() == null || den.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser mayor a 0 para denominación: " + den.getBillete());
            }
            if (den.getMonto() == null || den.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Monto debe ser mayor a 0 para denominación: " + den.getBillete());
            }
            
            // Calcular el monto esperado: billete * cantidad
            BigDecimal montoEsperado = BigDecimal.valueOf(den.getBillete()).multiply(BigDecimal.valueOf(den.getCantidad()));
            if (montoEsperado.compareTo(den.getMonto()) != 0) {
                throw new IllegalArgumentException("Monto no coincide con billete * cantidad para denominación: " + den.getBillete() + 
                    ". Esperado: " + montoEsperado + ", Recibido: " + den.getMonto());
            }
            
            suma = suma.add(den.getMonto());
        }

        if (suma.compareTo(montoTotal) != 0) {
            throw new IllegalArgumentException("La suma de denominaciones (" + suma + ") no coincide con el monto total (" + montoTotal + ")");
        }
    }
} 