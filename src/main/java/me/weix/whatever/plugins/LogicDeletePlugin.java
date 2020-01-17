package me.weix.whatever.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

public class LogicDeletePlugin extends PluginAdapter {


    public boolean validate(List<String> list){
        return true;
    }

    /**
     * 修改Mapper类
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        addLogicDeleteMethod(interfaze);
        return true;
    }

    private void addLogicDeleteMethod(Interface interfaze){
        // 方法的返回值
        FullyQualifiedJavaType returnTypeInt = FullyQualifiedJavaType.getIntInstance();

        Method logicDeleteById = new Method();
        // 1.设置方法可见性
        logicDeleteById.setVisibility(JavaVisibility.PUBLIC);
        // 2.设置返回值类型 int类型
        logicDeleteById.setReturnType(returnTypeInt);
        // 3.设置方法名
        logicDeleteById.setName("deleteLogicById");
        // 4.设置参数列表
        FullyQualifiedJavaType paramType = PrimitiveTypeWrapper.getNewArrayListInstance();
        logicDeleteById.addParameter(new Parameter(paramType, "ids"));
        interfaze.addMethod(logicDeleteById);

        Method logicDeleteByIds = new Method();
        // 1.设置方法可见性
        logicDeleteByIds.setVisibility(JavaVisibility.PUBLIC);
        // 2.设置返回值类型 int类型
        logicDeleteByIds.setReturnType(returnTypeInt);
        // 3.设置方法名
        logicDeleteByIds.setName("logicDeleteByIds");
        // 4.设置参数列表
        FullyQualifiedJavaType paramTypeSelective = PrimitiveTypeWrapper.getLongInstance();
        logicDeleteByIds.addParameter(new Parameter(paramTypeSelective, "ids", "@Param(\"id\")"));
        interfaze.addMethod(logicDeleteByIds);
    }



}