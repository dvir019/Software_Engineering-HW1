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
    private static final String NO_NAME_ERROR = "NO NAME";

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

    /**
     * Gets a year, and prints the number of males and females that was born in that year.
     *
     * @param year The year
     */
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

    /**
     * Gets year, a name and a gender, and returns the rank of the child in that year.
     * If the child doesn't appear in that year, -1 will be returned.
     *
     * @param year The year
     * @param name The name of the child
     * @param gender The gender of the child
     * @return The rank of the child in the given year
     */
    public int getRank(int year, String name, String gender) {
        String pathToCSV = getPathToCSV(year);
        SEFileUtil seFileUtil = new SEFileUtil(pathToCSV);
        CSVParser parser = seFileUtil.getCSVParser();
        List<CSVRecord> recordsList = parser.getRecords();
        int malePopularNameIndex = getCsvRowOfMostPopularNameByGender(year, MALE) - ONE;
        int loopStartValue = ZERO;  // Assume it's female
        if (gender.equals(MALE)) {  //
            loopStartValue = malePopularNameIndex;
        }
        int loopStopValue = malePopularNameIndex;
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

    /**
     * Gets a year, a rank and a gender, and returns the name of the child with that rank in that year.
     * If the rank doesn't appear in that year, -1 will be returned.
     *
     * @param year The year
     * @param popularity The rank of the child
     * @param gender The gender of the child
     * @return The name of the child with the given rank in the given year
     */
    public String getName(int year, int popularity, String gender) {
        String pathToCSV = getPathToCSV(year);
        SEFileUtil seFileUtil = new SEFileUtil(pathToCSV);
        CSVParser parser = seFileUtil.getCSVParser();
        List<CSVRecord> recordsList = parser.getRecords();
        int malePopularNameIndex = getCsvRowOfMostPopularNameByGender(year, MALE) - ONE;
        int indexShifting = -ONE;

        boolean isPopularityInRange = popularity >= ROW_OF_FEMALE_POPULAR_NAME
                && popularity <= malePopularNameIndex;
        if (gender.equals(MALE)) {
            isPopularityInRange = popularity + malePopularNameIndex > malePopularNameIndex && popularity + malePopularNameIndex <= recordsList.size();
            indexShifting += malePopularNameIndex;
        }
        if (!isPopularityInRange) {
            System.out.println(popularity > malePopularNameIndex);
            System.out.println(popularity + malePopularNameIndex <= recordsList.size());
            return NO_NAME_ERROR;
        }

        CSVRecord recordByPopularity = recordsList.get(popularity + indexShifting);
        return recordByPopularity.get(RECORD_NAME_INDEX);
    }

    /**
     * Gets two years, a name and a gender, and returns the year in which the rank of the child was the highest,
     * in all of the years between the given two years.
     * If the child doesn't appear in any year, -1 will be returned.
     *
     *
     * @param startYear The first year
     * @param stopYear The last year
     * @param name The name of the child
     * @param gender The gender of the child
     * @return The year with the highest rank of the child
     */
    int yearOfHighestRank(int startYear, int stopYear, String name, String gender) {
        int minPopularity = Integer.MAX_VALUE;
        int minPopularityYear = -ONE;

        for (int currentYear = startYear; currentYear <= stopYear; currentYear++) {
            int popularity = getRank(currentYear, name, gender);
            if (popularity != NAME_NOT_FOUND_ERROR && popularity < minPopularity) {
                minPopularity = popularity;
                minPopularityYear = currentYear;
            }
        }
        return minPopularityYear;
    }

    /**
     * Gets two years, a name and a gender, and returns the average rank of the child, in all of
     * the years between the given two years.
     * If the child doesn't appear in any year, -1 will be returned.
     *
     *
     * @param startYear The first year
     * @param stopYear The last year
     * @param name The name of the child
     * @param gender The gender of the child
     * @return The average rank of the child
     */
    double getAverageRank(int startYear, int stopYear, String name, String gender) {
        int ranksSum = ZERO;
        int numberOfOccurrences = ZERO;


        for (int currentYear = startYear; currentYear <= stopYear; currentYear++) {
            int rank = getRank(currentYear, name, gender);
            if (rank != NAME_NOT_FOUND_ERROR) {
                numberOfOccurrences++;
                ranksSum += rank;
            }
        }
        double rankAverage = NAME_NOT_FOUND_ERROR;
        if (numberOfOccurrences != ZERO) {
            rankAverage = ((double) ranksSum) / numberOfOccurrences;
        }
        return rankAverage;
    }

    /**
     * Gets a year, a name and a gender, and returns the number of births of children with higher
     * rank then the rank of the given child.
     *
     * @param year The year
     * @param name The name of the child
     * @param gender The gender of the child
     * @return The number of births of children with higher rank
     */
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        String pathToCSV = getPathToCSV(year);
        SEFileUtil seFileUtil = new SEFileUtil(pathToCSV);
        CSVParser parser = seFileUtil.getCSVParser();
        List<CSVRecord> recordsList = parser.getRecords();
        int malePopularNameIndex = getCsvRowOfMostPopularNameByGender(year, MALE) - ONE;
        int nameRank = getRank(year, name, gender);
        int numberOfBorns=ZERO;
        int indexShifting = -ONE;

        int loopStartValue = ZERO;  // Assume it's female
        int loopStopValue = nameRank;

        if (gender.equals(MALE)) {
            indexShifting += malePopularNameIndex;
            loopStartValue += malePopularNameIndex;
        }

        loopStopValue += indexShifting;

        for(int i=loopStartValue;i<loopStopValue;i++){
            CSVRecord csvRecord = recordsList.get(i);
            numberOfBorns+=Integer.parseInt(csvRecord.get(RECORD_NUM_BORN_INDEX));
        }

        return numberOfBorns;
    }

    /**
     * Tests the functions of the class.
     */
    public static void main(String[] args) {
        BirthStatistics birthStatistics = new BirthStatistics(args[0]);
        birthStatistics.totalBirths(2010);
        int rank = birthStatistics.getRank(2010, "Asher", "M");
        System.out.println("Rank is: " + rank);
        String name = birthStatistics.getName(2012, 10, "M");
        System.out.println("Name: " + name);
        System.out.println(birthStatistics.yearOfHighestRank(1880, 2014,"David", "M"));
        System.out.println(birthStatistics.yearOfHighestRank(1880, 2014,"Jennifer", "F"));
        System.out.println(birthStatistics.getAverageRank(1880, 2014, "Benjamin", "M"));
        System.out.println(birthStatistics.getAverageRank(1880,2014, "Lois", "F"));
        System.out.println(birthStatistics.getTotalBirthsRankedHigher(2014, "Draco", "M"));
        System.out.println(birthStatistics.getTotalBirthsRankedHigher(2014, "Sophia", "F"));
    }


}
