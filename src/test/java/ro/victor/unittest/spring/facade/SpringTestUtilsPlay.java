package ro.victor.unittest.spring.facade;

import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SpringTestUtilsPlay {
    private static final Integer V = 828;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field f = SpringTestUtilsPlay.class.getDeclaredField("V");
        f.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        f.set(null, 14);

//        ReflectionTestUtils.setField(SpringTestUtilsPlay.class, "V", 15);
        System.out.println(V);
    }
}
