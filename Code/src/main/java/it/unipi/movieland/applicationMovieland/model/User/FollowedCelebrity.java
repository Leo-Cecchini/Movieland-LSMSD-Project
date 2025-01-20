package applicationMovieland.model.User;

public class FollowedCelebrity {
    private int personId;
    private String name;

    public FollowedCelebrity(int personId, String name) {
        this.personId = personId;
        this.name = name;
    }

    // Getter e Setter
    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FollowedCelebrity{" +
                "personId=" + personId +
                '}';
    }
}