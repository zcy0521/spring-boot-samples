package com.sample.springboot.rest.server.controller;

import com.sample.springboot.rest.server.domain.SampleDO;
import com.sample.springboot.rest.server.model.SampleVO;
import com.sample.springboot.rest.server.orika.SampleVOMapper;
import com.sample.springboot.rest.server.page.Page;
import com.sample.springboot.rest.server.query.SampleQuery;
import com.sample.springboot.rest.server.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/samples")
public class SampleRestController {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleVOMapper sampleMapper;

    /**
     * 分页查询列表
     *
     * @param queryVO 查询条件
     */
    @GetMapping
    public ResponseEntity<List<SampleVO>> list(@RequestParam("query") SampleVO.QueryVO queryVO) {
        // 查询列表
        SampleQuery query = sampleMapper.toQuery(queryVO);
        List<SampleDO> samples = sampleService.findAll(query, queryVO.getNumber(), queryVO.getSize());
        List<SampleVO> sampleVOs = sampleMapper.fromSamples(samples);
        // 分页信息 service中返回非原始请求中的page信息
        Page page = query.getPage();
        return ResponseEntity.ok(sampleVOs);
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<SampleVO> get(@PathVariable Long id) {
        SampleDO sample = sampleService.findById(id);
        SampleVO sampleVO = sampleMapper.fromSample(sample);
        return ResponseEntity.ok(sampleVO);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody SampleVO sampleVO) {
        SampleDO sample = sampleMapper.toSample(sampleVO);
        Long id = sampleService.insert(sample);
        boolean success = null != id && id > 0;
        return success ?
                ResponseEntity.ok(id) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 更新
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody SampleVO sampleVO) {
        // id校验
        if (id < 1) {
            return ResponseEntity.badRequest().body("id有误");
        }

        // 更新
        SampleDO sample = sampleMapper.toSample(sampleVO);
        sample.setId(id);
        boolean success = sampleService.update(sample);
        return success ?
                ResponseEntity.ok("更新成功") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        // id校验
        if (id < 1) {
            return ResponseEntity.badRequest().body("id有误");
        }

        // 删除
        boolean success = sampleService.deleteById(id);
        return success ?
                ResponseEntity.ok("删除成功") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "/delete")
    public ResponseEntity<String> deleteByIds(@RequestBody Long[] ids) {
        // ids校验
        if (ids.length < 1) {
            return ResponseEntity.badRequest().body("ids有误");
        }

        // 批量删除
        int size = sampleService.deleteByIds(Arrays.stream(ids).collect(Collectors.toSet()));
        boolean success = size == ids.length;
        return success ?
                ResponseEntity.ok(String.format("成功删除%s条记录", size)) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }

}
