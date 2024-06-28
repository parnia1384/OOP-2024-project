import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class GameController {
    final private RegistryMenu registryMenu = new RegistryMenu();
    private MainMenu mainMenu = new MainMenu();
    private User loggedInUser = null;
    final private Outputs output = new Outputs();
    public void run(){
        mainMenu.addSpell();
        Scanner scan = new Scanner(System.in);
        String signup = "user create -u (\\S+) -p (\\S+) (\\S+) -email (\\S+) -n (\\S+)";
        String signupWithRandomPassword = "user create -u (\\S+) -p (\\S+) (\\S+) -email (\\S+) -n (\\S+)";
        String login = "user login -u (\\S+) -p (\\S+)";
        String logout = "log out";
        String forgotPassword = "Forgot my password -u (\\S+)";
        String admin = "login admin (\\S+)";//-login admin <pass>
        Matcher matcher;
        while(true){
            String command = scan.nextLine();
            if(command.equals("Exit")) break;
            else if(command.matches(signup)){
                matcher = getCommandMatcher(command, signup);
                registryMenu.signup(matcher, scan);
            }
            else if (command.matches("main menu")){
                if(loggedInUser == null) System.out.println("Please login first");
                else{
                    System.out.println("entered main menu");
                    mainMenu.run(scan,loggedInUser,registryMenu,output);
                }
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
            else if(command.equals("Show players")) showPlayers();
            else if(command.equals("command prompt")) Manuals();
            else System.out.println("invalid input!");
        }
    }
    private Matcher getCommandMatcher(String input, String regex){
        Pattern pattern=Pattern.compile(regex);
        return pattern.matcher(input);
    }
    public void getInformationFromFile(){
        try{
            File myFile = new File("C:\\Users\\ASUS\\Desktop\\OOP\\Phase1\\file.txt");
            Scanner scan = new Scanner(myFile);
            ArrayList<User> users = new ArrayList<>();
            String[] parts;
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(line.equals("new user:")){
                    String information = scan.nextLine();
                    parts = information.split(" ");
                    User user = new User(parts[0], parts[1], parts[2], parts[3]);
                    user.setNumberOfQuestion(Integer.parseInt(parts[4]));
                    user.setAnswer(parts[5]);
                    user.setCoin(Integer.parseInt(parts[6]));
                    user.setExp(Integer.parseInt(parts[7]));
                    user.setHp(Integer.parseInt(parts[8]));
                    users.add(user);
                }
            }
            scan.close();
            registryMenu.setUsers(users);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void writeInformationInFile(){
        ArrayList<User> users = registryMenu.getUsers();
        try{
            File myFile = new File("C:\\Users\\ASUS\\Desktop\\OOP\\Phase1\\file.txt");
            FileWriter writer = new FileWriter(myFile);
            for(User user : users){
                String str = user.toString();
                writer.write(str);
            }
            writer.close();
        }
        catch (Exception e){
            System.out.println(e);
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
    private void Manuals(){
        String signUp = "user create -u <username> -p <password> <passwordConfirmation> -email <yourEmail> -n <nickname>";
        String chooseQuestion = "question pick -q <numberOfQuestion> -a <answer> -c <answerConfirmation>";
        String changePassword = "Forgot my password -u <username>";
        String login = "user login -u <username> -p <password>";
        String admin = "login admin <adminPassword>";
        String addCardByAdmin = "add card <name> <defenceAttack> <duration> <damage> <upgradeLevel> <upgradeCost> <price>";
        String editCard = "edit card <name> <defenceAttack> <duration> <damage> <upgradeLevel> <upgradeCost> <price>";
        String deleteCard = "delete card <number>";
        System.out.println("To signup: " + signUp + "\nTo select recovery question after registration: " + chooseQuestion + "\nTo change password: " + changePassword
                + "\nTo login: " + login + "\nTo see users: Show players\nTo enter main menu: main menu\n_______________\nIf you are an admin:"
                + admin + "\nTo add card: " + addCardByAdmin + "\nTo edit card: " + editCard + "\nTo delete a card: " + deleteCard + "\nTo logout: back"
                + "\nTo see cards write: show cards\n_______________\nIf you are in the main menu:\nTo enter the shop: shop menu\nTo enter the profile menu: Profile menu\n"
                + "_______________\nIf you are in profile menu:\nTo see your Information: Show profile\nTo change your password: Profile change -o <oldPass> -n <newPass>\nTo change your username: " +
                "Profile change -u <newUsername>\nTo change your nickname: Profile change -n <newNickname>\nTo change your email: Profile change -e <newEmail>\nTo return to the main menu: Exit profile menu"
                + "\n_______________\nIf you are in the shop:\nTo buy a new card: buy card <name>\nTo upgrade a card: upgrade card <name>\nTo return to the main menu: back\n_______________\n");
    }
    private void showPlayers(){
        ArrayList<User> users = registryMenu.getUsers();
        System.out.println("Players without sorting:");
        System.out.println("|------------------------------------------------------------------------------|");
        for(User user : users) {
            System.out.println("| Username: " + user.getUsername() + " | Email: " + user.getEmail() + " | Nickname: " + user.getNickname() + " | Exp: " + user.getExp());
            System.out.println("|------------------------------------------------------------------------------|");
        }
    }
    private void admin(Matcher matcher, Scanner scanner){
        String addCard = "^add\\s+card\\s+(?<name>\\w+)\\s+(?<defenceAttack>\\d+)\\s+(?<duration>\\d+)\\s+(?<damage>\\d+)\\s+(?<upgradeLevel>\\d+)\\s+(?<upgradeCost>\\d+)\\s+(?<price>\\d+)$";
        String editCard = "^edit\\s+card\\s+(?<number>\\d+)\\s+(?<name>\\w+)\\s+(?<defenceAttack>\\d+)\\s+(?<duration>\\d+)\\s+(?<damage>\\d+)\\s+(?<upgradeLevel>\\d+)\\s+(?<upgradeCost>\\d+)\\s+(?<price>\\d+)$";
        String deleteCard = "^delete\\s+card\\s+(?<number>\\d+)$";
        String showCards = "^show\\s+cards$";

        if(!matcher.matches()) System.out.println("invalid input");
        else{
            String password=matcher.group(1);
            if(password.equals("1234")){
                System.out.println("admin logged in successfully!");
                while (true){
                    String command=scanner.nextLine();
                    if(command.equals("back")) {
                        System.out.println("Admin logged out successfully!");
                        break;
                    }
                    Matcher add=getCommandMatcher(command,addCard);
                    Matcher edit=getCommandMatcher(command,editCard);
                    Matcher delete=getCommandMatcher(command,deleteCard);
                    Matcher showCard=getCommandMatcher(command,showCards);
                    if(!add.matches()&&!edit.matches()&&delete.matches()&&!showCard.matches()) System.out.println("invalid request");
                    else {
                        if(showCard.matches())
                            mainMenu.showShopCards();
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
                                    mainMenu.getShopCards().remove(mainMenu.getShopCards().get(Integer.parseInt(delete.group("number"))-1));
                                }
                            }
                        }
                    }
                }
            }
            else System.out.println("invalid password");
        }
    }
}
