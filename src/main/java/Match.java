import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Match {
    @Id
    private Long id;

    @Convert(converter = GoalListConverter.class, dbType = String.class)
    private List<Goal> goals;

    public static class GoalListConverter implements PropertyConverter<List<Goal>, String> {
        @Override
        public List<Goal> convertToEntityProperty(String s) {
            if (s == null || s.isEmpty() || s.compareTo("[]")==0) return Collections.emptyList();
            List<Goal> result = new ArrayList<>();
            String[] split = s.split(";");
            for (String goal: split) {
                Goal g = Goal.fromString(goal);
                if (g!=null) result.add(g);
            }
            return result;
        }

        @Override
        public String convertToDatabaseValue(List<Goal> goals) {
            if (goals == null || goals.isEmpty()) return "[]";
            StringBuilder builder = new StringBuilder();
            for (Goal g: goals) {
                builder.append(g.toString()).append(";");
            }
            return builder.toString();
        }
    }
    private String date;
    private String stadium;
    private String firstTeam;
    private String secondTeam;
    private int firstScore;
    private int secondScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(String firstTeam) {
        this.firstTeam = firstTeam;
    }

    public String getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(String secondTeam) {
        this.secondTeam = secondTeam;
    }

    public int getFirstScore() {
        return firstScore;
    }

    public void setFirstScore(int firstScore) {
        this.firstScore = firstScore;
    }

    public int getSecondScore() {
        return secondScore;
    }

    public void setSecondScore(int secondScore) {
        this.secondScore = secondScore;
    }
}
