package ec.edu.espe.turno.mapper;

import ec.edu.espe.turno.dto.TurnoCajaDTO;
import ec.edu.espe.turno.dto.TurnoCajaCreacionDTO;
import ec.edu.espe.turno.modelo.TurnoCaja;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DenominacionMapper.class})
public interface TurnoCajaMapper {
    
    TurnoCajaDTO toDTO(TurnoCaja turnoCaja);
    
    TurnoCaja toEntity(TurnoCajaCreacionDTO dto);
} 