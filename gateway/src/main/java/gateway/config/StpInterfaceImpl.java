package gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import com.tongji.global.constrants.Constrants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl   implements StpInterface {

    @Autowired
    private StringRedisTemplate redisService;


    //返回一个账号所拥有的权限集合
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list=new ArrayList<>();
        String role=redisService.opsForValue().get(Constrants.USER_ROLE+ loginId);

//        User user = userService.getOne(Wrappers.<User>lambdaQuery()
//                .eq(User::getId, (Long)loginId)
//        );
//        String role=RoleEnum.find(user.getRole()).getName();

        list.add(role);
        System.out.println(list);
        return list;
    }

}
