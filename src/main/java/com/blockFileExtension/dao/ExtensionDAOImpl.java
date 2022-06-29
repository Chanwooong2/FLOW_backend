package com.blockFileExtension.dao;

import com.blockFileExtension.vo.ExtensionInfo;
import com.blockFileExtension.vo.FunctionInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExtensionDAOImpl implements ExtensionDAO{
    @Resource(name="MainSession")
    private final SqlSession session;
    private final String mapper = "mapper.postgresql.mapper.";

    public ExtensionDAOImpl(SqlSession session) {
        this.session = session;
    }

    @Override
    public List<FunctionInfo> getFunctionList() {
        return session.selectList(mapper+"getFunctionList");
    }

    @Override
    public Boolean checkFuncValidation(Integer funcKey) {
        boolean result = false;

        Map<String, Object> param = new HashMap<>();
        param.put("funcKey", funcKey);
        Integer selectResult = session.selectOne(mapper+"funcValidCheck", param);

        if(selectResult > 0){
            result = true;
        }

        return result;
    }

    @Override
    public List<ExtensionInfo> getExtensionList(ExtensionInfo extensionInfo) {
        return session.selectList(mapper+"getExtensionList", extensionInfo);
    }

    @Override
    public Integer addExtension(ExtensionInfo extensionInfo) {
        session.insert(mapper+"addExtension", extensionInfo);
        return extensionInfo.getExKey();
    }

    @Override
    public void addExtensionMappingTbl(ExtensionInfo extensionInfo) {
        session.insert(mapper+"addExtensionMappingTbl", extensionInfo);
    }

    @Override
    public void deleteExtension(Integer exKey) {
        Map<String, Object> param = new HashMap<>();
        param.put("exKey", exKey);

        session.delete(mapper+"deleteExtension", param);
    }
}
