import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.Random;

public class Main {
    
    private final static int NUMBER_OF_DATAPOINTS = 100000;
    private final static int NUMBER_OF_DIMENSIONS = 3;
    private final static int THRESHOLD_MIN = 650;
    private final static double FUZZY_BOARDER = 0.1;
    private final static String FILENAME = "sampledata.csv";
    
    private final Random rand = new Random();
    
    /**
     * Constructor.
     */
    public Main() {
        
    }
    
    /**
     * Creates a specific number of random values between 0 and 1000.
     * @param n Number of random values.
     * @return Array with random values.
     */
    public int [] createSampleRow(int n) {
        int [] result = new int[n];
        for (int i=0; i<n; i++) {
            result[i] = rand.nextInt(1000);
        }
        return result;
    }
    
    public String [] createHeader(int n) {
        String [] result = new String[n+1];
        String prefix = "value";
        for (int i=0; i<n; i++) {
            result[i] = prefix + "_" + i;
        }
        result[n] = "result";
        return result;
    }
    
    public void createData() throws Exception {
        
        FileWriter csvFile = new FileWriter(FILENAME);
        CSVWriter csvWriter = new CSVWriter(csvFile, ',', CSVWriter.NO_QUOTE_CHARACTER);
        
        String[] header = createHeader(NUMBER_OF_DIMENSIONS);
        csvWriter.writeNext(header);

        for (int i=0; i<NUMBER_OF_DATAPOINTS; i++) {
            int [] values = createSampleRow(NUMBER_OF_DIMENSIONS);
            boolean result = true;

            // check the threshold
            for (int j=0; j<NUMBER_OF_DIMENSIONS; j++) {
                result = result & values[j] >= THRESHOLD_MIN;
            }
            
            // insert some fuzzy borders
            int sum = 0;
            for (int j=0; j<NUMBER_OF_DIMENSIONS; j++) {
                sum += values[j];
            }
            
            int average = NUMBER_OF_DATAPOINTS * THRESHOLD_MIN;
            double min = average - average * FUZZY_BOARDER;
            double max = average + average * FUZZY_BOARDER;
            
            if (sum > min && sum < max) {
                result = rand.nextInt() % 2 == 0;
            }
            
            // create strings
            String [] valueStr = new String[NUMBER_OF_DIMENSIONS + 1];
            for (int j=0; j<NUMBER_OF_DIMENSIONS; j++) {
                valueStr[j] = String.valueOf(values[j]);
            }
            valueStr[NUMBER_OF_DIMENSIONS] = String.valueOf(result);
            
            // insert some na values
            if (rand.nextInt() % 4 == 0) {
                int index = Math.abs(rand.nextInt()) % NUMBER_OF_DIMENSIONS;
                valueStr[index] = "na";
            }

            csvWriter.writeNext(valueStr);
        }

        csvWriter.close();
        csvFile.close();
    }
    

    public static void main(String [] args) throws Exception {
        Main obj = new Main();
        obj.createData();
    }
}
