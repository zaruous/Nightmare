package etc;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class SQLNOLOCKAdderBuilder {

	public String addNOLOCKToTables(String sql) {
		String tablePattern = "\\b(?:FROM|JOIN)\\s+([\\w\\.]+)";
		Pattern pattern = Pattern.compile(tablePattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);

		List<String> tables = new ArrayList<>();

		while (matcher.find()) {
			String table = matcher.group(1);
			if (!tables.contains(table)) {
				tables.add(table);
			}
		}

		for (String table : tables) {
			String tableWithNolock = table + " WITH (NOLOCK)";
			sql = sql.replaceAll("\\b" + table + "\\b(?!\\s+WITH\\s*\\(NOLOCK\\))", tableWithNolock);
		}

		return sql;
	}

	public String processVelocitySQL(String velocitySQL) {
		String placeholder = "VELOCITY_PLACEHOLDER_";
		int placeholderCount = 0;
		List<String> velocityExpressions = new ArrayList<>();

		Pattern velocityVarPattern = Pattern.compile("\\$\\{?[\\w\\.]+\\}?|#\\w+[\\s\\S]*?#end");
		Matcher velocityVarMatcher = velocityVarPattern.matcher(velocitySQL);
		while (velocityVarMatcher.find()) {
			String velocityVar = velocityVarMatcher.group();
			velocityExpressions.add(velocityVar);
			velocitySQL = velocitySQL.replace(velocityVar, placeholder + placeholderCount);
			placeholderCount++;
		}

		String processedSQL = addNOLOCKToTables(velocitySQL);

		for (int i = 0; i < velocityExpressions.size(); i++) {
			processedSQL = processedSQL.replace(placeholder + i, velocityExpressions.get(i));
		}

		return processedSQL;
	}

	// 가상의 Velocity 엔진
	public String processVelocityTemplate(String template, Map<String, Object> params) {
		BufferedWriter writer = new BufferedWriter(new PrintWriter(new ByteArrayOutputStream()));
		var ctx = new VelocityContext(params) {
			private static final long serialVersionUID = 7234457145219251719L;

			@Override
			public Object internalGet(String key) {
				Object internalGet = super.internalGet(key);
				if (internalGet == null)
					return "";
				return internalGet;
			}
		};
		Velocity.evaluate(ctx, writer, "DaoWizard", template);
		String convetedString = writer.toString();
		return convetedString;
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            template = template.replace("$" + key, value.toString());
//            template = template.replace("${" + key + "}", value.toString());
//        }
//
//        // #if 문 처리
//        Pattern ifPattern = Pattern.compile("#if\\(\\$Utils\\.isNotEmpty\\(\\$([\\w]+)\\)\\)([\\s\\S]*?)#end");
//        Matcher ifMatcher = ifPattern.matcher(template);
//        StringBuffer sb = new StringBuffer();
//        while (ifMatcher.find()) {
//            String varName = ifMatcher.group(1);
//            String content = ifMatcher.group(2);
//            if (params.containsKey(varName) && !params.get(varName).toString().isEmpty()) {
//                ifMatcher.appendReplacement(sb, content);
//            } else {
//                ifMatcher.appendReplacement(sb, "");
//            }
//        }
//        ifMatcher.appendTail(sb);
//
//        return sb.toString();
	}

	// 가상의 SQL 실행기
	public boolean executeSQLAndVerify(String sql) {
		// 여기서는 간단히 SQL의 기본 구조만 확인합니다.
		// 실제로는 더 복잡한 검증이 필요할 수 있습니다.
		boolean hasSelect = sql.toLowerCase().contains("select");
		boolean hasFrom = sql.toLowerCase().contains("from");
		boolean hasNolock = sql.toLowerCase().contains("with (nolock)");
		return hasSelect && hasFrom && hasNolock;
	}

	HashMap<String, Object> extractVairables(String velocitySQL) {
		Pattern pattern = Pattern.compile("\\$([a-zA-Z0-9_]+)");
		Matcher matcher = pattern.matcher(velocitySQL);

		var params = new HashMap<String, Object>();
		while (matcher.find()) {
//            System.out.println(matcher.group(1));
			params.put(matcher.group(1), 1);
		}
		return params;
	}

	public class Utils {
		public static boolean isNotEmpty(String str) {
			return !str.isEmpty();
		}
	}

	private String originalSQL;
	private String processedSQL;
	private String finalSQL;
	HashMap<String, Object> vairables;

	private SQLNOLOCKAdderBuilder setSql(String sql, Map<String, Object> context) {
		this.originalSQL = sql;
		vairables = extractVairables(sql);
		this.processedSQL = processVelocitySQL(sql);

		System.out.println("Processed SQL:");
		System.out.println(processedSQL);

		// Velocity 템플릿 처리
		finalSQL = processVelocityTemplate(processedSQL, context);
		System.out.println("\nFinal SQL after Velocity processing:");
		System.out.println(finalSQL);

		return this;
	}

	private boolean isValid;

	public SQLNOLOCKAdderBuilder build() {
		isValid = executeSQLAndVerify(finalSQL);
		return this;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public static void main(String[] args) {
		SQLNOLOCKAdderBuilder builder = new SQLNOLOCKAdderBuilder();

		String velocitySQL = "select \n" + "    name,\n" + "    address,\n" + "    $lotNumber\n"
				+ "from tab1 left join tbl2\n" + "    on tab1.name = tbl2.name\n" + "where 1=1\n"
				+ "#if($Utils.isNotEmpty($name))\n" + "    and tab1.name = :name\n" + "#end\n"
				+ "#if($Utils.isNotEmpty($now))\n" + "    and tab1.date = :now\n" + "#end";

		// Velocity 컨텍스트 설정
		Map<String, Object> context = new HashMap<>();
		context.put("lotNumber", "lot12345");
		context.put("name", "John Doe");
		context.put("now", "2023-05-20");
		context.put("Utils", SQLNOLOCKAdderBuilder.Utils.class);

		builder.setSql(velocitySQL, context);

//		HashMap<String, Object> vairables = builder.extractVairables(velocitySQL);
//		System.out.println(vairables);

//		String processedSQL = builder.processVelocitySQL(velocitySQL);
//		System.out.println("Processed SQL:");
//		System.out.println(processedSQL);

		// SQL 실행 및 검증
		builder.build();
		boolean valid = builder.isValid();
		System.out.println("\nIs the SQL valid and executable? " + valid);
	}
}