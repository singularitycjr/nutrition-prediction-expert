
import com.alibaba.fastjson.JSON;
import com.tongji.model.dto.NutritionDTO;
import com.tongji.model.dto.SegRecDTO;
import com.tongji.model.vo.GoBankNutritionVO;
import com.tongji.model.vo.GoBankSegRecVO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;


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
