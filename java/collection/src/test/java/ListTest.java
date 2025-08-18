import com.oizys.study.list.ArrayList;
import com.oizys.study.list.LinkedList;
import com.oizys.study.list.List;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wyn
 * Created on 2025/8/6
 */
class ListTest {

    @Test
    void ArrayListTest() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i));
        }
        assertEquals(30, list.size());
        list.remove(15);
        list.remove("18");
        assertEquals(28, list.size());
        assertEquals("16", list.get(15));
        assertEquals("24", list.get(22));

        list.forEach(System.out::println);
    }

    @Test
    void LinkedListTest() {
        LinkedList<String> list = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i));
        }
        list.add("-1", 10);
        assertEquals(31, list.size());
        list.remove(15);
        list.remove("18");
        System.out.println(Arrays.toString(list.toArray()));
        assertEquals(29, list.size());
        assertEquals("16", list.get(16));
        assertEquals("23", list.get(22));

        list.forEach(System.out::println);
    }

}
