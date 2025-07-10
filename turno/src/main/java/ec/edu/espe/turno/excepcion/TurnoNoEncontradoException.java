package ec.edu.espe.turno.excepcion;

public class TurnoNoEncontradoException extends RuntimeException{
    private Integer errorCode;
    private String entityName;

    public TurnoNoEncontradoException(String message, Integer errorCode, String entityName) {
        super(message);
        this.errorCode = 2;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 