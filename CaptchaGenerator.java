import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;
public class CaptchaGenerator {
    public String generateRandomStringForPassword(){
        String upperCase = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String lowerCase = "qwertyuiopasdfghjklzxcvbnm";
        String numbers = "0123456789";
        String specialChars = "#!%$";
        String allChars = upperCase + lowerCase + numbers + specialChars;
        Random random = new Random();
        int length = random.nextInt(4) + 8;
        StringBuilder sb = new StringBuilder();
        sb.append(upperCase.charAt(random.nextInt(upperCase.length())));
        sb.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        sb.append(numbers.charAt(random.nextInt(numbers.length())));
        sb.append(specialChars.charAt(random.nextInt(specialChars.length())));
        for(int i = 4; i < length; i++)
            sb.append(allChars.charAt(random.nextInt(allChars.length())));
        String result = sb.toString();
        return new String(result);
    }
    private String generateRandomStringForCaptcha(){
        String upperCase = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String lowerCase = "qwertyuiopasdfghjklzxcvbnm";
        String allChars = upperCase + lowerCase;
        Random random = new Random();
        int length = 5;
        StringBuilder sb = new StringBuilder();
        sb.append(upperCase.charAt(random.nextInt(upperCase.length())));
        sb.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        for(int i = 2; i < length; i++)
            sb.append(allChars.charAt(random.nextInt(allChars.length())));
        String result = sb.toString();
        return new String(result);
    }
    private void asciiArt(String str){
        int width = 100;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("Sanserif", Font.BOLD, 24));
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(str, 10, 20);
        for(int y = 0; y < height; y++){
            StringBuilder stringBuilder = new StringBuilder();
            for(int x = 0; x < width; x++){
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : "*");
            }
            if(stringBuilder.toString().trim().isEmpty())
                continue;
            System.out.println(stringBuilder);
        }
    }
    public void asciiArtCaptcha(Scanner scanner){
        String captcha = generateRandomStringForCaptcha();
        asciiArt(captcha);
        String answer = scanner.nextLine();
        if(!answer.equals(captcha))
            asciiArtCaptcha(scanner);
    }
    public void simpleCaptcha(Scanner scanner){
        Random random = new Random();
        int num1 = random.nextInt(20);
        int num2 = random.nextInt(10);
        int n = random.nextInt();
        int result;
        String function;
        if(n % 3 == 0){
            function = "PLUS";
            result = (num1 + num2);
        }
        else if(n % 3 == 1){
            function = "MINUS";
            result = (num1 - num2);
        }
        else{
            function = "MULTIPLY BY";
            result = (num1 * num2);
        }
        System.out.println(num1 + " " + function + " " + num2);
        String answer1 = scanner.nextLine();
        int answer = Integer.parseInt(answer1);
        if(answer != result)
            simpleCaptcha(scanner);
    }
}
