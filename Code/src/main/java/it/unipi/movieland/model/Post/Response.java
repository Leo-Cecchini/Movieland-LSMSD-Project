package it.unipi.movieland.model.Post;

public class Response {
    private String response_id;

    public Response(String response_id) {
        this.response_id = response_id;
    }


    public String getResponse_id() {
        return response_id;
    }
    public void setResponse_id(String response_id) {
        this.response_id = response_id;
    }

    // Override di toString
    @Override
    public String toString() {
        return "Response{" +
                "responseId='" + response_id + '\'' +
                '}';
    }
}