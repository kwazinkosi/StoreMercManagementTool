package util;

import model.TShirt;
import model.TShirt.Gender;
import model.TShirt.Size;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
	
    public List<TShirt> parseCsv(File file) throws Exception {
        
    	List<TShirt> tShirts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean headerSkipped = false;
            
            while ((line = br.readLine()) != null) {
            	
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                
                String[] values = line.split("\\|"); // split by pipe character
                TShirt tShirt = new TShirt(
                	values[0].trim(),        // id
                    values[1].trim(),        // name
                    values[2].trim().toLowerCase(),        // colour
                    Gender.valueOf(values[3].trim().toUpperCase()), // gender
                    Size.valueOf(values[4].trim().toUpperCase()),   // size
                    new BigDecimal(values[5].trim()), // price
                    Double.parseDouble(values[6].trim()), // rating
                    Boolean.valueOf(values[7].trim().equals("Y"))
                );
                tShirts.add(tShirt);
                
            }
        }
        return tShirts;
    }
}