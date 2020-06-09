public class Goal {
    private String team;
    private String player;
    private int time;

    public Goal(String team, String player, int time) {
        this.team = team;
        this.player = player;
        this.time = time;
    }

    public String getTeam() {
        return team;
    }

    public String getPlayer() {
        return player;
    }

    public int getTime() {
        return time;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(team)
                .append(",")
                .append(player)
                .append(",")
                .append(time);
        return builder.toString();
    }

    public static Goal fromString(String s){
        String[] split = s.split(",");
        if (split.length != 3) return null;
        return new Goal(split[0], split[1], Integer.parseInt(split[2]));
    }
}
