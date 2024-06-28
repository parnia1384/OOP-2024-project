import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    private User hostPlayer, guestPlayer;
    private boolean doubleMood=false, betMood=false;
    private Block[] hostTimeLine=new Block[21];
    private Block[] guestTimeLine=new Block[21];
    private Card[] hostCards=new Card[5];
    private Card[] guestCards=new Card[5];
    private boolean isHostPlaying;

    Random random=new Random();

    public Game(User hostPlayer, Scanner scanner, RegistryMenu registryMenu, Outputs outputs){
        this.hostPlayer=hostPlayer;
        chooseMood(scanner, registryMenu, outputs);
        this.isHostPlaying=true;
    }
    private Matcher getCommandMatcher(String input, String regex){
        Pattern pattern=Pattern.compile(regex);
        return pattern.matcher(input);
    }
    public void chooseMood(Scanner scanner, RegistryMenu registryMenu, Outputs outputs){
        System.out.println("choose game mood: ");
        System.out.println("1. Double");
        System.out.println("2. Bet");
        String answer=scanner.nextLine();
        if(answer.equals("1")){
            this.doubleMood=true;
            guestLogin(scanner, registryMenu, outputs);
        }
        else if(answer.equals("2")){
            //I'll fill this later
        }
        else{
            System.out.println("invalid answer! please try again.");
            chooseMood(scanner, registryMenu, outputs);
        }
    }
    public void guestLogin(Scanner scanner, RegistryMenu registryMenu, Outputs outputs){
        System.out.println("guest login: ");
        String command=scanner.nextLine();
        String login = "user login -u (\\S+) -p (\\S+)";
        Matcher matcher=getCommandMatcher(command, login);
        if(!matcher.matches()) {
            System.out.println("invalid input!");
            guestLogin(scanner,registryMenu,outputs);
        }
        else{
            String username = matcher.group(1);
            String password = matcher.group(2);
            User user = registryMenu.findUserByUsername(username);
            if(user == null) {
                System.out.println(outputs.usernameDoesNotExist);
                guestLogin(scanner,registryMenu,outputs);
            }
            else if(!user.getPassword().equals(password)) {
                System.out.println(outputs.wrongPasswordEntered);
                guestLogin(scanner,registryMenu,outputs);
            }
            else{
                guestPlayer = user;
                System.out.println(outputs.loggedInSuccessfully);
                chooseCharachter(scanner);
            }
        }
    }
    public void chooseCharachter(Scanner scanner){
        System.out.println("Characters:");
        System.out.println("1. Harry Potter");
        System.out.println("2. Ronald Weasley");
        System.out.println("3. Hermione Granger");
        System.out.println("4. Draco Malfoy");
        System.out.println("choose host's character: ");
        String answer=scanner.nextLine();
        switch (answer){
            case "1": {
                hostPlayer.setCharacter("Harry Potter");
                System.out.println("Host's character is Harry Potter!");
                break;
            }
            case "2": {
                hostPlayer.setCharacter("Ronald Weasley");
                System.out.println("Host's character is Ronald Weasley!");
                break;
            }
            case "3": {
                hostPlayer.setCharacter("Hermione Granger");
                System.out.println("Host's character is Hermione Granger!");
                break;

            }
            case "4": {
                hostPlayer.setCharacter("Draco Malfoy");
                System.out.println("Host's character is Draco Malfoy!");
                break;
            }
        }
        System.out.println("choose guest's character: ");
        answer=scanner.nextLine();
        switch (answer){
            case "1": {
                guestPlayer.setCharacter("Harry Potter");
                System.out.println("Guest's character is Harry Potter!");
                break;
            }
            case "2": {
                guestPlayer.setCharacter("Ronald Weasley");
                System.out.println("Guest's character is Ronald Weasley!");
                break;
            }
            case "3": {
                guestPlayer.setCharacter("Hermione Granger");
                System.out.println("Guest's character is Hermione Granger!");
                break;

            }
            case "4": {
                guestPlayer.setCharacter("Draco Malfoy");
                System.out.println("Guest's character is Draco Malfoy!");
                break;
            }
        }
    }
    public void run(ArrayList<Damage_Heal> cards, ArrayList<Spell> spells, Scanner scanner){
        if(doubleMood){
           if(hostPlayer.getCardDeck().isEmpty()&&hostPlayer.getSpellDeck().isEmpty()) {
               hostPlayer.getRandDeck(cards, spells);
               System.out.println("Starterpack for the host:");
               hostPlayer.showDeck();
           }
           if(guestPlayer.getCardDeck().isEmpty()&&guestPlayer.getSpellDeck().isEmpty()) {
               guestPlayer.getRandDeck(cards, spells);
               System.out.println("Starterpack for the guest:");
               guestPlayer.showDeck();
           }
           for(int i=0; i<21; i++){
               if(i<5){
                   this.hostCards[i]=hostPlayer.getDeck().get(random.nextInt(hostPlayer.getDeck().size()));
                   this.guestCards[i]=guestPlayer.getDeck().get(random.nextInt(guestPlayer.getDeck().size()));
               }
               this.guestTimeLine[i]=new Block();
               this.hostTimeLine[i]=new Block();
           }
           hostTimeLine[random.nextInt(21)].setDestroyed();
           guestTimeLine[random.nextInt(21)].setDestroyed();
           for(int i=1; i<=4; i++){
               System.out.println("Round "+i+":");
               showGameDetails();
               System.out.println("Guest: ");
               //place card <name> in block <number>
               //deploy card <name>
               deploy(scanner,false);
               System.out.println("Host: ");
               deploy(scanner,true);
               System.out.println("Round "+i+" is over");
           }
        }
        else if(betMood){

        }
    }
    public void deploy(Scanner scanner, Boolean isHostPlaying){
        String command=scanner.nextLine();
        String deployCard="^place\\s+card\\s+(?<name>\\w+)\\s+in\\s+block\\s+(?<number>\\w+)$";
        String deploySpell="^deploy\\s+card\\s+(?<name>\\w+)$";
        Matcher cardMatcher=getCommandMatcher(command,deployCard);
        Matcher spellMatcher=getCommandMatcher(command,deploySpell);
        if(cardMatcher.matches()){
            if(isHostPlaying){
                if(getHostCardByName(cardMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, true);
                    return;
                }
                else if(getHostCardByName(cardMatcher.group("name")).getClass().equals(Spell.class)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, true);
                    return;
                }
                Damage_Heal card=(Damage_Heal) getHostCardByName(cardMatcher.group("name"));
                if(card.getDuration()+Integer.parseInt(cardMatcher.group("number"))-1>20||checkDestroyedOrFull(card.getDuration(),Integer.parseInt(cardMatcher.group("number")),true)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, true);
                    return;
                }
                for(int i=0; i<card.getDuration(); i++)
                    hostTimeLine[i+Integer.parseInt(cardMatcher.group("number"))-1].setCard(card);
                System.out.println("card placed successfully!");
            }
            if(!isHostPlaying){
                if(getGuestCardByName(cardMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, false);
                    return;
                }
                else if(getGuestCardByName(cardMatcher.group("name")).getClass().equals(Spell.class)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, false);
                    return;
                }
                Damage_Heal card=(Damage_Heal) getGuestCardByName(cardMatcher.group("name"));
                if(card.getDuration()+Integer.parseInt(cardMatcher.group("number"))-1>20||checkDestroyedOrFull(card.getDuration(),Integer.parseInt(cardMatcher.group("number")),false)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, false);
                    return;
                }
                for(int i=0; i<card.getDuration(); i++)
                    guestTimeLine[i+Integer.parseInt(cardMatcher.group("number"))-1].setCard(card);
                System.out.println("card placed successfully!");
            }
        }
        else if(spellMatcher.matches()){
            if(isHostPlaying){
                if(getHostCardByName(cardMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, isHostPlaying);
                    return;
                }
                else if(getHostCardByName(cardMatcher.group("name")).getClass().equals(Damage_Heal.class)){
                    System.out.println("can't deploy this card! try again.");
                    deploy(scanner, isHostPlaying);
                    return;
                }
                Spell card=(Spell) getHostCardByName(cardMatcher.group("name"));
                card.Deploy();
                System.out.println("card deployed successfully!");
            }
            if(!isHostPlaying){
                if(getGuestCardByName(cardMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, isHostPlaying);
                    return;
                }
                else if(getGuestCardByName(cardMatcher.group("name")).getClass().equals(Damage_Heal.class)){
                    System.out.println("can't deploy this card! try again.");
                    deploy(scanner, isHostPlaying);
                    return;
                }
                Spell card=(Spell) getGuestCardByName(cardMatcher.group("name"));
                card.Deploy();
                System.out.println("card deployed successfully!");
            }
        }
    }
    public boolean checkDestroyedOrFull(int duration, int block, boolean isHostPlaying){
        if(isHostPlaying){
            for(int i=0; i<duration; i++)
                if(hostTimeLine[block+i-1].isDestroyed()||hostTimeLine[block+i-1].getCard()!=null)
                    return true;
            return false;
        }
        for(int i=0; i<duration; i++)
            if(guestTimeLine[block+i-1].isDestroyed()||guestTimeLine[block+i-1].getCard()!=null)
                return true;
        return false;
    }
    public void showGameDetails(){
        //timeline
        System.out.println("time line:");
        System.out.print("host: ");
        for(int i=0; i<21; i++) {
            System.out.print("|");
            hostTimeLine[i].printBlock();
            if(i==20)
                System.out.print("|");
        }
        System.out.println();
        System.out.print("guest: ");
        for(int i=0; i<21; i++) {
            System.out.print("|");
            guestTimeLine[i].printBlock();
            if(i==20)
                System.out.print("|");
        }
        System.out.println();
        //cards
        System.out.println("available cards: ");
        System.out.print("host: ");
        for(int i=0; i<5; i++) {
            System.out.print("|"+hostCards[i].getName());
            if(i==4)
                System.out.print("|");
        }
        System.out.println();
        System.out.print("guest: ");
        for(int i=0; i<5; i++) {
            System.out.print("|"+guestCards[i].getName());
            if(i==4)
                System.out.print("|");
        }
        System.out.println();
        //players' damage???????????????????  Mr KHANDANNNNNNNNNNNNNNNNNNNNNNNNN
    }
    public Card getGuestCardByName(String name){
        for(int i=0; i<5; i++){
            if(guestCards[i].getName().equals(name))
                return guestCards[i];
        }
        return null;
    }
    public Card getHostCardByName(String name){
        for(int i=0; i<5; i++){
            if(hostCards[i].getName().equals(name))
                return hostCards[i];
        }
        return null;
    }
}
