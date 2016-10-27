package com.netease.hivetools;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.sun.codemodel.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzliuxun on 16/10/20.
 */
public class SchemaToMetaBean {
	private static final Logger logger = Logger.getLogger(SchemaToMetaBean.class.getName());

	public static void main(String[] args) {

		String fileContext = readSchemaFile(System.getProperty("user.dir") + "/src/main/resources/hive-schema.sql");

		List<String> createStatement = new ArrayList<>();

		String regex = "CREATE TABLE(\\d+)/(\\d+)ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		regex = "CREATE TABLE.*?ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fileContext);
		while (matcher.find()) {
			String statement  = matcher.group();
			createStatement.add(statement);
			schemaToJavaBean(statement);
		}
	}

	static String readSchemaFile(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuffer sbFileContext = new StringBuffer("");
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sbFileContext.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return sbFileContext.toString();
	}

	static public void schemaToJavaBean(String statement) {
		try {
			String result = SQLUtils.format(statement, JdbcConstants.MYSQL);
			logger.info(result);
			List<SQLStatement> stmtList = SQLUtils.parseStatements(statement, JdbcConstants.MYSQL);
			for (int i = 0; i < stmtList.size(); i++) {
				SQLStatement sqlStatement = stmtList.get(i);

				MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
				sqlStatement.accept(visitor);

				MySqlCreateTableStatement mySqlCreateTableStatement = (MySqlCreateTableStatement)sqlStatement;

				String tableName = mySqlCreateTableStatement.getTableSource().toString().toUpperCase().replaceAll("`", "");
				tableName = formatTableColumnName(tableName, true);

				JCodeModel jCodeModel = new JCodeModel();
				File destDir = new File(System.getProperty("user.dir") + "/src/main/java/");
				JPackage jPackage = jCodeModel._package("com.netease.hivetools.meta");
				JDefinedClass jDefinedClass = jPackage._class(JMod.PUBLIC, tableName, ClassType.CLASS);

				// init 方法
				JMethod initMethod = jDefinedClass.method(JMod.PUBLIC, jCodeModel.VOID, tableName);

				ArrayList<SQLTableElement> tableElementList = (ArrayList<SQLTableElement>)mySqlCreateTableStatement.getTableElementList();
				for (SQLTableElement element : tableElementList) {
					if (!(element instanceof SQLColumnDefinition)) {
						continue;
					}

					String eleName = ((SQLColumnDefinition)element).getName().toString().replaceAll("`", "").toLowerCase();
					String colName = formatTableColumnName(eleName, false);
					String ColName = formatTableColumnName(eleName, true);
					SQLDataType colDataType = ((SQLColumnDefinition)element).getDataType();

					Class dataTypeClass = null;
					if (true == colDataType.getName().equals("string")
							|| true == colDataType.getName().equals("varchar")
							|| true == colDataType.getName().equals("mediumtext")
							|| true == colDataType.getName().equals("text")
							|| true == colDataType.getName().equals("char")) {
//						jPrimitiveType = jCodeModel.BYTE;;//new JPrimitiveType(jCodeModel, "string", String.class);
						dataTypeClass = String.class;
					} else if(true == colDataType.getName().equals("blob")) {
						dataTypeClass = Blob.class;
					} else if(true == colDataType.getName().equals("int")) {
						dataTypeClass = Long.class;
					} else if(true == colDataType.getName().equals("tinyint")
							|| true == colDataType.getName().equals("mediumint")
							|| true == colDataType.getName().equals("smallint")) {
						dataTypeClass = Integer.class;
					} else if(true == colDataType.getName().equals("bit")) {
						dataTypeClass = Boolean.class;
					} else if (true == colDataType.getName().equals("bigint")) {
						dataTypeClass = Long.class;
					} else if(true == colDataType.getName().equals("float")) {
						dataTypeClass = Float.class;
					}  else if(true == colDataType.getName().equals("double")) {
						dataTypeClass = Double.class;
					} else {
						System.out.println("unknown data type : " + colDataType.toString());
						continue;
					}

					// 字段定义
					JFieldVar jFieldVar = jDefinedClass.field(JMod.PRIVATE, dataTypeClass, eleName);

					// set方法
					JMethod setMethod = jDefinedClass.method(JMod.PUBLIC, jCodeModel.VOID, "set" + ColName);
					setMethod.param(dataTypeClass, colName+"_");
					JBlock setBlock = setMethod.body();
					JFieldRef setFieldRef = JExpr.ref(colName+"_");
					setBlock.assign(jFieldVar, setFieldRef);

					// get方法
					JMethod getMethod = jDefinedClass.method(JMod.PUBLIC, dataTypeClass, "get" + ColName);
					JBlock getBlock = getMethod.body();
					JFieldRef getFieldRef = JExpr.ref(eleName);
					getBlock._return(getFieldRef);
				}
				jCodeModel.build(destDir);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static public String formatTableColumnName(String name, boolean firstUpper) {
		// 格式化字段名
		String[] names = name.split("_");
		String newName = "";
		int numNames = names.length;

		if (numNames < 2) {
			if (false == firstUpper) {
				newName = name.toLowerCase();
			} else {
				String firstChar = name.substring(0, 1);

				String tmp = name.substring(1, name.length()).toLowerCase();
				newName = firstChar.toUpperCase() + tmp;
			}
		} else {
			for (int n = 0; n < names.length; n++) {
				String tmp = names[n];
				tmp = tmp.toLowerCase();

				if (false == firstUpper && n == 0) {
					newName = newName + tmp;
					continue;
				}
				int len = tmp.length();
				String firstChar = tmp.substring(0, 1);
				newName = newName + firstChar.toUpperCase() + tmp.substring(1, len);
			}
		}

		return newName;
	}
}
