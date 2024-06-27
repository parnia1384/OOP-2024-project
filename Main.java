public class Main {
    public static void main(String[] args) {
        GameController run = new GameController();
        run.getInformationFromFile();
        System.out.println("If you don't know what to do, type \"command prompt\".");
        run.run();
        run.writeInformationInFile();
    }
}
