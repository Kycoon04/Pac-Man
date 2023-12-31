/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.pac.man.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String Nivel;
    private List<String> trophies = new ArrayList<>();
    private String Ghosteat="";
    private String BestTime="";
    private String Timeallgame="";

    public User() {
    }
    
    public void update(User newUser) {
        this.PointWin = newUser.getPointWin();
        this.Point1Live = newUser.getPoint1Live();
        this.LivesLose = newUser.getLivesLose();
        this.Ghosteat = newUser.getGhosteat();
        this.BestTime = newUser.getBestTime();
        this.Timeallgame = newUser.getTimeallgame();
        this.trophies = newUser.getTrophies();
    }

    public List<String> getTrophies() {
        return trophies;
    }

    public void setTrophies(List<String> trophies) {
        this.trophies = trophies;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String Nivel) {
        this.Nivel = Nivel;
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
        sb.append("\nNivel:").append(Nivel);
        for(int i = 0;i<trophies.size();i++){
        sb.append("\ntrophies_").append(trophies.get(i));
        }
        return sb.toString();
    }
}
