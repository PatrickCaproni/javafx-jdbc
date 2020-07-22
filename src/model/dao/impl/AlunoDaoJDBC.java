package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.AlunoDao;
import model.entities.Aluno;

public class AlunoDaoJDBC implements AlunoDao {

	private Connection conn;
	
	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO aluno "
					+ "(Nome, DataDeNascimento, Estado, Cidade, Bairro, Rua, NumeroCasa) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setDate(2, new java.sql.Date(obj.getDataDeNascimento().getTime()));
			st.setString(3, obj.getEstado());
			st.setString(4, obj.getCidade());
			st.setString(5, obj.getBairro());
			st.setString(6, obj.getRua());
			st.setInt(7, obj.getNumeroCasa());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int matricula = rs.getInt(1);
					obj.setMatricula(matricula);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE aluno "
					+ "SET Nome = ?, DataDeNascimento = ?, Estado = ?, Cidade = ?, Bairro = ?, Rua = ?, NumeroCasa = ? "
					+ "WHERE Matricula = ?");
			
			st.setString(1, obj.getNome());
			st.setDate(2, new java.sql.Date(obj.getDataDeNascimento().getTime()));
			st.setString(3, obj.getEstado());
			st.setString(4, obj.getCidade());
			st.setString(5, obj.getBairro());
			st.setString(6, obj.getRua());
			st.setInt(7, obj.getNumeroCasa());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteByMatricula(Integer matricula) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM aluno WHERE Id = ?");
			
			st.setInt(1, matricula);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Aluno findByMatricula(Integer matricula) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM Aluno WHERE Matricula = ?");
			
			st.setInt(1, matricula);
			rs = st.executeQuery();
			if (rs.next()) {
				Aluno obj = new Aluno();
				obj.setMatricula(rs.getInt("Matricula"));
				obj.setNome(rs.getString("Nome"));
				
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

//	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
//		Seller obj = new Seller();
//		obj.setId(rs.getInt("Id"));
//		obj.setName(rs.getString("Name"));
//		obj.setEmail(rs.getString("Email"));
//		obj.setBaseSalary(rs.getDouble("BaseSalary"));
//		obj.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
//		obj.setDepartment(dep);
//		return obj;
//	}
//
//	private Department instantiateDepartment(ResultSet rs) throws SQLException {
//		Department dep = new Department();
//		dep.setId(rs.getInt("DepartmentId"));
//		dep.setName(rs.getString("DepName"));
//		return dep;
//	}

	@Override
	public List<Aluno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM Aluno ORDER BY Nome");
			
			rs = st.executeQuery();
			
			List<Aluno> list = new ArrayList<>();
			
			while (rs.next()) {
				Aluno obj = new Aluno();
				obj.setMatricula(rs.getInt("Matricula"));
				obj.setNome(rs.getString("Nome"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
