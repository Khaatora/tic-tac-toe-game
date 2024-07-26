package com.example.tictactoegama.models;
import java.util.Date;
public class Log {
    private int gameid;
    private int userid;
    private int opponentid;
    private Date gameDate;
    private GameMoves gameMoves;
    public Log(int gameid, int userid, int opponentid, Date gameDate, GameMoves gameMoves) {
        this.gameid = gameid;
        this.userid = userid;
        this.opponentid = opponentid;
        this.gameDate = gameDate;
        this.gameMoves = gameMoves;
    }
    public int getGameid() {
        return gameid;
    }
    public void setGameid(int gameid) {
        this.gameid = gameid;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public int getOpponentid() {
        return opponentid;
    }
    public void setOpponentid(int opponentid) {
        this.opponentid = opponentid;
    }
    public Date getGameDate() {
        return gameDate;
    }
    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }
    public GameMoves getGameMoves() {
        return gameMoves;
    }
    public void setGameMoves(GameMoves gameMoves) {
        this.gameMoves = gameMoves;
    }
    @Override
    public String toString() {
        return "gameid=" + gameid + ", userid=" + userid + ", opponentid=" + opponentid + ", gameDate=" + gameDate
                + ", gameMoves=" + gameMoves;
    }
    public void toLogs(String query){
        String[] splitted = query.split(",");

        
    }
    
}