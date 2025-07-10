package ec.edu.espe.turno.excepcion;

public class CrearTurnoException extends RuntimeException{

    private final Integer errorCode;
    private final String entityName;

    public CrearTurnoException(String entityName, String message) {
        super(message);
        this.errorCode = 1;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 