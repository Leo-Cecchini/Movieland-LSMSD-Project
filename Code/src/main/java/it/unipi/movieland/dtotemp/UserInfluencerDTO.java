package it.unipi.movieland.DTO;

public class UserInfluencerDTO {
    private String username;
    private int totalPosts;
    private int totalComments;
    private double commentsPerPost;

    public UserInfluencerDTO(String username, int totalPosts, int totalComments, double commentsPerPost) {
        this.username = username;
        this.totalPosts = totalPosts;
        this.totalComments = totalComments;
        this.commentsPerPost = commentsPerPost;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public int getTotalPosts() {return totalPosts;}
    public void setTotalPosts(int totalPosts) {this.totalPosts = totalPosts;}

    public int getTotalComments() {return totalComments;}
    public void setTotalComments(int totalComments) {this.totalComments = totalComments;}

    public double getCommentsPerPost() {return commentsPerPost;}
    public void setCommentsPerPost(double commentsPerPost) {this.commentsPerPost = commentsPerPost;}
}
