import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class GameController {
    private RegistryMenu registryMenu = new RegistryMenu();
    private User loggedInUser = null;
    private User competitor = null;
    final private Outputs output = new Outputs();
    public void run(){
        Scanner scan = new Scanner(System.in);
        String signup = "user create -u (\\S+) -p (\\S+) (\\S+) -email (\\S+) -n (\\S+)";
        String chooseSecurityQuestion = "question pick -q (\\S+) -a (\\S+) -c (\\S+)";
        String login = "user login -u (\\S+) -p (\\S+)";
        String logout = "log out";
        String forgotPassword = "Forgot my password -u (\\S+)";
        Matcher matcher;
        while(true){
            String command = scan.nextLine();
            if(command.equals("Exit")) break;
            else if(command.matches(signup)){
                matcher = getCommandMatcher(command, signup);
                registryMenu.signup(matcher);
            }
            else if(command.matches(chooseSecurityQuestion)){
                matcher = getCommandMatcher(command, chooseSecurityQuestion);
                registryMenu.chooseSecurityQuestion(matcher, scan);
            }
            else if(command.matches(login)){
                matcher = getCommandMatcher(command, login);
                login(matcher);
            }
            else if(command.matches(logout)){
                loggedInUser = null;
                System.out.println("logged out successfully!");
            }
            else if(command.matches(forgotPassword)){
                matcher = getCommandMatcher(command, forgotPassword);
                registryMenu.forgotPassword(matcher, scan);
            }

            else System.out.println("invalid input!");
        }
    }
    private Matcher getCommandMatcher(String input, String regex){
        Pattern pattern=Pattern.compile(regex);
        return pattern.matcher(input);
    }
    private void login(Matcher matcher){
        if(!matcher.matches() || loggedInUser != null) System.out.println("invalid input!");
        else{
            String username = matcher.group(1);
            String password = matcher.group(2);
            User user = registryMenu.findUserByUsername(username);
            if(user == null) System.out.println(output.usernameDoesNotExist);
            else if(!user.getPassword().equals(password)) System.out.println(output.wrongPasswordEntered);
            else{
                loggedInUser = user;
                System.out.println(output.loggedInSuccessfully);
            }
        }
    }
}
