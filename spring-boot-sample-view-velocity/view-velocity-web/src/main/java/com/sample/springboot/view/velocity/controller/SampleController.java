package com.sample.springboot.view.velocity.controller;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.mapstruct.vo.SampleVOMapper;
import com.sample.springboot.view.velocity.query.SampleQuery;
import com.sample.springboot.view.velocity.service.SampleService;
import com.sample.springboot.view.velocity.vo.SampleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "samples")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleVOMapper sampleVOMapper;

    /**
     * 分页查询列表
     * @param query 查询条件
     */
    @GetMapping
    public String list(@ModelAttribute("query") SampleVO.Query query, Model model) {
        // 查询列表
        SampleQuery sampleQuery = sampleVOMapper.toSampleQuery(query);
        List<SampleDO> samples = sampleService.findAll(query.getNumber(), query.getSize(), sampleQuery);
        model.addAttribute("samples", sampleVOMapper.fromSamples(samples));
        // 分页信息 service中返回非原始请求中的page信息
        model.addAttribute("page", sampleQuery.getPage());
        return "sample/sampleList.vm";
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "get/{id}")
    public String get(@PathVariable Long id, Model model) {
        SampleDO sample = sampleService.findById(id);
        model.addAttribute("sample", sampleVOMapper.fromSample(sample));
        return "sample/sampleInfo.vm";
    }

    /**
     * 新增
     */
    @GetMapping(value = "create")
    public String create(@ModelAttribute("query") SampleVO.Query query, Model model) {
        model.addAttribute("action", "samples/create");
        return "sample/sampleForm.vm";
    }

    @PostMapping(value = "create")
    public String create(SampleVO sampleVO, RedirectAttributes redirectAttributes) {
        // 重定向查询请求
        redirectQuery(sampleVO.getQuery(), redirectAttributes);

        // 新增
        SampleDO sample = sampleVOMapper.toSample(sampleVO);
        Long id = sampleService.insert(sample);
        boolean success = null != id && id > 0;
        redirectAttributes.addFlashAttribute("status", success ? "success" : "error");
        redirectAttributes.addFlashAttribute("message", success ? String.format("成功添加记录ID: %s", id) : "添加失败");
        return "redirect:/samples";
    }

    /**
     * 更新
     */
    @GetMapping(value = "update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("query") SampleVO.Query query, Model model) {
        SampleDO sample = sampleService.findById(id);
        model.addAttribute("sample", sampleVOMapper.fromSample(sample));
        model.addAttribute("action", "samples/update/" + id);
        return "sample/sampleForm.vm";
    }

    @PostMapping(value = "update/{id}")
    public String update(@PathVariable Long id, SampleVO sampleVO, RedirectAttributes redirectAttributes) {
        // 重定向查询请求
        redirectQuery(sampleVO.getQuery(), redirectAttributes);

        // id校验
        if (id < 1) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "id有误");
            return "redirect:/samples";
        }

        // 更新
        SampleDO sample = sampleVOMapper.toSample(sampleVO);
        sample.setId(id);
        boolean success = sampleService.update(sample);
        redirectAttributes.addFlashAttribute("status", success ? "success" : "error");
        redirectAttributes.addFlashAttribute("message", success ? "更新成功" : "更新失败");
        return "redirect:/samples";
    }

    /**
     * 删除
     */
    @PostMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        // id校验
        if (id < 1) {
            return ResponseEntity.badRequest().body("id有误");
        }

        // 删除
        boolean success = sampleService.disableById(id);
        return success ?
                ResponseEntity.ok("删除成功") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "delete")
    public ResponseEntity<String> deleteByIds(@RequestParam("ids[]") Long[] ids) {
        // ids校验
        if (ids.length < 1) {
            return ResponseEntity.badRequest().body("ids有误");
        }

        // 批量删除
        int size = sampleService.disableByIds(Arrays.stream(ids).collect(Collectors.toSet()));
        boolean success = size == ids.length;
        return success ?
                ResponseEntity.ok(String.format("成功删除%s条记录", size)) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }


    /**
     * 重定向查询请求
     * @param query 查询条件
     */
    private void redirectQuery(SampleVO.Query query, RedirectAttributes redirectAttributes) {
        // 查询表单对象
        redirectAttributes.addFlashAttribute("query", query);

        // 拼接返回url
        redirectAttributes.addAttribute("number", query.getNumber());
        redirectAttributes.addAttribute("size", query.getSize());
        redirectAttributes.addAttribute("disabled", query.getDisabled());
        redirectAttributes.addAttribute("sampleInteger", query.getSampleInteger());
        redirectAttributes.addAttribute("sampleString", query.getSampleString());
        redirectAttributes.addAttribute("minAmount", query.getMinAmount());
        redirectAttributes.addAttribute("maxAmount", query.getMaxAmount());
        redirectAttributes.addAttribute("minDate", query.getMinDate());
        redirectAttributes.addAttribute("maxDate", query.getMaxDate());
        redirectAttributes.addAttribute("minDateTime", query.getMinDateTime());
        redirectAttributes.addAttribute("maxDateTime", query.getMaxDateTime());
        redirectAttributes.addAttribute("sampleEnums", query.getSampleEnums());
    }

}
