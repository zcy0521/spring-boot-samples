package com.sample.springboot.cache.redis.runner;

import com.google.common.collect.Sets;
import com.ibm.icu.text.NumberFormat;
import com.sample.springboot.cache.redis.domain.*;
import com.sample.springboot.cache.redis.mapper.DeptMapper;
import com.sample.springboot.cache.redis.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Order(1)
@Slf4j
@Component
@ConditionalOnProperty(prefix = "init-data", name = "dept", havingValue = "true")
public class InitialDeptRunner implements ApplicationRunner {

    /**
     * 树深
     */
    private static final int DEPT_DEPTH = 5;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptMapper deptMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化部门信息...");

        // 删除现有部门
        deptMapper.deleteAll();

        // 查询ROOT节点 没有就新创建一个
        DeptDO root = deptMapper.selectTreeRoot();

        // 递归创建节点 第一层 创建2个节点
        insertDept(Sets.newHashSet(root.getId()), 1, 2);

        log.info("初始化部门信息完成");
    }

    /**
     * 创建部门
     * @param parentIds 父节点ID
     * @param level 当前层数
     * @param nodes 当前层创建节点个数
     */
    private void insertDept(Set<Long> parentIds, int level, int nodes) {
        // ICU4J
        Locale chineseNumbers = new Locale("zh_CN@numbers=hans");
        NumberFormat formatter = NumberFormat.getInstance(chineseNumbers);
        String currentLevelName = formatter.format(level) + "级部门";

        // 接受本层创建部门的id
        Set<Long> ids = Sets.newHashSet();
        for (int i = 0; i < nodes; i++) {
            DeptDO dept = new DeptDO();
            dept.setParentId(getRandomId(parentIds));
            dept.setDeptName(currentLevelName + (i + 1));
            dept.setGmtCreate(new Date());
            deptService.insert(dept);
            ids.add(dept.getId());
        }

        // 递归创建下层节点
        // 将本层创建部门 ids 作为下层的 parentIds
        // 层数+1 向下传递
        // 下层创建节点数为 本层节点*10*本层层数 2 20 400 12000 480000
        if (level < DEPT_DEPTH) {
            insertDept(ids, level + 1, nodes * 10 * level);
        }
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

}
