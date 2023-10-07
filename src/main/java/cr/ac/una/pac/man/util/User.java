/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.pac.man.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jomav
 */
public class User {
    
    private String name="";
    private String PointWin="";
    private String Point1Live="";
    private String LivesLose="";
    private HashMap<String,Integer> Nivel;
    private String Ghosteat="";
    private String BestTime="";
    private String Timeallgame="";

    public User() {
    Nivel = new HashMap<String,Integer>();
    }
    
    public void update(User newUser) {
        this.PointWin = newUser.getPointWin();
        this.Point1Live = newUser.getPoint1Live();
        this.LivesLose = newUser.getLivesLose();
        this.Ghosteat = "4";
        this.BestTime = newUser.getBestTime();
        this.Timeallgame = newUser.getTimeallgame();
        this.Nivel = newUser.getNivel();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointWin() {
        return PointWin;
    }

    public void setPointWin(String PointWin) {
        this.PointWin = PointWin;
    }

    public String getPoint1Live() {
        return Point1Live;
    }

    public void setPoint1Live(String Point1Live) {
        this.Point1Live = Point1Live;
    }

    public String getLivesLose() {
        return LivesLose;
    }

    public void setLivesLose(String LivesLose) {
        this.LivesLose = LivesLose;
    }

    public HashMap<String, Integer> getNivel() {
        return Nivel;
    }

    public void setNivel(HashMap<String, Integer> Nivel) {
        this.Nivel = Nivel;
    }

    public String getGhosteat() {
        return Ghosteat;
    }

    public void setGhosteat(String Ghosteat) {
        this.Ghosteat = Ghosteat;
    }

    public String getBestTime() {
        return BestTime;
    }

    public void setBestTime(String BestTime) {
        this.BestTime = BestTime;
    }

    public String getTimeallgame() {
        return Timeallgame;
    }

    public void setTimeallgame(String Timeallgame) {
        this.Timeallgame = Timeallgame;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name:").append(name);
        sb.append("\nPointWin:").append(PointWin);
        sb.append("\nPoint1Live:").append(Point1Live);
        sb.append("\nLivesLose:").append(LivesLose);
        sb.append("\nGhosteat:").append(Ghosteat);
        sb.append("\nBestTime:").append(BestTime);
        sb.append("\nTimeallgame:").append(Timeallgame);
        for (Map.Entry<String, Integer> entry : Nivel.entrySet()) {
            sb.append("\nNivel_").append(entry.getKey()).append("=").append(entry.getValue());
        }
        // No añadir un salto de línea al final del registro
        return sb.toString();
    }
}
