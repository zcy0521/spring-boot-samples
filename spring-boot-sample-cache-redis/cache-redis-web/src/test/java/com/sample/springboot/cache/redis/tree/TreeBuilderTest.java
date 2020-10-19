package com.sample.springboot.cache.redis.tree;

import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.mapper.DeptMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeBuilderTest {

    @Autowired
    private DeptMapper deptMapper;

    @Test
    public void testBuildTree() {
        List<DeptDO> deptList = deptMapper.selectTree();

        TreeNode<DeptDO> deptTree = TreeBuilder.<DeptDO, Long>builder()
                .elements(deptList)
                .id(DeptDO::getId)
                .parentId(DeptDO::getParentId)
                .build();

        assertThat(deptTree, notNullValue());
    }

}
