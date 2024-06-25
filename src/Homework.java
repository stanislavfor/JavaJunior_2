import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.time.*;
import java.util.Date;
import java.util.Random;



public class Homework {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface RandomDate {

        long min() default 1704067200000L; // 1 января 2024 года UTC0

        long max() default 1735689600000L; // 1 января 2025 года UTC0

        //        String zone() default "Moscow";
        String zone() default "UTC";
    }

    public static class ObjectCreator {

        private static final Random random = new Random();
        public static void createRandomDate(Object object) throws IllegalAccessException {

            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {

                // Поддержка аннотации RandomDate
                if (field.isAnnotationPresent(RandomDate.class)) {

                    // Аннотация над полями типа java.util.Date
                    if (field.getType() == Date.class
                            || field.getType() == Instant.class
                            || field.getType() == LocalDate.class
                            || field.getType() == LocalDateTime.class) {


                        RandomDate randomDate = field.getAnnotation(RandomDate.class);
                        // Проверяем, что min < max
                        if (randomDate.min() >= randomDate.max()) {
                            throw new IllegalArgumentException("min должно быть < max в RandomDate");
                        }
                        // Вставлям UNIX-время в диапазоне [min, max)
                        long randomTime = randomDate.min() + (long) (random.nextDouble() * (randomDate.max() - randomDate.min()));

                        // В поле, помеченной аннотацией, вставляем рандомную дату
                        if (field.getType() == Date.class) {
                            field.set(object, new Date(randomTime));
                        }
                        // Добавляем поддержку для Instant
                        else if (field.getType() == Instant.class) {
                            field.set(object, Instant.ofEpochMilli(randomTime));
                        }
                        // Добавляем поддержку для LocalDate
                        else if (field.getType() == LocalDate.class) {
                        // Добавляем атрибут Zone//
                            field.set(object, Instant.ofEpochMilli(randomTime).atZone(ZoneId.of("UTC")).toLocalDate());
                        }
                        // Добавляем поддержку для LocalDateTime
                        else if (field.getType() == LocalDateTime.class) {
                            // Добавляем атрибут Zone//
                            field.set(object, Instant.ofEpochMilli(randomTime).atZone(ZoneId.of("UTC")).toLocalDateTime());
                        }
                        //    LocalDate.ofInstant(date.toInstant(), ZoneOffset.of("Moscow"));
                    }
                }
            }
        }

        public static class DatesService {
            @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
            private Date date;

            @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
            private Instant instant;

            @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
            private LocalDate localDate;

            @RandomDate(min = 1704067200000L, max = 1735689600000L, zone = "UTC")
            private LocalDateTime localDateTime;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }

            public Instant getInstant() {
                return instant;
            }

            public void setInstant(Instant instant) {
                this.instant = instant;
            }

            public LocalDate getLocalDate() {
                return localDate;
            }

            public void setLocalDate(LocalDate localDate) {
                this.localDate = localDate;
            }

            public LocalDateTime getLocalDateTime() {
                return localDateTime;
            }

            public void setLocalDateTime(LocalDateTime localDateTime) {
                this.localDateTime = localDateTime;
            }

        }

        public static void main(String[] args) {

            long t = 1704067200000L;
            Date date = new Date(t);
            System.out.println(date);

            DatesService service = new DatesService();
            try {
                ObjectCreator.createRandomDate(service);
                System.out.println("Date : " + service.date);
                System.out.println("Instant : " + service.instant);
                System.out.println("Local Date : " + service.localDate);
                System.out.println("local Date Time : " + service.localDateTime);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

//    LocalDate.ofInstant(date.toInstant(), ZoneOffset.of("Moscow"));
        }

    }
}