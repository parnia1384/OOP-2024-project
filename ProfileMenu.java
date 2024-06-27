import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ProfileMenu extends RegistryMenu{
    public void myProfile(Scanner scanner, User loggedInUser){
        String command;
        Matcher matcher;
        String changeUsername = "Profile change -u (\\S+)";
        String changeNickname = "Profile change -n (\\S+)";
        String changeEmail = "profile change -e (\\S+)";
        String changePassword = "profile change password -o (\\S+) -n (\\S+)";
        while(true){
            command = scanner.nextLine();
            if(command.equals("Exit profile menu")){
                System.out.println("returned to main menu successfully.");
                break;
            }
            else if(command.equals("Show profile")) showProfile(loggedInUser);
            else if(command.matches(changeUsername)){
                matcher = getCommandMatcher(command, changeUsername);
                changeUsername(matcher, loggedInUser);
            }
            else if(command.matches(changeNickname)){
                matcher = getCommandMatcher(command, changeNickname);
                changeNickname(matcher, loggedInUser);
            }
            else if(command.matches(changeEmail)){
                matcher = getCommandMatcher(command, changeEmail);
                changeEmail(matcher, loggedInUser);
            }
            else if(command.matches(changePassword)){
                matcher = getCommandMatcher(command, changePassword);
                changePassword(matcher, loggedInUser);
            }
            else System.out.println("invalid input!");
        }
    }
    private Matcher getCommandMatcher(String input, String regex){
        Pattern pattern=Pattern.compile(regex);
        return pattern.matcher(input);
    }
    private void showProfile(User user){
        System.out.println("Username: " + user.getUsername() + "\nPassword: " + user.getPassword() + "\nEmail: " + user.getEmail() + "\nNickname: " + user.getNickname()
        + "\nLevel: " + user.getLevel() + "\nExp: " + user.getExp() + "\nHp: " + user.getHp());
    }
    private void changeUsername(Matcher matcher, User user){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String newUsername = matcher.group(1);
            if(!isUsernameCorrect(newUsername)) System.out.println(output.wrongUsernameFormat);
            else if(!isUsernameNew(newUsername)) System.out.println(output.duplicateUsername);
            else user.changeUsername(newUsername);
        }
    }
    private void changeNickname(Matcher matcher, User user){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String newNickname = matcher.group(1);
            if(!isUsernameCorrect(newNickname)) System.out.println("Please try again.");
            else user.changeNickname(newNickname);
        }
    }
    private void changeEmail(Matcher matcher, User user){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String newEmail = matcher.group(1);
            if(!isEmailCorrect(newEmail)) System.out.println(output.wrongEmailFormat);
            else user.changeEmail(newEmail);
        }
    }
    private void changePassword(Matcher matcher, User user){
        if(!matcher.matches()) System.out.println("invalid input!");
        else{
            String oldPass = matcher.group(1);
            String newPass = matcher.group(2);
            if(!user.getPassword().equals(oldPass)) System.out.println(output.wrongPasswordEnteredForChange);
            else if(newPass.length() < 8 || newPass.length() > 32) System.out.println(output.passwordLengthFault);
            else if(!doesPasswordContainsLowerCase(newPass) || !doesPasswordContainUpperCase(newPass))
                System.out.println(output.passwordDoesNotContainUpperOrLowerCase);
            else if(!doesPasswordContainNumber(newPass)) System.out.println(output.passwordDoesNotContainNumber);
            else if(!passwordContainsCharacter(newPass)) System.out.println(output.passwordDoesNotContainCharacters);
            else{
                user.changePassword(newPass);
                System.out.println(output.passwordChanged);
            }
        }
    }
}
