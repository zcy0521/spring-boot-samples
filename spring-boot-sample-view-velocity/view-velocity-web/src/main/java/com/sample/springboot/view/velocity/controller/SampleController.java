package com.sample.springboot.view.velocity.controller;

import com.google.common.collect.Lists;
import com.sample.springboot.view.velocity.domain.FileDO;
import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.enums.FileType;
import com.sample.springboot.view.velocity.model.SampleVO;
import com.sample.springboot.view.velocity.orika.SampleVOMapper;
import com.sample.springboot.view.velocity.page.Page;
import com.sample.springboot.view.velocity.query.SampleQuery;
import com.sample.springboot.view.velocity.service.SampleService;
import com.sample.springboot.view.velocity.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "samples")
public class SampleController {

    private static final String LOCATION = "sample";

    @Autowired
    private SampleService sampleService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SampleVOMapper sampleMapper;

    /**
     * 分页查询列表
     *
     * @param queryVO 查询条件
     */
    @GetMapping
    public String list(@ModelAttribute("query") SampleVO.QueryVO queryVO, Model model) {
        // 查询列表
        SampleQuery query = sampleMapper.toQuery(queryVO);
        // 查询结果
        List<SampleDO> samples = sampleService.findAll(query);
        List<SampleVO> sampleVOs = sampleMapper.fromSamples(samples);
        // 分页信息
        Page page = query.getPage();

        model.addAttribute("samples", sampleVOs);
        model.addAttribute("page", page);
        return "sample/sampleList.vm";
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "get/{id}")
    public String get(@PathVariable Long id, Model model) {
        SampleDO sample = sampleService.findById(id);
        SampleVO sampleVO = sampleMapper.fromSample(sample);

        // 附件
        List<FileDO> files = sampleService.findAllFiles(id);
        files.forEach(imageFile -> {
            String fileName = imageFile.getFileName();
            Resource resource = storageService.loadAsResource(fileName, LOCATION, String.valueOf(id));
//            resource.
        });

        // 图像
//        Map<Long, String> imageFiles = files.stream()
//                .filter(file -> file.getFileType().equals(FileType.IMAGE))
//                .map()
//                .collect(Collectors.toList());
//
//        // 音频
//        List<FileDO> audioFiles = files.stream()
//                .filter(file -> file.getFileType().equals(FileType.AUDIO))
//                .collect(Collectors.toList());
//
//        // 视频
//        List<FileDO> videoFiles = files.stream()
//                .filter(file -> file.getFileType().equals(FileType.VIDEO))
//                .collect(Collectors.toList());
//
//        // 文档
//        List<FileDO> excelFiles = files.stream()
//                .filter(file -> file.getFileType().equals(FileType.EXCEL))
//                .collect(Collectors.toList());

        model.addAttribute("sample", sampleVO);
        return "sample/sampleInfo.vm";
    }

    /**
     * 新增
     */
    @GetMapping(value = "create")
    public String create(@ModelAttribute("query") SampleVO.QueryVO queryVO, Model model) {
        SampleDO sample = new SampleDO();
        SampleVO sampleVO = sampleMapper.fromSample(sample);
        model.addAttribute("sample", sampleVO);
        model.addAttribute("action", "samples/create");
        return "sample/sampleForm.vm";
    }

    @PostMapping(value = "create")
    public String create(SampleVO sampleVO, RedirectAttributes redirectAttributes) {
        // 新增
        SampleDO sample = sampleMapper.toSample(sampleVO);
        Long id = sampleService.insert(sample);

        // 保存文件
        storageFile(id, sampleVO);

        // 返回
        boolean success = null != id && id > 0;
        redirectAttributes.addFlashAttribute("status", success ? "success" : "error");
        redirectAttributes.addFlashAttribute("message", success ? String.format("成功添加记录ID: %s", id) : "添加失败");
        redirectQuery(sampleVO.getQuery(), redirectAttributes);
        return "redirect:/samples";
    }

    /**
     * 更新
     */
    @GetMapping(value = "update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("query") SampleVO.QueryVO queryVO, Model model) {
        SampleDO sample = sampleService.findById(id);
        SampleVO sampleVO = sampleMapper.fromSample(sample);
        model.addAttribute("sample", sampleVO);
        model.addAttribute("action", "samples/update/" + id);
        return "sample/sampleForm.vm";
    }

    @PostMapping(value = "update/{id}")
    public String update(@PathVariable Long id, SampleVO sampleVO, RedirectAttributes redirectAttributes) {
        if (id < 1) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "id有误");
            return "redirect:/samples";
        }

        // 更新
        SampleDO sample = sampleMapper.toSample(sampleVO);
        sample.setId(id);
        boolean success = sampleService.update(sample);

        // 保存文件
        storageFile(id, sampleVO);

        // 返回
        redirectAttributes.addFlashAttribute("status", success ? "success" : "error");
        redirectAttributes.addFlashAttribute("message", success ? "更新成功" : "更新失败");
        redirectQuery(sampleVO.getQuery(), redirectAttributes);
        return "redirect:/samples";
    }

    /**
     * 删除
     */
    @PostMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (id < 1) {
            return ResponseEntity.badRequest().body("id有误");
        }

        // 删除
        boolean success = sampleService.deleteById(id);

        // 返回
        return success ?
                ResponseEntity.ok("删除成功") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "delete")
    public ResponseEntity<String> deleteByIds(@RequestParam("ids[]") Long[] ids) {
        if (ids.length < 1) {
            return ResponseEntity.badRequest().body("ids有误");
        }

        // 批量删除
        int size = sampleService.deleteByIds(Arrays.stream(ids).collect(Collectors.toSet()));

        // 返回
        boolean success = size == ids.length;
        return success ?
                ResponseEntity.ok(String.format("成功删除%s条记录", size)) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }


    private void storageFile(Long sampleId, SampleVO vo) {
        // 图像
        List<MultipartFile> images = vo.getImages().stream()
                .filter(image -> !image.isEmpty())
                .collect(Collectors.toList());
        List<FileDO> imageFiles = Lists.newArrayList();
        images.forEach(image -> {
            // 上传
            String fileName = storageService.store(image, LOCATION, String.valueOf(sampleId));

            FileDO file = new FileDO();
            file.setFileName(fileName);
            file.setSize(image.getSize());
            file.setContentType(image.getContentType());
            file.setFileType(FileType.IMAGE);
            imageFiles.add(file);
        });
        sampleService.saveFiles(sampleId, imageFiles);

        // 音频
        List<MultipartFile> audios = vo.getAudios().stream()
                .filter(image -> !image.isEmpty())
                .collect(Collectors.toList());
        List<FileDO> audioFiles = Lists.newArrayList();
        audios.forEach(audio -> {
            // 上传
            String fileName = storageService.store(audio, LOCATION, String.valueOf(sampleId));

            FileDO file = new FileDO();
            file.setFileName(fileName);
            file.setSize(audio.getSize());
            file.setContentType(audio.getContentType());
            file.setFileType(FileType.AUDIO);
            audioFiles.add(file);
        });
        sampleService.saveFiles(sampleId, audioFiles);

        // 视频
        List<MultipartFile> videos = vo.getVideos().stream()
                .filter(image -> !image.isEmpty())
                .collect(Collectors.toList());
        List<FileDO> videoFiles = Lists.newArrayList();
        videos.forEach(video -> {
            // 上传
            String fileName = storageService.store(video, LOCATION, String.valueOf(sampleId));

            FileDO file = new FileDO();
            file.setFileName(fileName);
            file.setSize(video.getSize());
            file.setContentType(video.getContentType());
            file.setFileType(FileType.VIDEO);
            videoFiles.add(file);
        });
        sampleService.saveFiles(sampleId, videoFiles);

        // Excel
        List<MultipartFile> excels = vo.getExcels().stream()
                .filter(image -> !image.isEmpty())
                .collect(Collectors.toList());
        List<FileDO> excelFiles = Lists.newArrayList();
        excels.forEach(excel -> {
            // 上传
            String fileName = storageService.store(excel, LOCATION, String.valueOf(sampleId));

            FileDO file = new FileDO();
            file.setFileName(fileName);
            file.setSize(excel.getSize());
            file.setContentType(excel.getContentType());
            file.setFileType(FileType.EXCEL);
            excelFiles.add(file);
        });
        sampleService.saveFiles(sampleId, excelFiles);
    }

    /**
     * 重定向查询请求
     * @param query 查询条件
     */
    private void redirectQuery(SampleVO.QueryVO query, RedirectAttributes redirectAttributes) {
        // 查询表单对象
        redirectAttributes.addFlashAttribute("query", query);

        // 拼接返回url
        redirectAttributes.addAttribute("number", query.getNumber());
        redirectAttributes.addAttribute("size", query.getSize());
        redirectAttributes.addAttribute("deleted", query.getDeleted());
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
