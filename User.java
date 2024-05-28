public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String passwordRecoveryQuestion;
    private String answer;
    private int exp;
    private int hp;
    private int coin;
    User(String username, String password, String email, String nickname){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.hp = 100;

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
    public void setRecoveryConfirmation(String number, String answer){
        if(number.equals("1")) this.passwordRecoveryQuestion = "What is your father's name ?";
        if(number.equals("2")) this.passwordRecoveryQuestion = "What is your favourite color ?";
        if(number.equals("3")) this.passwordRecoveryQuestion = "What was the name of your first pet?";
        this.answer = answer;
    }
    public void saveInformation(){
        String userInformation = username + " " + password + " " + nickname + " " + email;
        String userConfirmation = passwordRecoveryQuestion + " " + answer;
        String numbers = String.valueOf(exp) + " " + String.valueOf(hp) + " " + String.valueOf(coin);
    }
}