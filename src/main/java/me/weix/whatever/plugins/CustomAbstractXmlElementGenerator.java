package me.weix.whatever.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		// 增加base_query
		XmlElement sql = new XmlElement("sql");
		sql.addAttribute(new Attribute("id", "base_query"));
		//在这里添加where条件
        XmlElement selectTrimElement = new XmlElement("trim"); //设置trim标签
        selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));  
        selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR")); //添加where和and
		StringBuilder sb = new StringBuilder();
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append("null != ");
			sb.append(introspectedColumn.getJavaProperty());
			selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
			sb.setLength(0);
			// 添加and
			sb.append(" and ");
			// 添加别名t
			sb.append("t.");
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			// 添加等号
			sb.append(" = ");
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            selectNotNullElement.addElement(new TextElement(sb.toString()));
            selectTrimElement.addElement(selectNotNullElement);
		}
		sql.addElement(selectTrimElement);
		parentElement.addElement(sql);
		
		// 公用select
		sb.setLength(0);
		sb.append("select ");
		sb.append("t.* ");
		sb.append("from ");
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		sb.append(" t");
		TextElement selectText = new TextElement(sb.toString());
		
		// 公用include
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "base_query"));
		
		// 增加find
		XmlElement find = new XmlElement("select");
		find.addAttribute(new Attribute("id", "find"));
		find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		find.addElement(selectText);
		find.addElement(include);
		parentElement.addElement(find);

//		// 增加list
//		XmlElement list = new XmlElement("select");
//		list.addAttribute(new Attribute("id", "list"));
//		list.addAttribute(new Attribute("resultMap", "BaseResultMap"));
//		list.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
//		list.addElement(selectText);
//		list.addElement(include);
//		parentElement.addElement(list);
//
//		// 增加pageList
//		XmlElement pageList = new XmlElement("select");
//		pageList.addAttribute(new Attribute("id", "pageList"));
//		pageList.addAttribute(new Attribute("resultMap", "BaseResultMap"));
//		pageList.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
//		pageList.addElement(selectText);
//		pageList.addElement(include);
//		parentElement.addElement(pageList);

		// 单个逻辑删除
		XmlElement deleteLogicByIdElement = new XmlElement("update");
		deleteLogicByIdElement.addAttribute(new Attribute("id", "deleteLogicById"));
		deleteLogicByIdElement.addAttribute(new Attribute("parameterType", "java.lang.Integer"));

		deleteLogicByIdElement.addElement(
				new TextElement(
						"update " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " set deleted = 1 where id = #{id} "
				));

		parentElement.addElement(deleteLogicByIdElement);

		// 批量查询
		XmlElement selectByIdsElement = new XmlElement("select");
		selectByIdsElement.addAttribute(new Attribute("id", "selectByIds"));
		selectByIdsElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		selectByIdsElement.addAttribute(new Attribute("parameterType", "java.util.List"));


		selectByIdsElement.addElement(
				new TextElement(
						"select \n"
								+"\t<include refid=\"Base_Column_List\" />\n"
								+"\tfrom " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\n"
								+"\twhere id in <foreach item=\"item\" index=\"index\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> "
				));

		parentElement.addElement(selectByIdsElement);

		// 批量删除
		XmlElement deleteLogicByIdsElement = new XmlElement("update");
		deleteLogicByIdsElement.addAttribute(new Attribute("id", "deleteLogicByIds"));
		deleteLogicByIdsElement.addAttribute(new Attribute("parameterType", "java.util.List"));

		deleteLogicByIdsElement.addElement(
				new TextElement(
						"update " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " set deleted = 1 where id in \n"
								+ "\t<foreach item=\"item\" index=\"index\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> "
				));

		parentElement.addElement(deleteLogicByIdsElement);
	}

}
