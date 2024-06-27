import java.util.ArrayList;

public class User {
    final private String username;
    private String password;
    final private String nickname;
    final private String email;
    private String passwordRecoveryQuestion;
    private String answer;
    private int exp;
    private int hp;
    private int coin;
    private int level;
    private ArrayList<Damage_Heal> cardDeck=new ArrayList<>();
    private ArrayList<Spell> spellDeck=new ArrayList<>();

    User(String username, String password, String email, String nickname){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.hp = 100;
        this.level=1;
    }
    public void getRandDeck(ArrayList<Damage_Heal> cards, ArrayList<Spell> spells){

    }
    public void addToDeck(Damage_Heal card){cardDeck.add(card);}
    public void addToDeck(Spell card){spellDeck.add(card);}

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
    public void upgradeCard(String name){
        if(getCardFromDeckByName(name)!=null) {
            if(getCardFromDeckByName(name).getUpgradeCost()>this.getCoin()||getCardFromDeckByName(name).getUpgradeLevel()>this.getLevel())
                System.out.println("not enough level or money!");
            else
                getCardFromDeckByName(name).upgrade();
        }

    }
    public int getLevel(){return level;}
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
        this.password = password;
    }
    public void setCoin(int newCoin){
        this.coin = newCoin;
    }
    public int getCoin(){
        return coin;
    }
    public void setHp(int hp){
        this.hp = hp;
    }
    public int getHp(){
        return hp;
    }
    public void setExp(int newExp){
        this.exp = newExp;
    }
    public int getExp(){
        return exp;
    }
    public void setPasswordRecoveryQuestion(String question){
        this.passwordRecoveryQuestion = question;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    public void showDeck(){
        int num=0;
        for(Damage_Heal card: cardDeck)
            System.out.println(++num+". name: "+card.getName()+"defence_attack: "+card.getDefence_attack()+"duration: "+card.getDuration()+"damage: "+card.getDamage()+"upgradeLeve: "+card.getUpgradeLevel()+"upgradeCost: "+card.getUpgradeCost());
        for(Spell card: spellDeck)
            System.out.println(++num+". name: "+card.getName());
    }
    public void showProfile(){

    }
    // @Override
    // public String toString(){
    //     String userInformation = username + " " + password + " " + email + " " + ;
    //     return userInformation;
    // }
}
