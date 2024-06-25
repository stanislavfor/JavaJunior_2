import java.util.Date;

public class Main {
    public static void main(String[] args) {

        System.out.println("Homework result :\n");

        Homework.ObjectCreator.DatesService service = new Homework.ObjectCreator.DatesService();
        try {
            Homework.ObjectCreator.createRandomDate(service);
            System.out.println("Date : " + service.getDate());
            System.out.println("Instant : " + service.getInstant());
            System.out.println("Local Date : " + service.getLocalDate());
            System.out.println("local Date Time : " + service.getLocalDateTime());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}