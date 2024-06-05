package org.example.backend.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.backend.exceptions.FileException;
import org.example.backend.exceptions.ImportException;
import org.example.backend.exceptions.NoSubmissionException;
import org.example.backend.importmodels.*;
import org.example.database.SubmissionRepository;
import org.example.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ImportService {

    private final AccountService accountService;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    /**
     * Constructor for ImportService, Autowired by Spring
     *
     * @param userRepository Repository used to save users
     * @param submissionRepository Repository used to save submissions
     */
    @Autowired
    public ImportService(UserRepository userRepository, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.accountService = new AccountService(userRepository, submissionRepository);
    }
    /**
     * Import data as downloaded from Brightspace and ProjectForum to the application
     *
     * @param zipFile .zip file containing student submissions
     * @param csvFile .csv file containing a list of all students
     * @param xlsxFile .xlsx file containing a list of all projects, from ProjectForum
     * @throws NoSubmissionException if a directory does not contain any submissions
     * @throws ZipException if the zip file behaves unexpectedly
     * @throws FileException if the files could not be parsed
     * @throws ImportException if the association process fails
     * @throws SQLException if accounts could not be created
     */
    public void importData(File zipFile, File csvFile, File xlsxFile)
        throws NoSubmissionException, ZipException, FileException, ImportException, SQLException {

        List<Student> studentList = processCsv(csvFile);
        List<Submission> submissionList = extractZip(zipFile);
        List<Project> projectList = processXlsx(xlsxFile);
        List<Association> associations = associate(studentList, submissionList);
        List<Coordinator> coordinators = associateCoordinators(associations, projectList);
        accountService.createCoordinatorsAccounts(coordinators);
        accountService.createStudentAccounts(studentList);
    }

    /**
     * Associates submissions to coordinators.
     *
     * @param associations student-submission pairs
     * @param projects List of projects from ProjectForum
     */
    private List<Coordinator> associateCoordinators(List<Association> associations, List<Project> projects) {
        // TODO - verifici daca toate student number sunt din aceeasi grupa

        Map<Coordinator, List<Association>> map = new HashMap<>();
        for(Project p : projects)
            for(Coordinator c : p.getCoordinators())
                map.put(c, new ArrayList<>());

        for(Project p : projects) {
            List<String> studentNumbers = p.getStudentNos();

            for(Association a : associations) {
                String studentNr = a.getStudent().getStudentNo();

                if(studentNumbers.contains(studentNr)) {
                    for(Coordinator c : p.getCoordinators()) {
                        List<Association> value = map.get(c);
                        value.add(a);
                        map.put(c, value);
                    }
                }
            }
        }

        List<Coordinator> coordinators = new ArrayList<>();

        for(Map.Entry entry : map.entrySet()) {
            Coordinator coordinator = (Coordinator) entry.getKey();
            coordinator.setAssociations((List<Association>) entry.getValue());
            coordinators.add(coordinator);
        }

        return coordinators;
    }

    /**
     * Associates students to their submissions, based on their full name.
     * NOTE: this may have unexpected behaviour in the situation where two students with the
     * exact same name are enrolled in the same edition of the same course.
     *
     * @param students List of students extracted from the .csv file
     * @param submissions List of submissions extracted from the .zip file
     */
    private List<Association> associate(List<Student> students, List<Submission> submissions) {
        List<Association> associations = new ArrayList<>();
        for (Submission current : submissions) {
            Optional<Student> item = students.stream()
                .filter(s -> s.getStudentName().equals(current.getStudentName()) &&
                s.getGroupName().equals(current.getGroupName()))

                .findFirst();
            item.ifPresent(student -> associations.add(new Association(student, current)));
        }
        return associations;
    }

    /**
     * Extracts data from the zip file containing student submissions, retrieved from Brightspace.
     *
     * @param zipFile .zip file containing student submissions
     * @return List of Submission objects, containing submission data
     * @throws NoSubmissionException if a directory does not contain any submissions
     * @throws ZipException if the zip file behaves unexpectedly
     */
    private List<Submission> extractZip(File zipFile) throws NoSubmissionException, ZipException {
        List<Submission> submissionList = new ArrayList<>();
        try (ZipFile file = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = file.entries();
            Set<String> directories = new HashSet<>();
            while(entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                /* This if statement should not be entered when working with zip files generated by
                Brightspace. However, it may be entered when using zip files from other sources.
                If the export tool from Brightspace changes the way it generates zip files, this
                place should be the first place to look for eventual errors. */
                if(entry.isDirectory()) {

                    InputStream inputStream = file.getInputStream(entry);
                    File currentEntry = new File(System.getProperty("java.io.tmpdir") + "/" + entry.getName());
                    OutputStream outputStream = new FileOutputStream(currentEntry);
                    outputStream.write(inputStream.readAllBytes());

                    inputStream.close();
                    outputStream.close();

                    Submission submission = processFileName(name);
                    File submittedFile = Objects.requireNonNull(currentEntry.listFiles())[0];
                    InputStream submittedStream = new FileInputStream(submittedFile);
                    submission.setFileName(entry.getName());
                    submission.setSubmittedFile(submittedStream.readAllBytes());
                    submissionList.add(submission);
                } else {
                    /* extract directory. Functionality can be further extended by checking for
                    different directory names, and further processing files from that directory. */
                    int lastIndex = name.lastIndexOf('/');
                    if(lastIndex != -1) {
                        String dirName = name.substring(0, lastIndex);
                        directories.add(dirName);

                        InputStream inputStream = file.getInputStream(entry);

                        Submission submission = processFileName(dirName);
                        submission.setFileName(name.substring(lastIndex + 1));
                        submission.setSubmittedFile(inputStream.readAllBytes());
                        submissionList.add(submission);
                        inputStream.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new ZipException("Zip file could not be read.");
        } catch (NullPointerException e) {
            throw new NoSubmissionException("Not all directories contain submissions. " +
                    "Please ensure all submissions are registered before trying to upload the zip file. " +
                    "If this is not the case, please use the manual upload functionality.");
        }
        return submissionList;
    }

    /**
     * Extracts data from a csv file containing a list of all students, retrieved from Brightspace
     *
     * @param csvFile .csv file containing a list of all students
     * @return List of Student objects, containing student data
     */
    private List<Student> processCsv(File csvFile) throws FileException {
        if(!csvFile.exists())
            throw new FileException("csv file appears to not exist. Please try again");
        List<Student> students = new ArrayList<>();
        try (Scanner scanner = new Scanner(csvFile)) {
            scanner.nextLine();
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String studentNo = data[0];
                String username = data[1];
                String studentName = data[3] + " " + data[2];
                String email = data[4];
                String groupCategory = data[5];
                String groupName = data[6].replaceAll("\n", "");
                Student student = new Student(studentNo, username, studentName, email, groupCategory, groupName);
                students.add(student);
            }
        } catch (FileNotFoundException e) {
            throw new FileException("Csv file could not be processed");
        }
        return students;
    }

    /**
     * Processes a submission directory name, parsing all data according to the format
     * defined by the Brightspace export tool.
     *
     * @param name The name of a directory containing student submissions
     * @return a Submission object, containing all data
     */
    private Submission processFileName(String name) {
        String[] split = name.split("-");
        String groupNo = split[0];
        String assignmentNo = split[1].substring(0, split[1].length() - 1);
        String groupName = split[2].substring(1, split[2].length() - 1);
        String studentName = split[3].substring(1, split[3].length() - 1);
        String date = split[4].substring(1);
        return new Submission(groupNo, assignmentNo, groupName, studentName, date);
    }

    /**
     * Processes a .xlsx file, parsing all data according to the format defined by the
     * ProjectForum export tool.
     *
     * @param xlsxFile File exported from ProjectForum
     * @throws FileException if the file behaves unexpectedly.
     */
    private List<Project> processXlsx(File xlsxFile) throws FileException {
        List<Project> projects = new ArrayList<>();
        try(InputStream inputStream = new FileInputStream(xlsxFile)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for(Row row : sheet) {

                i++;
                if(i == 1)
                    continue;

                int j = 0;
                String name = null, email;
                List<Coordinator> coordinators = new ArrayList<>();
                List<String> studentNos = new ArrayList<>();
                for(Cell cell : row) {
                    if(j >= 8 && j <= 17) {
                        if (j % 2 == 0) {
                            name = cell.getStringCellValue();
                        } else {
                            email = cell.getStringCellValue();
                            if(!(name == null) && !name.isEmpty())
                                coordinators.add(new Coordinator(name, email));
                        }
                    } else if (j == 21) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        studentNos = Arrays.stream(parseCell(cell.getStringCellValue())).toList();
                    }
                    j++;
                }
                projects.add(new Project(studentNos, coordinators));
            }
        } catch (FileNotFoundException e) {
            throw new FileException("Excel file containing supervisor data was not found");
        } catch (IOException e) {
            throw new FileException("Excel file containing supervisor data could not be closed");
        }
        return projects;
    }

    /**
     * Method to parse data containing students in a cell, from a .xlsx file generated
     * automatically by ProjectForum. The format is:
     * data1, data2, data3, data4 and data5
     *
     * @param cellData Data from the cell in string format.
     * @return Strings containing data for each student.
     */
    private String[] parseCell(String cellData) {
        String[] data = cellData.split(", |and ");

        List<String> ans = new ArrayList<>();

        int j=0;
        for(int i = 0; i < data.length; i++) {
            data[i] = data[i].replaceAll(" ", "");

            if(!data[i].isEmpty()) {
                ans.add(data[i].trim());
                j++;
            }
        }

        Object[] aux = ans.toArray();
        String[] ret = Arrays.copyOf(aux, aux.length, String[].class);

        return ret;
    }

}
