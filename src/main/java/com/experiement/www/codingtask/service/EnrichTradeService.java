package com.experiement.www.codingtask.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class EnrichTradeService {
    private static final Logger logger = LoggerFactory.getLogger(EnrichTradeService.class);
    private static final String DELIMITER = ",";

    public EnrichTradeService() {}

    public OutputStream Enrich(byte[] tradeFile, OutputStream response) throws InterruptedException, IOException {
        HashMap<Integer, String> IDProductMap = this.ReadProductFile();
        
        return this.ReplaceProductID(tradeFile, IDProductMap, response);
    }

    private OutputStream ReplaceProductID(byte[] file, HashMap<Integer, String> productMap, OutputStream response) throws IOException {
        OutputStream out = response;
        CSVPrinter csvPrinter;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(file)));
            String line = null;
            if ((line = reader.readLine()) != null) {
                csvPrinter = new CSVPrinter(new PrintWriter(out), 
                    CSVFormat.DEFAULT.withHeader(line.split(EnrichTradeService.DELIMITER)));
            } else {
                csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
            }
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(EnrichTradeService.DELIMITER);
                try {
                    // validate date string format
                    if (GenericValidator.isDate(values[0], "yyyyMMDD", true)) {
                        // replace product id
                        values[1] = productMap.get(Integer.parseInt(values[1]));
                        if (values[1] == null) {
                            values[1] = "Missing Product Name";
                            logger.warn("product name unavailable, replaced with \"Missing Product Name\"");
                        }
                        // remove trailing zeros for the numbers
                        BigDecimal bd = new BigDecimal(values[3]).stripTrailingZeros();
                        values[3] = bd.toPlainString();

                        csvPrinter.printRecord(values[0], values[1], values[2], values[3]);
                        csvPrinter.flush();
                        
                    } else {
                        logger.warn("record with invalid date, discarded");
                        continue;
                    }
                } catch (NumberFormatException nfe) {
                    logger.warn("record with invalid number, discarded: ", nfe);
                    continue;
                }
            }
            if (csvPrinter != null) {
                csvPrinter.flush();
            }
            return out;

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    private HashMap<Integer, String> ReadProductFile() {
        Path productFile = Paths.get("files/product.csv");
        List<String[]> records = new ArrayList<String[]>();
        if (Files.exists(productFile)) {
            try {
                CSVReader csvReader = new CSVReaderBuilder(new FileReader(productFile.toFile())).withSkipLines(1).build();
                String[] values = null;
                while ((values = csvReader.readNext()) != null) {
                    records.add(values);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        HashMap<Integer, String> IDProductMap = new HashMap<Integer, String>();
        for (String[] product : records) {
            IDProductMap.put(Integer.parseInt(product[0]), product[1]);
        }

        return IDProductMap;
    }
}
