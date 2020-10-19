package com.sample.springboot.cache.redis.runner;

import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Slf4j
@Component
@ConditionalOnProperty(prefix = "init-data", name = "role", havingValue = "true")
public class InitialRoleRunner implements ApplicationRunner {

    private static final int ROLE_NUM = 5;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleService roleMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化角色信息...");

        // 删除现有角色
        roleMapper.deleteAll();

        // 创建角色
        for (int i = 0; i < ROLE_NUM; i++) {
            RoleDO role = new RoleDO();
            role.setRoleName("角色" + (i + 1));
            roleService.insert(role);
        }

        log.info("初始化角色信息完成");
    }

}
