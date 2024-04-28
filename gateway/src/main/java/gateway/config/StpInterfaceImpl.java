package gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.tongji.global.helper.LoginObj;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl   implements StpInterface {



    //返回一个账号所拥有的权限集合
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object obj, String loginType) {
        List<String> list=new ArrayList<>();
        LoginObj loginObj= JSON.parseObject((String) StpUtil.getLoginId(), LoginObj.class);
        list.add(loginObj.getRole());
        return list;
    }

}
