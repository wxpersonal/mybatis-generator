package com.wx.whatever.plugins;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class MapperPlugin extends PluginAdapter {

    /**
     * {@inheritDoc}
     */
    public boolean validate(List<String> warnings) {
        return true;
    }



    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
                                                           Interface interfaze, IntrospectedTable introspectedTable) {

        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        interfaze.addMethod(generateSelectByIds(method,
                introspectedTable));
        interfaze.addMethod(generateDeleteLogicById(method,
                introspectedTable));
        interfaze.addMethod(generateDeleteLogicByIds(method,
                introspectedTable));
        return true;
    }



    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名

        XmlElement parentElement = document.getRootElement();

        // 单个删除
        XmlElement deleteLogicByIdElement = new XmlElement("update");
        deleteLogicByIdElement.addAttribute(new Attribute("id", "deleteLogicByIds"));

        deleteLogicByIdElement.addElement(
                new TextElement(
                        "update " + tableName + " set deleted = 1 where id = #{id} "
                ));

        parentElement.addElement(deleteLogicByIdElement);

        // 批量查询
        XmlElement selectByIdsElement = new XmlElement("select");
        selectByIdsElement.addAttribute(new Attribute("id", "selectByIds"));


        selectByIdsElement.addElement(
                new TextElement(
                        "select \n"
                                +"\t<include refid=\"Base_Column_List\" />\n"
                                +"\tfrom " + tableName + "\n"
                                +"\twhere id in <foreach item=\"item\" index=\"index\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> "
                ));

        parentElement.addElement(selectByIdsElement);

        // 批量删除
        XmlElement deleteLogicByIdsElement = new XmlElement("update");
        deleteLogicByIdsElement.addAttribute(new Attribute("id", "deleteLogicById"));

        deleteLogicByIdsElement.addElement(
                new TextElement(
                        "update " + tableName + " set deleted = 1 where id in \n"
                                + "\t<foreach item=\"item\" index=\"index\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> "
                ));

        parentElement.addElement(deleteLogicByIdsElement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private Method generateDeleteLogicByIds(Method method, IntrospectedTable introspectedTable) {

        Method m = new Method("deleteLogicByIds");

        m.setVisibility(method.getVisibility());

        m.setReturnType(FullyQualifiedJavaType.getIntInstance());
        m.addParameter(new Parameter(new FullyQualifiedJavaType("Integer[]"), "ids", "@Param(\"ids\")"));

        context.getCommentGenerator().addGeneralMethodComment(m,
                introspectedTable);
        return m;
    }

    private Method generateDeleteLogicById(Method method, IntrospectedTable introspectedTable) {

        Method m = new Method("deleteLogicById");

        m.setVisibility(method.getVisibility());
        m.setReturnType(FullyQualifiedJavaType.getIntInstance());
        m.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"), "id"));

        context.getCommentGenerator().addGeneralMethodComment(m,
                introspectedTable);
        return m;
    }

    private Method generateSelectByIds(Method method, IntrospectedTable introspectedTable) {

        Method m = new Method("selectByIds");

        m.setVisibility(method.getVisibility());

        m.setReturnType(FullyQualifiedJavaType.getIntInstance());
        m.addParameter(new Parameter(new FullyQualifiedJavaType("Integer[]"), "ids", "@Param(\"ids\")"));

        context.getCommentGenerator().addGeneralMethodComment(m,
                introspectedTable);
        return m;
    }

}