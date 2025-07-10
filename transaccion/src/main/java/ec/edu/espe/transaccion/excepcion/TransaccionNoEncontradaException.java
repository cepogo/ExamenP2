package ec.edu.espe.transaccion.excepcion;

public class TransaccionNoEncontradaException extends RuntimeException{
    private Integer errorCode;
    private String entityName;

    public TransaccionNoEncontradaException(String message, Integer errorCode, String entityName) {
        super(message);
        this.errorCode = 2;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
} 