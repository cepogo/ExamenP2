package ec.edu.espe.turno.controlador;

import ec.edu.espe.turno.dto.TurnoCajaDTO;
import ec.edu.espe.turno.mapper.TurnoCajaMapper;
import ec.edu.espe.turno.modelo.TurnoCaja;
import ec.edu.espe.turno.servicio.TurnoCajaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validaciones")
@Tag(name = "Validaciones", description = "Endpoints de validación para otros microservicios")
@RequiredArgsConstructor
@Slf4j
public class ValidacionControlador {

    private final TurnoCajaService turnoCajaService;
    private final TurnoCajaMapper turnoCajaMapper;

    @GetMapping("/turno/{codigoTurno}")
    @Operation(summary = "Validar estado de turno", description = "Valida si un turno existe y está abierto para procesar transacciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turno válido y abierto"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @ApiResponse(responseCode = "409", description = "Turno no está abierto")
    })
    public ResponseEntity<TurnoCajaDTO> validarTurno(@PathVariable String codigoTurno) {
        log.info("Validación de turno para transacciones: {}", codigoTurno);
        
        validarCodigoTurno(codigoTurno);
        
        TurnoCaja turno = turnoCajaService.obtenerTurno(codigoTurno);
        
        if (!"ABIERTO".equals(turno.getEstado())) {
            return ResponseEntity.status(409).build();
        }
        
        return ResponseEntity.ok(turnoCajaMapper.toDTO(turno));
    }

    @GetMapping("/caja/{codigoCaja}")
    @Operation(summary = "Validar caja", description = "Valida si una caja existe y está activa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Caja válida"),
            @ApiResponse(responseCode = "404", description = "Caja no encontrada")
    })
    public ResponseEntity<Boolean> validarCaja(@PathVariable String codigoCaja) {
        log.info("Validación de caja: {}", codigoCaja);
        
        validarCodigoCaja(codigoCaja);
        
        // Por ahora solo validamos formato, en producción se consultaría BD de cajas
        return ResponseEntity.ok(true);
    }

    @GetMapping("/cajero/{codigoCajero}")
    @Operation(summary = "Validar cajero", description = "Valida si un cajero existe y está autorizado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cajero válido"),
            @ApiResponse(responseCode = "404", description = "Cajero no encontrado")
    })
    public ResponseEntity<Boolean> validarCajero(@PathVariable String codigoCajero) {
        log.info("Validación de cajero: {}", codigoCajero);
        
        validarCodigoCajero(codigoCajero);
        
        // Por ahora solo validamos formato, en producción se consultaría BD de empleados
        return ResponseEntity.ok(true);
    }

    @GetMapping("/caja/{codigoCaja}/cajero/{codigoCajero}")
    @Operation(summary = "Validar cajero en caja", description = "Valida si un cajero está autorizado para operar en una caja específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cajero autorizado en caja"),
            @ApiResponse(responseCode = "404", description = "Cajero no autorizado en caja")
    })
    public ResponseEntity<Boolean> validarCajeroEnCaja(
            @PathVariable String codigoCaja,
            @PathVariable String codigoCajero) {
        
        log.info("Validación de cajero en caja: caja={}, cajero={}", codigoCaja, codigoCajero);
        
        validarCodigoCaja(codigoCaja);
        validarCodigoCajero(codigoCajero);
        
        // Por ahora solo validamos formato, en producción se consultaría BD de autorizaciones
        return ResponseEntity.ok(true);
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
} 