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
