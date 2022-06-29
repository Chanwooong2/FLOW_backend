package com.blockFileExtension.controller;

import com.blockFileExtension.service.ExtensionService;
import com.blockFileExtension.vo.ExtensionInfo;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExtensionController {
    private final ExtensionService extensionService;

    public ExtensionController(ExtensionService extensionService) {
        this.extensionService = extensionService;
    }

    @GetMapping(value = "/functions/{funcKey}/extensions")
    public Map<String, Object> getExtensionList(@PathVariable Integer funcKey){
        Map<String, Object> retData = new HashMap<>();

        List<ExtensionInfo> extensionList = extensionService.getExtensionListByFuncKey(funcKey);
        retData.put("extensionList", extensionList);

        return retData;
    }

    @PostMapping(value = "/extensions")
    public Map<String, Object> saveExtension(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        boolean result = false;
        String msg = "";
        boolean missParamYn = false;

        Integer funcKey = null;
        if(jsonParam.has("funcKey")){
            funcKey = jsonParam.getInt("funcKey");
        }else{
            missParamYn = true;
            msg = "MISS PARAMETER - funcKey";
        }

        String code = null;
        if(jsonParam.has("code")){
            code = jsonParam.getString("code");
        }else{
            missParamYn = true;
            msg = "MISS PARAMETER - code";
        }

        if( !missParamYn ){
            try{
                extensionService.saveExtension(funcKey, code);
                result = true;
                msg = "커스텀 확장자 저장 성공";
            }catch (Exception e){
                msg = "커스텀 확장자 저장 실패";
            }
        }

        retData.put("result", result);
        retData.put("msg", msg);

        return retData;
    }

    @DeleteMapping(value = "/extensions/{code}")
    public Map<String, Object> deleteExtension(@PathVariable String code){
        Map<String, Object> retData = new HashMap<>();
        boolean result = false;
        String msg = "";

        try{
            extensionService.deleteExtension(code);
            result = true;
            msg = "커스텀 확장자 삭제 성공";
        }catch (Exception e){
            msg = "커스텀 확장자 삭제 실패";
        }

        retData.put("result", result);
        retData.put("msg", msg);

        return retData;
    }
}
