package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static String describe(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("=== Reflection: ").append(clazz.getSimpleName()).append(" ===\n");
        sb.append("Full name: ").append(clazz.getName()).append("\n");

        sb.append("\n--- Fields ---\n");
        for (Field f : getAllFields(clazz)) {
            f.setAccessible(true);
            sb.append(Modifier.toString(f.getModifiers())).append(" ")
                    .append(f.getType().getSimpleName()).append(" ")
                    .append(f.getName());
            try {
                Object val = f.get(obj);
                sb.append(" = ").append(val);
            } catch (IllegalAccessException e) {
                sb.append(" (inaccessible)");
            }
            sb.append("\n");
        }

        sb.append("\n--- Public methods ---\n");
        for (Method m : clazz.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;
            sb.append(Modifier.toString(m.getModifiers())).append(" ")
                    .append(m.getReturnType().getSimpleName()).append(" ")
                    .append(m.getName()).append("(...)\n");
        }
        return sb.toString();
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                fields.add(f);
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static void printDescription(Object obj) {
        System.out.println(describe(obj));
    }
}
