package services;

import model.Participant;

import java.util.List;

public interface ITriatlonObserver {
    void updateReceived() throws TriatlonException;
}
