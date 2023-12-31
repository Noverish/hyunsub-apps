package kim.hyunsub.common.database

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.TypeHandler
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

open class DatabaseEnumTypeHandler<T : DatabaseEnum>(
	private val clazz: Class<T>,
) : TypeHandler<T> {
	override fun setParameter(ps: PreparedStatement, i: Int, parameter: T?, jdbcType: JdbcType?) {
		if (parameter != null) {
			ps.setString(i, parameter.value)
		} else {
			ps.setNull(i, Types.VARCHAR)
		}
	}

	override fun getResult(rs: ResultSet, columnName: String): T? {
		return getResult(rs.getString(columnName))
	}

	override fun getResult(rs: ResultSet, columnIndex: Int): T? {
		return getResult(rs.getString(columnIndex))
	}

	override fun getResult(cs: CallableStatement, columnIndex: Int): T? {
		return getResult(cs.getString(columnIndex))
	}

	private fun getResult(value: String): T? = clazz.enumConstants.firstOrNull { it.value == value }
}
