package onlineVoting;

import java.io.*;
import java.util.*;

public class User {

    public static void main(String[] args) {

        Login login = new Login();

        while (true) {

            System.out.println("\n====================================");
            System.out.println("       STUDENTS ELECTION PORTAL");
            System.out.println("====================================");
            System.out.println("1 - Sign Up");
            System.out.println("2 - Login");
            System.out.println("3 - View Total Votes");
            System.out.println("4 - View Result");
            System.out.println("5 - Exit");

            System.out.print("Enter your choice: ");
            String choice = Login.sc.next();

            switch (choice) {

            case "1":
                login.signup();
                break;

            case "2":
                login.login();
                break;

            case "3":
                Login.cnd.displayTotalVotes();
                break;

            case "4":
                Login.cnd.displayResult();
                break;

            case "5":
                System.out.println("Thank you!");
                return;

            default:
                System.out.println("Invalid choice!");
            }
        }
    }
}


// =====================================================
// ELECTION PORTAL CLASS
// =====================================================

class ElectionPortal {

    private String username;
    private String password;
    private long phnno;

    public ElectionPortal(String username, String password, long phnno) {
        this.username = username;
        this.password = password;
        this.phnno = phnno;
    }

    public String getName() {
        return username;
    }

    public String getPass() {
        return password;
    }

    public long getPh() {
        return phnno;
    }

    public void setPass(String password) {
        this.password = password;
    }
}


// =====================================================
// CANDIDATES CLASS
// =====================================================

class Candidates {

    Map<String, Integer> candidates = new TreeMap<>();

    Map<Integer, String> candidateList = new HashMap<>();

    Map<String, Boolean> votedUsers = new HashMap<>();

    File votesFile = new File("votes.txt");
    File votersFile = new File("voters.txt");


    Candidates() {

        candidateList.put(1, "Rakesh");
        candidateList.put(2, "Madhu");
        candidateList.put(3, "Varshini");
        candidateList.put(4, "Akhil");

        for (String name : candidateList.values()) {
            candidates.put(name, 0);
        }

        loadVotes();
        loadVoters();
    }


    // =================================================
    // DISPLAY CANDIDATES
    // =================================================

    void displayCandidates() {

        System.out.println("\n========== CANDIDATES ==========");

        for (Map.Entry<Integer, String> entry : candidateList.entrySet()) {

            String name = entry.getValue();

            System.out.println(
                    entry.getKey() + " - " + name
            );
        }
    }


    // =================================================
    // CAST VOTE
    // =================================================

    void castVote(String username) {

        // Check whether user already voted

        if (votedUsers.getOrDefault(username, false)) {

            System.out.println(
                    "You have already voted!"
            );

            return;
        }


        displayCandidates();

        System.out.print("Enter candidate number: ");

        int choice = Login.sc.nextInt();

        if (!candidateList.containsKey(choice)) {

            System.out.println(
                    "Invalid candidate number!"
            );

            return;
        }


        String candidateName = candidateList.get(choice);

        int currentVotes =
                candidates.get(candidateName);

        candidates.put(
                candidateName,
                currentVotes + 1
        );


        // Mark user as voted

        votedUsers.put(username, true);


        // Save data

        saveVotes();
        saveVoters();


        System.out.println(
                "\nVote cast successfully!"
        );

        System.out.println(
                "You voted for: " + candidateName
        );
    }


    // =================================================
    // DISPLAY TOTAL VOTES
    // =================================================

    void displayTotalVotes() {

        System.out.println("\n========== TOTAL VOTES ==========");

        int total = 0;

        for (int votes : candidates.values()) {

            total = total + votes;
        }

        System.out.println(
                "Total votes: " + total
        );


        for (Map.Entry<String, Integer> entry
                : candidates.entrySet()) {

            System.out.println(
                    entry.getKey()
                    + " : "
                    + entry.getValue()
            );
        }
    }


    // =================================================
    // DISPLAY RESULT
    // =================================================

    void displayResult() {

        System.out.println("\n========== ELECTION RESULT ==========");

        String winner = null;
        String runnerUp = null;

        int highest = -1;
        int secondHighest = -1;


        for (Map.Entry<String, Integer> entry
                : candidates.entrySet()) {

            String name = entry.getKey();
            int votes = entry.getValue();


            if (votes > highest) {

                secondHighest = highest;
                runnerUp = winner;

                highest = votes;
                winner = name;

            } else if (votes > secondHighest) {

                secondHighest = votes;
                runnerUp = name;
            }
        }


        if (winner == null) {

            System.out.println(
                    "No election result available."
            );

            return;
        }


        System.out.println(
                "Winner : "
                + winner
                + " - "
                + highest
                + " votes"
        );


        if (secondHighest > 0 && runnerUp != null) {

            System.out.println(
                    "Runner-up : "
                    + runnerUp
                    + " - "
                    + secondHighest
                    + " votes"
            );
        }
    }


    // =================================================
    // SAVE VOTES
    // =================================================

    void saveVotes() {

        try (PrintWriter pw =
                new PrintWriter(votesFile)) {

            for (Map.Entry<String, Integer> entry
                    : candidates.entrySet()) {

                pw.println(
                        entry.getKey()
                        + ","
                        + entry.getValue()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error saving votes."
            );
        }
    }


    // =================================================
    // LOAD VOTES
    // =================================================

    void loadVotes() {

        if (!votesFile.exists()) {
            return;
        }

        try (Scanner fileScanner =
                new Scanner(votesFile)) {

            while (fileScanner.hasNextLine()) {

                String line =
                        fileScanner.nextLine();

                String[] data =
                        line.split(",");

                if (data.length == 2) {

                    String candidate =
                            data[0];

                    int votes =
                            Integer.parseInt(data[1]);

                    candidates.put(
                            candidate,
                            votes
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading votes."
            );
        }
    }


    // =================================================
    // SAVE VOTERS
    // =================================================

    void saveVoters() {

        try (PrintWriter pw =
                new PrintWriter(votersFile)) {

            for (Map.Entry<String, Boolean> entry
                    : votedUsers.entrySet()) {

                if (entry.getValue()) {

                    pw.println(entry.getKey());
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error saving voters."
            );
        }
    }


    // =================================================
    // LOAD VOTERS
    // =================================================

    void loadVoters() {

        if (!votersFile.exists()) {
            return;
        }

        try (Scanner fileScanner =
                new Scanner(votersFile)) {

            while (fileScanner.hasNextLine()) {

                String username =
                        fileScanner.nextLine();

                votedUsers.put(
                        username,
                        true
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading voters."
            );
        }
    }
}


// =====================================================
// LOGIN CLASS
// =====================================================

class Login {

    static Scanner sc =
            new Scanner(System.in);


    static Map<String, ElectionPortal> users =
            new HashMap<>();


    File usersFile =
            new File("users.txt");


    static Candidates cnd =
            new Candidates();


    String currentUsername;


    Login() {

        loadUsers();
    }


    // =================================================
    // LOGIN
    // =================================================

    void login() {

        System.out.println("\n========== LOGIN ==========");


        while (true) {

            // Ask username

            System.out.print("Username: ");

            String username =
                    sc.next();


            // Check username

            if (!users.containsKey(username)) {

                System.out.println(
                        "Invalid username! Please try again."
                );

                continue;
            }


            ElectionPortal user =
                    users.get(username);


            // Password loop

            while (true) {

                System.out.print("Password: ");

                String password =
                        sc.next();


                // Correct password

                if (password.equals(user.getPass())) {

                    currentUsername =
                            username;

                    System.out.println(
                            "\nLogin successful!"
                    );

                    afterLogin();

                    return;
                }


                // Wrong password

                System.out.println(
                        "\nInvalid password! Please try again."
                );

                System.out.println(
                        "1 - Try password again"
                );

                System.out.println(
                        "2 - Forgot password / Reset password"
                );

                System.out.println(
                        "3 - Exit"
                );


                System.out.print(
                        "Enter your choice: "
                );

                char choice =
                        sc.next().charAt(0);


                if (choice == '1') {

                    // Ask password again

                    continue;

                } else if (choice == '2') {

                    // Reset password

                    resetPassword(username);

                    return;

                } else if (choice == '3') {

                    System.out.println(
                            "Returning to main menu..."
                    );

                    return;

                } else {

                    System.out.println(
                            "Invalid choice! Returning to main menu..."
                    );

                    return;
                }
            }
        }
    }


    // =================================================
    // AFTER LOGIN
    // =================================================

    void afterLogin() {

        while (true) {

            System.out.println(
                    "\n========== USER MENU =========="
            );

            System.out.println(
                    "1 - Cast Vote"
            );

            System.out.println(
                    "2 - View Total Votes"
            );

            System.out.println(
                    "3 - View Result"
            );

            System.out.println(
                    "4 - Logout"
            );


            System.out.print(
                    "Enter your choice: "
            );

            String choice =
                    sc.next();


            switch (choice) {

            case "1":

                cnd.castVote(
                        currentUsername
                );

                break;


            case "2":

                cnd.displayTotalVotes();

                break;


            case "3":

                cnd.displayResult();

                break;


            case "4":

                System.out.println(
                        "Logged out successfully!"
                );

                return;


            default:

                System.out.println(
                        "Invalid choice!"
                );
            }
        }
    }


    // =================================================
    // SIGN UP
    // =================================================

    void signup() {

        System.out.println(
                "\n========== SIGN UP =========="
        );


        System.out.print(
                "Enter username: "
        );

        String username =
                sc.next();


        // Check username already exists

        if (users.containsKey(username)) {

            System.out.println(
                    "Username already exists!"
            );

            return;
        }


        // Password

        String password;

        while (true) {

            System.out.print(
                    "Enter password: "
            );

            password =
                    sc.next();


            if (isValidPassword(password)) {

                break;

            } else {

                System.out.println(
                        "Invalid password!"
                );

                System.out.println(
                        "Password must contain:"
                );

                System.out.println(
                        "- At least 8 characters"
                );

                System.out.println(
                        "- One uppercase letter"
                );

                System.out.println(
                        "- One lowercase letter"
                );

                System.out.println(
                        "- One number"
                );

                System.out.println(
                        "- One special character"
                );
            }
        }


        // Mobile number

        long phnno;

        while (true) {

            System.out.print(
                    "Enter mobile number: "
            );

            String phnoInput =
                    sc.next();


            if (isValidMobileNumber(phnoInput)) {

                phnno =
                        Long.parseLong(phnoInput);

                break;

            } else {

                System.out.println(
                        "Invalid mobile number!"
                );
            }
        }


        // OTP

        if (!otp()) {

            System.out.println(
                    "OTP verification failed."
            );

            return;
        }


        // Create user

        ElectionPortal ep =
                new ElectionPortal(
                        username,
                        password,
                        phnno
                );


        users.put(
                username,
                ep
        );


        // Save user to file

        saveUsers();


        System.out.println(
                "\nRegistration successful!"
        );
    }


    // =================================================
    // OTP
    // =================================================

    boolean otp() {

        int generatedOtp =
                1000
                + (int)(Math.random() * 9000);


        System.out.println(
                "\nYour OTP is: "
                + generatedOtp
        );


        System.out.print(
                "Enter OTP: "
        );

        int enteredOtp =
                sc.nextInt();


        if (enteredOtp == generatedOtp) {

            System.out.println(
                    "OTP verified successfully!"
            );

            return true;

        } else {

            System.out.println(
                    "Invalid OTP!"
            );

            return false;
        }
    }


    // =================================================
    // PASSWORD VALIDATION
    // =================================================

    boolean isValidPassword(String password) {

        return password.matches(
                "^(?=.*[a-z])"
                + "(?=.*[A-Z])"
                + "(?=.*\\d)"
                + "(?=.*[@#$%^&+=!])"
                + "(?=\\S+$)"
                + ".{8,}$"
        );
    }


    // =================================================
    // MOBILE VALIDATION
    // =================================================

    boolean isValidMobileNumber(
            String phnoInput) {

        return phnoInput.matches(
                "[7-9][0-9]{9}"
        );
    }


    // =================================================
    // RESET PASSWORD
    // =================================================

    void resetPassword(String username) {

        System.out.println(
                "\n========== RESET PASSWORD =========="
        );


        ElectionPortal user =
                users.get(username);


        if (user == null) {

            System.out.println(
                    "User not found!"
            );

            return;
        }


        String newPassword;


        while (true) {

            System.out.print(
                    "Enter new password: "
            );

            newPassword =
                    sc.next();


            if (isValidPassword(newPassword)) {

                break;

            } else {

                System.out.println(
                        "Invalid password!"
                );

                System.out.println(
                        "Please enter a valid password."
                );
            }
        }


        // Change password

        user.setPass(
                newPassword
        );


        // Save updated password

        saveUsers();


        System.out.println(
                "Password reset successfully!"
        );
    }


    // =================================================
    // SAVE USERS
    // =================================================

    void saveUsers() {

        try (PrintWriter pw =
                new PrintWriter(usersFile)) {


            for (ElectionPortal user
                    : users.values()) {


                pw.println(
                        user.getName()
                        + ","
                        + user.getPass()
                        + ","
                        + user.getPh()
                );
            }


        } catch (Exception e) {

            System.out.println(
                    "Error saving users."
            );
        }
    }


    // =================================================
    // LOAD USERS
    // =================================================

    void loadUsers() {

        if (!usersFile.exists()) {

            return;
        }


        try (Scanner fileScanner =
                new Scanner(usersFile)) {


            while (fileScanner.hasNextLine()) {

                String line =
                        fileScanner.nextLine();


                String[] data =
                        line.split(",");


                if (data.length == 3) {

                    String username =
                            data[0];

                    String password =
                            data[1];

                    long phnno =
                            Long.parseLong(data[2]);


                    ElectionPortal user =
                            new ElectionPortal(
                                    username,
                                    password,
                                    phnno
                            );


                    users.put(
                            username,
                            user
                    );
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "Error loading users."
            );
        }
    }
}