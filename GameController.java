import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class GameController {
    private RegistryMenu registryMenu = new RegistryMenu();
    private MainMenu mainMenu= new MainMenu();
    private User loggedInUser = null;
    private User competitor = null;
    final private Outputs output = new Outputs();
    public void run(){
        mainMenu.addSpell();
        Scanner scan = new Scanner(System.in);
        String signup = "user create -u (\\S+) -p (\\S+) (\\S+) -email (\\S+) -n (\\S+)";
        String chooseSecurityQuestion = "question pick -q (\\S+) -a (\\S+) -c (\\S+)";
        String login = "user login -u (\\S+) -p (\\S+)";
        String logout = "log out";
        String forgotPassword = "Forgot my password -u (\\S+)";
        String admin = "login admin (\\S+)";//-login admin <pass>
        Matcher matcher;

        while(true){
            String command = scan.nextLine();
            if(command.equals("Exit")) break;
            else if (command.matches("main menu")){
                System.out.println("entered main menu");
                mainMenu.run(scan,loggedInUser);
            }
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
            else if(command.matches(admin)){
                matcher=getCommandMatcher(command, admin);
                admin(matcher, scan);
            }
            else System.out.println("invalid input!");
        }
    }
    private Matcher getCommandMatcher(String input, String regex){
        Pattern pattern=Pattern.compile(regex);
        return pattern.matcher(input);
    }
    private void admin(Matcher matcher, Scanner scanner){
        String addCard="^add\\s+card\\s+(?<name>\\w+)\\s+(?<defenceAttack>\\d+)\\s+(?<duration>\\d+)\\s+(?<damage>\\d+)\\s+(?<upgradeLevel>\\d+)\\s+(?<upgradeCost>\\d+)\\s+(?<price>\\d+)$";
        String editCard="^edit\\s+card\\s+(?<number>\\d+)\\s+(?<name>\\w+)\\s+(?<defenceAttack>\\d+)\\s+(?<duration>\\d+)\\s+(?<damage>\\d+)\\s+(?<upgradeLevel>\\d+)\\s+(?<upgradeCost>\\d+)\\s+(?<price>\\d+)$";
        String deleteCard="^delete\\s+card\\s+(?<number>\\d+)$";
        String showCards="^show\\s+cards$";

        if(!matcher.matches()) System.out.println("invalid input");
        else{
            String password=matcher.group(1);
            if(password.equals("1234")){
                System.out.println("admin logged in successfully!");
                while (true){
                    String command=scanner.nextLine();
                    if(command.equals("back")) break;
                    Matcher add=getCommandMatcher(command,addCard);
                    Matcher edit=getCommandMatcher(command,editCard);
                    Matcher delete=getCommandMatcher(command,deleteCard);
                    Matcher showCard=getCommandMatcher(command,showCards);
                    if(!add.matches()&&!edit.matches()&&delete.matches()&&!showCard.matches()) System.out.println("invalid request");
                    else {
                        if(showCard.matches())
                            mainMenu.getShopCards();
                        else if(add.matches()){
                            if(mainMenu.getCardByName(add.group("name"))!=null)
                                System.out.println("card already exists");
                            else if(Integer.parseInt(add.group("defenceAttack"))<10||Integer.parseInt(add.group("defenceAttack"))>100)
                                System.out.println("invalid defence_attack");
                            else if(Integer.parseInt(add.group("duration"))<1||Integer.parseInt(add.group("duration"))>5)
                                System.out.println("invalid duration");
                            else if(Integer.parseInt(add.group("damage"))<10||Integer.parseInt(add.group("damage"))>50)
                                System.out.println("invalid damage");
                            else {
                                Damage_Heal newCard=new Damage_Heal(add.group("name"),Integer.parseInt(add.group("defenceAttack")),Integer.parseInt(add.group("duration")),Integer.parseInt(add.group("damage")),Integer.parseInt(add.group("upgradeLevel")),Integer.parseInt(add.group("upgradeCost")),Integer.parseInt(add.group("price")));
                                mainMenu.addDamage_Heal(newCard);
                                System.out.println("card added successfully");
                            }
                        }
                        else if(edit.matches()){
                            if(Integer.parseInt(edit.group("number"))>mainMenu.getShopCards().size()) System.out.println("invalid number");
                            else if(Integer.parseInt(edit.group("defenceAttack"))<10||Integer.parseInt(edit.group("defenceAttack"))>100)
                                System.out.println("invalid defence_attack");
                            else if(Integer.parseInt(edit.group("duration"))<1||Integer.parseInt(edit.group("duration"))>5)
                                System.out.println("invalid duration");
                            else if(Integer.parseInt(edit.group("damage"))<10||Integer.parseInt(edit.group("damage"))>50)
                                System.out.println("invalid damage");
                            else{
                                System.out.println("are you sure you want to edit this card? y/n");
                                String answer=scanner.nextLine();
                                if(answer.equalsIgnoreCase("y"))
                                {
                                    System.out.println("card edited successfully");
                                    mainMenu.getCardByName(edit.group("name")).edit(Integer.parseInt(edit.group("defenceAttack")),Integer.parseInt(edit.group("duration")),Integer.parseInt(edit.group("damage")),Integer.parseInt(edit.group("upgradeLevel")),Integer.parseInt(edit.group("upgradeCost")),Integer.parseInt(add.group("price")));
                                }
                            }
                        }
                        else if(delete.matches()){
                            if(Integer.parseInt(edit.group("number"))>mainMenu.getShopCards().size()) System.out.println("invalid number");
                            else{
                                System.out.println("are you sure you want to delete this card? y/n");
                                String answer=scanner.nextLine();
                                if(answer.equalsIgnoreCase("y"))
                                {
                                    System.out.println("card deleted successfully");
                                    mainMenu.getShopCards().remove(mainMenu.getShopCards().get(Integer.parseInt(delete.group("number"))));
                                }
                            }
                        }
                    }
                }
            }
            else
                System.out.println("invalid password");
        }
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
