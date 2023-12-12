import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tongji.model.pojo.User;
import com.tongji.user.UserServiceApplication;
import com.tongji.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserServiceApplication.class)
public class aaffda {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void test() {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, 6).set(User::getName, "test");
        this.userMapper.update(null, lambdaUpdateWrapper);
    }
}
