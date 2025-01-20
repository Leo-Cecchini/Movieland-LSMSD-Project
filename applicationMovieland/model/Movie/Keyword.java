package applicationMovieland.model.Movie;

import java.util.List;

public class Keyword {
    private List<String> keywordList;

    public Keyword(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "keywordList=" + keywordList +
                '}';
    }
}