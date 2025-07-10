package ec.edu.espe.transaccion.mapper;

import ec.edu.espe.transaccion.dto.DenominacionDTO;
import ec.edu.espe.transaccion.modelo.Denominacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DenominacionMapper {
    
    DenominacionDTO toDTO(Denominacion denominacion);
    
    Denominacion toEntity(DenominacionDTO dto);
} 