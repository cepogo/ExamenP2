package ec.edu.espe.transaccion.excepcion;

public class ActualizarTransaccionException extends RuntimeException{

    private final Integer errorCode;
    private final String entityName;

    public ActualizarTransaccionException(String entityName, String message) {
        super(message);
        this.errorCode = 3;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 