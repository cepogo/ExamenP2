package ec.edu.espe.turno.controlador;

import ec.edu.espe.turno.dto.TurnoCajaCreacionDTO;
import ec.edu.espe.turno.dto.TurnoCajaCierreDTO;
import ec.edu.espe.turno.dto.TurnoCajaDTO;
import ec.edu.espe.turno.mapper.TurnoCajaMapper;
import ec.edu.espe.turno.modelo.TurnoCaja;
import ec.edu.espe.turno.servicio.TurnoCajaService;
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
@RequestMapping("/api/turnos")
@Tag(name = "Turnos de Caja", description = "Gestiona los turnos de cajeros en las ventanillas")
@RequiredArgsConstructor
@Slf4j
public class TurnoCajaControlador {

    private final TurnoCajaService turnoCajaService;
    private final TurnoCajaMapper turnoCajaMapper;
    
    private static final Set<Integer> DENOMINACIONES_VALIDAS = Set.of(1, 5, 10, 20, 50, 100);

    @PostMapping("/abrir")
    @Operation(summary = "Abrir turno de caja", description = "Abre un nuevo turno para un cajero en una caja específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turno abierto exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un turno abierto para este cajero")
    })
    public ResponseEntity<TurnoCajaDTO> abrirTurno(@Valid @RequestBody TurnoCajaCreacionDTO dto) {
        log.info("Solicitud para abrir turno: caja={}, cajero={}", dto.getCodigoCaja(), dto.getCodigoCajero());
        
        // Validaciones
        validarCodigoCaja(dto.getCodigoCaja());
        validarCodigoCajero(dto.getCodigoCajero());
        validarMontoInicial(dto.getMontoInicial());
        validarDenominaciones(dto.getDenominacionesIniciales(), dto.getMontoInicial());
        
        TurnoCaja turno = turnoCajaService.abrirTurno(dto);
        return ResponseEntity.ok(turnoCajaMapper.toDTO(turno));
    }

    @PutMapping("/cerrar")
    @Operation(summary = "Cerrar turno de caja", description = "Cierra un turno existente y registra el monto final")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turno cerrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El turno no está abierto")
    })
    public ResponseEntity<TurnoCajaDTO> cerrarTurno(@Valid @RequestBody TurnoCajaCierreDTO dto) {
        log.info("Solicitud para cerrar turno: {}", dto.getCodigoTurno());
        
        // Validaciones
        validarCodigoTurno(dto.getCodigoTurno());
        validarMontoFinal(dto.getMontoFinal());
        validarDenominaciones(dto.getDenominacionesFinales(), dto.getMontoFinal());
        
        TurnoCaja turno = turnoCajaService.cerrarTurno(dto);
        return ResponseEntity.ok(turnoCajaMapper.toDTO(turno));
    }

    @GetMapping("/{codigoTurno}")
    @Operation(summary = "Obtener turno por código", description = "Consulta un turno específico por su código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<TurnoCajaDTO> obtenerTurno(@PathVariable String codigoTurno) {
        log.info("Solicitud para obtener turno: {}", codigoTurno);
        
        validarCodigoTurno(codigoTurno);
        
        TurnoCaja turno = turnoCajaService.obtenerTurno(codigoTurno);
        return ResponseEntity.ok(turnoCajaMapper.toDTO(turno));
    }

    @GetMapping("/caja/{codigoCaja}/cajero/{codigoCajero}")
    @Operation(summary = "Listar turnos por cajero", description = "Lista todos los turnos de un cajero en una caja específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<TurnoCajaDTO>> listarTurnosPorCajero(
            @PathVariable String codigoCaja,
            @PathVariable String codigoCajero) {
        
        log.info("Solicitud para listar turnos: caja={}, cajero={}", codigoCaja, codigoCajero);
        
        validarCodigoCaja(codigoCaja);
        validarCodigoCajero(codigoCajero);
        
        List<TurnoCaja> turnos = turnoCajaService.listarTurnosPorCajero(codigoCaja, codigoCajero);
        List<TurnoCajaDTO> turnosDTO = turnos.stream()
                .map(turnoCajaMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(turnosDTO);
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

    private void validarMontoInicial(BigDecimal montoInicial) {
        if (montoInicial == null || montoInicial.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto inicial debe ser mayor a 0");
        }
    }

    private void validarMontoFinal(BigDecimal montoFinal) {
        if (montoFinal == null || montoFinal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto final debe ser mayor a 0");
        }
    }

    private void validarDenominaciones(List<ec.edu.espe.turno.dto.DenominacionDTO> denominaciones, BigDecimal montoTotal) {
        if (denominaciones == null || denominaciones.isEmpty()) {
            throw new IllegalArgumentException("Las denominaciones son requeridas");
        }

        BigDecimal suma = BigDecimal.ZERO;
        for (ec.edu.espe.turno.dto.DenominacionDTO den : denominaciones) {
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