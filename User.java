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
    public void setPasswordRecoveryQuestion(String question){
        this.passwordRecoveryQuestion = question;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }

    // @Override
    // public String toString(){
    //     String userInformation = username + " " + password + " " + email + " " + ;
    //     return userInformation;
    // }
}
