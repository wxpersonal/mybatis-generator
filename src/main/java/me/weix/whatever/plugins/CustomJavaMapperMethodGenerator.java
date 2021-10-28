package me.weix.whatever.plugins;

import org.apache.ibatis.annotations.Param;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

public class CustomJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

	@Override
	public void addInterfaceElements(Interface interfaze) {
		interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
		interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
//		addInterfaceFind(interfaze);
//		addInterfaceDeleteLogicById(interfaze);
//		addInterfaceDeleteLogicByIds(interfaze);
		addInterfaceSelectByIds(interfaze);
		addInterfaceSelectByCondition(interfaze);
//		addInterfacePageList(interfaze);
		addInterfaceDeleteByIds(interfaze);
		addInterfaceBatchInsert(interfaze);
	}

	private void addInterfaceBatchInsert(Interface interfaze) {
		// 先创建import对象
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
		// 添加List的包
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
		// 创建方法对象
		Method method = new Method("batchInsert");
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import参数类型对象
		importedTypes.add(parameterType);
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(new FullyQualifiedJavaType("List<"+parameterType.getShortName()+">"), "records", "@Param(\"records\")"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}
	
	private void addInterfaceFind(Interface interfaze) {
		// 先创建import对象
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
		// 添加List的包
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
		// 创建方法对象
		Method method = new Method("find");
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import参数类型对象
		importedTypes.add(parameterType);
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$
		//
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

	/*
	 * Dao中添加方法
	 */
	private void addInterfaceDeleteLogicByIds(Interface interfaze) {
		// 先创建import对象
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		Method method = new Method("deleteLogicByIds");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer[]"), "ids", "@Param(\"ids\")"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import参数类型对象
		importedTypes.add(parameterType);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

	/*
	 * Dao中添加方法
	 */
	private void addInterfaceDeleteByIds(Interface interfaze) {
		// 先创建import对象
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
		Method method = new Method("deleteByIds");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer[]"), "ids", "@Param(\"ids\")"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import参数类型对象
		importedTypes.add(parameterType);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceDeleteLogicById(Interface interfaze) {

		Method method = new Method("deleteLogicById");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"), "id"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}

	private void addInterfaceSelectByIds(Interface interfaze) {


		Method method = new Method("selectByIds");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(new FullyQualifiedJavaType("List<Integer>"), "ids", "@Param(\"ids\")"));
		method.setReturnType(new FullyQualifiedJavaType("java.util.List<"+ introspectedTable.getTableConfiguration().getDomainObjectName()+">"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addMethod(method);
		}
	}
	
	private void addInterfaceSelectByCondition(Interface interfaze) {
		// 先创建import对象
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
		// 添加Lsit的包
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
		FullyQualifiedJavaType listType;
		// 设置List的类型是实体类的对象
		listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		importedTypes.add(listType);
		// 返回类型对象设置为List
		returnType.addTypeArgument(listType);
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("selectByCondition");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import参数类型对象
		importedTypes.add(parameterType);
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$
		//
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}
//
	private void addInterfacePageList(Interface interfaze) {
		// 先创建import对象
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
		// 添加Page的包
		importedTypes.add(new FullyQualifiedJavaType("com.github.pagehelper.Page"));
		// 创建方法对象
		Method method = new Method();
		// 设置该方法为public
		method.setVisibility(JavaVisibility.PUBLIC);
		// 设置返回类型是List
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Page");
		FullyQualifiedJavaType listType;
		// 设置List的类型是实体类的对象
		listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		importedTypes.add(listType);
		// 返回类型对象设置为List
		returnType.addTypeArgument(listType);
		// 方法对象设置返回类型对象
		method.setReturnType(returnType);
		// 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
		method.setName("pageList");

		// 设置参数类型是对象
		FullyQualifiedJavaType parameterType;
		parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import参数类型对象
		importedTypes.add(parameterType);
		// 为方法添加参数，变量名称record
		method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$
		//
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

}
