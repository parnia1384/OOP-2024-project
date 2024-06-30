import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String passwordRecoveryQuestion;
    private int numberOfQuestion;
    private String answer;
    private int exp;
    private int hp;
    private int coin;
    private int level;
    private int score;
    private String character;
    private ArrayList<Damage_Heal> cardDeck = new ArrayList<>();
    private ArrayList<Spell> spellDeck = new ArrayList<>();
    final private ArrayList<String> gamesHistory = new ArrayList<>();
    final private ArrayList<Game> games = new ArrayList<>();
    Random random = new Random();
    User(String username, String password, String email, String nickname){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.hp = 100;
        this.level = 1;
        this.coin = 300;
        this.score=10;
    }
    User(){
        this.username = " ";
    }
    //setter methods:
    public void setScore(int score){
        this.score = score;
    }
    public void setCharacter(String character){this.character=character;}
    public void setLevel(int level){
        this.level = level;
    }
    public void setCoin(int newCoin){
        this.coin = newCoin;
    }
    public void setNumberOfQuestion(int number){
        this.numberOfQuestion = number;
        if(number == 1) this.passwordRecoveryQuestion = "What is your father’s name ?";
        if(number == 2) this.passwordRecoveryQuestion = "What is your favourite color ?";
        if(number == 3) this.passwordRecoveryQuestion = "What was the name of your first pet?";
    }
    //getter methods:
    public int getScore(){
        return score;
    }
    public ArrayList<Game> getGames(){
        return games;
    }
    public int getLevel(){
        return this.level;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getNickname(){
        return nickname;
    }
    public String getEmail(){
        return email;
    }
    public int getCoin(){
        return coin;
    }
    public int getHp(){
        return hp;
    }
    public int getExp(){
        return exp;
    }
    public void setHp(int hp){
        this.hp = hp;
    }
    public void reduceHP(int amount){this.hp-=amount;}
    public void resetHP(){this.hp=100;}
    public void setExp(int newExp){
        this.exp = newExp;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    public ArrayList<Damage_Heal> getCardDeck(){return cardDeck;}
    public ArrayList<Spell> getSpellDeck(){return spellDeck;}
    public ArrayList<Card> getDeck(){
        ArrayList<Card> deck = new ArrayList<>();
        deck.addAll(spellDeck);
        deck.addAll(cardDeck);
        return deck;
    }
    public String getCharacter(){return character;}
    public void getRandDeck(ArrayList<Damage_Heal> cards, ArrayList<Spell> spells) {
        ArrayList<Damage_Heal> tempCards = new ArrayList<>(cards);
        ArrayList<Spell> tempSpells = new ArrayList<>(spells);
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(tempSpells.size());
            Spell card = tempSpells.get(Math.abs(randomIndex));
            spellDeck.add(card);
            tempSpells.remove(Math.abs(randomIndex));
        }
        for (int i = 0; i < 15; i++) {
            try {
                int randomIndex = random.nextInt(tempCards.size());
                Damage_Heal card = tempCards.get(Math.abs(randomIndex));
                cardDeck.add(card);
                tempCards.remove(Math.abs(randomIndex));
            }
            catch (Exception exception){
                System.out.println(exception);
            }
        }
    }
    public Damage_Heal getCardFromDeckByName(String name){
        for(Damage_Heal card: cardDeck)
            if(card.getName().equals(name))
                return card;
        return null;
    }
    public Spell getSpellFromDeckByName(String name){
        for(Spell card: spellDeck)
            if(card.getName().equals(name))
                return card;
        return null;
    }
    public String getPasswordRecoveryQuestion(){
        return passwordRecoveryQuestion;
    }
    public boolean trueAnswer(String enteredAnswer){
        if(enteredAnswer.equals(answer)) return true;
        else return false;
    }
    //methods for add/update/remove something:
    public void updateLevel(){
        int tempLevel=level;
        while(exp>150*level*level){
            exp-=150*level*level;
            level++;
        }
        if(level>tempLevel){
            System.out.println(username+"'s level increased to "+level+"! "+100*level+" coins added to their wallet.");
            coin+=100*level;
            score+=100*(level-tempLevel);
        }
    }
    public void addCoin(int amount){this.coin+=amount;}
    public void addExp(int amount){this.exp+=amount;}
    public void removeCardFromDeck(Card card){
        if(card.getClass().equals(Spell.class))
            spellDeck.remove(card);
    }
    public void addToDeck(Damage_Heal card){
        cardDeck.add(card);
    }
    public void addToDeck(Spell card){
        spellDeck.add(card);
    }
    public void upgradeCard(String name){
        if(getCardFromDeckByName(name)!=null) {
            if(getCardFromDeckByName(name).getUpgradeCost()>this.getCoin()||getCardFromDeckByName(name).getUpgradeLevel()>this.getLevel())
                System.out.println("not enough level or money!");
            else
                getCardFromDeckByName(name).upgrade();
        }
    }
    public void addGame(String game){
        this.gamesHistory.add(game);
    }
    public void addGamesToGames(Game game){
        games.add(game);
    }
    //changer:
    public void changePassword(String newPassword){
        this.password = newPassword;
    }
    public void changeUsername(String username){
        this.username = username;
        System.out.println("Username changed successfully!\nYour new username is: " + this.username);
    }
    public void changeEmail(String newEmail){
        this.email = newEmail;
        System.out.println("Email changed successfully!\nYour new email is: " + email);
    }
    public void changeNickname(String newNickname){
        this.nickname = newNickname;
        System.out.println("Nickname changed successfully!\nYour new nickname is: " + nickname);
    }
    //sort methods:
    public void sortGamesByDateAndTime(){
        Comparator<Game> gamesComparator = Comparator.comparing(Game::getDate).thenComparing(Game::getTime);
        Collections.sort(games, gamesComparator);
    }
    public void sortGamesBasedOnStatus(){
        Comparator<Game> gamesComparator = Comparator.comparing(Game::getStatus);
        Collections.sort(games, gamesComparator);
    }
    public void sortGamesBasedOnCompetitor(){
        Comparator<Game> gamesComparator = Comparator.comparing(Game::getSecondUser);
        Collections.sort(games, gamesComparator);
    }
    public boolean isCardCharacter(Card card){
        if(character.equals("Harry Potter")&&(card.getName().equals("QUILIN")||card.getName().equals("HIPPOGRIFF")||card.getName().equals("PHONIX")||card.getName().equals("CENTAUR")))
            return true;
        if(character.equals("Ronald Weasley")&&(card.getName().equals("UNICORN")||card.getName().equals("NIFFLER")||card.getName().equals("THESTRAL")))
            return true;
        if(character.equals("Hermione Granger")&&(card.getName().equals("HOUSEELF")||card.getName().equals("DEATHEATOR")||card.getName().equals("BOGGART")||card.getName().equals("WERWOLF")))
            return true;
        if(character.equals("Draco Malfoy")&&(card.getName().equals("ARAGOG")||card.getName().equals("VAMPIRE")||card.getName().equals("BASILLISK")||card.getName().equals("DEMENTOR")))
            return true;
        return false;
    }
    public void showDeck(){
        int num = 0;
        if(cardDeck.isEmpty() && spellDeck.isEmpty())
            System.out.println("Your deck is empty.");
        else{
            for(Damage_Heal card: cardDeck)
                System.out.println(++num+". name: "+card.getName()+" defence_attack: "+card.getDefence_attack()+" duration: "+card.getDuration()+" damage: "+card.getDamage()+" upgradeLeve: "+card.getUpgradeLevel()+" upgradeCost: "+card.getUpgradeCost());
            for(Spell card: spellDeck)
                System.out.println(++num+". name: "+card.getName());
        }
    }
    @Override
    public String toString(){
        String userInformation = "new user:\n" + username + " " + password + " " + email + " " + nickname + " " + numberOfQuestion + " " + answer + " " + coin + " " + exp + " " + hp + " " + score + " " + level + '\n';
        userInformation += "user damage_heal cards:\n";
        for(Damage_Heal card : cardDeck)
            userInformation += (card.toString() + '\n');
        userInformation += "Done!\nSpell cards:\n";
        for(Spell card : spellDeck)
            userInformation += (card.getName() + '\n');
        userInformation += "Done!\nmy games:\n";
        for(String game : gamesHistory)
            userInformation += (game + '\n');
        userInformation += "Done!\n";
        return userInformation;
    }
}
