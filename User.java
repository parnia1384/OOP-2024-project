import java.util.ArrayList;
import java.util.Collections;
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
    private ArrayList<String> gamesHistory = new ArrayList<>();
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
    public ArrayList<Damage_Heal> getCardDeck(){return cardDeck;}
    public ArrayList<Spell> getSpellDeck(){return spellDeck;}
    public ArrayList<Card> getDeck(){
        ArrayList<Card> deck = new ArrayList<>();
        deck.addAll(spellDeck);
        deck.addAll(cardDeck);
        return deck;
    }
    public String getCharacter(){return character;}
    public void addCoin(int amount){this.coin+=amount;}
    public void addExp(int amount){this.exp+=amount;}
    public void removeCardFromDeck(Card card){
        if(card.getClass().equals(Damage_Heal.class))
            cardDeck.remove(card);
        else if(card.getClass().equals(Spell.class))
            spellDeck.remove(card);
    }
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
            int randomIndex = random.nextInt(tempCards.size());
            Damage_Heal card = tempCards.get(Math.abs(randomIndex));
            cardDeck.add(card);
            tempCards.remove(Math.abs(randomIndex));
        }
    }
    public void setCharacter(String character){this.character=character;}
    public void addToDeck(Damage_Heal card){
        cardDeck.add(card);
    }
    public void addToDeck(Spell card){
        spellDeck.add(card);
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
    public void upgradeCard(String name){
        if(getCardFromDeckByName(name)!=null) {
            if(getCardFromDeckByName(name).getUpgradeCost()>this.getCoin()||getCardFromDeckByName(name).getUpgradeLevel()>this.getLevel())
                System.out.println("not enough level or money!");
            else
                getCardFromDeckByName(name).upgrade();
        }
    }
    public int getLevel(){
        return this.level;
    }
    public void setLevel(int level){
        this.level = level;
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
    public String getPasswordRecoveryQuestion(){
        return passwordRecoveryQuestion;
    }
    public boolean trueAnswer(String enteredAnswer){
        if(enteredAnswer.equals(answer)) return true;
        else return false;
    }
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
    public void setCoin(int newCoin){
        this.coin = newCoin;
    }
    public void setNumberOfQuestion(int number){
        this.numberOfQuestion = number;
        if(number == 1) this.passwordRecoveryQuestion = "What is your father’s name ?";
        if(number == 2) this.passwordRecoveryQuestion = "What is your favourite color ?";
        if(number == 3) this.passwordRecoveryQuestion = "What was the name of your first pet?";
    }
    public int getCoin(){
        return coin;
    }
    public void setHp(int hp){
        this.hp = hp;
    }
    public void reduceHP(int amount){this.hp-=amount;}
    public int getHp(){
        return hp;
    }
    public void setExp(int newExp){
        this.exp = newExp;
    }
    public int getExp(){
        return exp;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    public void setGamesHistory(ArrayList<String> file){
        this.gamesHistory = file;
    }
    public void addGame(String game){
        this.gamesHistory.add(game);
    }
    public ArrayList<String> getGamesHistory(){
        return gamesHistory;
    }
    @Override
    public String toString(){
        String userInformation = "new user:\n" + username + " " + password + " " + email + " " + nickname + " " + numberOfQuestion + " " + answer + " " + coin + " " + exp + " " + hp + '\n';
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
