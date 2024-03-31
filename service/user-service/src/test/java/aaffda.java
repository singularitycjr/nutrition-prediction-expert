
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
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ericwvi.site/bgmp/api/diet?Action=SegRec";

        // 为restTemplate添加请求头
        /* 请求头 */
        HttpHeaders header = new HttpHeaders();

        header.add("x-api-key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Nn0.0C-pklGl8S9T6aiJgxZAudKW3x2gbisxKUaxiRj2WbA");

        NutritionDTO nutritionDTO = new NutritionDTO();
        nutritionDTO.setDietId(1);
        NutritionDTO.Food food = new NutritionDTO.Food();
        food.setFood(1);
        food.setRegion("cn");
        food.setName("apple");
        nutritionDTO.setFoods(List.of(food));

        HttpEntity<NutritionDTO> httpEntity = new HttpEntity<>(nutritionDTO, header);

        String res = restTemplate.postForEntity(url, httpEntity, String.class).getBody();
        System.out.println(res);
        GoBankNutritionVO goBankSegRecVO = JSON.parseObject(res, GoBankNutritionVO.class);
        assert goBankSegRecVO != null;
        System.out.println(goBankSegRecVO.getMessage().getResults());
    }
}
