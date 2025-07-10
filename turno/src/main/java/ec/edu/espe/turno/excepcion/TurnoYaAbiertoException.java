package ec.edu.espe.turno.excepcion;

public class TurnoYaAbiertoException extends RuntimeException{

    private final Integer errorCode;
    private final String entityName;

    public TurnoYaAbiertoException(String entityName, String message) {
        super(message);
        this.errorCode = 4;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 