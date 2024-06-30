public class Outputs {
    final public String emptyField = "Empty field is not allowed!";
    final public String wrongPasswordRepetition = "Your password confirmation statement is incorrect, please try again.";
    final public String wrongUsernameFormat = "Your username format is incorrect, please try again.";
    final public String duplicateUsername = "Username is used by someone else, please choose another username.";
    final public String wrongEmailFormat = "Your email format is incorrect, please try again.";
    final public String passwordLengthFault = "The length of the password must be greater than 8 and less than 32.";
    final public String passwordDoesNotContainUpperOrLowerCase = "Your password must contain at least one lowercase letter and one uppercase letter.";
    final public String passwordDoesNotContainNumber = "Your password must contain at least a number.";
    final public String passwordDoesNotContainCharacters = "Your password must contain at least a character from ! \" # % & $";
    final public String successfullyAccount = "User created successfully. Please choose a security question :\n1-What is your father’s name ? \n2-What is your favourite color ?\n3-What was the name of your first pet?";
    final public String wrongAnswerRepetition = "Your answer confirmation statement is incorrect, please try again with this format: <answer> <confirmation>";
    final public String usernameDoesNotExist = "Username doesn't exist.";
    final public String wrongPasswordEntered = "Password and Username don’t match!";
    final public String loggedInSuccessfully = "user logged in successfully!";
    final public String passwordChanged = "Password changed successfully!";
    final public String wrongPasswordEnteredForChange = "The password entered is incorrect. Please try again.";
    final public String registryMenuManual = "You are currently in the signup menu\n1) To create a new user:\n\tuser create -u <username> -p <password> <passwordConfirmation> -email <email> -n <nickname>" +
    "\n2) To create a new user with a random password:\n\tuser create -u <username> -p random -email <email> -n <nickname>\n3) To login to an existing account:\n\tuser login -u <username> -p <password>" +
    "\n4) To logout from your account:\n\tlog out\n5) If you are an admin:\n\tlogin admin <password>\n6) If you forgot your password:\n\tForgot my password -u <username>\n7) To enter main menu:\n\tmain menu" +
    "\n8) To see users:\n\tShow players\n_____________________________________________________________________________________";
    final public String mainMenuManual = "1) to start a game:\n\tstart game\n2) To see your deck:\n\tshow my deck\n3) To see games history:\n\tshow history\n"
    + "4) To go to the shop menu:\n\tshop menu\n5) To go to the Profile menu:\n\tProfile menu\n6) To return to the signup menu:\n\tback\n_____________________________________________________________________________________";
    final public String profileMenuManual = "1) To see your profile:\n\tShow profile\n2) To change your username:\n\tProfile change -u <newUsername>\n3) To change your password:\n\tProfile change password -o <oldPass> -n <newPass>"
    + "\n4) To change your email:\n\tProfile change -e <newEmail>\n5) To change your nickname:\n\tProfile change -n <newNickname>\n6) To see your games:\n\tShow my history\n7) To return to the main menu:\n\tExit profile menu\n"
    + "_____________________________________________________________________________________";
    final public String adminManual = "1) To add a card:\n\tadd card <name> <defenceAttack> <duration> <damage> <upgradeLevel> <upgradeCost> <price>\n2) To edit a card:\n"
    + "\tedit card <name> <defenceAttack> <duration> <damage> <upgradeLevel> <upgradeCost> <price>\n3) To delete a card:\n\tdelete card <number>\nTo logout:\n\tback\n_____________________________________________________________________________________";
    final public String shopMenuManual = "1) To buy a card:\n\tbuy card <name>\n2) To upgrade a card:\n\tupgrade a card <name>\n3) To return to the main menu:\n\tback\n_____________________________________________________________________________________";
    final public String gameManual = "1) To bring a guest:\n\tuser login -u <username> -p <password>\n2) To use a Damage_Heal card:\n\t place card <name> in block <number>\n3) To use a Spell card:\n\tdeploy card <name>\n_____________________________________________________________________________________";
}
