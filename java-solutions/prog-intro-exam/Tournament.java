import java.io.EOFException;
import java.util.*;

public class Tournament {
    private final boolean isTournamentLogging;
    private final boolean isGameLogging;
    private final Map<Player, Integer> playerToRound = new HashMap<>();
    private final Map<Player, Integer> playerToId = new HashMap<>();
    private final List<Game> round = new ArrayList<>();
    private final List<Player> poolOfRemainingPlayers;
    private int numberOfMembers;
    private int m;
    private int n;
    private int k;
    private List<Player> players = null;
    private int numberOfRound = 0;

    public Tournament(List<Player> givenPlayers, boolean tournamentLog, boolean gameLog) throws EOFException {
        isTournamentLogging = tournamentLog;
        isGameLogging = gameLog;
        players = givenPlayers;
        numberOfMembers = givenPlayers.size();

        poolOfRemainingPlayers = new ArrayList<>(givenPlayers);
        for (int i = 0; i < givenPlayers.size(); i++) {
            playerToId.put(players.get(i), i + 1);
        }
    }

    private void createNextRound() {
        round.clear();
        numberOfRound++;

        for (int i = 0; i + 1 < poolOfRemainingPlayers.size(); i += 2) {
            // randomizePlayers(poolOfRemainingPlayers, i);

            round.add(
                    new Game(
                            isGameLogging,
                            poolOfRemainingPlayers.get(i),
                            poolOfRemainingPlayers.get(i + 1)
                    )
            );
        }

        // last player automatically gets to next round
        if (poolOfRemainingPlayers.size() % 2 == 1) {
            Player lastPlayer = poolOfRemainingPlayers.get(poolOfRemainingPlayers.size() - 1);
            poolOfRemainingPlayers.clear();
            poolOfRemainingPlayers.add(lastPlayer);
        } else {
            poolOfRemainingPlayers.clear();
        }

    }

    public boolean hasNextRound() {
        return !(playerToRound.size() + 1 == numberOfMembers);
    }

    public void playRound() throws EOFException {
        createNextRound();

        if (isTournamentLogging) {
            logRoundNumber();
        }

        for (Game game : round) {
            if (isTournamentLogging) {
                logPlayersGame(game);
            }

            if (isGameLogging) {
                System.out.println();
            }

            int result = 0;
            while (result == 0) {
                result = game.play(new Board());
            }

            Player winner = game.getPlayer(result);
            Player loser = game.getPlayer(3 - result);

            storeResults(loser);
            poolOfRemainingPlayers.add(winner);
        }

        if (isTournamentLogging) {
            System.out.println();
        }
    }


    private void randomizePlayers(List<Player> players, int index) {
        Random random = new Random();

        if (random.nextInt(2) == 1) {
            Collections.swap(players, index, index + 1);
        }
    }

    private void storeResults(Player player) {
        playerToRound.put(player, numberOfRound);
    }

    private void logPlayersGame(Game game) {
        Player firstPlayer = game.getPlayer(1);
        Player secondPlayer = game.getPlayer(2);
        System.out.printf(
                "|%d vs %d| ", playerToId.get(firstPlayer), playerToId.get(secondPlayer)
        );
    }

    private void logRoundNumber() {
        System.out.printf("Round %d: ", numberOfRound);
    }

    public void showTable() {
        // putting winner to the highest round
        playerToRound.put(poolOfRemainingPlayers.get(0), numberOfRound + 1);

        List<Map.Entry<Player, Integer>> entries = new ArrayList<>(
                playerToRound.entrySet()
        );
        entries.sort(Map.Entry.comparingByValue());
        Collections.reverse(entries);

        int place = 1;
        System.out.printf("%d : ", place);
        for (int i = 0; i < entries.size(); i++) {
            var currentEntry = entries.get(i);

            if (i - 1 >= 0 && entries.get(i - 1).getValue() > currentEntry.getValue()) {
                place += 1;
                System.out.println();
                System.out.printf("%d : ", place);
            }

            System.out.printf("%s ", playerToId.get(currentEntry.getKey()));
        }

    }

}
