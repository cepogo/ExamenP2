package ec.edu.espe.transaccion.mapper;

import ec.edu.espe.transaccion.dto.TransaccionTurnoDTO;
import ec.edu.espe.transaccion.dto.TransaccionTurnoCreacionDTO;
import ec.edu.espe.transaccion.modelo.TransaccionTurno;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DenominacionMapper.class})
public interface TransaccionTurnoMapper {
    
    TransaccionTurnoDTO toDTO(TransaccionTurno transaccionTurno);
    
    TransaccionTurno toEntity(TransaccionTurnoCreacionDTO dto);
} 