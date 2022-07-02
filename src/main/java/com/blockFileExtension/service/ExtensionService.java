package com.blockFileExtension.service;

import com.blockFileExtension.dao.ExtensionDAO;
import com.blockFileExtension.exception.ExtensionAlreadyExistException;
import com.blockFileExtension.exception.NotExistFuncKeyException;
import com.blockFileExtension.exception.RowDeleteException;
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

    public Boolean checkFuncValidation(Integer funcKey){
        return extensionDAO.checkFuncValidation(funcKey);
    }
    public Boolean checkExistExtension(Integer funcKey, String code){
        return extensionDAO.checkExistExtension(funcKey, code);
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

    public void saveExtension(Integer funcKey, String code) throws NotExistFuncKeyException, ExtensionAlreadyExistException {
//        funcKey 유효성 검사
        if(this.checkFuncValidation(funcKey) == false){
            throw new NotExistFuncKeyException("존재하지 않는 기능키입니다. funcKey를 확인해주세요.");
        }
        if(this.checkExistExtension(funcKey, code) == true){
            throw new ExtensionAlreadyExistException("이미 추가된 확장자입니다.");
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

    public void deleteExtension(Integer funcKey, String code) throws RowDeleteException {
        Integer exKey = this.getExtensionByCode(code).getExKey();
        this.deleteExtension(funcKey, exKey);
    }
    public void deleteAllExtension(Integer funcKey) throws RowDeleteException{
        boolean result = extensionDAO.deleteExtension(funcKey, null);
        if(result == false){
            throw new RowDeleteException("확장자 삭제에 실패했습니다.");
        }
    }
    public void deleteExtension(Integer funcKey, Integer exKey) throws RowDeleteException{
        boolean result = extensionDAO.deleteExtension(funcKey, exKey);
        if(result == false){
            throw new RowDeleteException("확장자 삭제에 실패했습니다.");
        }
    }
}
