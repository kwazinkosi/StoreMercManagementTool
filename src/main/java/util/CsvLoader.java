package util;

import model.TShirt;
import service.IDataReader;
import exception.DataReaderException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.stereotype.Component;

@Component
public class CsvLoader implements IDataReader {
//    private static final Logger logger = LoggerFactory.getLogger(CsvLoader.class);
    private static final int MAX_THREADS = 5;
    private static final long SHUTDOWN_TIMEOUT = 5;

    private final CsvParser parser;

    
    public CsvLoader() {
        this.parser = new CsvParser();
    }

    @Override
    public List<TShirt> readData(String[] filePaths) {
        
    	List<TShirt> allTShirts = new ArrayList<>();
        int numThreads = Math.min(filePaths.length, MAX_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        try {
            List<Future<List<TShirt>>> futures = submitParsingTasks(filePaths, executorService);
            collectResults(allTShirts, futures);
        } catch (DataReaderException e) {
//            logger.error("Error reading data:", e);
            throw e; // Propagate custom exceptions
        } catch (Exception e) {
//            logger.error("Unexpected error during data reading", e);
            throw new DataReaderException("Unexpected error during data reading", e,
                    DataReaderException.ErrorType.GENERAL_ERROR);
        } finally {
            shutdownExecutorService(executorService);
        }

        return allTShirts;
    }

    private List<Future<List<TShirt>>> submitParsingTasks(String[] filePaths, ExecutorService executorService) throws Exception {
        List<Future<List<TShirt>>> futures = new ArrayList<>();

        for (String filePath : filePaths) {
            Path path = Paths.get(filePath);
            if (!Files.exists(path) || !Files.isReadable(path)) {
//                logger.error("File not found or not readable: {}", filePath);
                throw DataReaderException.fileNotFound(filePath);
            }

            File file = path.toFile();
            // This just submits the task to the executor service, it doesn't start the task yet
            futures.add(executorService.submit(() -> {
                try {
                    return parser.parseCsv(file);
                } catch (Exception e) {
                    throw DataReaderException.parsingError(filePath, e);
                }
            }));
        }

        return futures;
    }

    private void collectResults(List<TShirt> allTShirts, List<Future<List<TShirt>>> futures) {
        for (Future<List<TShirt>> future : futures) {
            try {
                allTShirts.addAll(future.get()); // Collect results from completed tasks
            } catch (ExecutionException e) {
                // Log specific parsing errors
                if (e.getCause() instanceof DataReaderException) {
//                    logger.error(e.getCause().getMessage());
                } else {
//                    logger.error("Unexpected error processing file", e);
                    throw new DataReaderException("Unexpected error during file processing", e,
                            DataReaderException.ErrorType.GENERAL_ERROR);
                }
            } catch (InterruptedException e) {
//                logger.error("File processing interrupted", e);
                Thread.currentThread().interrupt();
                throw DataReaderException.threadInterrupted(e.getMessage());
            }
        }
    }

    private void shutdownExecutorService(ExecutorService executorService) {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
//                logger.warn("Forcing executor service shutdown");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
//            logger.error("Executor service shutdown interrupted", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}