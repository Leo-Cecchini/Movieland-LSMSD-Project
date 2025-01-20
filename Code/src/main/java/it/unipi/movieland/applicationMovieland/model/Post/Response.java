package it.unipi.movieland.applicationMovieland.model.Post;

public class Response {
    private String responseId;

    // Costruttore
    public Response(String responseId) {
        this.responseId = responseId;
    }

    // Getter e Setter
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    // Override di toString
    @Override
    public String toString() {
        return "Response{" +
                "responseId='" + responseId + '\'' +
                '}';
    }
}