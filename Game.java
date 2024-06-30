import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class Game {
    private User hostPlayer, guestPlayer;
    private boolean doubleMood=false, betMood=false;
    private Block[] hostTimeLine=new Block[21];
    private Block[] guestTimeLine=new Block[21];
    private Card[] hostCards=new Card[6];
    private Card[] guestCards=new Card[6];
    private int betCoin=0;
    Random random=new Random();
    private String time, date;
    private int round;
    private String status;
    private String secondUser;
    private boolean endGame=false;
    public Game(User hostPlayer, Scanner scanner, RegistryMenu registryMenu, Outputs outputs){
        this.hostPlayer=hostPlayer;
        chooseMood(scanner, registryMenu, outputs);
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        this.date = currentDate.format(dateFormatter);
        this.time = currentTime.format(timeFormatter);
    }
    public Game(String guestUsername, String date, String time, String status){
        secondUser = guestUsername;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    //we are new:
    public String getDateAndTime(){
        return date+" "+time;
    }
    public String getStatus(){
        return status;
    }
    public String getTime(){
        return time;
    }
    public String getDate(){
        return date;
    }
    public String getSecondUser(){
        return secondUser;
    }
    public void addBetToCoin(int amount){this.betCoin+=amount;}
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
        System.out.println("How much is "+hostPlayer.getUsername()+" paying?");
        int amount=scanner.nextInt();
        if(amount>hostPlayer.getCoin()){
            System.out.println("Not enough money in "+hostPlayer.getUsername()+"'s wallet! suggest another amount.");
            getBetCoinsFromHost(scanner);
            return;
        }
        if(amount<=0){
            System.out.println("must bet a positive amount! try again.");
            getBetCoinsFromHost(scanner);
            return;
        }
        hostPlayer.addCoin(-1*amount);
        addBetToCoin(amount);
    }
    public void getBetCoinsFromGuest(Scanner scanner){
        System.out.println("How much is "+guestPlayer.getUsername()+" paying?");
        int amount=scanner.nextInt();
        if(amount>guestPlayer.getCoin()){
            System.out.println("Not enough money in "+guestPlayer.getUsername()+"'s wallet! suggest another amount.");
            getBetCoinsFromGuest(scanner);
            return;
        }
        if(amount<=0){
            System.out.println("must bet a positive amount! try again.");
            getBetCoinsFromGuest(scanner);
            return;
        }
        guestPlayer.addCoin(-1*amount);
        addBetToCoin(amount);
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
            else if(user.getUsername().equals(hostPlayer.getUsername())){
                System.out.println("can't play with yourself! try again.");
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
        System.out.println("choose "+hostPlayer.getUsername()+"'s character: ");
        String answer=scanner.nextLine();
        switch (answer){
            case "1": {
                hostPlayer.setCharacter("Harry Potter");
                System.out.println(hostPlayer.getUsername()+"'s character is Harry Potter!");
                break;
            }
            case "2": {
                hostPlayer.setCharacter("Ronald Weasley");
                System.out.println(hostPlayer.getUsername()+"'s character is Ronald Weasley!");
                break;
            }
            case "3": {
                hostPlayer.setCharacter("Hermione Granger");
                System.out.println(hostPlayer.getUsername()+"'s character is Hermione Granger!");
                break;

            }
            case "4": {
                hostPlayer.setCharacter("Draco Malfoy");
                System.out.println(hostPlayer.getUsername()+"'s character is Draco Malfoy!");
                break;
            }
        }
        System.out.println("choose "+guestPlayer.getUsername()+"'s character: ");
        answer=scanner.nextLine();
        switch (answer){
            case "1": {
                guestPlayer.setCharacter("Harry Potter");
                System.out.println(guestPlayer.getUsername()+"'s character is Harry Potter!");
                break;
            }
            case "2": {
                guestPlayer.setCharacter("Ronald Weasley");
                System.out.println(guestPlayer.getUsername()+"'s character is Ronald Weasley!");
                break;
            }
            case "3": {
                guestPlayer.setCharacter("Hermione Granger");
                System.out.println(guestPlayer.getUsername()+"'s character is Hermione Granger!");
                break;

            }
            case "4": {
                guestPlayer.setCharacter("Draco Malfoy");
                System.out.println(guestPlayer.getUsername()+"'s character is Draco Malfoy!");
                break;
            }
        }
    }
    public void run(ArrayList<Damage_Heal> cards, ArrayList<Spell> spells, Scanner scanner, RegistryMenu registryMenu){
        if(hostPlayer.getCardDeck().isEmpty()&&hostPlayer.getSpellDeck().isEmpty()) {
            hostPlayer.getRandDeck(cards, spells);
            System.out.println("Starterpack for "+hostPlayer.getUsername()+":");
            hostPlayer.showDeck();
        }
        if(guestPlayer.getCardDeck().isEmpty()&&guestPlayer.getSpellDeck().isEmpty()) {
            guestPlayer.getRandDeck(cards, spells);
            System.out.println("Starterpack for "+guestPlayer+":");
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
        hostCards[5]=null;
        guestCards[5]=null;
        hostTimeLine[random.nextInt(21)].setDestroyed();
        guestTimeLine[random.nextInt(21)].setDestroyed();
        round=0;
        while(true){
            System.out.println("Round "+(++round)+":");
            showGameDetails();
            System.out.println(guestPlayer.getUsername()+": ");
            deploy(scanner,false, round);
            if(endGame)
                break;
            System.out.println(hostPlayer.getUsername()+": ");
            deploy(scanner,true, round);
            if(endGame)
                break;
            checkTimeLine();
            System.out.println("Round "+(round)+" is over");
            if(round==4)
                break;
        }
        if(!endGame)
            checkWinner(cards,spells,scanner, registryMenu);
    }
    public void deploy(Scanner scanner, Boolean isHostPlaying, int round){
        String command=scanner.nextLine();
        if(command.equals("next round"))
            return;
        else if(command.equals("end game")){
            endGame=true;
            return;
        }
        String deployCard="^place\\s+card\\s+(?<name>\\w+(?: \\w+)*)\\s+in\\s+block\\s+(?<number>\\w+)$";
        String deploySpell="^deploy\\s+card\\s+(?<name>\\w+(?: \\w+)*)$";
        Matcher cardMatcher=getCommandMatcher(command,deployCard);
        Matcher spellMatcher=getCommandMatcher(command,deploySpell);
        if(cardMatcher.matches()){
            if(isHostPlaying){
                if(getHostCardByName(cardMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, true, round);
                    return;
                }
                else if(getHostCardByName(cardMatcher.group("name")).getClass().equals(Spell.class)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, true, round);
                    return;
                }
                Damage_Heal card=(Damage_Heal) getHostCardByName(cardMatcher.group("name"));
                if(card.getDuration()+Integer.parseInt(cardMatcher.group("number"))-1>20||checkDestroyedOrFull(card.getDuration(),Integer.parseInt(cardMatcher.group("number")),true)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, true, round);
                    return;
                }
                for(int i=0; i<card.getDuration(); i++)
                    hostTimeLine[i+Integer.parseInt(cardMatcher.group("number"))-1].setCard(card);
                hostPlayer.removeCardFromDeck(card);
                System.out.println("card placed successfully!");
                for(int i=0; i<6; i++)
                    if(hostCards[i]!=null&&hostCards[i].getName().equals(card.getName())) {
                        this.hostCards[i]=hostPlayer.getDeck().get(random.nextInt(hostPlayer.getDeck().size()));
                        break;
                    }
            }
            if(!isHostPlaying){
                if(getGuestCardByName(cardMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, false, round);
                    return;
                }
                else if(getGuestCardByName(cardMatcher.group("name")).getClass().equals(Spell.class)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, false, round);
                    return;
                }
                Damage_Heal card=(Damage_Heal) getGuestCardByName(cardMatcher.group("name"));
                if(card.getDuration()+Integer.parseInt(cardMatcher.group("number"))-1>20||checkDestroyedOrFull(card.getDuration(),Integer.parseInt(cardMatcher.group("number")),false)){
                    System.out.println("can't place this card! try again.");
                    deploy(scanner, false, round);
                    return;
                }
                for(int i=0; i<card.getDuration(); i++)
                    guestTimeLine[i+Integer.parseInt(cardMatcher.group("number"))-1].setCard(card);
                guestPlayer.removeCardFromDeck(card);
                System.out.println("card placed successfully!");
                for(int i=0; i<6; i++)
                    if(guestCards[i]!=null&&guestCards[i].getName().equals(card.getName())) {
                        this.guestCards[i]=guestPlayer.getDeck().get(random.nextInt(guestPlayer.getDeck().size()));
                        break;
                    }
            }
        }
        else if(spellMatcher.matches()){
            if(isHostPlaying){
                if(getHostCardByName(spellMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, true, round);
                    return;
                }
                else if(getHostCardByName(spellMatcher.group("name")).getClass().equals(Damage_Heal.class)){
                    System.out.println("can't deploy this card! try again.");
                    deploy(scanner, true, round);
                    return;
                }
                Spell card=(Spell) getHostCardByName(spellMatcher.group("name"));
                card.Deploy(hostTimeLine,guestTimeLine,hostCards,guestCards,true,scanner,guestPlayer,hostPlayer,round);
                hostPlayer.removeCardFromDeck(card);
                System.out.println("card deployed successfully!");
                for(int i=0; i<6; i++)
                    if(hostCards[i]!=null&&hostCards[i].getName().equals(card.getName())) {
                        this.hostCards[i]=hostPlayer.getDeck().get(random.nextInt(hostPlayer.getDeck().size()));
                        break;
                    }
            }
            if(!isHostPlaying){
                if(getGuestCardByName(spellMatcher.group("name"))==null){
                    System.out.println("you don't have this card! try again.");
                    deploy(scanner, false, round);
                    return;
                }
                else if(getGuestCardByName(spellMatcher.group("name")).getClass().equals(Damage_Heal.class)){
                    System.out.println("can't deploy this card! try again.");
                    deploy(scanner, false, round);
                    return;
                }
                Spell card=(Spell) getGuestCardByName(spellMatcher.group("name"));
                card.Deploy(hostTimeLine,guestTimeLine,hostCards,guestCards,false,scanner,guestPlayer,hostPlayer,round);
                guestPlayer.removeCardFromDeck(card);
                System.out.println("card deployed successfully!");
                for(int i=0; i<6; i++)
                    if(guestCards[i]!=null&&guestCards[i].getName().equals(card.getName())) {
                        this.guestCards[i]=guestPlayer.getDeck().get(random.nextInt(guestPlayer.getDeck().size()));
                        break;
                    }
            }
        }
        else {
            System.out.println("invalid command! try again.");
            deploy(scanner, isHostPlaying, round);
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
        System.out.println(hostPlayer.getUsername()+": "+getHostDamage());
        System.out.println(guestPlayer.getUsername()+": "+getGuestDamage());
    }
    public int getHostDamage(){
        int hostDamage=0;
        for(int i=0; i<21; i++)
            if (hostTimeLine[i].getCard() != null && !hostTimeLine[i].hasFailed() && !hostTimeLine[i].isDestroyed()) {
                Damage_Heal hostCard = (Damage_Heal) hostTimeLine[i].getCard();
                if(hostPlayer.isCardCharacter(hostTimeLine[i].getCard()))
                    hostDamage += (hostCard.getDamage() / hostCard.getDuration()+5);
                else
                    hostDamage += hostCard.getDamage() / hostCard.getDuration();
            }
        return hostDamage;
    }
    public int getGuestDamage(){
        int guestDamage=0;
        for(int i=0; i<21; i++)
            if (guestTimeLine[i].getCard() != null && !guestTimeLine[i].hasFailed() && !guestTimeLine[i].isDestroyed()) {
                Damage_Heal guestCard = (Damage_Heal) guestTimeLine[i].getCard();
                if(guestPlayer.isCardCharacter(guestTimeLine[i].getCard()))
                    guestDamage+=(guestCard.getDamage() / guestCard.getDuration()+5);
                else
                    guestDamage+=guestCard.getDamage() / guestCard.getDuration();
            }
        return guestDamage;
    }
    public void checkWinner(ArrayList<Damage_Heal> cards, ArrayList<Spell> spells, Scanner scanner, RegistryMenu registryMenu){
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
                System.out.println("Game has ended. Winner: "+ guestPlayer.getUsername());
                if(betMood) {
                    System.out.println("Bet coin "+betCoin+" added to "+guestPlayer.getUsername()+"'s wallet!");
                    guestPlayer.addCoin(betCoin);
                }
                System.out.println(Math.abs(getGuestDamage()-getHostDamage())+" coins added to "+guestPlayer.getUsername()+"'s wallet!");
                System.out.println(10*(Math.abs(getGuestDamage()-getHostDamage()))+" added to "+guestPlayer.getUsername()+"'s experience!");
                guestPlayer.addCoin(Math.abs(getGuestDamage()-getHostDamage()));
                guestPlayer.addExp(10*(Math.abs(getGuestDamage()-getHostDamage())));
                guestPlayer.updateLevel();
                guestPlayer.reduceHP(-50);
                hostPlayer.resetHP();
                String gameForMainMenuHistory = hostPlayer.getUsername() + " vs " + guestPlayer.getUsername() + " " + getDateAndTime() + " winner: " + guestPlayer.getUsername();
                String forWinner = hostPlayer.getUsername() + " " + getDateAndTime() + " won";
                String forLoser = guestPlayer.getUsername() + " " + getDateAndTime() + " lose";
                hostPlayer.addGame(forLoser);
                guestPlayer.addGame(forWinner);
                registryMenu.addGame(gameForMainMenuHistory);
                return;
            }
            else if(guestPlayer.getHp()<=0){
                System.out.println("Game has ended. Winner: "+hostPlayer.getUsername());
                if(betMood) {
                    System.out.println("Bet coin "+betCoin+" added to "+hostPlayer.getUsername()+"'s wallet!");
                    hostPlayer.addCoin(betCoin);
                }
                System.out.println(Math.abs(getGuestDamage()-getHostDamage())+" coins added to "+hostPlayer.getUsername()+"'s wallet!");
                System.out.println(10*(Math.abs(getGuestDamage()-getHostDamage()))+" added to "+hostPlayer.getUsername()+"'s experience!");
                hostPlayer.addCoin(Math.abs(getGuestDamage()-getHostDamage()));
                hostPlayer.addExp(10*(Math.abs(getGuestDamage()-getHostDamage())));
                hostPlayer.updateLevel();
                hostPlayer.reduceHP(-50);
                guestPlayer.resetHP();
                String gameForMainMenuHistory = hostPlayer.getUsername() + " vs " + guestPlayer.getUsername() + " " + getDateAndTime() + " winner: " + hostPlayer.getUsername();
                String forLoser = hostPlayer.getUsername() + " " + getDateAndTime() + " lose";
                String forWinner = guestPlayer.getUsername() + " " + getDateAndTime() + " won";
                hostPlayer.addGame(forWinner);
                guestPlayer.addGame(forLoser);
                registryMenu.addGame(gameForMainMenuHistory);
                return;
            }
        }
        System.out.println("No winner, next round begins!");
        run(cards,spells,scanner, registryMenu);
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
        System.out.println("Time line:");
        System.out.print(hostPlayer.getUsername()+": ");
        for(int i=0; i<21; i++) {
            System.out.print("|");
            hostTimeLine[i].printBlock();
            if(hostTimeLine[i].hasFailed())
                System.out.print("*");
            if(i==20)
                System.out.print("|");
        }
        System.out.println();
        System.out.print(guestPlayer.getUsername()+": ");
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
        System.out.println("Available cards: ");
        System.out.print(hostPlayer.getUsername()+": ");
        for(int i=0; i<6; i++) {
            if(hostCards[5]==null&&hostCards[i]!=null) {
                System.out.print("|" + hostCards[i].getName());
                if (i == 4)
                    System.out.print("|");
            }
            else if(hostCards[i]!=null) {
                System.out.print("|" + hostCards[i].getName());
                if (i == 5)
                    System.out.print("|");
            }
        }
        System.out.println();
        System.out.print(guestPlayer.getUsername()+": ");
        for(int i=0; i<6; i++) {
            if(guestCards[5]==null&&guestCards[i]!=null) {
                System.out.print("|" + guestCards[i].getName());
                if (i == 4)
                    System.out.print("|");
            }
            else if(guestCards[i]!=null) {
                System.out.print("|" + guestCards[i].getName());
                if (i == 5)
                    System.out.print("|");
            }
        }
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("Players' damage: ");
        getPlayersDamage();
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("Players' hp: ");
        System.out.println(hostPlayer.getUsername()+": "+hostPlayer.getHp());
        System.out.println(guestPlayer.getUsername()+": "+guestPlayer.getHp());
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("Players' character: ");
        System.out.println(hostPlayer.getUsername()+": "+hostPlayer.getCharacter());
        System.out.println(guestPlayer.getUsername()+": "+guestPlayer.getCharacter());
    }
    public Card getGuestCardByName(String name){
        for(int i=0; i<6; i++){
            if(guestCards[i]!=null&&guestCards[i].getName().equals(name))
                return guestCards[i];
        }
        return null;
    }
    public Card getHostCardByName(String name){
        for(int i=0; i<6; i++){
            if(hostCards[i]!=null&&hostCards[i].getName().equals(name))
                return hostCards[i];
        }
        return null;
    }

    @Override
    public String toString(){
        return "vs\t" + this.secondUser + " " + this.time + " " + this.date + " " + this.status;
    }
}
