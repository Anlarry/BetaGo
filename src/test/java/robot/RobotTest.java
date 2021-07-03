package robot;

import java.awt.Color;
import java.time.Year;

import com.client.robot.Robot;
import com.client.ui.ChessPoint;

public class RobotTest {
    public static void main(String[] args) {
        ChessPoint[][] cp = new ChessPoint[15][15];
        cp[7][7] = new ChessPoint(7, 7, Color.BLACK);
        cp[7][8] = new ChessPoint(7, 8, Color.BLACK);
        // cp[8][9] = new ChessPoint(8, 9, Color.BLACK);
        cp[9][9] = new ChessPoint(9, 9, Color.BLACK);
        Robot robot = new Robot(Color.WHITE, 3);
        ChessPoint res = robot.go(cp);
        System.out.println(res.getCPX() + " " + res.getCPY()); 
    }
}
