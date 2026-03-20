package com.knowledge.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dean
 * @date 2026/3/20
 * @description
 */
public class PgVectorTypeHandler extends BaseTypeHandler<List<Float>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Float> parameter, JdbcType jdbcType) throws SQLException {
        // 将 List<Float> 转换为 pgvector 需要的字符串格式 "[0.1,0.2,...]"
        // 或者利用 PGobject
        PGobject pGobject = new PGobject();
        pGobject.setType("vector");
        // 格式化：[0.1, 0.2, 0.3]
        String value = parameter.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
        pGobject.setValue(value);
        ps.setObject(i, pGobject);
    }

    @Override
    public List<Float> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseVector(rs.getString(columnName));
    }

    @Override
    public List<Float> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseVector(rs.getString(columnIndex));
    }

    @Override
    public List<Float> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseVector(cs.getString(columnIndex));
    }

    private List<Float> parseVector(String value) {
        if (value == null || value.isEmpty()) return null;
        // 去掉方括号并分割
        String clean = value.replace("[", "").replace("]", "");
        if (clean.isEmpty()) {
            return Collections.emptyList();
        }
        return java.util.Arrays.stream(clean.split(","))
                .map(Float::parseFloat)
                .collect(Collectors.toList());
    }
}
