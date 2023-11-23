/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.pac.man.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dilan
 */
public class TextCSV {

    public TextCSV() {
    }

    public User LoadUser(String name, String filePath) {
        boolean si = true;
        File file = new File(filePath);
        List<User> users = new ArrayList<>();
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("name:") && sb.length() != 0) {
                        User existingUser = parseUser(sb.toString());
                        users.add(existingUser);
                        sb.setLength(0);
                    }
                    sb.append(line).append("\n");
                }
                if (sb.length() != 0) {
                    User existingUser = parseUser(sb.toString());
                    users.add(existingUser);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (User existingUser : users) {
                if (existingUser.getName().equals(name)) {
                    return existingUser;
                }
            }
        }
        return null;
    }

    public void saveUserToFile(User user, String filePath) {
        boolean si = true;
        File file = new File(filePath);
        List<User> users = new ArrayList<>();
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("name:") && sb.length() != 0) {
                        User existingUser = parseUser(sb.toString());
                        users.add(existingUser);
                        sb.setLength(0);
                    }
                    sb.append(line).append("\n");
                }
                if (sb.length() != 0) {
                    User existingUser = parseUser(sb.toString());
                    users.add(existingUser);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (User existingUser : users) {
                if (existingUser.getName().equals(user.getName())) {
                    existingUser = user;
                    si = false;
                }
            }
        }
        if (si) {
            users.add(user);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (User existingUser : users) {
                if (users.indexOf(existingUser) != 0) {
                    writer.newLine();
                }
                if (existingUser.getName().equals(user.getName())) {
                    existingUser = user;
                }
                writer.write("name:" + existingUser.getName());
                writer.newLine();
                writer.write("PointWin:" + existingUser.getPointWin());
                writer.newLine();
                writer.write("Point1Live:" + existingUser.getPoint1Live());
                writer.newLine();
                writer.write("LivesLose:" + existingUser.getLivesLose());
                writer.newLine();
                writer.write("Ghosteat:" + existingUser.getGhosteat());
                writer.newLine();
                writer.write("BestTime:" + existingUser.getBestTime());
                writer.newLine();
                writer.write("Nivel:" + existingUser.getNivel());
                writer.newLine();
                writer.write("Timeallgame:" + existingUser.getTimeallgame());
                for (int i = 0; i < existingUser.getTrophies().size(); i++) {
                    writer.write("\ntrophies_" + existingUser.getTrophies().get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User parseUser(String line) {
        User user = new User();
        String[] parts = line.split("\n");
        for (String part : parts) {
            if (part.startsWith("name:") && part.length() > 5) {
                user.setName(part.substring(5));
            } else if (part.startsWith("PointWin:") && part.length() > 9) {
                user.setPointWin(part.substring(9));
            } else if (part.startsWith("Point1Live:") && part.length() > 11) {
                user.setPoint1Live(part.substring(11));
            } else if (part.startsWith("LivesLose:") && part.length() > 10) {
                user.setLivesLose(part.substring(10));
            } else if (part.startsWith("Ghosteat:") && part.length() > 9) {
                user.setGhosteat(part.substring(9));
            } else if (part.startsWith("BestTime:") && part.length() > 9) {
                user.setBestTime(part.substring(9));
            } else if (part.startsWith("Timeallgame:") && part.length() > 12) {
                user.setTimeallgame(part.substring(12));
            } else if (part.startsWith("Nivel:") && part.length() > 6) {
                user.setNivel(part.substring(6));
            }else if (part.startsWith("trophies_") && part.length() > 9) {
                user.getTrophies().add(part.substring(9));
            }
        }
        return user;
    }
}
