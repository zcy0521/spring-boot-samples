package com.sample.springboot.cache.redis.runner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.enums.Position;
import com.sample.springboot.cache.redis.mapper.*;
import com.sample.springboot.cache.redis.service.DeptService;
import com.sample.springboot.cache.redis.service.RoleService;
import com.sample.springboot.cache.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(3)
@Slf4j
@Component
@ConditionalOnProperty(prefix = "init-data", name = "user", havingValue = "true")
public class InitialUserRunner implements ApplicationRunner {

    private static final int USER_NUM = 150;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化用户信息...");

        // 删除用户
        userService.deleteAll();

        // 第五代子部门
        DeptDO root = deptMapper.selectTreeRoot();
        List<DeptDO> lvl5Dept = deptMapper.selectDescendantsWithLevel(root.getId(), 5);
        Set<Long> lvl5DeptIds = lvl5Dept.stream().map(DeptDO::getId).collect(Collectors.toSet());

        // 创建用户
        Set<Long> userIds = Sets.newHashSet();
        for (int i = 0; i < USER_NUM; i++) {
            UserDO user = new UserDO();
            user.setUserName("用户" + (i + 1));
            user.setDeptId(getRandomId(lvl5DeptIds));
            userMapper.insert(user);
            userIds.add(user.getId());
        }

        // 角色
        List<RoleDO> roles = roleMapper.selectAll();
        Set<Long> roleIds = roles.stream().map(RoleDO::getId).collect(Collectors.toSet());

        // 分配角色
        for(Long userId : userIds){
            Set<Long> userRoleIds = getRandomIds(roleIds, RandomUtils.nextInt(1, roleIds.size()));
            for (Long userRoleId : userRoleIds) {
                roleService.saveUserRole(userId, userRoleId);
            }
        }

        // 第一至三代部门
        List<DeptDO> deptList = Lists.newArrayList();
        List<DeptDO> lvl1Dept = deptMapper.selectDescendantsWithLevel(root.getId(), 1);
        List<DeptDO> lvl2Dept = deptMapper.selectDescendantsWithLevel(root.getId(), 2);
        List<DeptDO> lvl3Dept = deptMapper.selectDescendantsWithLevel(root.getId(), 3);
        deptList.addAll(lvl1Dept);
        deptList.addAll(lvl2Dept);
        deptList.addAll(lvl3Dept);
        Set<Long> deptIds = deptList.stream().map(DeptDO::getId).collect(Collectors.toSet());

        // 分配部门管理员
        for (Long deptId : deptIds) {
            // POSITION_A 1个
            Long positionAUserId = getRandomId(userIds);
            deptService.saveAdmin(deptId, positionAUserId, Position.POSITION_A);

            // POSITION_B 1-2个
            Set<Long> positionBUserIds = getRandomIds(userIds, RandomUtils.nextInt(1, 2));
            for (Long positionBUserId : positionBUserIds) {
                deptService.saveAdmin(deptId, positionBUserId, Position.POSITION_B);
            }

            // POSITION_C 1-3个
            Set<Long> positionCUserIds = getRandomIds(userIds, RandomUtils.nextInt(1, 3));
            for (Long positionCUserId : positionCUserIds) {
                deptService.saveAdmin(deptId, positionCUserId, Position.POSITION_C);
            }
        }

        log.info("初始化用户信息完成");
    }

    /**
     * 随机获取一个id
     * @param ids ID集合
     */
    private static Long getRandomId(Set<Long> ids) {
        Long[] idArray = ids.toArray(new Long[0]);
        int index = RandomUtils.nextInt(0, ids.size());
        return idArray[index];
    }

    /**
     * 随机获取n个id
     * @param ids ID集合
     * @param num 需要获取的ID个数
     */
    private static Set<Long> getRandomIds(Set<Long> ids, int num) {
        Long[] idArray = ids.toArray(new Long[0]);

        Set<Long> randomIds = new HashSet<>(num);
        while (randomIds.size() < num) {
            int index = RandomUtils.nextInt(0, ids.size());
            Long randomId = idArray[index];
            randomIds.add(randomId);
        }
        return randomIds;
    }
}
