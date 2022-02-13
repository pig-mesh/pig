package com.anjiplus.template.gaea.business.modules.dict.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.bean.KeyValue;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.dict.controller.dto.GaeaDictDTO;
import com.anjiplus.template.gaea.business.modules.dict.controller.param.GaeaDictParam;
import com.anjiplus.template.gaea.business.modules.dict.dao.entity.GaeaDict;
import com.anjiplus.template.gaea.business.modules.dict.service.GaeaDictItemService;
import com.anjiplus.template.gaea.business.modules.dict.service.GaeaDictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * (GaeaDict)实体类
 *
 * @author lr
 * @since 2021-02-23 10:01:02
 */
@RestController
@RequestMapping("/gaeaDict")
@Api(value = "/gaeaDict", tags = "")
public class GaeaDictController extends GaeaBaseController<GaeaDictParam, GaeaDict, GaeaDictDTO> {

    @Autowired
    private GaeaDictService gaeaDictService;

    @Autowired
    private GaeaDictItemService gaeaDictItemService;

    @Override
    public GaeaBaseService<GaeaDictParam, GaeaDict> getService() {
        return gaeaDictService;
    }

    @Override
    public GaeaDict getEntity() {
        return new GaeaDict();
    }

    @Override
    public GaeaDictDTO getDTO() {
        return new GaeaDictDTO();
    }


    /**
     * 刷新指定字典项
     * @return
     */
    @PostMapping("/freshDict")
    @Permission(code = "fresh",name = "刷新")
    public ResponseBean refreshDict(@RequestBody List<String> dictCodes) {
        //刷新
        gaeaDictService.refreshCache(dictCodes);
        return responseSuccess();
    }

    /**
     * 下拉菜单
     * @return
     */
    @GetMapping("/select/{dictCode}")
    @Permission(code = "query",name = "下拉")
    public ResponseBean select(@PathVariable("dictCode") String dictName){
        Locale locale = LocaleContextHolder.getLocale();
        //语言
        String language = locale.getLanguage();

        List<KeyValue> keyValues = gaeaDictService.select(dictName,language);
        return responseSuccessWithData(keyValues);
    }


    /**
     * 指定语言的字典项
     * @return
     */
    @GetMapping("/map/{dictCode}")
    public ResponseBean dictItemByLang(@PathVariable("dictCode") String dictCode){
        return responseSuccessWithData(gaeaDictItemService.getItemMap(dictCode));
    }
    /**
     * 下拉菜单
     * @return
     */
    @GetMapping("/selectAll/{project}")
    public ResponseBean selectTypecodes(@PathVariable String project){
        Locale locale = LocaleContextHolder.getLocale();
        //语言
        String language = locale.getLanguage();

        Collection<KeyValue> keyValues = gaeaDictService.selectTypeCode(project,language);
        return responseSuccessWithData(keyValues);
    }

    /**
     * 获取所有字典
     * @return
     */
    @GetMapping("/all")
    public ResponseBean all(){
        Locale locale = LocaleContextHolder.getLocale();
        //语言
        String language = locale.getLanguage();

        Map<String, List<KeyValue>> all = gaeaDictService.all(language);
        return responseSuccessWithData(all);
    }
}
