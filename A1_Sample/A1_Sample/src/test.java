import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class test {
    //测试
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        String a = "\"2,002\"";
        a = a.replace(",", "");
        System.out.println(a);
    }
}
//In 1938, after his father Professor Henry Jones, Sr. goes missing while pursuing the Holy Grail, Professor Henry""Indiana"" Jones, Jr. finds himself up against Adolf Hitler's Nazis again to stop them from obtaining its powers.
//In a futuristic city sharply divided between the working class and the city planners, the son of the city's mastermind falls in love with a working-class prophet who predicts the coming of a savior to mediate their differences.
//In 1938, after his father Professor Henry Jones, Sr. goes missing while pursuing the Holy Grail, Professor Henry ""Indiana"" Jones, Jr. finds himself up against Adolf Hitler's Nazis again to stop them from obtaining its powers.
//In a futuristic city sharply divided between the working class and the city planners, the son of the city's mastermind falls in love with a working-class prophet who predicts the coming of a savior to mediate their differences.