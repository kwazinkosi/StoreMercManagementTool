package service;

import java.util.List;

import model.TShirt;


public interface IDataReader {
    List<TShirt> readData(String[] sources);
}