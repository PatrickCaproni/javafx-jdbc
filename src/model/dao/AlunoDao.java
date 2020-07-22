package model.dao;

import java.util.List;

import model.entities.Aluno;

public interface AlunoDao {

	void insert(Aluno obj);
	void update(Aluno obj);
	void deleteByMatricula(Integer matricula);
	Aluno findByMatricula(Integer matricula);
	List<Aluno> findAll();
}
