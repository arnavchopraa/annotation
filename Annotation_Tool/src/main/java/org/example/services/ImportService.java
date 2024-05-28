package org.example.services;

import lombok.NoArgsConstructor;
import org.example.exceptions.NoSubmissionException;
import org.example.models.Student;
import org.example.models.Submission;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@NoArgsConstructor
public class ImportService {

    /**
     * Import data as downloaded from Brightspace to the application
     *
     * @param zipFile .zip file containing student submissions
     * @param csvFile .csv file containing a list of all students
     * @throws NoSubmissionException if a directory does not contain any submissions
     * @throws ZipException if the zip file behaves unexpectedly
     */
    public void importData(File zipFile, File csvFile) throws NoSubmissionException, ZipException {
        List<Student> studentList = processCsv(csvFile);
        List<Submission> submissionList = extractZip(zipFile);
        //TODO - asociez submisie cu student
        //TODO - creez modelul pt baza de date studentNo - fisier - assigned coordinator
    }

    public void associate(List<Student> students, List<Submission> submissions) {

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
            while(entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if(entry.isDirectory()) {
                    String name = entry.getName();

                    InputStream inputStream = file.getInputStream(entry);
                    File currentEntry = new File(System.getProperty("java.io.tmpdir") + "/" + entry.getName());
                    OutputStream outputStream = new FileOutputStream(currentEntry);
                    outputStream.write(inputStream.readAllBytes());

                    inputStream.close();
                    outputStream.close();

                    Submission submission = processFileName(name);
                    File submittedFile = Objects.requireNonNull(currentEntry.listFiles())[0];
                    submission.setSubmittedFile(submittedFile);
                    submissionList.add(submission);
                }  //TODO else - for now, should be ignored. Later, should also support files with nested zips / containing csv file.
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
    private List<Student> processCsv(File csvFile) {
        List<Student> students = new ArrayList<>();
        try (Scanner scanner = new Scanner(csvFile)) {
            scanner.nextLine();
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String studentNo = data[0];
                String username = data[1];
                String lastName = data[2];
                String firstName = data[3];
                String email = data[4];
                String groupCategory = data[5];
                String groupName = data[6].replaceAll("\n", "");
                Student student = new Student(studentNo, username, lastName, firstName, email, groupCategory, groupName);
                students.add(student);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
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
}
