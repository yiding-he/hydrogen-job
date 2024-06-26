package com.hyd.job.server.sql;


import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.springframework.util.StringUtils.hasText;

/**
 * 用于动态的生成 SQL 语句
 * 本类中的有些方法命名以大写字母开头，是为了避免与关键字冲突
 *
 * @author yiding.he
 */
@SuppressWarnings({
  "unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue", "LombokGetterMayBeUsed", "unchecked"
})
public abstract class Sql<T extends Sql<?>> {

    public static final Object NULL = new Object();

    private Sql() {

    }

    private static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }

        String str = obj.toString();
        return !hasText(str);
    }

    public abstract SqlCommand toCommand();

    protected String table;

    protected String statement;

    protected List<Object> params = new ArrayList<>();

    protected List<Pair> conditions = new ArrayList<>();

    protected List<Join> joins = new ArrayList<>();

    protected String suffix;

    public String getSql() {
        return toCommand().getStatement();
    }

    public String getTable() {
        return table;
    }

    public List<Object> getParams() {
        return params;
    }

    public boolean hasConditions() {
        return !conditions.isEmpty();
    }

    public boolean hasParams() {
        return !params.isEmpty();
    }

    // sample: .LeftJoin("t2 on t2.parent_id=t1.id and t2.deleted=?", 0)
    public T LeftJoin(String statement, Object... params) {
        this.joins.add(new Join(JoinType.LeftJoin, statement, params));
        return (T) this;
    }

    // sample: .RightJoin("t2 on t2.parent_id=t1.id and t2.deleted=?", 0)
    public T RightJoin(String statement, Object... params) {
        this.joins.add(new Join(JoinType.RightJoin, statement, params));
        return (T) this;
    }

    public T InnerJoin(String statement, Object... params) {
        this.joins.add(new Join(JoinType.InnerJoin, statement, params));
        return (T) this;
    }

    public T OuterJoin(String statement, Object... params) {
        this.joins.add(new Join(JoinType.OuterJoin, statement, params));
        return (T) this;
    }

    public T Where(String statement) {
        if (this instanceof Insert) {
            throw new IllegalStateException("cannot use 'where' block in Insert");
        }
        this.conditions.add(new Pair(Joint.AND, statement));
        return (T) this;
    }

    public T Where(String statement, Object... args) {
        if (this instanceof Insert) {
            throw new IllegalStateException("cannot use 'where' block in Insert");
        }
        this.conditions.add(new Pair(Joint.AND, statement, args));
        return (T) this;
    }

    public T Where(String statement, Sql<T> child) {
        return Where(true, statement, child);
    }

    public T Where(boolean exp, String statement) {
        if (this instanceof Insert) {
            throw new IllegalStateException("cannot use 'where' block in Insert");
        }
        if (exp) {
            this.conditions.add(new Pair(Joint.AND, statement));
        }
        return (T) this;
    }

    public T Where(boolean exp, String statement, Object... args) {
        if (this instanceof Insert) {
            throw new IllegalStateException("cannot use 'where' block in Insert");
        }
        if (exp) {
            this.conditions.add(new Pair(Joint.AND, statement, args));
        }
        return (T) this;
    }

    // for sql like 'select * from t1 where id in (select ...)'
    public T Where(boolean exp, String statement, Sql<T> child) {
        if (this instanceof Insert) {
            throw new IllegalStateException("cannot use 'where' block in Insert");
        }
        if (exp) {
            SqlCommand childCmd = child.toCommand();
            this.conditions.add(new Pair(Joint.AND, statement + "(" + childCmd.getStatement() + ")", childCmd.getParams()));
        }
        return (T) this;
    }

    public T And(String statement) {
        this.conditions.add(new Pair(Joint.AND, statement));
        return (T) this;
    }

    public T And(String statement, Object... args) {
        this.conditions.add(new Pair(Joint.AND, statement, args));
        return (T) this;
    }

    // for sql like 'select * from t1 where id in (select...)'
    public T And(String statement, Sql<T> child) {
        SqlCommand childCmd = child.toCommand();
        this.conditions.add(new Pair(Joint.AND, statement + "(" + childCmd.getStatement() + ")", childCmd.getParams()));
        return (T) this;
    }

    // if exp is false, remove the last condition
    public T If(boolean exp) {
        if (!exp && !this.conditions.isEmpty()) {
            this.conditions.remove(this.conditions.size() - 1);
        }
        return (T) this;
    }

    protected String generateJoinBlock() {
        StringBuilder joinBlock = new StringBuilder();
        for (Join join : joins) {
            joinBlock.append(join.type.getCode()).append(join.statement);
            this.params.addAll(join.params);
        }
        return joinBlock.toString();
    }

    protected String generateWhereBlock() {
        String where = "";

        if (!this.conditions.isEmpty()) {
            where = "where ";

            for (int i = 0, conditionsSize = conditions.size(); i < conditionsSize; i++) {
                Pair condition = conditions.get(i);
                where = processCondition(i, where, condition);
            }

        }

        return " " + where;
    }

    private String processCondition(int index, String where, Pair condition) {
        where = where.trim();

        // 第一个条件不能加 and 和 or 前缀
        if (index > 0 && !where.endsWith("(")) {
            where += (condition.joint == null ? "" : (" " + condition.joint.name() + " "));
        }

        where += " ";

        if (!condition.hasArg()) {       // 不带参数的条件
            where += condition.statement;

        } else if (condition.args.size() == 1 &&
          condition.firstArg() instanceof List<?> objects) {   // 参数为 List 的条件（即 in 条件）

          // marks = "(?,?,?,...,?)"
            String marks = "(" +
                           objects.stream()
                               .map(o -> {
                                   this.params.add(o);
                                   return "?";
                               })
                               .collect(Collectors.joining(",")) +
                           ")";

            // "A in ?" -> "A in (?,?,?)"
            where += condition.statement.replace("?", marks);

        } else if (condition.statement.endsWith("in ?")) {

            // marks = "(?,?,?,...,?)"
            String marks = "(" +
                           condition.args.stream()
                               .map(o -> {
                                   this.params.add(o);
                                   return "?";
                               }).collect(Collectors.joining(",")) +
                           ")";

            // "A in ?" -> "A in (?,?,?)"
            where += condition.statement.replace("?", marks);

        } else {
            where += condition.statement;
            this.params.addAll(condition.args);
        }

        return where;
    }

    /////////////////////////////////////////////////////////

    public static Select Select(String columns) {
        return new Select(columns);
    }

    public static Select Select(String... columns) {
        return new Select(columns);
    }

    public static Update Update(String table) {
        return new Update(table);
    }

    public static Insert Insert(String table) {
        return new Insert(table);
    }

    public static Delete Delete(String table) {
        return new Delete(table);
    }

    /////////////////////////////////////////////////////////

    public enum Joint {
        AND, OR
    }

    public enum JoinType {
        InnerJoin(" INNER JOIN "), OuterJoin(" OUTER JOIN "), LeftJoin(" LEFT JOIN "), RightJoin(" RIGHT JOIN ");

        private final String code;

        JoinType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static class Pair {

        private Joint joint = Joint.AND;  // AND/OR/null

        private final String statement;

        private List<Object> args;

        public Pair(String statement) {
            this.statement = statement;
        }

        public Pair(Joint joint, String statement) {
            this(joint, statement, (Object[]) null);
        }

        public Pair(String statement, Object... args) {
            this(null, statement, args);
        }

        public Pair(Joint joint, String statement, Object... args) {
            this.joint = joint;
            this.statement = statement.trim();
            this.args = args == null ? emptyList() : Arrays.asList(args);
        }

        public Pair(Joint joint, String statement, List<Object> args) {
            this.joint = joint;
            this.statement = statement.trim();
            this.args = args;
        }

        public Object firstArg() {
            return this.args == null || this.args.isEmpty() ? null : this.args.get(0);
        }

        public boolean hasArg() {
            return this.args != null && !this.args.isEmpty();
        }

        protected static String joinPairName(List<Pair> pairs) {
            if (pairs.isEmpty()) {
                return "";
            } else {
                StringBuilder result = new StringBuilder();
                for (Pair pair : pairs) {
                    result.append(pair.statement).append(",");
                }
                result = new StringBuilder(result.substring(0, result.length() - 1));
                return result.toString();
            }
        }

        protected static String joinPairHolder(List<Pair> pairs) {
            StringBuilder s = new StringBuilder();
            for (int size = pairs.size(), i = 0; i < size; i++) {
                s.append("?").append(i == size - 1 ? "" : ",");
            }
            return s.toString();
        }

        protected static List<Object> joinPairValue(List<Pair> pairs) {
            if (pairs.isEmpty()) {
                return emptyList();
            }

            List<Object> result = new ArrayList<>();
            for (Pair pair : pairs) {
                result.addAll(pair.args);
            }

            return result;
        }
    }

    public static class Join {

        public final JoinType type;

        public final String statement;

        public final List<Object> params;

        public Join(JoinType type, String statement) {
            this(type, statement, emptyList());
        }

        public Join(JoinType type, String statement, Object... params) {
            this.type = type;
            this.statement = statement;
            this.params = Arrays.asList(params);
        }
    }

    /////////////////////////////////////////////////////////

    public static class Insert extends Sql<Insert> {

        private final List<Pair> pairs = new ArrayList<>();

        private boolean onDuplicateKeyUpdate = false;

        private boolean onDuplicateKeyIgnore = false;

        private List<String> duplicateKeyUpdateColumns = emptyList();

        public Insert(String table) {
            this.table = table;
        }

        public Insert Values(String column, Object value) {
            return Values(!isEmpty(value), column, value);
        }

        public Insert Values(boolean ifTrue, String column, Object value) {
            if (ifTrue) {
                pairs.add(new Pair(column, value));
            }
            return this;
        }

        public Insert Values(Map<String, Object> map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Values(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Insert OnDuplicateKeyUpdate(String... updateColumns) {
            this.onDuplicateKeyUpdate = true;
            this.duplicateKeyUpdateColumns = List.of(updateColumns);
            this.suffix = hasText(this.suffix) ? this.suffix : "";
            this.suffix += Stream.of(updateColumns).map(c -> c + "=?").collect(Collectors.joining(","));
            return this;
        }

        public Insert OnDuplicateKeyIgnore() {
            this.onDuplicateKeyIgnore = true;
            return this;
        }

        @Override
        public SqlCommand toCommand() {
            this.statement =
                "insert " +
                (onDuplicateKeyIgnore ? "ignore " : "") +
                "into " + table +
                "(" + Pair.joinPairName(pairs) + ") values " +
                "(" + Pair.joinPairHolder(pairs) + ")" +
                (onDuplicateKeyUpdate ? " on duplicate key update " : "") +
                (suffix == null ? "" : suffix);

            params = Pair.joinPairValue(pairs);
            if (onDuplicateKeyUpdate) {
                var pairMap = new HashMap<String, Object>();
                pairs.forEach(p -> pairMap.put(p.statement, p.firstArg()));
                duplicateKeyUpdateColumns.forEach(c -> params.add(pairMap.get(c)));
            }

            return new SqlCommand(statement, params);
        }
    }

    /////////////////////////////////////////////////////////

    /**
     * 用于生成 update 语句的帮助类
     */
    @SuppressWarnings({"StringConcatenationInLoop", "unused"})
    public static class Update extends Sql<Update> {

        private final List<Pair> updates = new ArrayList<>();

        public Update(String table) {
            this.table = table;
        }

        public List<Pair> getUpdates() {
            return updates;
        }

        @Override
        public SqlCommand toCommand() {
            this.params.clear();
            this.statement = "update " + table +
                             " set " + generateSetBlock() + " " + generateWhereBlock();

            return new SqlCommand(this.statement, this.params);
        }

        private String generateSetBlock() {
            String statement = "";

            for (int i = 0, updatesSize = updates.size(); i < updatesSize; i++) {
                Pair pair = updates.get(i);
                if (!pair.hasArg()) {
                    statement += pair.statement;
                } else if (pair.args.get(0) == NULL) {
                    statement += pair.statement + "=null";
                } else if (pair.statement.contains("?")) {
                    this.params.addAll(pair.args);
                    statement += pair.statement;
                } else {
                    this.params.addAll(pair.args);
                    statement += pair.statement + "=?";
                }

                if (i < updatesSize - 1) {
                    statement += ",";
                }
            }

            return statement;
        }

        public Update Set(boolean exp, String column, Object value) {
            if (exp) {
                this.updates.add(new Pair(column, value));
            }
            return this;
        }

        public Update Set(String column, Object value) {
            this.updates.add(new Pair(column, value));
            return this;
        }

        public Update Set(String setStatement) {
            this.updates.add(new Pair(setStatement));
            return this;
        }

        public Update Set(boolean exp, String setStatement) {
            if (exp) {
                this.updates.add(new Pair(setStatement));
            }
            return this;
        }

        public Update SetIfNotNull(String column, Object value) {
            return Set(value != null, column, value);
        }

        public Update SetIfNotEmpty(String column, Object value) {
            return Set(!isEmpty(value), column, value);
        }
    }

    /////////////////////////////////////////////////////////

    /**
     * 用于生成 select 语句的帮助类
     */
    public static class Select extends Sql<Select> {

        private String columns;

        private String from;

        private String orderBy;

        private String groupBy;

        private long offset = -1;

        private long limit = -1;

        public Select(String columns) {
            this.columns = columns;
        }

        public Select(String... columns) {
            this.columns = String.join(",", columns);
        }

        public Select Columns(String... columns) {
            this.columns = String.join(",", columns);
            return this;
        }

        public Select From(String from) {
            this.from = from;
            return this;
        }

        public Select From(String... from) {
            this.from = String.join(",", from);
            return this;
        }

        public Select OrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public Select GroupBy(String groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        public Select Offset(long offset) {
            this.offset = offset;
            return this;
        }

        public Select Limit(long limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public SqlCommand toCommand() {
            this.params.clear();
            this.statement = "select " + this.columns + " from " + this.from + " ";
            this.statement += generateJoinBlock();
            this.statement += generateWhereBlock();
            this.statement += generateGroupBy();
            this.statement += generateOrderBy();
            this.statement += this.limit > 0 ? (" limit " + this.limit + " ") : "";
            this.statement += this.offset > 0 ? (" offset " + this.offset + " ") : "";
            return new SqlCommand(this.statement, this.params);
        }

        private String generateGroupBy() {
            return isEmpty(this.groupBy) ? "" : (" group by " + this.groupBy);
        }

        private String generateOrderBy() {
            return isEmpty(this.orderBy) ? "" : (" order by " + this.orderBy);
        }
    }

    /////////////////////////////////////////////////////////

    public static class Delete extends Sql<Delete> {

        public Delete(String table) {
            this.table = table;
        }

        @Override
        public SqlCommand toCommand() {
            this.params.clear();
            this.statement = "delete from " + table + generateWhereBlock();
            return new SqlCommand(this.statement, this.params);
        }
    }
}


