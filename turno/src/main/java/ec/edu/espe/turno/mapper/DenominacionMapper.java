package ec.edu.espe.turno.mapper;

import ec.edu.espe.turno.dto.DenominacionDTO;
import ec.edu.espe.turno.modelo.Denominacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DenominacionMapper {
    
    DenominacionDTO toDTO(Denominacion denominacion);
    
    Denominacion toEntity(DenominacionDTO dto);
} 