package com.blockFileExtension.controller;

import com.blockFileExtension.service.ExtensionService;
import com.blockFileExtension.vo.ExtensionInfo;
import com.blockFileExtension.vo.FunctionInfo;
import net.sf.json.JSONSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ViewController {
    private final ExtensionService extensionService;

    public ViewController(ExtensionService extensionService) {
        this.extensionService = extensionService;
    }

    @RequestMapping(value = "/main")
    public String main(Model model){
        List<FunctionInfo> functionList = extensionService.getFunctionList();
        List<ExtensionInfo> defaultExtensionList = extensionService.getDefaultExtensionList();

        model.addAttribute("functionList", JSONSerializer.toJSON(functionList));
        model.addAttribute("defaultExtensionList", JSONSerializer.toJSON(defaultExtensionList));

        return "main";
    }
}
