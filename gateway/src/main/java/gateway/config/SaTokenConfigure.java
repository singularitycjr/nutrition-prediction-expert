package gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.tongji.global.enums.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 配置类
 *
 * @author click33
 */
@Configuration
public class SaTokenConfigure {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude("/favicon.ico")

                .addExclude("/user/user/login")
                .addExclude("/user/user/register")
                .addExclude("/user/user/sendCode/**")
                .addExclude("/user/user/sendCodeUpdate/**")
                .addExclude("/user/user/forgetPassword")
                .addExclude("/user/common/uploadMask")

                .addExclude("/doctor/doctor/login")
                .addExclude("/doctor/doctor/register")
                .addExclude("/doctor/doctor/sendCode/**")
                .addExclude("/doctor/doctor/sendCodeUpdate/**")
                .addExclude("/doctor/doctor/forgetPassword")
                .addExclude("/doctor/common/uploadMask")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .notMatch("/user/user/login")
                            .notMatch("/doctor/doctor/login")
                            .check(r -> StpUtil.checkLogin());

//                    进入角色鉴权
                    // 权限/角色认证 -- 不同模块, 校验不同权限/角色
                    SaRouter.match("/food/**", r -> StpUtil.checkRole(RoleEnum.PATIENT.getName()));
//                    SaRouter.match("/doctor/**", r -> StpUtil.checkRole(RoleEnum.DOCTOR.getName()));

                    // 更多匹配 ...  */
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> SaResult.error(e.getMessage()));
    }
}