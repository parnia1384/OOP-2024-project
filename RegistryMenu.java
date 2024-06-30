import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistryMenu {
    private ArrayList<User> users = new ArrayList<>();
    private User signedUpUser = null;
    final protected Outputs output = new Outputs();
    private ArrayList<String> gamesHistory = new ArrayList<>();
    public void signup(Matcher matcher, Scanner scanner){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String username = matcher.group(1);
            String password = matcher.group(2);
            String passwordRepetition = matcher.group(3);
            String email = matcher.group(4);
            String nickname = matcher.group(5);
            if(username.isEmpty() || password.isEmpty() || passwordRepetition.isEmpty() || email.isEmpty() || nickname.isEmpty())
                System.out.println(output.emptyField);
            else if(!password.equals(passwordRepetition))
                System.out.println(output.wrongPasswordRepetition);
            else if(!isUsernameCorrect(username))
                System.out.println(output.wrongUsernameFormat);
            else if(!isUsernameNew(username)) System.out.println(output.duplicateUsername);
            else if(!isEmailCorrect(email)) System.out.println(output.wrongEmailFormat);
            else if(password.length() < 8 || password.length() > 32) System.out.println(output.passwordLengthFault);
            else if(!doesPasswordContainsLowerCase(password) || !doesPasswordContainUpperCase(password)) System.out.println(output.passwordDoesNotContainUpperOrLowerCase);
            else if(!doesPasswordContainNumber(password)) System.out.println(output.passwordDoesNotContainNumber);
            else if(!passwordContainsCharacter(password)) System.out.println(output.passwordDoesNotContainCharacters);
            else{
                User newUser = new User(username, password, email, nickname);
                signedUpUser = newUser;
                users.add(newUser);
                System.out.println(output.successfullyAccount);
                String chooseSecurityQuestion = "question pick -q (\\S+) -a (\\S+) -c (\\S+)";
                String command;
                while(true){
                    command = scanner.nextLine();
                    if(command.matches(chooseSecurityQuestion)){
                        matcher = getCommandMatcher(command, chooseSecurityQuestion);
                        chooseSecurityQuestion(matcher, scanner);
                        break;
                    }
                    else System.out.println("You have to choose your security question first!");
                }
            }
        }
    }
    public void signupWithRandomPassword(Matcher matcher, Scanner scanner){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String username = matcher.group(1);
            String email = matcher.group(2);
            String nickname = matcher.group(3);
            if(!isUsernameCorrect(username) || !isUsernameCorrect(nickname)) System.out.println(output.wrongUsernameFormat);
            else if(!isUsernameNew(username)) System.out.println(output.duplicateUsername);
            else if(!isEmailCorrect(email)) System.out.println(output.wrongEmailFormat);
            else{
                CaptchaGenerator passwordGenerator = new CaptchaGenerator();
                String password = passwordGenerator.generateRandomStringForPassword();
                System.out.println(password);
                System.out.println("Enter password confirmation:");
                String confirmation = scanner.nextLine();
                while(!confirmation.equals(password)){
                    System.out.println("Password confirmation is not true. Please try again:");
                    confirmation = scanner.nextLine();
                }
                User newUser = new User(username, password, email, nickname);
                signedUpUser = newUser;
                users.add(newUser);
                System.out.println(output.successfullyAccount);
                String chooseSecurityQuestion = "question pick -q (\\S+) -a (\\S+) -c (\\S+)";
                String command;
                while(true){
                    command = scanner.nextLine();
                    if(command.matches(chooseSecurityQuestion)){
                        matcher = getCommandMatcher(command, chooseSecurityQuestion);
                        chooseSecurityQuestion(matcher, scanner);
                        break;
                    }
                    else System.out.println("You have to choose your security question first!");
                }
            }
        }
    }
    public void chooseSecurityQuestion(Matcher matcher, Scanner scanner){
        if(!matcher.matches() || signedUpUser == null) System.out.println("invalid input!");
        else{
            String questionNumber = matcher.group(1);
            String answer = matcher.group(2);
            String answerConfirmation = matcher.group(3);
            while(!answer.equals(answerConfirmation)) {
                System.out.println(output.wrongAnswerRepetition);
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                answer = parts[0];
                answerConfirmation = parts[1];
            }
            if(questionNumber.equals("1")) signedUpUser.setNumberOfQuestion(1);
            if(questionNumber.equals("2")) signedUpUser.setNumberOfQuestion(2);
            if(questionNumber.equals("3")) signedUpUser.setNumberOfQuestion(3);
            CaptchaGenerator captchaGenerator = new CaptchaGenerator();
            Random random = new Random();
            int i = random.nextInt();
            if(i % 2 == 0) captchaGenerator.simpleCaptcha(scanner);
            else captchaGenerator.asciiArtCaptcha(scanner);
            signedUpUser.setAnswer(answer);
            System.out.println("welcome " + signedUpUser.getUsername() + "!");
            signedUpUser = null;
        }
    }
    public void forgotPassword(Matcher matcher, Scanner scanner){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String username = matcher.group(1);
            User user = findUserByUsername(username);
            if(user == null) System.out.println(output.usernameDoesNotExist);
            else{
                System.out.println(user.getPasswordRecoveryQuestion());
                String answer = scanner.nextLine();
                if(!user.trueAnswer(answer)){
                    System.out.println("Wrong answer!");
                }
                else{
                    System.out.println("Enter new password:");
                    String newPassword = scanner.nextLine();
                    if(newPassword.length() < 8 || newPassword.length() > 32) System.out.println(output.passwordLengthFault);
                    else if(!doesPasswordContainsLowerCase(newPassword) || !doesPasswordContainUpperCase(newPassword)) System.out.println(output.passwordDoesNotContainUpperOrLowerCase);
                    else if(!doesPasswordContainNumber(newPassword)) System.out.println(output.passwordDoesNotContainNumber);
                    else if(!passwordContainsCharacter(newPassword)) System.out.println(output.passwordDoesNotContainCharacters);
                    else{
                        user.changePassword(newPassword);
                        System.out.println(output.passwordChanged);
                    }
                }
            }
        }
    }
    public User findUserByUsername(String username){
        for(User user : users)
            if(user.getUsername().equals(username))
                return user;
        return null;
    }
    public void addGame(String game){
        gamesHistory.add(game);
    }
    public ArrayList<String> getGamesHistory(){
        return gamesHistory;
    }
    protected Matcher getCommandMatcher(String input, String regex){
        Pattern pattern=Pattern.compile(regex);
        return pattern.matcher(input);
    }
    protected boolean isUsernameCorrect(String username){
        for(int i = 0; i < username.length(); i++){
            if(!((username.charAt(i) >= 48 && username.charAt(i) <= 57)
                    || (username.charAt(i) >= 65 && username.charAt(i) <= 90)
                    || (username.charAt(i) >= 97 && username.charAt(i) <= 122)
                    || username.charAt(i) == 95))
                return false;
        }
        return true;
    }
    protected boolean isUsernameNew(String username){
        for(User user : users){
            if(user.getUsername().equals(username))
                return false;
        }
        return true;
    }
    protected boolean isEmailCorrect(String email){
        if(email.endsWith("@gmail.com") || email.endsWith("@email.com") || email.endsWith("@yahoo.com"))
            return true;
        return false;
    }
    protected boolean doesPasswordContainUpperCase(String password){
        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >= 65 && password.charAt(i) <= 90)
                return true;
        }
        return false;
    }
    protected boolean doesPasswordContainsLowerCase(String password){
        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >=97 && password.charAt(i) <= 122)
                return true;
        }
        return false;
    }
    protected boolean doesPasswordContainNumber(String password){
        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >= 48 && password.charAt(i) <= 57)
                return true;
        }
        return false;
    }
    protected boolean passwordContainsCharacter(String password){
        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >= 33 && password.charAt(i) <= 38)
                return true;
        }
        return false;
    }
    public void setUsers(ArrayList<User> users){
        this.users = users;
    }
    public ArrayList<User> getUsers(){
        return users;
    }
    public void sortUsers(ArrayList<User> users){
        Comparator<User> userComparator = Comparator.comparing(User::getScore);
        Collections.sort(users, userComparator);
    }
}
