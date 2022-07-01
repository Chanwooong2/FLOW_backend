package com.blockFileExtension.dao;

import com.blockFileExtension.vo.ExtensionInfo;
import com.blockFileExtension.vo.FunctionInfo;

import java.util.List;

public interface ExtensionDAO {

    List<FunctionInfo> getFunctionList();
    Boolean checkFuncValidation(Integer funcKey);
    Boolean checkExistExtension(Integer funcKey, String code);
    List<ExtensionInfo> getExtensionList(ExtensionInfo extensionInfo);
    Integer addExtension(ExtensionInfo extensionInfo);
    void addExtensionMappingTbl(ExtensionInfo extensionInfo);
    Boolean deleteExtension(Integer funcKey, Integer exKey);

}
