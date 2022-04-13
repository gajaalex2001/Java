package repository;

import model.Proba;

public interface ProbaRepository extends repository.Repository<Integer, Proba> {
    boolean addResult(int idproba, int idpart, int scor);
}
