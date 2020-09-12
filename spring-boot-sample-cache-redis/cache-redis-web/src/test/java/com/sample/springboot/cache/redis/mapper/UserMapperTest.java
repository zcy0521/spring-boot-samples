package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperTest {

    private int deptNum = 3;

    private int subDeptNum = 5;

    private Set<Long> deptIds = new HashSet<>(deptNum*subDeptNum + 1);

    private int userNum = 20;

    private Set<Long> userIds = new HashSet<>(userNum);

    private int roleNum = 5;

    private Set<Long> roleIds = new HashSet<>(roleNum);

    private int orderNum = 35;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    @Rollback(false)
    public void testInsert() {
        // Dept
        insertDept();
        // User
        insertUser();
        // Order
        insertOrder();
        // Role
        insertRole();
        // UserRole
        insertUserRole();
    }

    @Test
    @Rollback(false)
    public void testDelete() {
        // Dept
        int deptNum = deptMapper.deleteAll();
        assertThat(deptNum, anyOf(is(0), is(1 + this.deptNum + (this.deptNum*this.subDeptNum))));
        log.warn("删除部门记录: {}条", deptNum);

        // User
        int userNum = userMapper.deleteAll();
        assertThat(userNum, anyOf(is(0), is(this.userNum)));
        log.warn("删除用户记录: {}条", userNum);

        // Order
        int orderNum = orderMapper.deleteAll();
        assertThat(orderNum, anyOf(is(0), is(this.orderNum)));
        log.warn("删除订单记录: {}条", orderNum);

        // Role
        int roleNum = roleMapper.deleteAll();
        assertThat(roleNum, anyOf(is(0), is(this.roleNum)));
        log.warn("删除角色记录: {}条", roleNum);

        // UserRole
        int userRoleNum = userRoleMapper.deleteAll();
        log.warn("删除用户角色关系记录: {}条", userRoleNum);
    }

    /**
     * 创建部门
     */
    private void insertDept() {
        // 部门名称
        String topName = "总公司";
        // 部门pid
        Long topPid = 0L;
        // 部门对象
        DeptDO topDept = new DeptDO();
        topDept.setPid(topPid);
        topDept.setDeptName(topName);
        topDept.setGmtCreate(new Date());
        deptMapper.insertSelective(topDept);
        deptIds.add(topDept.getId());

        for (int i = 0; i < deptNum; i++) {
            // 部门名称
            String deptName = "分公司" + (i + 1);
            // 部门pid
            Long topId = topDept.getId();
            // 部门对象
            DeptDO dept = new DeptDO();
            dept.setPid(topId);
            dept.setDeptName(deptName);
            dept.setGmtCreate(new Date());
            deptMapper.insertSelective(dept);
            deptIds.add(dept.getId());

            for (int j = 0; j < subDeptNum; j++) {
                // 部门名称
                String subDeptName = deptName + "部门" + (j + 1);
                // 部门pid
                Long pid = dept.getId();
                // 部门对象
                DeptDO subDept = new DeptDO();
                subDept.setPid(pid);
                subDept.setDeptName(subDeptName);
                subDept.setGmtCreate(new Date());
                deptMapper.insertSelective(subDept);
                deptIds.add(subDept.getId());
            }
        }
    }

    /**
     * 创建用户
     */
    private void insertUser() {
        for (int i = 0; i < userNum; i++) {
            // 用户名
            String userName = "用户" + (i + 1);
            // 所在部门id
            Long deptId = getRandomId(deptIds);
            // 用户对象
            UserDO user = new UserDO();
            user.setUserName(userName);
            user.setDeptId(deptId);
            user.setGmtCreate(new Date());
            userMapper.insertSelective(user);
            userIds.add(user.getId());
        }
    }

    /**
     * 创建角色
     */
    private void insertRole() {
        for (int i = 0; i < roleNum; i++) {
            // 角色名
            String roleName = "角色" + (i + 1);
            // 角色对象
            RoleDO role = new RoleDO();
            role.setRoleName(roleName);
            role.setGmtCreate(new Date());
            roleMapper.insertSelective(role);
            roleIds.add(role.getId());
        }
    }
    private void insertUserRole() {
        for(Long userId : userIds){
            // 当前用户分配的role个数
            int roleNum = RandomUtils.nextInt(1, this.roleNum + 1);
            // 当前用户分配的roleIds
            Set<Long> roleIds = getRandomIds(this.roleIds, roleNum);
            for(Long roleId : roleIds) {
                // 用户角色关系对象
                UserRoleDO userRole = new UserRoleDO();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setGmtCreate(new Date());
                userRoleMapper.insertSelective(userRole);
            }
        }
    }

    /**
     * 创建订单
     */
    private void insertOrder() {
        for (int i = 0; i < orderNum; i++) {
            // 所属用户id
            Long userId = getRandomId(userIds);
            // 订单名
            String subject = "订单" + (i + 1);
            // 订单金额 [0.01, 10000)
            double amount = RandomUtils.nextDouble(0.01d, 10000d);
            // 订单对象
            OrderDO order = new OrderDO();
            order.setUserId(userId);
            order.setSubject(subject);
            order.setTotalAmount(new BigDecimal(amount));
            order.setGmtCreate(new Date());
            orderMapper.insertSelective(order);
        }
    }

    /**
     * 随机获取一个id
     */
    private static Long getRandomId(Set<Long> ids) {
        Long[] idArray = ids.toArray(new Long[0]);
        int index = RandomUtils.nextInt(0, ids.size());
        return idArray[index];
    }

    /**
     * 随机获取n个id
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
