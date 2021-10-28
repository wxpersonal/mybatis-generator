package me.weix.whatever.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		TextElement selectText = getBaseXml(parentElement);
		// 公用include
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "base_query"));

//		addXmlFind(parentElement, selectText, include);
		addXmlSelectByCondition(parentElement, selectText, include);
		addXmlSelectByIds(parentElement);
//		addXmlLogicDeleteById(parentElement);
//		addXmlLogicDeleteByIds(parentElement);
		addXmlDeleteByIds(parentElement);
		addXmlBatchInsert(parentElement);
	}


	private TextElement getBaseXml(XmlElement parentElement) {
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
		return selectText;
	}

	private void addXmlLogicDeleteByIds(XmlElement parentElement) {
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

	private void addXmlDeleteByIds(XmlElement parentElement) {
		XmlElement deleteByIdsElement = new XmlElement("delete");
		deleteByIdsElement.addAttribute(new Attribute("id", "deleteByIds"));
		deleteByIdsElement.addAttribute(new Attribute("parameterType", "java.util.List"));

		deleteByIdsElement.addElement(
				new TextElement(
						"delete from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " where id in \n"
								+ "\t<foreach item=\"item\" index=\"index\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> "
				));

		parentElement.addElement(deleteByIdsElement);
	}

	private void addXmlSelectByIds(XmlElement parentElement) {
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
	}

	private void addXmlLogicDeleteById(XmlElement parentElement) {
		XmlElement deleteLogicByIdElement = new XmlElement("update");
		deleteLogicByIdElement.addAttribute(new Attribute("id", "deleteLogicById"));
		deleteLogicByIdElement.addAttribute(new Attribute("parameterType", "java.lang.Integer"));

		deleteLogicByIdElement.addElement(
				new TextElement(
						"update " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " set deleted = 1 where id = #{id} "
				));

		parentElement.addElement(deleteLogicByIdElement);
	}

	private void addXmlFind(XmlElement parentElement, TextElement selectText, XmlElement include) {
		// 增加find
		XmlElement find = new XmlElement("select");
		find.addAttribute(new Attribute("id", "find"));
		find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		find.addElement(selectText);
		find.addElement(include);
		parentElement.addElement(find);
	}

	private void addXmlSelectByCondition(XmlElement parentElement, TextElement selectText, XmlElement include) {
		// 增加find
		XmlElement find = new XmlElement("select");
		find.addAttribute(new Attribute("id", "selectByCondition"));
		find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

		find.addElement(selectText);
		find.addElement(include);
		parentElement.addElement(find);
	}

	private void addXmlBatchInsert(XmlElement parentElement) {

		// 增加find
		XmlElement batchInsert = new XmlElement("insert");
		batchInsert.addAttribute(new Attribute("id", "batchInsert"));
		batchInsert.addAttribute(new Attribute("parameterType", "java.util.List"));
		StringBuilder insertClause = new StringBuilder();
		StringBuilder valuesClause = new StringBuilder();
		insertClause.append("insert into ");
		insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		insertClause.append(" (");
		valuesClause.append(" (");

		XmlElement foreachElement = new XmlElement("foreach"); //设置foreach标签
		foreachElement.addAttribute(new Attribute("collection", "list"));
		foreachElement.addAttribute(new Attribute("item", "item"));
		foreachElement.addAttribute(new Attribute("separator", ","));

		List<String> valuesClauses = new ArrayList<>();
		List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
		for(int i = 0; i < columns.size(); i++) {
			IntrospectedColumn introspectedColumn = columns.get(i);
			insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
			String str1 = parameterClause.substring(0, parameterClause.indexOf('{') + 1);
			String str2 = parameterClause.substring(parameterClause.indexOf('{') + 1);
			valuesClause.append(str1).append("item.").append(str2);

			if(i + 1 < columns.size()) {
				insertClause.append(", ");
				valuesClause.append(", ");
			}

			if (valuesClause.length() > 80) {
				batchInsert.addElement(new TextElement(insertClause.toString()));
				insertClause.setLength(0);
				OutputUtilities.xmlIndent(insertClause, 1);

				valuesClauses.add(valuesClause.toString());
				valuesClause.setLength(0);
				OutputUtilities.xmlIndent(valuesClause, 1);
			}
		}

		insertClause.append(") values ");
		valuesClause.append(')');

		valuesClauses.add(valuesClause.toString());
		for (String clause : valuesClauses) {
			foreachElement.addElement(new TextElement(clause));
		}
		batchInsert.addElement(new TextElement(insertClause.toString()));
		batchInsert.addElement(foreachElement);
		parentElement.addElement(batchInsert);

	}

}
