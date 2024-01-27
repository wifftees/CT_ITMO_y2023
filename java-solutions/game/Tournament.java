package game;

import java.io.EOFException;
import java.util.*;

public class Tournament {
    private int numberOfMembers;
    private int m;
    private int n;
    private int k;
    private final boolean isTournamentLogging;
    private final boolean isGameLogging;
    private List<Player> players = null;
    private final Map<Player, Integer> playerToRound = new HashMap<>();
    private final Map<Player, Integer> playerToId = new HashMap<>();
    private final List<Game> round = new ArrayList<>();
    private int numberOfRound = 0;
    private final List<Player> poolOfRemainingPlayers;

    private void checkForInputTermination(Scanner scanner) throws EOFException {
        if (!scanner.hasNextLine()) {
            throw new EOFException("Input terminated");
        }
    }

    private void gatherNumberOfPlayers(Scanner scanner) throws EOFException {
        while (true) {
            System.out.println("Please, enter number of members: ");

            checkForInputTermination(scanner);

            String inputLine = scanner.nextLine();

            if (InputChecker.isValidInput(inputLine)) {
                numberOfMembers = InputManager.parseLine(inputLine, 1)[0];
                break;
            }
        }
    }

    private void gatherMNK(Scanner scanner) throws EOFException {
        while (true) {
            System.out.println("Please type m, n, k: ");

            checkForInputTermination(scanner);

            String inputLine = scanner.nextLine();

            if (InputChecker.isValidInput(inputLine)) {
                int[] lineArgs;

                try {
                    lineArgs = InputManager.parseLine(inputLine, 3);
                    m = lineArgs[0];
                    n = lineArgs[1];
                    k = lineArgs[2];
                } catch (NoSuchElementException e) {
                    System.out.println("Not enough arguments");
                    continue;
                }

                if (!InputChecker.isValidBoardSize(m, n)) {
                    System.out.println("Invalid board size, try again");
                    continue;
                } else if (!InputChecker.isValidKValue(k)) {
                    System.out.println("Invalid k value, try again");
                    continue;
                }

                break;
            }
        }
    }

    public Tournament(Scanner scanner, List<Player> givenPlayers, boolean tournamentLog, boolean gameLog) throws EOFException {
        isTournamentLogging = tournamentLog;
        isGameLogging = gameLog;
        players = givenPlayers;
        numberOfMembers = givenPlayers.size();

        gatherMNK(scanner);

        poolOfRemainingPlayers = new ArrayList<>(givenPlayers);
        for (int i = 0; i < givenPlayers.size(); i++) {
            playerToId.put(players.get(i), i + 1);
        }
    }

    public Tournament(Scanner scanner, boolean tournamentLog, boolean gameLog) throws EOFException {
        gatherNumberOfPlayers(scanner);
        gatherMNK(scanner);

        isTournamentLogging = tournamentLog;
        isGameLogging = gameLog;
        List<Player> defaultPlayers = new ArrayList<>();

        for (int i = 0; i < numberOfMembers; i++) {
            Player defaultPlayer = new RandomPlayer();
            playerToId.put(defaultPlayer, i + 1);
            defaultPlayers.add(defaultPlayer);
        }

        players = defaultPlayers;
        poolOfRemainingPlayers = new ArrayList<>(defaultPlayers);
    }

    private void createNextRound() {
        round.clear();
        numberOfRound++;

        for (int i = 0; i + 1 < poolOfRemainingPlayers.size(); i += 2)  {
            randomizePlayers(poolOfRemainingPlayers, i);

            round.add(
                    new Game(
                            isGameLogging,
                            poolOfRemainingPlayers.get(i),
                            poolOfRemainingPlayers.get(i + 1),
                            k
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

        for (Game game: round) {
            if (isTournamentLogging) {
                logPlayersGame(game);
            }

            if (isGameLogging) {
                System.out.println();
            }

            int result = 0;
            while (result == 0) {
                result = game.play(new MNBoard(m, n));
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
