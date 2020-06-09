package objectbox;

import io.objectbox.Box;
import io.objectbox.BoxStore;
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
        int matchTime = ConsoleUtils.getNumber(1, -1);
        newMatch.setFirstTeam(firstTeam);
        newMatch.setSecondTeam(secondTeam);
        newMatch.setTime(matchTime);
        Pair<Integer, Integer> result = ConsoleUtils.getScores(firstTeam, secondTeam);
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
        int id = ConsoleUtils.getNumber(1, -1);
        Match match = box.get(id);
        if (match == null) {
            System.out.println("Nie znaleziono meczu o takim id");
        } else ConsoleUtils.printMatch(match);
    }

    public static void main(String[] args) {
        BoxStore store = MyObjectBox.builder().name("objectbox-match-db").build();
        Box<Match> box = store.boxFor(Match.class);
        System.out.println("Aplikacja na PSR lab 6 - ObjectBOX");
        System.out.println("Piotr Walczak gr. 1ID22B");
        while (true){
            switch (ConsoleUtils.getMenuOption()) {
                case 'd':
                    putMatch(box);
                    break;
                case 'w': {
                    for (Match m : box.getAll()) {
                        ConsoleUtils.printMatch(m);
                    }
                    break;
                }
                case 'i':
                    getById(box);
                    break;
                case 'z':
                    store.close();
                    break;
                default:
                    System.out.println("Podano nieznaną operację. Spróbuj ponownie.");
            }
        }
    }
}
