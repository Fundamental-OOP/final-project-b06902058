package model.world.event;

import middleware.AsyncMessageHandler;
import model.role.Role;
import model.role.state.InHospital;
import view.GUI;
import utils.Delay;
import utils.ImageStateUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ThugEvent extends RichmanEventAbstract{
    private Image im;
    private Point hospitalPosition;
    private List<Image> slashing;
    private List<Image> escaping;
    private int slash_cnt = 0;
    private int escape_cnt = 0;
    private int slash_loc;
    private Thug state = Thug.NOTHING;
    private Image thug;
    enum Thug {
        NOTHING,
        BEATING, BEFORE_BEATING,
    }
    public ThugEvent() {
        this.slashing = new ArrayList<>();
        this.escaping = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            this.slashing.add(ImageStateUtils.readImageFromString("assets/event/thug/runSlashing/Run Slashing_00" + Integer.toString(i) + ".png"));
        }
        for(int i = 10; i < 18; i++){
            this.slashing.add(ImageStateUtils.readImageFromString("assets/event/thug/runSlashing/Run Slashing_0" + Integer.toString(i) + ".png"));
        }
        thug = ImageStateUtils.readImageFromString("assets/event/thug/thugInfo.jpg");
        im = ImageStateUtils.readImageFromString("assets/event/thug/thugInMap.png");
    }

    @Override
    public void render(Point position, Graphics g) {
        int newX = 600 + (int) (0.92 * (position.getX() - position.getY()));
        int newY = 200 + (int) (0.32 * (position.getX() + position.getY()));

        newX = (int) ((double) newX * GUI.HEIGHT / 810);
        newY = (int) ((double) newY * GUI.WIDTH / 1440);
        int delta = (int) (13.5 * ((double)GUI.HEIGHT / 810 + (double)GUI.WIDTH / 1440 ));
        g.drawImage(im, newX+3*delta-45, newY-80, 80, 80, null);
    }
    public void stateRender(Graphics g){
        switch(this.state){
            case BEFORE_BEATING:
                g.drawImage(this.thug, (100) * GUI.WIDTH / 1440, 100 * GUI.HEIGHT / 810, this.thug.getWidth(null) * 500 * GUI.WIDTH / (1440*this.thug.getHeight(null)), 500 * GUI.HEIGHT / 810, null);
                break;
            case BEATING:
                g.drawImage(this.slashing.get(slash_cnt), (150+slash_loc) * GUI.WIDTH / 1440, 400 * GUI.HEIGHT / 810, this.slashing.get(slash_cnt).getWidth(null) * 180 * GUI.WIDTH / (1440*this.slashing.get(slash_cnt).getHeight(null)), 180 * GUI.HEIGHT / 810, null);
                g.drawImage(this.escaping.get(escape_cnt), (350+slash_loc) * GUI.WIDTH / 1440, 400 * GUI.HEIGHT / 810, this.slashing.get(escape_cnt).getWidth(null) * 180 * GUI.WIDTH / (1440*this.slashing.get(escape_cnt).getHeight(null)), 180 * GUI.HEIGHT / 810, null);
                if(slash_loc %10 == 0){
                    slash_cnt = (slash_cnt+1)%slashing.size();
                    if(slash_loc % 50 == 0)
                        escape_cnt = (escape_cnt+1)%escaping.size();
                }
                slash_loc += 5;
                break;
            default:
                break;

        }
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
        this.state = Thug.BEFORE_BEATING;
        Delay.delay(1000);
        this.state = Thug.BEATING;
        Delay.delay(3000);
        this.state = Thug.NOTHING;
        this.sendToHospital(caster);
        caster.setState(new InHospital(3));
        this.escaping.clear();
        this.escape_cnt = 0;
        this.slash_cnt = 0;
        this.slash_loc = 0;
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
        this.hospitalPosition = new Point(arguments.get(0), arguments.get(1));
    }

}
