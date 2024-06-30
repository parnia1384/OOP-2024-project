import java.util.Scanner;
import java.util.regex.Matcher;
public class ProfileMenu extends RegistryMenu{
    public void myProfile(Scanner scanner, User loggedInUser){
        String command;
        Matcher matcher;
        String changeUsername = "Profile change -u (\\S+)";
        String changeNickname = "Profile change -n (\\S+)";
        String changeEmail = "Profile change -e (\\S+)";
        String changePassword = "Profile change password -o (\\S+) -n (\\S+)";
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
            else if(command.equals("Show my deck")){
                loggedInUser.showDeck();
            }
            else if(command.equals("Show my history")){
                showMyHistory(scanner, loggedInUser);
            }
            else if(command.equals("command prompt")) System.out.println(output.profileMenuManual);
            else System.out.println("invalid input!");
        }
    }
    private void showProfile(User user){
        System.out.println("Username: " + user.getUsername() + "\nPassword: " + user.getPassword() + "\nEmail: " + user.getEmail() + "\nNickname: " + user.getNickname()
        + "\nLevel: " + user.getLevel() + "\nExp: " + user.getExp() + "\nHp: " + user.getHp() + "\nCoin: " + user.getCoin() + "\nScore: " + user.getScore());
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
    public void showMyHistory(Scanner scanner, User user){
        if(user.getGames().isEmpty()) System.out.println("You haven't played yet. Returned to profile menu successfully.");
        else{
            System.out.println("sort by:\n1) date and time\n2) status(win/lose)\n3) the second player username\nEnter only the desired \"number\".");
            String num = scanner.nextLine();
            if(num.equals("1")){
                user.sortGamesByDateAndTime();
                String[] games = new String[user.getGames().size()];
                for(int i = 0; i < games.length; i++)
                    games[i] = user.getGames().get(i).toString();
                System.out.println("newest or oldest?");
                String condition = scanner.nextLine();
                if(condition.equals("newest")){
                    String[] upsideDown = new String[games.length];
                    int j = games.length - 1;
                    for(int i = 0; i < games.length; i++){
                        upsideDown[j] = games[i];
                        j -= 1;
                    }
                    if(games.length <= 5){
                        for(int i = 0; i < 5; i++)
                            System.out.println((i + 1) + ". " + upsideDown[i]);
                        System.out.println("You are in page: 1 / 1.\nYour current menu: Profile menu.");
                    }
                    else{
                        for(int i = 0; i < 5; i++)
                            System.out.println((i + 1) + ". " + upsideDown[i]);
                        int counter = 0;
                        int pageNumber = 1;
                        int maximumPages;
                        if(upsideDown.length % 5 == 0) maximumPages = upsideDown.length / 5;
                        else maximumPages = (upsideDown.length / 5 + 1);
                        System.out.println("You are in page 1 / " + maximumPages);
                        System.out.println("If you want to see other games:\n\tnext page\nTo return to the profile menu:\n\tback");
                        while(true) {
                            String command = scanner.nextLine();
                            if (command.equals("back")) {
                                System.out.println("Returned to profile menu successfully");
                                break;
                            }
                            else if (command.equals("next page")) {
                                if (pageNumber == maximumPages)
                                    System.out.println("invalid command!");
                                else {
                                    counter += 1;
                                    pageNumber += 1;
                                    if (((counter + 1) * 5) <= upsideDown.length) {
                                        for (int i = counter * 5; i < (counter + 1) * 5; i++) {
                                            System.out.println((i + 1) + ". " + upsideDown[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                    } else {
                                        for (int i = counter * 5; i < upsideDown.length; i++) {
                                            System.out.println((i + 1) + ". " + upsideDown[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("end.\nIf you want to go to the previous page:\n\tprevious page");
                                    }
                                }
                            }
                            else if (command.equals("previous page")) {
                                if (pageNumber == 1) System.out.println("invalid command!");
                                else {
                                    counter -= 1;
                                    pageNumber -= 1;
                                    for (int i = (counter * 5); i < (counter + 1) * 5; i++) {
                                        System.out.println((i + 1) + ". " + upsideDown[i]);
                                    }
                                    System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                    System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                }
                            }
                        }
                    }
                }
                else if(condition.equals("oldest")){
                    if(games.length <= 5){
                        int i = 1;
                        for(String str : games) {
                            System.out.println(i + ". " + str);
                            i += 1;
                        }
                        System.out.println("You are in page: 1 / 1.\nYour current menu: Profile menu.");
                    }
                    else{
                        for(int i = 0; i < 5; i++)
                            System.out.println((i + 1) + ". " + games[i]);
                        int counter = 0;
                        int pageNumber = 1;
                        int maximumPages;
                        if(games.length % 5 == 0) maximumPages = games.length / 5;
                        else maximumPages = (games.length / 5 + 1);
                        System.out.println("You are in page 1 / " + maximumPages);
                        System.out.println("If you want to see other games:\n\tnext page\nTo return to the profile menu:\n\tback");
                        while(true) {
                            String command = scanner.nextLine();
                            if (command.equals("back")) {
                                System.out.println("Returned to profile menu successfully");
                                break;
                            }
                            else if (command.equals("next page")) {
                                if (pageNumber == maximumPages)
                                    System.out.println("invalid command!");
                                else {
                                    counter += 1;
                                    pageNumber += 1;
                                    if (((counter + 1) * 5) <= games.length) {
                                        for (int i = counter * 5; i < (counter + 1) * 5; i++) {
                                            System.out.println((i + 1) + ". " + games[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                    } else {
                                        for (int i = counter * 5; i < games.length; i++) {
                                            System.out.println((i + 1) + ". " + games[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("end.\nIf you want to go to the previous page:\n\tprevious page");
                                    }
                                }
                            }
                            else if (command.equals("previous page")) {
                                if (pageNumber == 1) System.out.println("invalid command!");
                                else {
                                    counter -= 1;
                                    pageNumber -= 1;
                                    for (int i = (counter * 5); i < (counter + 1) * 5; i++) {
                                        System.out.println((i + 1) + ". " + games[i]);
                                    }
                                    System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                    System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                }
                            }
                        }
                    }
                }
                else System.out.println("invalid order.\nReturned to profile menu successfully.");
            }
            else if(num.equals("2")){
                user.sortGamesBasedOnStatus();
                String[] games = new String[user.getGames().size()];
                for(int i = 0; i < games.length; i++)
                    games[i] = user.getGames().get(i).toString();
                System.out.println("win or lose first?");
                String whichOne = scanner.nextLine();
                if(whichOne.equals("win")){
                    if(games.length <= 5){
                        int i = 1;
                        for(int j = 4; 0 <= j; j--){
                            System.out.println(i + ". " + games[i]);
                            i += 1;
                        }
                        System.out.println("You are in page: 1 / 1.\nYour current menu: Profile menu.");
                    }
                    else{
                        String[] revert = new String[games.length];
                        int j = 0;
                        for(int i = (games.length - 1); 0 <= i; i--){
                            revert[j] = games[i];
                            j += 1;
                        }
                        for(int i = 0; i < 5; i++)
                            System.out.println((i + 1) + ". " + revert[i]);
                        int counter = 0;
                        int pageNumber = 1;
                        int maximumPages;
                        if(games.length % 5 == 0) maximumPages = games.length / 5;
                        else maximumPages = (games.length / 5 + 1);
                        System.out.println("You are in page 1 / " + maximumPages);
                        System.out.println("If you want to see other games:\n\tnext page\nTo return to the profile menu:\n\tback");
                        while(true) {
                            String command = scanner.nextLine();
                            if (command.equals("back")) {
                                System.out.println("Returned to profile menu successfully");
                                break;
                            }
                            else if (command.equals("next page")) {
                                if (pageNumber == maximumPages)
                                    System.out.println("invalid command!");
                                else {
                                    counter += 1;
                                    pageNumber += 1;
                                    if (((counter + 1) * 5) <= revert.length) {
                                        for (int i = counter * 5; i < (counter + 1) * 5; i++) {
                                            System.out.println((i + 1) + ". " + revert[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                    }
                                    else {
                                        for (int i = counter * 5; i < revert.length; i++) {
                                            System.out.println((i + 1) + ". " + revert[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("end.\nIf you want to go to the previous page:\n\tprevious page");
                                    }
                                }
                            }
                            else if (command.equals("previous page")) {
                                if (pageNumber == 1) System.out.println("invalid command!");
                                else {
                                    counter -= 1;
                                    pageNumber -= 1;
                                    for (int i = (counter * 5); i < (counter + 1) * 5; i++) {
                                        System.out.println((i + 1) + ". " + revert[i]);
                                    }
                                    System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                    System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                }
                            }
                        }
                    }
                }
                else if(whichOne.equals("lose")){
                    if(games.length <= 5){
                        int i = 1;
                        for(String str : games){
                            System.out.println(i + ". " + str);
                            i += 1;
                        }
                        System.out.println("You are in page: 1 / 1.\nYour current menu: Profile menu.");
                    }
                    else{
                        for(int i = 0; i < 5; i++)
                            System.out.println((i + 1) + ". " + games[i]);
                        int counter = 0;
                        int pageNumber = 1;
                        int maximumPages;
                        if(games.length % 5 == 0) maximumPages = games.length / 5;
                        else maximumPages = (games.length / 5 + 1);
                        System.out.println("You are in page 1 / " + maximumPages);
                        System.out.println("If you want to see other games:\n\tnext page\nTo return to the profile menu:\n\tback");
                        while(true) {
                            String command = scanner.nextLine();
                            if (command.equals("back")) {
                                System.out.println("Returned to profile menu successfully");
                                break;
                            }
                            else if (command.equals("next page")) {
                                if (pageNumber == maximumPages)
                                    System.out.println("invalid command!");
                                else {
                                    counter += 1;
                                    pageNumber += 1;
                                    if (((counter + 1) * 5) <= games.length) {
                                        for (int i = counter * 5; i < (counter + 1) * 5; i++) {
                                            System.out.println((i + 1) + ". " + games[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                    } else {
                                        for (int i = counter * 5; i < games.length; i++) {
                                            System.out.println((i + 1) + ". " + games[i]);
                                        }
                                        System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                        System.out.println("end.\nIf you want to go to the previous page:\n\tprevious page");
                                    }
                                }
                            }
                            else if (command.equals("previous page")) {
                                if (pageNumber == 1) System.out.println("invalid command!");
                                else {
                                    counter -= 1;
                                    pageNumber -= 1;
                                    for (int i = (counter * 5); i < (counter + 1) * 5; i++) {
                                        System.out.println((i + 1) + ". " + games[i]);
                                    }
                                    System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                    System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                }
                            }
                        }
                    }
                }
                else System.out.println("invalid status! Your current menu: Profile menu");
            }
            else if(num.equals("3")){
                user.sortGamesBasedOnCompetitor();
                String[] games = new String[user.getGames().size()];
                for(int i = 0; i < games.length; i++)
                    games[i] = user.getGames().get(i).toString();
                if(games.length <= 5){
                    int i = 1;
                    for(String str : games) {
                        System.out.println(i + ". " + str);
                        i += 1;
                    }
                    System.out.println("You are in page: 1 / 1.\nYour current menu: Profile menu.");
                }
                else{
                    for(int i = 0; i < 5; i++)
                        System.out.println((i + 1) + ". " + games[i]);
                    int counter = 0;
                    int pageNumber = 1;
                    int maximumPages;
                    if(games.length % 5 == 0) maximumPages = games.length / 5;
                    else maximumPages = (games.length / 5 + 1);
                    System.out.println("You are in page 1 / " + maximumPages);
                    System.out.println("If you want to see other games:\n\tnext page\nTo return to the profile menu:\n\tback");
                    while(true) {
                        String command = scanner.nextLine();
                        if (command.equals("back")) {
                            System.out.println("Returned to profile menu successfully");
                            break;
                        }
                        else if (command.equals("next page")) {
                            if (pageNumber == maximumPages)
                                System.out.println("invalid command!");
                            else {
                                counter += 1;
                                pageNumber += 1;
                                if (((counter + 1) * 5) <= games.length) {
                                    for (int i = counter * 5; i < (counter + 1) * 5; i++) {
                                        System.out.println((i + 1) + ". " + games[i]);
                                    }
                                    System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                    System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                                } else {
                                    for (int i = counter * 5; i < games.length; i++) {
                                        System.out.println((i + 1) + ". " + games[i]);
                                    }
                                    System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                    System.out.println("end.\nIf you want to go to the previous page:\n\tprevious page");
                                }
                            }
                        }
                        else if (command.equals("previous page")) {
                            if (pageNumber == 1) System.out.println("invalid command!");
                            else {
                                counter -= 1;
                                pageNumber -= 1;
                                for (int i = (counter * 5); i < (counter + 1) * 5; i++) {
                                    System.out.println((i + 1) + ". " + games[i]);
                                }
                                System.out.println("You are in page " + pageNumber + " / " + maximumPages);
                                System.out.println("next page or previous?\nIf you want to go to the previous page:\n\tprevious page");
                            }
                        }
                    }
                }
            }
            else System.out.println("invalid input! Returned to profile menu successfully!");
        }
    }
}
