import com.oizys.study.ArrayList;
import com.oizys.study.LinkedList;
import com.oizys.study.List;
import org.junit.jupiter.api.Test;

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
        List<String> list = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i));
        }
        list.add("-1", 10);
        assertEquals(31, list.size());
        list.remove(15);
        list.remove("18");
        assertEquals(28, list.size());
        assertEquals("16", list.get(16));
        assertEquals("24", list.get(22));

        list.forEach(System.out::println);
    }

}
