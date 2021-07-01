package model.world.event;

import middleware.AsyncMessageHandler;
import model.role.Role;
import model.role.state.InHospital;
import utils.Delay;
import utils.ImageStateUtils;
import utils.Render3DUtils;
import view.GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class DingoEvent extends RichmanEventAbstract{
    private Image im;
    private List<Image> running;
    private Point hospitalPosition;
    private DingoState state = DingoState.NOTHING;
    private int run_loc = 0;
    private int run_cnt = 0;
    private int escape_cnt = 0;
    private List<Image> escaping;
    enum DingoState {
        NOTHING,
        CHASING,
    }
    public DingoEvent(){
        this.running = new ArrayList<>();
        this.escaping = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            this.running.add(ImageStateUtils.readImageFromString("assets/event/dingo/run_" + Integer.toString(i) + ".png"));
        }
        im = ImageStateUtils.readImageFromString("assets/event/dingo/3DDingo.png");
    }

    @Override
    public void render(Point position, Graphics g) {
        Render3DUtils.drawDiamond(position, g);
        Render3DUtils.drawRole(im, g, (int)position.getX(), (int)position.getY(), 70, 70);
    }
    
    @Override
    public void trigger(Role caster, AsyncMessageHandler handler, List<Role> castee) {
        File dir = new File(Paths.get("assets", "roles", caster.getName(), "walking", "right").toString());
        try {
            getEscapingRole(dir.listFiles());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.sendToHospital(caster);
        caster.setState(new InHospital(3));
        this.state = DingoState.CHASING;
        Delay.delay(3000);
        this.state = DingoState.NOTHING;
        this.escaping.clear();
        this.escape_cnt = 0;
        this.run_cnt = 0;
        this.run_loc = 0;
    }
    private void getEscapingRole(File[] files) throws IOException {
        for(File file:files){
            this.escaping.add(ImageIO.read(file));
        }
    }
    private void sendToHospital(Role caster) {
        caster.setGrid(super.positionToGrid.get(this.hospitalPosition));
    }
    @Override
    public void setArgs(List<Integer> arguments) {
        // TODO Auto-generated method stub
        this.hospitalPosition = new Point(arguments.get(0), arguments.get(1));
    }

    @Override
    public void stateRender(Graphics g) {
        // TODO Auto-generated method stub
        switch(this.state){
            case NOTHING:
                break;
            case CHASING:
                int dogeX = (150+run_loc) * GUI.WIDTH / 1440;
                int dogeY = 400 * GUI.HEIGHT / 810;
                int dogeWidth = this.running.get(run_cnt).getWidth(null) * 180 * GUI.WIDTH / (1440*this.running.get(run_cnt).getHeight(null));
                int dogeHeight = 180 * GUI.HEIGHT / 810;
                int RoleX = (350+run_loc) * GUI.WIDTH / 1440;
                int RoleY =  400 * GUI.HEIGHT / 810;
                int RoleWidth = this.running.get(escape_cnt).getWidth(null) * 180 * GUI.WIDTH / (1440*this.running.get(escape_cnt).getHeight(null));
                int RoleHeight = 180 * GUI.HEIGHT / 810;
                g.drawImage(this.running.get(run_cnt), dogeX, dogeY, dogeWidth, dogeHeight, null);
                g.drawImage(this.escaping.get(escape_cnt), RoleX, RoleY, RoleWidth, RoleHeight, null);
                if(run_loc %10 == 0){
                    run_cnt = (run_cnt+1)%running.size();
                    if(run_loc % 50 == 0)
                        escape_cnt = (escape_cnt+1)%escaping.size();
                }
                run_loc += 5;
            break;
        }
        
    }
    
}
