package com.anjiplus.template.gaea.business.modules.reportexcel.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessException;
import com.anji.plus.gaea.utils.GaeaAssert;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.enums.ExportTypeEnum;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.DataSetDto;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.OriginalDataDto;
import com.anjiplus.template.gaea.business.modules.dataset.service.DataSetService;
import com.anjiplus.template.gaea.business.modules.file.dao.GaeaFileMapper;
import com.anjiplus.template.gaea.business.modules.file.entity.GaeaFile;
import com.anjiplus.template.gaea.business.modules.report.dao.ReportMapper;
import com.anjiplus.template.gaea.business.modules.report.dao.entity.Report;
import com.anjiplus.template.gaea.business.modules.reportexcel.controller.dto.ReportExcelDto;
import com.anjiplus.template.gaea.business.modules.reportexcel.dao.ReportExcelMapper;
import com.anjiplus.template.gaea.business.modules.reportexcel.dao.entity.ReportExcel;
import com.anjiplus.template.gaea.business.modules.reportexcel.service.ReportExcelService;
import com.anjiplus.template.gaea.business.modules.reportexcel.util.CellType;
import com.anjiplus.template.gaea.business.modules.reportexcel.util.XlsSheetUtil;
import com.anjiplus.template.gaea.business.modules.reportexcel.util.XlsUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/4/13 15:14
 */
@Service
public class ReportExcelServiceImpl implements ReportExcelService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReportExcelMapper reportExcelMapper;

    @Autowired
    private DataSetService dataSetService;


    @Autowired
    private ReportMapper reportMapper;

    @Value("${customer.file.dist-path:''}")
    private String dictPath;

    @Value("${customer.file.downloadPath:''}")
    private String fileDownloadPath;

    @Autowired
    private GaeaFileMapper gaeaFileMapper;


    @Override
    public GaeaBaseMapper<ReportExcel> getMapper() {
        return reportExcelMapper;
    }

    @Override
    public ReportExcelDto detailByReportCode(String reportCode) {
        QueryWrapper<ReportExcel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_code", reportCode);
        ReportExcel reportExcel = reportExcelMapper.selectOne(queryWrapper);
        if (reportExcel != null) {
            ReportExcelDto dto = new ReportExcelDto();
            BeanUtils.copyProperties(reportExcel, dto);
            return dto;
        }
        return null;
    }

    /**
     * 操作前处理
     *
     * @param entity        前端传递的对象
     * @param operationEnum 操作类型
     * @throws BusinessException 阻止程序继续执行或回滚事务
     */
    @Override
    public void processBeforeOperation(ReportExcel entity, BaseOperationEnum operationEnum) throws BusinessException {
        if (operationEnum.equals(BaseOperationEnum.INSERT)) {
            String reportCode = entity.getReportCode();
            ReportExcel report = this.selectOne("report_code", reportCode);
            if (null != report) {
                this.deleteById(report.getId());
            }
        }
    }

    /**
     * 报表预览
     */
    @Override
    public ReportExcelDto preview(ReportExcelDto reportExcelDto) {
        // 根据id查询 报表详情
        ReportExcel reportExcel = selectOne("report_code", reportExcelDto.getReportCode());
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_code", reportExcelDto.getReportCode());
        Report report = reportMapper.selectOne(queryWrapper);
        GaeaAssert.notNull(reportExcel, ResponseCode.RULE_CONTENT_NOT_EXIST, "reportExcel");
        String setParam = reportExcelDto.getSetParam();

        GaeaBeanUtils.copyAndFormatter(reportExcel, reportExcelDto);
        if (StringUtils.isNotBlank(setParam)) {
            reportExcelDto.setSetParam(setParam);
        }
        reportExcelDto.setReportName(report.getReportName());
        // 数据集解析
        String jsonStr = analysisReportData(reportExcelDto);
        reportExcelDto.setJsonStr(jsonStr);
//        reportExcelDto.setTotal(jsonObject.getJSONObject("rows").size());
        return reportExcelDto;
    }

    @Override
    public Boolean exportExcel(ReportExcelDto reportExcelDto) {
        String reportCode = reportExcelDto.getReportCode();
        String exportType = reportExcelDto.getExportType();
        logger.error("导出...");
        if (exportType.equals(ExportTypeEnum.GAEA_TEMPLATE_EXCEL.getCodeValue())) {
            ReportExcelDto report = detailByReportCode(reportCode);
            reportExcelDto.setJsonStr(report.getJsonStr());
            String jsonStr = analysisReportData(reportExcelDto);
            List<JSONObject> lists=(List<JSONObject> ) JSON.parse(jsonStr);
            OutputStream out;
            try {
                String fileId = UUID.randomUUID().toString();
                String filePath = dictPath + File.separator + fileId + ".xlsx";
                String urlPath = fileDownloadPath + java.io.File.separator + fileId;

                GaeaFile gaeaFile = new GaeaFile();
                gaeaFile.setFilePath(filePath);
                gaeaFile.setFileId(fileId);
                gaeaFile.setUrlPath(urlPath);
                gaeaFile.setFileType("xlsx");
                gaeaFile.setFileInstruction(reportCode + ".xlsx");

                out = new FileOutputStream(filePath);
                XlsUtil.exportXlsFile(out, true, lists);

                gaeaFileMapper.insert(gaeaFile);
                logger.info("导出成功：{}", gaeaFile);
            } catch (IOException e) {
                logger.error("导出失败", e);
            }
        }
        return true;
    }

    /**
     * 解析报表数据，动态插入列表数据和对象数据
     */
    private String analysisReportData(ReportExcelDto reportExcelDto) {

        String jsonStr = reportExcelDto.getJsonStr();
        String setParam = reportExcelDto.getSetParam();
        List<JSONObject> dbObjectList = (List<JSONObject>) JSON.parse(jsonStr);

        if (dbObjectList != null && dbObjectList.size() > 0) {
            for (int x = 0; x < dbObjectList.size(); x++) {
                analysisSheetCellData(dbObjectList.get(x), setParam);
            }
        }
        //fastjson $ref 循环引用
        return JSONObject.toJSONString(dbObjectList, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 解析单sheet data
     *
     * @param dbObject
     */
    private void analysisSheet(JSONObject dbObject, String setParma) {
        //data是一个二维数组
        if (dbObject.containsKey("data") && null != dbObject.get("data")) {
            List<JSONArray> data = (List<JSONArray>) dbObject.get("data");


            //行
            for (int r = 0; r < data.size(); r++) {
                JSONArray jsonArray = data.get(r);
                //列
                for (int c = 0; c < jsonArray.size(); c++) {
                    //单元格
                    JSONObject cell = jsonArray.getJSONObject(c);
                    if (null != cell && cell.containsKey("v") && StringUtils.isNotBlank(cell.getString("v"))) {
                        String v = cell.getString("v");
                        DataSetDto dataSet = getDataSet(v, setParma);
                        if (null != dataSet) {
                            OriginalDataDto originalDataDto = dataSetService.getData(dataSet);
                            if (null != originalDataDto.getData()) {
                                if (originalDataDto.getData().size() == 1) {
                                    //对象
                                    JSONObject jsonObject = originalDataDto.getData().get(0);
                                    String fieldLabel = jsonObject.getString(dataSet.getFieldLabel());

                                    String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".").concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);
                                    dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("v", replace);
                                    dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("m", replace);

                                } else {
                                    //集合
                                    JSONObject jsonObject = originalDataDto.getData().get(0);
                                    String fieldLabel = jsonObject.getString(dataSet.getFieldLabel());

                                    String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".").concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);
                                    dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("v", replace);
                                    dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("m", replace);
                                }
                            }

                        }
                    }



                }
            }


            System.out.println("aaaa");


        }


    }

    /**
     * 解析单sheet celldata
     *
     * @param dbObject
     */
    private void analysisSheetCellData(JSONObject dbObject, String setParam) {
        //清空data值
        dbObject.remove("data");
        //celldata是一个一维数组
        if (dbObject.containsKey("celldata") && null != dbObject.get("celldata")) {
            List<JSONObject> celldata = new ArrayList<>();
            celldata.addAll((List<JSONObject>) dbObject.get("celldata"));

            //整理celldata数据，转换为map格式，方便后续使用单元格位置获取对应的cell对象
            Map<String,JSONObject> cellDataMap = cellDataList2Map(celldata);
            //清除原有的数据
            dbObject.getJSONArray("celldata").clear();
            //获取配置项中的合并属性
            JSONObject merge = dbObject.getJSONObject("config").getJSONObject("merge");
            if(merge != null) merge.clear();
            //定义存储每一列动态扩展的行数
            Map<Integer,Integer> colAddCntMap = new HashMap<>();
            // 遍历已存在的单元格，查看是否存在动态参数
            for (int i = 0; i < celldata.size(); i++) {
                //单元格对象
                JSONObject cellObj = celldata.get(i);
                //fastjson深拷贝问题
                String cellStr = cellObj.toJSONString();
                analysisCellData(cellObj,setParam,colAddCntMap,cellStr,merge, dbObject,cellDataMap);
            }
        }
    }

    /**
     * 开始解析并渲染 cellData
     * @param cellObject
     */
    public void analysisCellData(JSONObject cellObject,String setParam,Map<Integer,Integer> colAddCntMap,String cellStr,
                                 JSONObject merge,JSONObject dbObject,Map<String,JSONObject> cellDataMap){
        //获取行号
        Integer cellR = cellObject.getInteger("r");
        //获取列数
        Integer cellC = cellObject.getInteger("c");
        //获取此行已经动态增加的行数，默认0行
        int cnt = colAddCntMap.get(cellC) == null ? 0 : colAddCntMap.get(cellC);
        //获取单元格类型
        CellType cellType = getCellType(cellObject);
        switch (cellType){
            case BLACK:
                //空数据单元格不处理
                break;
            case DYNAMIC_MERGE:
            case DYNAMIC:
                //处理动态单元格
                String v = cellObject.getJSONObject("v").getString("v");
                DataSetDto dataSet = getDataSet(v, setParam);
                handleDynamicCellObject(dataSet,v,cellStr,cnt,cellR,cellC,merge,dbObject,colAddCntMap);
                break;
            default:
                //处理静态单元格
                handleStaticCellObject(cellStr,dbObject,cnt,cellR,cellC,cellDataMap,setParam,merge,colAddCntMap,cellType);
                break;
        }
    }

    /**
     * 处理动态数据单元格自动扩展
     * @param dataSet
     * @param v
     * @param cellStr
     * @param cnt
     * @param r
     * @param c
     * @param merge
     * @param dbObject
     * @param colAddCntMap
     */
    public void handleDynamicCellObject(DataSetDto dataSet,String v,String cellStr,int cnt,int r,int c,
                                        JSONObject merge,JSONObject dbObject,Map<Integer,Integer> colAddCntMap){
        //获取动态数据
        OriginalDataDto originalDataDto = dataSetService.getData(dataSet);
        List<JSONObject> cellDynamicData = originalDataDto.getData();

        if(cellDynamicData != null){
            //循环数据赋值
            for (int j = 0; j < cellDynamicData.size(); j++) {
                //新增的行数据
                JSONObject addCell = cellDynamicData.get(j);
                //字段
                String fieldLabel = addCell.getString(dataSet.getFieldLabel());
                String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".").concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);
                //转字符串，解决深拷贝问题
                JSONObject addCellData = JSONObject.parseObject(cellStr);

                addCellData.put("r",  cnt + r + j); //行数增加
                addCellData.put("c", c);
                addCellData.getJSONObject("v").put("v", replace);
                addCellData.getJSONObject("v").put("m", replace);
                JSONObject cellMc = addCellData.getJSONObject("v").getJSONObject("mc");
                //判断是否是合并单元格
                if(null != cellMc){
                    //处理合并单元格
                    Integer rs = cellMc.getInteger("rs");
                    cellMc.put("r",  cnt + r + rs*j); //行数增加
                    cellMc.put("c", c);
                    addCellData.put("r",  cnt + r + rs*j);
                    //合并单元格需要处理config.merge
                    merge.put(cellMc.getString("r")+"_"+cellMc.getString("c"),cellMc);
                    //处理单元格扩展之后此列扩展的总行数
                    colAddCntMap.put(c,cnt + rs * cellDynamicData.size() - 1);
                }else{
                    //处理单元格扩展之后此列扩展的总行数
                    colAddCntMap.put(c,cnt + cellDynamicData.size() - 1);
                }
                dbObject.getJSONArray("celldata").add(addCellData);
            }
        }
    }

    /**
     * 处理静态单元格数据
     * @param cellStr
     * @param dbObject
     * @param cnt
     * @param r
     * @param c
     * @param cellDataMap
     * @param setParam
     * @param merge
     * @param colAddCntMap
     * @param cellType
     */
    public void handleStaticCellObject(String cellStr,JSONObject dbObject,int cnt,int r,int c,
                                       Map<String,JSONObject> cellDataMap,String setParam,
                                       JSONObject merge,Map<Integer,Integer> colAddCntMap,CellType cellType){
        //转字符串，解决深拷贝问题
        JSONObject addCellData = JSONObject.parseObject(cellStr);
        int rows = 0;
        switch(cellType){
            case STATIC:
            case STATIC_MERGE:
                //静态不扩展单元格只需要初始化位置就可以
                initCellPosition(addCellData,cnt,merge);
                break;
            case STATIC_AUTO:
                //获取静态单元格右侧动态单元格的总行数
                rows = getRightDynamicCellRows(addCellData,cellDataMap,setParam,rows,cellType);
                initCellPosition(addCellData,cnt,merge);
                if(rows > 1){
                    //需要把这个静态扩展单元格 改变为 静态合并扩展单元格，就是增加合并属性 mc 以及merge配置
                    JSONObject mc = new JSONObject();
                    mc.put("rs",rows);
                    mc.put("cs",1);
                    mc.put("r",addCellData.getIntValue("r"));
                    mc.put("c",addCellData.getIntValue("c"));
                    addCellData.getJSONObject("v").put("mc",mc);
                    //合并单元格需要处理config.merge
                    merge.put((mc.getInteger("r")) + "_" + mc.getString("c"),mc);
                    //处理单元格扩展之后此列扩展的总行数
                    colAddCntMap.put(c,cnt + rows - 1);
                }
                break;
            case STATIC_MERGE_AUTO:
                //获取静态单元格右侧动态单元格的总行数
                rows = getRightDynamicCellRows(addCellData,cellDataMap,setParam,rows,cellType);
                initCellPosition(addCellData,cnt,merge);
                if(rows > 0){
                    //需要修改单元格mc中的rs
                    JSONObject cellMc = addCellData.getJSONObject("v").getJSONObject("mc");
                    int addCnt = cellMc.getInteger("rs");
                    cellMc.put("rs",rows);
                    //合并单元格需要处理config.merge
                    merge.put((cellMc.getInteger("r")) + "_" + cellMc.getString("c"),cellMc);
                    //处理单元格扩展之后此列扩展的总行数
                    colAddCntMap.put(c,cnt + rows - addCnt);
                }
                break;
        }
        dbObject.getJSONArray("celldata").add(addCellData);
    }

    /**
     * 初始化单元格位置，主要是这一列已经动态增加的行数
     * @param addCellData
     * @param cnt
     * @param merge
     */
    public void initCellPosition(JSONObject addCellData,int cnt,JSONObject merge){
        addCellData.put("r", cnt + addCellData.getInteger("r"));//行数增加
        //是否是合并单元格
        JSONObject mc = addCellData.getJSONObject("v").getJSONObject("mc");
        if(mc != null){
            mc.put("r",addCellData.getInteger("r"));
            initCellMerge(merge,mc);
        }
    }

    /**
     * 初始化单元格合并属性的行数
     * @param merge
     * @param mc
     */
    public void initCellMerge(JSONObject merge,JSONObject mc){
        merge.put((mc.getInteger("r"))+"_"+mc.getString("c"),mc);
    }

    /**
     * 获取合并单元格右侧的动态扩展行数，用来设置当前单元格的实际
     * @param addCellData
     * @param cellDataMap
     * @param setParam
     * @param sumRows
     * @param cellType
     * @return
     */
    public int getRightDynamicCellRows(JSONObject addCellData,Map<String,JSONObject> cellDataMap,String setParam,int sumRows,CellType cellType){
        //1、获取此单元格右侧关联的所有单元格
        List<JSONObject> rightCellList = getRightDynamicCell(addCellData,cellDataMap,cellType);
        //2、循环获取每个单元格的扩展行数
        for (JSONObject rightCell : rightCellList) {
            //首先判断这个单元格是否也是【静态扩展单元格】
            CellType rightCellType = getCellType(rightCell);
            switch (rightCellType){
                case STATIC_AUTO:
                case STATIC_MERGE_AUTO:
                    //递归查找
                    sumRows = getRightDynamicCellRows(rightCell,cellDataMap,setParam,sumRows,rightCellType);
                    break;
                case BLACK:
                case STATIC:
                    sumRows++;
                    break;
                case STATIC_MERGE:
                    sumRows += rightCell.getJSONObject("v").getJSONObject("mc").getInteger("rs");
                    break;
                default:
                    List<JSONObject> cellDynamicData = getDynamicDataList(rightCell.getJSONObject("v").getString("v"),setParam);
                    if(cellDynamicData != null && cellDynamicData.size() > 1){
                        int size = cellDynamicData.size();
                        sumRows += size;
                    }else{
                        sumRows++;
                    }
                    break;
            }
        }
        return sumRows;
    }

    /**
     * 获取扩展单元格右侧相邻的所有单元格实体
     * @param addCellData
     * @param cellDataMap
     * @param cellType
     * @return
     */
    public List<JSONObject> getRightDynamicCell(JSONObject addCellData,Map<String,JSONObject> cellDataMap,CellType cellType){
        //静态数据合并单元格需要根据右侧的单元格进行自动向下扩展
        //1、先获取右侧一列的关联的单元格，根据自身的位置，以及自己合并的合并的信息推断
        //如果自己位置是 2，5，并且本身合并 行数2，列数3，则需要推断出两个单元格的位置
        //分别是2，8 和 3，8
        Integer cellR = addCellData.getInteger("r");
        Integer cellC = addCellData.getInteger("c");
        Integer cellRs = 0;
        Integer cellCs = 0;
        switch (cellType){
            case STATIC_AUTO:
                cellRs = 1;
                cellCs = 1;
                break;
            case STATIC_MERGE_AUTO:
                cellRs = addCellData.getJSONObject("v").getJSONObject("mc").getInteger("rs");
                cellCs = addCellData.getJSONObject("v").getJSONObject("mc").getInteger("cs");
                break;
        }
        List<JSONObject> rightCells = new ArrayList<>();
        for(int nums = 0;nums < cellRs;nums++){
            int r = cellR + nums;
            int c = cellC + cellCs;
            String key = r + "," + c;
            if(cellDataMap.containsKey(key)){
                JSONObject cellData = cellDataMap.get(r + "," + c);
                rightCells.add(cellData);
            }
        }
        return rightCells;
    }

    /**
     * 判断单元格类型
     * @param cellObject
     * @return
     */
    public CellType getCellType(JSONObject cellObject){
        JSONObject cellV1 = cellObject.getJSONObject("v");
        if (null != cellV1 && cellV1.containsKey("v") && StringUtils.isNotBlank(cellV1.getString("v"))) {
            String cellV2 = cellObject.getJSONObject("v").getString("v");
            String auto = cellObject.getJSONObject("v").getString("auto");
            JSONObject mc = cellObject.getJSONObject("v").getJSONObject("mc");
            if(cellV2.contains("#{") && cellV2.contains("}") ){
                //动态单元格
                if(mc != null){
                    return CellType.DYNAMIC_MERGE;
                }else{
                    return CellType.DYNAMIC;
                }
            }else{
                //静态单元格
                if(mc != null && "1".equals(auto)){
                    return CellType.STATIC_MERGE_AUTO;
                }else {
                    if("1".equals(auto)){
                        return CellType.STATIC_AUTO;
                    }else if(mc != null){
                        return CellType.STATIC_MERGE;
                    }else{
                        return CellType.STATIC;
                    }
                }
            }
        }else{
            return CellType.BLACK;
        }
    }

    /**
     * list转为map结构，方便使用行列号查找对应cell对象
     * @param cellDataList
     * @return
     */
    public Map<String,JSONObject> cellDataList2Map(List<JSONObject> cellDataList){
        Map<String,JSONObject> cellDataMap = new HashMap<>();
        for (JSONObject cellData : cellDataList) {
            String r = cellData.getString("r");
            String c = cellData.getString("c");
            cellDataMap.put(r + "," + c, cellData);
        }
        return cellDataMap;
    }

    /**
     * 解析 #{xxxx.xxxxx} 数据
     * @param v
     * @return
     */
    private DataSetDto getDataSet(String v, String setParam) {

        DataSetDto dto = new DataSetDto();
        if (v.contains("#{") && v.contains("}")) {
            int start = v.indexOf("#{") + 2;
            int end = v.indexOf("}");
            if (start < end) {
                String substring = v.substring(start, end);
                if (substring.contains(".")) {
                    String[] split = substring.split("\\.");
                    dto.setSetCode( split[0]);
                    dto.setFieldLabel(split[1]);
                    getContextData(setParam, dto);
                    return dto;
                }
            }
        }
        return null;
    }

    /**
     * 获取单元格对应的动态数据集
     * @param v
     * @param setParam
     * @return
     */
    private List<JSONObject> getDynamicDataList(String v, String setParam){
        if(StringUtils.isNotBlank(v)){
            DataSetDto dataSet = getDataSet(v,setParam);
            if(dataSet != null){
                OriginalDataDto originalDataDto = dataSetService.getData(dataSet);
                List<JSONObject> cellDynamicData = originalDataDto.getData();
                return cellDynamicData;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 动态参数替换
     * @param setParam
     * @param dto
     */
    private void getContextData(String setParam, DataSetDto dto) {
        if (StringUtils.isNotBlank(setParam)) {
            JSONObject setParamJson = JSONObject.parseObject(setParam);
            Map<String, Object> map = new HashMap<>();
            // 查询条件
            if (setParamJson.containsKey(dto.getSetCode())) {
                JSONObject paramCondition = setParamJson.getJSONObject(dto.getSetCode());
                paramCondition.forEach(map::put);
            }
            dto.setContextData(map);
        }
    }

}
