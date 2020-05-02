package part1;

import csv.CSVParser;
import csv.CSVRecord;
import csv.SEFileUtil;

import java.io.File;
import java.util.List;

public class BirthStatistics {

    public final String pathToDirCSVs;

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final String FEMALE = "F";
    private static final String MALE = "M";
    private static final int ROW_OF_FEMALE_POPULAR_NAME = 1;
    private static final int NAME_NOT_FOUND_ERROR = -1;
    private static final int RECORD_NAME_INDEX = 0;
    private static final int RECORD_GENDER_INDEX = 1;
    private static final int RECORD_NUM_BORN_INDEX = 2;

    public BirthStatistics(String pathCSVs) {
        pathToDirCSVs = pathCSVs;
    }

    /**
     * This method returns the path to the CSV file of the specified year
     *
     * @param year
     * @return
     */
    private String getPathToCSV(int year) {
        File[] csvFiles = new File(pathToDirCSVs).listFiles();
        for (File csvF : csvFiles) {
            if (csvF.getName().contains(Integer.toString(year))) {
                return csvF.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * This method returns the row number in the CSV file of the most popular name by the given gender
     *
     * @param year
     * @param gender
     * @return
     */
    private int getCsvRowOfMostPopularNameByGender(int year, String gender) {
        int rank = -1;
        SEFileUtil seFileUtil = new SEFileUtil(getPathToCSV(year));
        for (CSVRecord record : seFileUtil.getCSVParser()) {
            String currGender = record.get(1);
            if (currGender.equals(gender)) {
                rank = (int) record.getRecordNumber();
                break;
            }
        }
        return rank;
    }

    public void totalBirths(int year) {
        String pathToCSV = getPathToCSV(year);
        int femaleBirthCounter = ZERO;
        int maleBirthCounter = ZERO;
        int totalBirthCounter;

        SEFileUtil seFileUtil = new SEFileUtil(pathToCSV);
        CSVParser parser = seFileUtil.getCSVParser();

        for (CSVRecord rec : parser) {
            String gender = rec.get(RECORD_GENDER_INDEX);
            int numBorn = Integer.parseInt(rec.get(RECORD_NUM_BORN_INDEX));
            if (gender.equals(FEMALE)) {
                femaleBirthCounter += numBorn;
            } else {
                maleBirthCounter += numBorn;
            }
        }

        totalBirthCounter = femaleBirthCounter + maleBirthCounter;

        System.out.println("total births = " + totalBirthCounter);
        System.out.println("female girls = " + femaleBirthCounter);
        System.out.println("male boys = " + maleBirthCounter);
    }

    public int getRank(int year, String name, String gender) {
        String pathToCSV = getPathToCSV(year);
        SEFileUtil seFileUtil = new SEFileUtil(pathToCSV);
        CSVParser parser = seFileUtil.getCSVParser();
        List<CSVRecord> recordsList = parser.getRecords();
        int malePopularNameRow = getCsvRowOfMostPopularNameByGender(year, MALE) - ONE;
        int loopStartValue = ZERO;  // Assume it's female
        if (gender.equals(MALE)) {  //
            loopStartValue = malePopularNameRow;
        }
        int loopStopValue = malePopularNameRow;
        if (gender.equals(MALE)) {
            loopStopValue = recordsList.size();
        }
        int namePopularity = NAME_NOT_FOUND_ERROR;

        for (int i = loopStartValue; i < loopStopValue && namePopularity == NAME_NOT_FOUND_ERROR; i++) {
            CSVRecord csvRecord = recordsList.get(i);
            if (csvRecord.get(RECORD_NAME_INDEX).equals(name)) {
                namePopularity = i - loopStartValue + ONE;
            }
        }
        return namePopularity;
    }


    public static void main(String[] args) {

        BirthStatistics birthStatistics = new BirthStatistics(args[0]);
//        birthStatistics.totalBirths(2010);
        int rank = birthStatistics.getRank(2012, "Mason", "M");
        System.out.println("Rank is: " + rank);
//        String name = birthStatistics.getName(2012, 10, "M");
//        System.out.println("Name: " + name);
//        System.out.println(birthStatistics.yearOfHighestRank(1880, 2014,"David", "M"));
//        System.out.println(birthStatistics.yearOfHighestRank(1880, 2014,"Jennifer", "F"));
//        System.out.println(birthStatistics.getAverageRank(1880, 2014, "Benjamin", "M"));
//        System.out.println(birthStatistics.getAverageRank(1880,2014, "Lois", "F"));
//        System.out.println(birthStatistics.getTotalBirthsRankedHigher(2014, "Draco", "M"));
//        System.out.print(birthStatistics.getTotalBirthsRankedHigher(2014, "Sophia", "F"));

        //BirthStatistics b = new BirthStatistics(args[0]);
        //System.out.print(b.getPathToCSV(1980));
    }


}
