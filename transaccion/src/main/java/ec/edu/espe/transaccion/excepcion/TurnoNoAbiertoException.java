package ec.edu.espe.transaccion.excepcion;

public class TurnoNoAbiertoException extends RuntimeException{

    private final Integer errorCode;
    private final String entityName;

    public TurnoNoAbiertoException(String entityName, String message) {
        super(message);
        this.errorCode = 4;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 