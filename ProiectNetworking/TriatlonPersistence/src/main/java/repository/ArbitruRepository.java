package repository;

import model.Arbitru;

public interface ArbitruRepository extends repository.Repository<Arbitru, Integer> {
    public Arbitru getAccount(String user, String password);
}
