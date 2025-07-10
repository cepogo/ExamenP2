package ec.edu.espe.transaccion.servicio;

import ec.edu.espe.transaccion.excepcion.TurnoNoAbiertoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidacionTurnoService {

    private final RestTemplate restTemplate;
    
    @Value("${turno.service.url:http://localhost:8083}")
    private String turnoServiceUrl;

    public void validarTurnoAbierto(String codigoTurno) {
        log.info("Validando turno abierto: {}", codigoTurno);
        
        try {
            String url = turnoServiceUrl + "/api/validaciones/turno/" + codigoTurno;
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Turno válido y abierto: {}", codigoTurno);
            } else if (response.getStatusCode().value() == 409) {
                throw new TurnoNoAbiertoException("TransaccionTurno", "El turno no está abierto: " + codigoTurno);
            } else {
                throw new TurnoNoAbiertoException("TransaccionTurno", "Turno no encontrado: " + codigoTurno);
            }
        } catch (Exception e) {
            log.error("Error al validar turno: {}", e.getMessage());
            throw new TurnoNoAbiertoException("TransaccionTurno", "Error al validar turno: " + e.getMessage());
        }
    }

    public void validarCaja(String codigoCaja) {
        log.info("Validando caja: {}", codigoCaja);
        
        try {
            String url = turnoServiceUrl + "/api/validaciones/caja/" + codigoCaja;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            
            if (!response.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(response.getBody())) {
                throw new IllegalArgumentException("Caja no válida: " + codigoCaja);
            }
            
            log.info("Caja válida: {}", codigoCaja);
        } catch (Exception e) {
            log.error("Error al validar caja: {}", e.getMessage());
            throw new IllegalArgumentException("Error al validar caja: " + e.getMessage());
        }
    }

    public void validarCajero(String codigoCajero) {
        log.info("Validando cajero: {}", codigoCajero);
        
        try {
            String url = turnoServiceUrl + "/api/validaciones/cajero/" + codigoCajero;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            
            if (!response.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(response.getBody())) {
                throw new IllegalArgumentException("Cajero no válido: " + codigoCajero);
            }
            
            log.info("Cajero válido: {}", codigoCajero);
        } catch (Exception e) {
            log.error("Error al validar cajero: {}", e.getMessage());
            throw new IllegalArgumentException("Error al validar cajero: " + e.getMessage());
        }
    }

    public void validarCajeroEnCaja(String codigoCaja, String codigoCajero) {
        log.info("Validando cajero en caja: caja={}, cajero={}", codigoCaja, codigoCajero);
        
        try {
            String url = turnoServiceUrl + "/api/validaciones/caja/" + codigoCaja + "/cajero/" + codigoCajero;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            
            if (!response.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(response.getBody())) {
                throw new IllegalArgumentException("Cajero no autorizado en caja: " + codigoCajero + " - " + codigoCaja);
            }
            
            log.info("Cajero autorizado en caja: caja={}, cajero={}", codigoCaja, codigoCajero);
        } catch (Exception e) {
            log.error("Error al validar cajero en caja: {}", e.getMessage());
            throw new IllegalArgumentException("Error al validar cajero en caja: " + e.getMessage());
        }
    }
} 