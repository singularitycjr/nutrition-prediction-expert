
import org.junit.jupiter.api.Test;


//@SpringBootTest(classes = UserServiceApplication.class)
public class aaffda {

    //@Autowired
    //private UserMapper userMapper;
    @Test
    public void test() {

        String str = "HelloWorld123dsfdfs";
        str = str.replaceAll("[a-zA-Z]+$", "");
        System.out.println("去掉末尾字母后的字符串：" + str);
    }
}
