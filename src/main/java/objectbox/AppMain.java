package objectbox;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppMain {

    private static void putMatch(Box<Match> box) {
        Match newMatch = new Match();
        newMatch.setDate(ConsoleUtils.getFormattedDate());
        System.out.println("Podaj nazwę obiektu: ");
        newMatch.setStadium(ConsoleUtils.getText(1));
        System.out.println("Podaj nazwę pierwszej drużyny:");
        String firstTeam = ConsoleUtils.getText(1);
        System.out.println("Podaj nazwę drugiej drużyny:");
        String secondTeam = ConsoleUtils.getText(1);
        System.out.println("Podaj czas trwania meczu:");
        int matchTime = ConsoleUtils.getNumber(90, -1);
        newMatch.setFirstTeam(firstTeam);
        newMatch.setSecondTeam(secondTeam);
        newMatch.setTime(matchTime);
        Pair<Integer, Integer> result = ConsoleUtils.getScores(firstTeam, secondTeam);
        newMatch.setFirstScore(result.getKey());
        newMatch.setSecondScore(result.getValue());
        List<Goal> goals = Stream.concat(
                ConsoleUtils.getGoalsForTeam(result.getKey(), matchTime, firstTeam).stream(),
                ConsoleUtils.getGoalsForTeam(result.getValue(), matchTime, secondTeam).stream()
        ).collect(Collectors.toList());
        newMatch.setGoals(goals);
        //System.out.println(goals.toString());
        box.put(newMatch);
    }

    private static void getById(Box<Match> box) {
        System.out.println("Podaj id do wyszukania:");
        long id = ConsoleUtils.getId();
        Match match = box.get(id);
        if (match == null) {
            System.out.println("Nie znaleziono meczu o takim id");
        } else ConsoleUtils.printMatch(match);
    }

    private static void getByQuery(Box<Match> box) {
        System.out.println("Pozostaw domyślnie * by wyszukiwać wszystkie");
        String date = ConsoleUtils.getFormattedDate("*");
        System.out.println("Podaj nazwę obiektu - wpisz * by wyszukiwać wszystkie");
        String stadium = ConsoleUtils.getText(1);
        List<Match> results;
        if (date.compareTo("*") == 0 && stadium.compareTo("*") == 0) {
            results = box.getAll();
        } else if (date.compareTo("*") != 0 && stadium.compareTo("*") == 0) {
            results = box.query().equal(Match_.date, date).build().find();
        } else if (date.compareTo("*") == 0) {
            results = box.query().equal(Match_.stadium, stadium).build().find();
        } else {
            QueryBuilder<Match> builder = box.query();
            builder.equal(Match_.date, date).equal(Match_.stadium, stadium);
            results = builder.build().find();
        }
        for (Match m : results) {
            ConsoleUtils.printMatch(m);
        }
    }

    private static void deleteMatch(Box<Match> box) {
        System.out.println("Podaj id do usunięcia:");
        long id = ConsoleUtils.getId();
        if (box.remove(id)) {
            System.out.println("Usunięto mecz.");
        } else System.out.println("Usuwanie nie powiodło się.");
    }

    private static void getAll(Box<Match> box) {
        List<Match> matches = box.getAll();
        if (matches.isEmpty()) {
            System.out.println("Nie ma żadnych meczy w lidze");
            return;
        }
        for (Match m : matches) {
            ConsoleUtils.printMatch(m);
        }
    }

    private static void update(Box<Match> box) {
        System.out.println("Podaj id do wyszukania:");
        long id = ConsoleUtils.getId();
        Match match = box.get(id);
        if (match == null) {
            System.out.println("Nie znaleziono meczu o takim id");
            return;
        }
        match.setDate(ConsoleUtils.getFormattedDate(match.getDate()));
        System.out.println("Podaj nazwę obiektu. Obecna wartość: "+match.getStadium()+". Pozostaw puste by nie zmieniać");
        String tmp = ConsoleUtils.getText(0);
        match.setStadium(tmp.isEmpty() ? match.getStadium() : tmp);
        System.out.println("Podaj nazwę pierwszej drużyny. Obecna wartość: "+match.getFirstTeam()+". Pozostaw puste by nie zmieniać");
        tmp = ConsoleUtils.getText(0);
        match.setFirstTeam(tmp.isEmpty() ? match.getFirstTeam() : tmp);
        System.out.println("Podaj nazwę drugiej drużyny. Obecna wartość: "+match.getSecondTeam()+". Pozostaw puste by nie zmieniać");
        tmp = ConsoleUtils.getText(0);
        match.setSecondTeam(tmp.isEmpty() ? match.getSecondTeam() : tmp);
        System.out.println("Podaj czas trwania meczu:");
        match.setTime(ConsoleUtils.getNumber(match.getTime(), 90, -1));
        System.out.println("Po wprowadzeniu zmian podaj nowy wynik i gole:");
        Pair<Integer, Integer> result = ConsoleUtils.getScores(match.getFirstTeam(), match.getSecondTeam());
        match.setFirstScore(result.getKey());
        match.setSecondScore(result.getValue());
        List<Goal> goals = Stream.concat(
                ConsoleUtils.getGoalsForTeam(result.getKey(), match.getTime(), match.getFirstTeam()).stream(),
                ConsoleUtils.getGoalsForTeam(result.getValue(), match.getTime(), match.getSecondTeam()).stream()
        ).collect(Collectors.toList());
        match.setGoals(goals);
        box.put(match);
    }

    public static void main(String[] args) {
        BoxStore store = MyObjectBox.builder().name("objectbox-match-db").build();
        Box<Match> box = store.boxFor(Match.class);
        System.out.println("Aplikacja na PSR lab 6 - ObjectBOX");
        System.out.println("Piotr Walczak gr. 1ID22B");
        while (true) {
            switch (ConsoleUtils.getMenuOption()) {
                case 'd':
                    putMatch(box);
                    break;
                case 'u':
                    deleteMatch(box);
                    break;
                case 'a':
                    update(box);
                    break;
                case 'i':
                    getById(box);
                    break;
                case 'w':
                    getAll(box);
                    break;
                case 'p':
                    getByQuery(box);
                    break;
                case 'z':
                    store.close();
                    return;
                default:
                    System.out.println("Podano nieznaną operację. Spróbuj ponownie.");
            }
        }
    }
}
