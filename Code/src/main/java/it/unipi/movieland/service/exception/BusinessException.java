package it.unipi.movieland.service.exception;

public class BusinessException extends Exception {

    private Exception e;

    public BusinessException(Exception ex){
        super(ex);
    }
    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String error_during_authentication, Exception e) {
        this.e = e;
    }

    public String getMessage(){
        if(e != null)
            return e.getMessage();
        return null;
    }

}

/*
package it.unipi.movieland.service.exception;

public class BusinessException extends Exception {

    public BusinessException(Exception ex) {
        super(ex);  // Chiamata corretta al costruttore della superclasse
    }

    public BusinessException(String message) {
        super(message);  // Passa solo il messaggio
    }

    public BusinessException(String message, Exception cause) {
        super(message, cause);  // Mantiene il messaggio e la causa originale
    }
}
*/
