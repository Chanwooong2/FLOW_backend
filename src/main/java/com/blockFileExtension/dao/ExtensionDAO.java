package com.blockFileExtension.dao;

import com.blockFileExtension.vo.ExtensionInfo;
import com.blockFileExtension.vo.FunctionInfo;

import java.util.List;

public interface ExtensionDAO {

    List<FunctionInfo> getFunctionList();
    Boolean checkFuncValidation(Integer funcKey);
    List<ExtensionInfo> getExtensionList(ExtensionInfo extensionInfo);
    Integer addExtension(ExtensionInfo extensionInfo);
    void addExtensionMappingTbl(ExtensionInfo extensionInfo);
    void deleteExtension(Integer exKey);

}
