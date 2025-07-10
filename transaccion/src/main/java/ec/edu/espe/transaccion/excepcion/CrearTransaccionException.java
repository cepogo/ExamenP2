package ec.edu.espe.transaccion.excepcion;

public class CrearTransaccionException extends RuntimeException{

    private final Integer errorCode;
    private final String entityName;

    public CrearTransaccionException(String entityName, String message) {
        super(message);
        this.errorCode = 1;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 