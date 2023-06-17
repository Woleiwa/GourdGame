package Observer;

import Being.Role;
import Gui.Frame.GameFrame;
import Gui.Label.BulletLabel;
import Gui.Label.CreatureLabel;
import Gui.Label.RoleLabel;

import java.util.ArrayList;

public class SingleGameObserver extends Observer{
    protected ArrayList<CreatureLabel> creatureLabels = new ArrayList<>();
    protected RoleLabel roleLabel;

    public SingleGameObserver(GameFrame gameFrame) {
        super(gameFrame);
    }

    public void addCreature(CreatureLabel creatureLabel){
        this.creatureLabels.add(creatureLabel);
    }

    public void addRole(RoleLabel roleLabel) {
        this.roleLabel = roleLabel;
    }

    @Override
    public void bulletNotify(BulletLabel bulletLabel) {
        if (bulletLabel.getLauncher()){
            if(this.hitCreature(bulletLabel)){
                bulletLabel.disappear();
            }
        }
        else{
            if (this.hitRole(bulletLabel)){
                bulletLabel.disappear();
            }
        }
    }

    @Override
    public void creatureNotify(CreatureLabel creatureLabel) {
        int t_x = this.roleLabel.get_x();
        int t_y = this.roleLabel.get_y();
        int x = creatureLabel.getCur_x() / creatureLabel.getWidth();
        int y = creatureLabel.getCur_y() / creatureLabel.getHeight();
        int dx = t_x - x;
        int dy = t_y - y;
        int jd = dx * dx + dy * dy;
        if(jd <= 1){
            Role role = roleLabel.getRole();
            role.setHp(role.getHp() - creatureLabel.getCreatureAtk());
            roleLabel.updateHp();
        }
    }

    private boolean hitCreature(BulletLabel bulletLabel){
        if(bulletLabel.getLauncher()){
            for(int i = 0; i < creatureLabels.size(); i++){
                if(creatureLabels.get(i).hitByBullet(bulletLabel)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitRole(BulletLabel bulletLabel){
        if(!bulletLabel.getLauncher()){
            if(roleLabel.hitByBullet(bulletLabel)){
                return true;
            }
        }
        return false;
    }
    @Override
    public void roleNotify(RoleLabel roleLabel) {
        for(int i = 0; i < this.creatureLabels.size(); i++){
            CreatureLabel creaturelabel = creatureLabels.get(i);
            int dx = this.roleLabel.get_x() - creaturelabel.get_x();
            int dy = this.roleLabel.get_y() - creaturelabel.get_y();
            int distance = dx * dx + dy * dy;
            if(distance <= 1){
                creaturelabel.getAtk();
            }
        }
    }

    @Override
    public void roleDied(RoleLabel roleLabel) {
        this.gameFrame.fail();
    }

    @Override
    public void creatureDied(CreatureLabel creatureLabel) {
        this.gameFrame.killMonster();
    }
}
