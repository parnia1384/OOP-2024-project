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
    private User winner, loser;
    private int betCoin;
    Random random=new Random();

    public Game(User hostPlayer, Scanner scanner, RegistryMenu registryMenu, Outputs outputs){
        this.hostPlayer=hostPlayer;
        chooseMood(scanner, registryMenu, outputs);
        this.betCoin=0;
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
            this.betMood=true;
            guestLogin(scanner, registryMenu, outputs);
            getBetCoinsFromHost(scanner);
            getBetCoinsFromGuest(scanner);
        }
        else{
            System.out.println("invalid answer! please try again.");
            chooseMood(scanner, registryMenu, outputs);
        }
    }
    public void getBetCoinsFromHost(Scanner scanner){
        System.out.println("How much is the host paying?");
        int amount=scanner.nextInt();
        if(amount>hostPlayer.getCoin()){
            System.out.println("Not enough money in host wallet! suggest another amount.");
            getBetCoinsFromHost(scanner);
            return;
        }
        betCoin+=amount;
    }
    public void getBetCoinsFromGuest(Scanner scanner){
        System.out.println("How much is the guest paying?");
        int amount=scanner.nextInt();
        if(amount>guestPlayer.getCoin()){
            System.out.println("Not enough money in guest's wallet! suggest another amount.");
            getBetCoinsFromHost(scanner);
            return;
        }
        betCoin+=amount;
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
               checkTimeLine();
               System.out.println("Round "+i+" is over");
           }
           checkWinner(cards,spells,scanner);
    }
    public void deploy(Scanner scanner, Boolean isHostPlaying){
        String command=scanner.nextLine();
        if(command.equals("next round"))
            return;
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
                hostPlayer.removeCardFromDeck(card);
                System.out.println("card placed successfully!");
                for(int i=0; i<5; i++)
                    if(hostCards[i].getName().equals(card.getName())) {
                        this.hostCards[i]=hostPlayer.getDeck().get(random.nextInt(hostPlayer.getDeck().size()));
                        break;
                    }
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
                guestPlayer.removeCardFromDeck(card);
                System.out.println("card placed successfully!");
                for(int i=0; i<5; i++)
                    if(guestCards[i].getName().equals(card.getName())) {
                        this.guestCards[i]=guestPlayer.getDeck().get(random.nextInt(guestPlayer.getDeck().size()));
                        break;
                    }
            }
        }
        else if(spellMatcher.matches()){
            if(isHostPlaying){
                if(getHostCardByName(spellMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, true);
                    return;
                }
                else if(getHostCardByName(spellMatcher.group("name")).getClass().equals(Damage_Heal.class)){
                    System.out.println("can't deploy this card! try again.");
                    deploy(scanner, true);
                    return;
                }
                Spell card=(Spell) getHostCardByName(spellMatcher.group("name"));
                card.Deploy();
                hostPlayer.removeCardFromDeck(card);
                System.out.println("card deployed successfully!");
                for(int i=0; i<5; i++)
                    if(hostCards[i].getName().equals(card.getName())) {
                        this.hostCards[i]=hostPlayer.getDeck().get(random.nextInt(hostPlayer.getDeck().size()));
                        break;
                    }
            }
            if(!isHostPlaying){
                if(getGuestCardByName(spellMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, false);
                    return;
                }
                else if(getGuestCardByName(spellMatcher.group("name")).getClass().equals(Damage_Heal.class)){
                    System.out.println("can't deploy this card! try again.");
                    deploy(scanner, false);
                    return;
                }
                Spell card=(Spell) getGuestCardByName(spellMatcher.group("name"));
                card.Deploy();
                guestPlayer.removeCardFromDeck(card);
                System.out.println("card deployed successfully!");
                for(int i=0; i<5; i++)
                    if(guestCards[i].getName().equals(card.getName())) {
                        this.guestCards[i]=guestPlayer.getDeck().get(random.nextInt(guestPlayer.getDeck().size()));
                        break;
                    }
            }
        }
        else {
            System.out.println("invalid command! try again.");
            deploy(scanner, isHostPlaying);
        }
    }
    public void checkTimeLine(){
        for(int i=0; i<21; i++){
            if(hostTimeLine[i].getCard()!=null&&guestTimeLine[i].getCard()!=null){
                Damage_Heal hostCard=(Damage_Heal) hostTimeLine[i].getCard();
                Damage_Heal guestCard=(Damage_Heal) guestTimeLine[i].getCard();
                if(hostCard.getDamage()/hostCard.getDuration()>guestCard.getDamage()/guestCard.getDuration())
                    guestTimeLine[i].setFailed();
                else if(hostCard.getDamage()/hostCard.getDuration()<guestCard.getDamage()/guestCard.getDuration())
                    hostTimeLine[i].setFailed();
                else {
                    guestTimeLine[i].setFailed();
                    hostTimeLine[i].setFailed();
                }
            }
        }
    }
    public void getPlayersDamage(){
        System.out.println("Host: "+getHostDamage());
        System.out.println("Guest: "+getGuestDamage());
    }
    public int getHostDamage(){
        int hostDamage=0;
        for(int i=0; i<21; i++)
            if (hostTimeLine[i].getCard() != null && !hostTimeLine[i].hasFailed() && !hostTimeLine[i].isDestroyed()) {
                Damage_Heal hostCard = (Damage_Heal) hostTimeLine[i].getCard();
                hostDamage += hostCard.getDamage() / hostCard.getDuration();
            }
        return hostDamage;
    }
    public int getGuestDamage(){
        int guestDamage=0;
        for(int i=0; i<21; i++)
            if (guestTimeLine[i].getCard() != null && !guestTimeLine[i].hasFailed() && !guestTimeLine[i].isDestroyed()) {
                Damage_Heal guestCard = (Damage_Heal) guestTimeLine[i].getCard();
                guestDamage+=guestCard.getDamage() / guestCard.getDuration();
            }
        return guestDamage;
    }
    public void checkWinner(ArrayList<Damage_Heal> cards, ArrayList<Spell> spells, Scanner scanner){
        for(int i=0; i<21; i++){
            if(hostTimeLine[i].getCard()!=null&&!hostTimeLine[i].hasFailed()&&!hostTimeLine[i].isDestroyed()) {
                Damage_Heal hostCard=(Damage_Heal) hostTimeLine[i].getCard();
                guestPlayer.reduceHP(hostCard.getDamage()/hostCard.getDuration());
            }
            else if(guestTimeLine[i].getCard()!=null&&!guestTimeLine[i].hasFailed()&&!guestTimeLine[i].isDestroyed()) {
                Damage_Heal guestCard=(Damage_Heal) guestTimeLine[i].getCard();
                hostPlayer.reduceHP(guestCard.getDamage()/guestCard.getDuration());
            }
            if(hostPlayer.getHp()<=0){
                System.out.println("Game has ended. Winner: "+guestPlayer.getUsername());
                if(betMood) {
                    System.out.println("Bet coin "+betCoin+" added to the guest's wallet!");
                    guestPlayer.addCoin(betCoin);
                }
                System.out.println(Math.abs(getGuestDamage()-getHostDamage())+" coins added to the guest's wallet!");
                System.out.println(10*(Math.abs(getGuestDamage()-getHostDamage()))+" added to the guest's experience!");
                guestPlayer.addCoin(Math.abs(getGuestDamage()-getHostDamage()));
                guestPlayer.addExp(10*(Math.abs(getGuestDamage()-getHostDamage())));
                return;
            }
            else if(guestPlayer.getHp()<=0){
                System.out.println("Game has ended. Winner: "+hostPlayer.getUsername());
                if(betMood) {
                    System.out.println("Bet coin "+betCoin+" added to the host's wallet!");
                    hostPlayer.addCoin(betCoin);
                }
                System.out.println(Math.abs(getGuestDamage()-getHostDamage())+" coins added to the host's wallet!");
                System.out.println(10*(Math.abs(getGuestDamage()-getHostDamage()))+" added to the host's experience!");
                guestPlayer.addCoin(Math.abs(getGuestDamage()-getHostDamage()));
                guestPlayer.addExp(10*(Math.abs(getGuestDamage()-getHostDamage())));
                return;
            }
        }
        System.out.println("No winner, next round begins!");
        run(cards,spells,scanner);
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
            if(hostTimeLine[i].hasFailed())
                System.out.print("*");
            if(i==20)
                System.out.print("|");
        }
        System.out.println();
        System.out.print("guest: ");
        for(int i=0; i<21; i++) {
            System.out.print("|");
            guestTimeLine[i].printBlock();
            if(guestTimeLine[i].hasFailed())
                System.out.print("*");
            if(i==20)
                System.out.print("|");
        }
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------");
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
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("players' damage: ");
        getPlayersDamage();
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("players' hp: ");
        System.out.println("Host: "+hostPlayer.getHp());
        System.out.println("Guest: "+guestPlayer.getHp());
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("players' character: ");
        System.out.println("Host: "+hostPlayer.getCharacter());
        System.out.println("Guest: "+guestPlayer.getCharacter());
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
