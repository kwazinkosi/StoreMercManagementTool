package service;

import model.TShirt;
import repository.IRepository;
import exception.DuplicateTShirtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataImportService {

	private final IRepository repository;
	private final IDataReader dataReader;

	@Autowired
	public DataImportService(IRepository repository, IDataReader dataReader) {
		this.repository = repository;
		this.dataReader = dataReader;
	}

	@Transactional
	public void importFromCsv(String[] filePaths) {

		List<TShirt> tShirts = dataReader.readData(filePaths);
		try {
			tShirts.forEach(repository::save);
		} catch (DuplicateTShirtException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error importing data: " + e.getMessage());
		}
	}
}