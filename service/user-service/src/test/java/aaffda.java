import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import org.junit.jupiter.api.Test;

public class aaffda {

    @Test
    public void test() {
        System.out.println("Hello World!");
        String password = "021106";
        String salt = "FB1laK";
        password = SaSecureUtil.md5(password + salt);
        System.out.println(password);
        StpUtil.login(111);
    }
}
