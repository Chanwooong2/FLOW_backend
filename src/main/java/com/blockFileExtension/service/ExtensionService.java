package com.blockFileExtension.service;

import com.blockFileExtension.dao.ExtensionDAO;
import com.blockFileExtension.vo.ExtensionInfo;
import com.blockFileExtension.vo.FunctionInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtensionService {
    private final ExtensionDAO extensionDAO;

    public ExtensionService(ExtensionDAO extensionDAO) {
        this.extensionDAO = extensionDAO;
    }

    public Boolean funcValidCheck(Integer funcKey){
        return extensionDAO.checkFuncValidation(funcKey);
    }
    public List<FunctionInfo> getFunctionList(){
        return extensionDAO.getFunctionList();
    }
    public List<ExtensionInfo> getDefaultExtensionList(){
        return this.getExtensionList(null, null, true);
    }
    public List<ExtensionInfo> getExtensionListByFuncKey(Integer funcKey){
        return this.getExtensionList(funcKey, null, null);
    }
    public ExtensionInfo getExtensionByCode(String code){
        List<ExtensionInfo> extensionList = this.getExtensionList(null, code, null);
        if(extensionList.size() > 0){
            return extensionList.get(0);
        }
        return null;
    }
    public List<ExtensionInfo> getExtensionList(Integer funcKey, String code, Boolean defaultYn){
        ExtensionInfo extensionInfo = new ExtensionInfo();
        extensionInfo.setFuncKey(funcKey);
        extensionInfo.setCode(code);
        extensionInfo.setDefaultYn(defaultYn);

        return extensionDAO.getExtensionList(extensionInfo);
    }

    public void saveExtension(Integer funcKey, String code){
//        funcKey 유효성 검사
        if(this.funcValidCheck(funcKey) == false){
            try {
                throw new Exception();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        ExtensionInfo extensionInfo = new ExtensionInfo();
        extensionInfo.setCode(code);

        Integer exKey = extensionDAO.addExtension(extensionInfo);
        if(exKey == null){
            exKey = this.getExtensionByCode(code).getExKey();
        }

        extensionInfo.setFuncKey(funcKey);
        extensionInfo.setExKey(exKey);
        extensionDAO.addExtensionMappingTbl(extensionInfo);
    }

    public void deleteExtension(String code){
        Integer exKey = this.getExtensionByCode(code).getExKey();
        this.deleteExtension(exKey);
    }
    public void deleteExtension(Integer exKey){
        extensionDAO.deleteExtension(exKey);
    }
}
